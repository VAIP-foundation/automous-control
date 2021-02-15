package com.autonomous.pm.model.Dto;

import java.util.Date;

public class DrvRawYymm {
    private Long idDr;
    private Long idV;
    private String tss;
    private Float spd;
    private Integer hdng;
    private Integer dStts;
    private Byte front;
    private Integer angl;
    private Byte brk;
    private Integer drTmS;
    private Byte drAuto;
    private Date cDt;
    private Double lng;
    private Double lat;
    
	public Long getIdDr() {
		return idDr;
	}
	public void setIdDr(Long idDr) {
		this.idDr = idDr;
	}
	public Long getIdV() {
		return idV;
	}
	public void setIdV(Long idV) {
		this.idV = idV;
	}
	public String getTss() {
		return tss;
	}
	public void setTss(String tss) {
		this.tss = tss;
	}
	public Float getSpd() {
		return spd;
	}
	public void setSpd(Float spd) {
		this.spd = spd;
	}
	public Integer getHdng() {
		return hdng;
	}
	public void setHdng(Integer hdng) {
		this.hdng = hdng;
	}
	public Integer getdStts() {
		return dStts;
	}
	public void setdStts(Integer dStts) {
		this.dStts = dStts;
	}
	public Byte getFront() {
		return front;
	}
	public void setFront(Byte front) {
		this.front = front;
	}
	public Integer getAngl() {
		return angl;
	}
	public void setAngl(Integer angl) {
		this.angl = angl;
	}
	public Byte getBrk() {
		return brk;
	}
	public void setBrk(Byte brk) {
		this.brk = brk;
	}
	public Integer getDrTmS() {
		return drTmS;
	}
	public void setDrTmS(Integer drTmS) {
		this.drTmS = drTmS;
	}
	public Byte getDrAuto() {
		return drAuto;
	}
	public void setDrAuto(Byte drAuto) {
		this.drAuto = drAuto;
	}
	public Date getcDt() {
		return cDt;
	}
	public void setcDt(Date cDt) {
		this.cDt = cDt;
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
	
	@Override
	public String toString() {
		return "DrvRawYymm [" + (idDr != null ? "idDr=" + idDr + ", " : "") + (idV != null ? "idV=" + idV + ", " : "")
				+ (tss != null ? "tss=" + tss + ", " : "") + (spd != null ? "spd=" + spd + ", " : "")
				+ (hdng != null ? "hdng=" + hdng + ", " : "") + (dStts != null ? "dStts=" + dStts + ", " : "")
				+ (front != null ? "front=" + front + ", " : "") + (angl != null ? "angl=" + angl + ", " : "")
				+ (brk != null ? "brk=" + brk + ", " : "") + (drTmS != null ? "drTmS=" + drTmS + ", " : "")
				+ (drAuto != null ? "drAuto=" + drAuto + ", " : "") + (cDt != null ? "cDt=" + cDt + ", " : "")
				+ (lng != null ? "lng=" + lng + ", " : "") + (lat != null ? "lat=" + lat : "") + "]";
	}
    
}