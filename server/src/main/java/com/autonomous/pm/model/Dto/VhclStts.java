package com.autonomous.pm.model.Dto;

import java.util.Date;

public class VhclStts {
    private Long seq;
    private Long idV;
    private Float spd;
    private Integer drvStts;
    private Long fromPoi;
    private Long destPoi;
    private String destFlightnum;
    private Long estDrTmS;
    private Long isSnsr;
    private Long idDr;
    private Long idTrip;
    private Date cDt;
    private Date mDt;
    private Double lng;
    private Double lat;
    public Long getSeq() {
        return seq;
    }
    public void setSeq(Long seq) {
        this.seq = seq;
    }
    public Long getIdV() {
        return idV;
    }
    public void setIdV(Long idV) {
        this.idV = idV;
    }
    public Float getSpd() {
        return spd;
    }
    public void setSpd(Float spd) {
        this.spd = spd;
    }
    public Integer getDrvStts() {
        return drvStts;
    }
    public void setDrvStts(Integer drvStts) {
        this.drvStts = drvStts;
    }
    public Long getFromPoi() {
        return fromPoi;
    }
    public void setFromPoi(Long fromPoi) {
        this.fromPoi = fromPoi;
    }
    public Long getDestPoi() {
        return destPoi;
    }
    public void setDestPoi(Long destPoi) {
        this.destPoi = destPoi;
    }
    public Long getEstDrTmS() {
        return estDrTmS;
    }
    public void setEstDrTmS(Long estDrTmS) {
        this.estDrTmS = estDrTmS;
    }
    public Long getIsSnsr() {
        return isSnsr;
    }
    public void setIsSnsr(Long isSnsr) {
        this.isSnsr = isSnsr;
    }
    public Long getIdDr() {
        return idDr;
    }
    public void setIdDr(Long idDr) {
        this.idDr = idDr;
    }
    public Long getIdTrip() {
        return idTrip;
    }
    public void setIdTrip(Long idTrip) {
        this.idTrip = idTrip;
    }
    public Date getcDt() {
        return cDt;
    }
    public void setcDt(Date cDt) {
        this.cDt = cDt;
    }
    public Date getmDt() {
        return mDt;
    }
    public void setmDt(Date mDt) {
        this.mDt = mDt;
    }
	public Double getLng() {
		return lng;
	}
	public void setLng(Double lng) {
		this.lng = lng;
	}
	public Double getLat() {
		return lat;
	}
	public void setLat(Double lat) {
		this.lat = lat;
	}
	public String getDestFlightnum() {
		return destFlightnum;
	}
	public void setDestFlightnum(String destFlightnum) {
		this.destFlightnum = destFlightnum;
	}
    
}