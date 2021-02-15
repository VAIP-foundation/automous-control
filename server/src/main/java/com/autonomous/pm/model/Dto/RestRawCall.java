package com.autonomous.pm.model.Dto;

import java.util.Date;

public class RestRawCall {
	String search; // 기간
	String term; // 터미널
	Long idV; // 차량ID
	String vrn; // 차량명
	Date callDt; // 배차일시
	String callTy; // 배차방식
	String destPoiCd; // 목적지 POI Code
	String destPoiNm; // 목적지 POI 이름
	String isSeat; // 탑승여부
	Date dptDt; // 운행시작일시
	Date arvDt; // 운행종료일시
	
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
	public Date getCallDt() {
		return callDt;
	}
	public void setCallDt(Date callDt) {
		this.callDt = callDt;
	}
	public String getCallTy() {
		return callTy;
	}
	public void setCallTy(String callTy) {
		this.callTy = callTy;
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
	public String getIsSeat() {
		return isSeat;
	}
	public void setIsSeat(String isSeat) {
		this.isSeat = isSeat;
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
	
}