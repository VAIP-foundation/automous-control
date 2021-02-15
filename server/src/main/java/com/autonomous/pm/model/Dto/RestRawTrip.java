package com.autonomous.pm.model.Dto;

import java.util.Date;

public class RestRawTrip {
	String search; // 기간
	String term; // 터미널
	Long idV; // 차량ID
	String vrn; // 차량명
	Date dptDt; // 출발일시
	Date arvDt; // 도착일시
	Date callDt; // 배차일시
	String fromPoiCd; // 출발 POI Code
	String fromPoiNm; // 출발 POI 이름
	String destPoiCd; // 도착 POI Code
	String destPoiNm; // 도착 POI 이름
	String rsnNm; // 종료사유
	Integer drKm; // 주행거리
	String isSeat; // 탑승여부
	Integer peoplCnt; // 탑승인원수
	String isLoad; // 적재여부
	Integer stars; // 만족도조사점수
	
	public String getSearch() {
		return search;
	}
	public void setSearch(String search) {
		this.search = search;
	}
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
	public Date getDptDt() {
		return dptDt;
	}
	public void setDptDt(Date dptDt) {
		this.dptDt = dptDt;
	}
	public Date getArvDt() {
		return arvDt;
	}
	public void setArvDt(Date arvDt) {
		this.arvDt = arvDt;
	}
	public Date getCallDt() {
		return callDt;
	}
	public void setCallDt(Date callDt) {
		this.callDt = callDt;
	}
	public String getFromPoiCd() {
		return fromPoiCd;
	}
	public void setFromPoiCd(String fromPoiCd) {
		this.fromPoiCd = fromPoiCd;
	}
	public String getFromPoiNm() {
		return fromPoiNm;
	}
	public void setFromPoiNm(String fromPoiNm) {
		this.fromPoiNm = fromPoiNm;
	}
	public String getDestPoiCd() {
		return destPoiCd;
	}
	public void setDestPoiCd(String destPoiCd) {
		this.destPoiCd = destPoiCd;
	}
	public String getDestPoiNm() {
		return destPoiNm;
	}
	public void setDestPoiNm(String destPoiNm) {
		this.destPoiNm = destPoiNm;
	}
	public String getRsnNm() {
		return rsnNm;
	}
	public void setRsnNm(String rsnNm) {
		this.rsnNm = rsnNm;
	}
	public Integer getDrKm() {
		return drKm;
	}
	public void setDrKm(Integer drKm) {
		this.drKm = drKm;
	}
	public String getIsSeat() {
		return isSeat;
	}
	public void setIsSeat(String isSeat) {
		this.isSeat = isSeat;
	}
	public Integer getPeoplCnt() {
		return peoplCnt;
	}
	public void setPeoplCnt(Integer peoplCnt) {
		this.peoplCnt = peoplCnt;
	}
	public String getIsLoad() {
		return isLoad;
	}
	public void setIsLoad(String isLoad) {
		this.isLoad = isLoad;
	}
	public Integer getStars() {
		return stars;
	}
	public void setStars(Integer stars) {
		this.stars = stars;
	}
	
}