package com.autonomous.pm.domain.structure.mem;

import java.util.Date;
import java.util.List;

import com.autonomous.pm.domain.structure.DrivingInfo;
import com.autonomous.pm.domain.structure.Gps;
import com.autonomous.pm.domain.structure.Poi;
import com.autonomous.pm.domain.structure.SensorInfo;
import com.autonomous.pm.domain.structure.TripInfo;
import com.autonomous.pm.memcache.MemDB;
import com.autonomous.pm.model.Do.TTrpRaw;
import com.autonomous.pm.model.Dto.ResAdminPoi;
import com.autonomous.pm.util.GpsUtil;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
public class TripInfoMem extends CommonMem {
	
	public TripInfoMem(Long idV, TripInfo info) {
		this.setIdV(idV);
		this.setData(info);
	}
	
	public TripInfoMem() {
	}
	
	/**
	 * @param raw
	 */
	public void setDataByEntity(TTrpRaw raw) {
		TripInfo info = new TripInfo();

		if ( raw.getArvDt() != null ) {
			info.setTs(raw.getArvDt());
		} else {
			info.setTs(raw.getDptDt());
		}
		List<ResAdminPoi> pois = MemDB.POI.selectAll();
		
		ResAdminPoi destPoi = pois.stream().filter(p->p.getPoiCd().equals(raw.getDestPoiCd())).findFirst().orElse(null);
		if ( destPoi != null ) {
			Poi to = new Poi();
			log.debug("info.getTo()" + info.getTo());
			log.debug("destPoi.getTerm()" + destPoi.getTerm());
			to.setTerm(destPoi.getTerm());
			to.setGrp(destPoi.getGopCd());
			to.setGrpnm(destPoi.getGopNm());
			to.setPoicd(destPoi.getPoiCd());

			to.setPoinm(destPoi.getPoiNm());
			to.setTy(destPoi.getPoiTy());
			to.setLat(destPoi.getLat());
			to.setLng(destPoi.getLng());
			
			info.setTo(to);
		}
		
		
		if ( raw.getArvDt() != null) {
			info.setTy(2);	// 종료일시가 있으면 END(2) 로 간주
		} else if ( raw.getDptDt() != null) {
			info.setTy(1);	// 시작일시가 있으면 START(1) 로 간주
		} else {
			info.setTy(1);	// 시작일시가 있으면 START(1) 로 간주
		}
		
		info.setResult(raw.getFinRslt());
		info.setReason(raw.getRsn());
		info.setFlghtnum(raw.getFlightnum());
		info.setDist(raw.getDist());
		
		this.setIdV(raw.getIdV());
		this.setData(info);
	}
	
	@Override
	public TripInfo getData() {
		return (TripInfo) data;
	}
	
	public TTrpRaw toEntity() {
		TTrpRaw entity = new TTrpRaw();
		TripInfo info = this.getData();
		Long idV = this.getIdV();
		entity.setIdTrip(null);
		entity.setIdV(idV);
		if ( info.getTy() == 1) {	// 1=start. TRIP출발보고
//			entity.setDptDt(info.getTs());	// 출발시간 기록
			entity.setDptDt(new Date());	// 출발시간 기록	// 차량의시간과 서버의 시간이 다른경우 시작시간보다 종료시간이 빠르게 되는 역전 현상이 발생할 수 있으므로 시스템시간으로 통일한다.
			
			// 차량의 가장 마지막 SensorInfo 정보를 가져와 좌석정보와 화물적재 정보를 입력해준다.
			SensorInfoMem wsi = MemDB.SNSR.select(idV);
			if ( wsi != null && wsi.getData() != null) {
				SensorInfo si = wsi.getData();
				entity.setSeat(si.getSeatToByte());
				entity.setCargo(si.getLoadToByte());
			}
			
			// 차량의 가장 마지막 DrivingInfo 정보를 가져와 좌표를 가져온다.
			// 해당 좌표를 기준으로 Poi 리스트 중 가장 가까운 Poi를 찾는다.
			ResAdminPoi closestPoi = GpsUtil.findClosestPoi(this.idV);
			if(closestPoi!=null) {
				entity.setFromPoiCd(closestPoi.getPoiCd());
			}
			
		} else if ( info.getTy() == 2) {	// 2=end. TRIP종료보고
			entity.setArvDt(info.getTs());
			entity.setFinRslt(info.getResult());	// 완료 상태
			if ( info.getResult() == 1 ) {				// 운행실패시
				entity.setRsn(info.getReason());		// 실패이유
			}
			entity.setDist(info.getDist());				// 주행거리(m단위)
		}
		
		if ( info.getTo() != null ) {
			entity.setDestPoiCd(info.getTo().getPoicd());
		}
		
		entity.setFlightnum(info.getFlghtnum());
		entity.setcDt(null);
		entity.setmDt(null);
		
		return entity;
		
	}
	
	public ResAdminPoi getCloestPoi() {
		return GpsUtil.findClosestPoi(this.idV);
	}

	@Override
	public String toString() {
		return "TripInfoMem [" + (idV != null ? "idV=" + idV + ", " : "") + "dirty=" + dirty + ", "
				+ (data != null ? "data=" + getData() : "") + "]";
	}
	
	
}