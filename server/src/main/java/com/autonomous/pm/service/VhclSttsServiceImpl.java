package com.autonomous.pm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autonomous.pm.dao.TrpRawMapper;
import com.autonomous.pm.dao.VhclSttsMapper;
import com.autonomous.pm.domain.structure.DrivingInfo;
import com.autonomous.pm.domain.structure.Gps;
import com.autonomous.pm.domain.structure.SensorInfo;
import com.autonomous.pm.domain.structure.TripInfo;
import com.autonomous.pm.memcache.MemDB;
import com.autonomous.pm.model.Do.TTrpRaw;
import com.autonomous.pm.model.Do.TVhclStts;
import com.autonomous.pm.model.Do.VPoiGate;
import com.autonomous.pm.model.Do.VVhcl;
import com.autonomous.pm.model.Dto.ResAdminPoi;
import com.autonomous.pm.model.Dto.VhclStts;
import com.autonomous.pm.util.GpsUtil;

@Service
public class VhclSttsServiceImpl implements VhclSttsService {

	public static final Logger logger = LoggerFactory.getLogger(VhclSttsServiceImpl.class);

	@Autowired
	VhclSttsMapper vhclSttsMapper;
	@Autowired
	TrpRawMapper trpRawMapper;
	
	@Override
	public void insert(VhclStts vhclStts) {
		vhclSttsMapper.insertSelective(vhclStts);
	}
	
	@Override
	public void update(VVhcl vvhcl) {
		if (vvhcl != null) {
			VhclStts vhclStts = new VhclStts();
			vhclStts.setIdV(vvhcl.getIdV());
			vhclStts.setSpd(vvhcl.getSpd());
			vhclStts.setDrvStts(vvhcl.getDrvStts());
			vhclStts.setFromPoi(vvhcl.getFromPoi());
			vhclStts.setDestPoi(vvhcl.getDestPoi());
			if (vvhcl.getEstDrTmS() != null) {
				vhclStts.setEstDrTmS(Long.valueOf(vvhcl.getEstDrTmS()));
			}
			vhclStts.setIsSnsr(vvhcl.getIdSnsr());
			vhclStts.setIdDr(vvhcl.getIdDr());
			vhclStts.setIdTrip(vvhcl.getIdTrip());
			vhclStts.setLng(vvhcl.getLng());
			vhclStts.setLat(vvhcl.getLat());
			vhclSttsMapper.updateByIdvSelective(vhclStts);
		}
	}
	
	@Override
	public void update(Long idV, TVhclStts vhclStts) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Update 대상: MemDB.VHCL, DB.T_VHCL_STTS
	 */
	@Override
	public void update(Long idV, TripInfo tripInfo) {
		
		Integer tripTy = tripInfo.getTy();			
		Integer tripResult = tripInfo.getResult();	
		Integer tripReason = tripInfo.getReason();	
		Integer drvStts = null;	
		Long fromPoi = null;	
		Long destPoi = null;	
		String destFlightnum = null; 
		
		
		if ( tripTy == 1) {												// --- TRIP 출발보고 시 ---
			drvStts = 2;												// 차량상태 -> 2:주행중
			Gps lastGps = MemDB.DRV.select(idV).getData().getGps();
			ResAdminPoi closestPoi = GpsUtil.findClosestPoi(idV);
			fromPoi = closestPoi != null ? closestPoi.getIdPoi() : null;// 출발지 -> 가장가까운 GPS로 판단된 POI의 idPoi
			if ( tripInfo.getTo() != null ) {
				String toPoiCd = tripInfo.getTo().getPoicd();
				ResAdminPoi idPoi = MemDB.POI.selectAll().stream()
						.filter(p->p.getPoiCd().equals(toPoiCd))
						.findFirst().orElse(null);
				destPoi = idPoi != null ? idPoi.getIdPoi() : null;		// 도착지 -> 목적지 poiCd로 찾아낸 idPoi값
				
			}
			destFlightnum = tripInfo.getFlghtnum();
			
		} else if ( tripTy == 2) {					
			if ( tripResult == 1) {					
				if ( tripReason == 1) {				
					drvStts = 11;
				} else if ( tripReason == 2) {		
					drvStts = 1;
				} else if ( tripReason == 3) {		
					drvStts = 21;
				} else if ( tripReason == 4) {		
					drvStts = 2;
				} else if ( tripReason == 5) {		
					drvStts = 12;
				} else if ( tripReason == 100) {	
					drvStts = 0;
				} else {							
					drvStts = 1;
				}
				
			} else if ( tripResult == 2) {			
				drvStts = 1;						
			}
		}
		
		TTrpRaw lastTrpRaw = trpRawMapper.selectLastByIdV(idV);
		Long lastIdTrip = lastTrpRaw.getIdTrip();	// TRIP 정보
		
		
		// MemDB.VHCL 업데이트
		VVhcl vhcl = MemDB.VHCL.select(idV);
		vhcl.setDrvStts(drvStts);
		vhcl.setIdTrip(lastIdTrip);
		if ( tripTy == 1 ) {
			vhcl.setFromPoi(fromPoi);
			vhcl.setDestPoi(destPoi);
			vhcl.setDestFlightnum(destFlightnum);
			
		} else if ( tripTy == 2 ) {	
//			vhcl.setFromPoi(null);
//			vhcl.setDestPoi(null);
		}
		MemDB.VHCL.insertSafety(idV, vhcl);
		
		
		// DB.T_VHCL_STTS 업데이트
		VhclStts vhclStts = new VhclStts();
		vhclStts.setIdV(idV);
		vhclStts.setIdTrip(lastIdTrip);
		if ( tripTy == 1 ) {
			vhclStts.setFromPoi(fromPoi);
			vhclStts.setDestPoi(destPoi);
			vhclStts.setDestFlightnum(destFlightnum);
			vhclSttsMapper.updateFromDest(vhclStts);	// from과 dest는 null일 수도 있으므로 별도로 업데이트 처리를 해준다
		} else if ( tripTy == 2 ) {
		}
		int result = vhclSttsMapper.updateByIdvSelective(vhclStts);
		
		// 아직 차량현재상태 정보가 없을 경우 등록해준다
		if (result == 0 ) {
			insert(vhclStts);
		}
		
	}

	@Override
	public void update(Long idV, DrivingInfo drivingInfo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Long idV, SensorInfo sensorInfo) {
		// TODO Auto-generated method stub
		
	}


}
