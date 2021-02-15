package com.autonomous.pm.model.Dto;

import java.util.Date;

public class RestRawEvent {
	String term;	// 터미널
	Long idV;		// 차량ID
	String vrn;		// 차량명
	String search;	// 기간
	Date dts;		// 일시
	String evtCd;	// 이벤트코드
	String evtNm;	// 이벤트명
	String failCd;	// fail코드
	String poiNm;	// POI 명
	String poiCd;	// POI code
	Double lng;		// 위도(WGS84)
	Double lat;		// 경도(WGS84)
	Integer hd;		// 방위각
	Float speed;	// 속도(km/h)
	
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public Long getIdV() {
		return idV;
	}
	public void setIdV(Long idV) {
		this.idV = idV;
	}
	public String getVrn() {
		return vrn;
	}
	public void setVrn(String vrn) {
		this.vrn = vrn;
	}
	public Date getDts() {
		return dts;
	}
	public void setDts(Date dts) {
		this.dts = dts;
	}
	public String getEvtCd() {
		return evtCd;
	}
	public void setEvtCd(String evtCd) {
		this.evtCd = evtCd;
	}
	public String getEvtNm() {
		return evtNm;
	}
	public void setEvtNm(String evtNm) {
		this.evtNm = evtNm;
	}
	public String getPoiNm() {
		return poiNm;
	}
	public void setPoiNm(String poiNm) {
		this.poiNm = poiNm;
	}
	public String getPoiCd() {
		return poiCd;
	}
	public void setPoiCd(String poiCd) {
		this.poiCd = poiCd;
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
	public Integer getHd() {
		return hd;
	}
	public void setHd(Integer hd) {
		this.hd = hd;
	}
	public Float getSpeed() {
		return speed;
	}
	public void setSpeed(Float speed) {
		this.speed = speed;
	}
	public String getSearch() {
		return search;
	}
	public void setSearch(String search) {
		this.search = search;
	}
	public String getFailCd() {
		return failCd;
	}
	public void setFailCd(String failCd) {
		this.failCd = failCd;
	}
	
	
}