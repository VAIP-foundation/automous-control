package com.autonomous.pm.domain.structure.mem;

import com.autonomous.pm.domain.structure.DrivingInfo;
import com.autonomous.pm.domain.structure.Gps;
import com.autonomous.pm.model.Dto.DrvRawYymm;
import com.autonomous.pm.util.DateUtil;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DrivingInfoMem extends CommonMem {
	
	public DrivingInfoMem(Long idV, DrivingInfo info) {
		this.setIdV(idV);
		this.setData(info);
	}
	
	public DrivingInfoMem() {
	}
	
	public DrivingInfo getData() {
		return (DrivingInfo) data;
	}
	
	/**
	 * @param raw
	 */
	public void setDataByEntity(DrvRawYymm raw) {
		DrivingInfo info = new DrivingInfo();
		
		info.setTs(DateUtil.convertStringToUtcDate(raw.getTss(), "YYYYMMDDhhmiss"));
		info.setSpeed(raw.getSpd());
		Gps gps = new Gps();
		gps.setLat(raw.getLat());
		gps.setLng(raw.getLng());
		gps.setHd(raw.getHdng());
		info.setGps(gps);
		
		info.setStts(raw.getdStts());
		info.setFront((raw.getFront()==1));
		info.setSwa(raw.getAngl());
		info.setBrk((raw.getBrk()==1));
		info.setDtm(raw.getDrTmS());
		info.setAuto((raw.getDrAuto()==1));
		
		this.setIdV(raw.getIdV());
		this.setData(info);
	}
	
	public DrvRawYymm toEntity() {
		DrvRawYymm entity = new DrvRawYymm();
		DrivingInfo info = this.getData();
		entity.setIdDr(null);
		entity.setIdV(this.getIdV());
		entity.setTss(DateUtil.getUtcDatetimeByFormat(info.getTs(), "YYYYMMDDhhmiss"));
		entity.setSpd(info.getSpeed() == null ? 0: info.getSpeed());
		if ( info.getGps() != null ) {
			entity.setHdng(info.getGps().getHd());
			entity.setLng(info.getGps().getLng());
			entity.setLat(info.getGps().getLat());
		}
		entity.setdStts(info.getStts());
		entity.setFront((byte)(info.isFront()?1:0));
		entity.setAngl(info.getSwa());
		entity.setBrk((byte)(info.isBrk()?1:0));
		entity.setDrTmS(info.getDtm());
		entity.setDrAuto((byte)(info.isAuto()?1:0));
		entity.setcDt(null);
		return entity;
	}

	@Override
	public String toString() {
		return "DrivingInfoMem [" + (idV != null ? "idV=" + idV + ", " : "") + "dirty=" + dirty + ", "
				+ (data != null ? "data=" + getData() : "") + "]";
	}
	
	
}