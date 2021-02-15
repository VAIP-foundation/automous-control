package com.autonomous.pm.model.Dto;

import java.util.Date;

public class RouteHst {
    private Long idRoute;
    private Long idV;
    private String dts;
    private Double fromLat;
	private Double fromLng;
	private Double toLat;
	private Double toLng;
    private String points;
    private Date cDt;
	public Long getIdRoute() {
		return idRoute;
	}
	public void setIdRoute(Long idRoute) {
		this.idRoute = idRoute;
	}
	public Long getIdV() {
		return idV;
	}
	public void setIdV(Long idV) {
		this.idV = idV;
	}
	public String getDts() {
		return dts;
	}
	public void setDts(String dts) {
		this.dts = dts;
	}
	public Double getFromLat() {
		return fromLat;
	}
	public void setFromLat(Double fromLat) {
		this.fromLat = fromLat;
	}
	public Double getFromLng() {
		return fromLng;
	}
	public void setFromLng(Double fromLng) {
		this.fromLng = fromLng;
	}
	public Double getToLat() {
		return toLat;
	}
	public void setToLat(Double toLat) {
		this.toLat = toLat;
	}
	public Double getToLng() {
		return toLng;
	}
	public void setToLng(Double toLng) {
		this.toLng = toLng;
	}
	public String getPoints() {
		return points;
	}
	public void setPoints(String points) {
		this.points = points;
	}
	public Date getcDt() {
		return cDt;
	}
	public void setcDt(Date cDt) {
		this.cDt = cDt;
	}
	@Override
	public String toString() {
		return "RouteHst [" + (idRoute != null ? "idRoute=" + idRoute + ", " : "")
				+ (idV != null ? "idV=" + idV + ", " : "") + (dts != null ? "dts=" + dts + ", " : "") + "fromLat="
				+ fromLat + ", fromLng=" + fromLng + ", toLat=" + toLat + ", toLng=" + toLng + ", "
				+ (points != null ? "points=" + points + ", " : "") + (cDt != null ? "cDt=" + cDt : "") + "]";
	}
    
    
    
}