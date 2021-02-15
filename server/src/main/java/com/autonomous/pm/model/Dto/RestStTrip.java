package com.autonomous.pm.model.Dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class RestStTrip {
	private String term;
	private Long idV;
	private String vrn;
	private String dts;
//	private Integer dist;
//	private Integer drvTm;
//	private Integer users;
	private Integer drKm;
	private Integer drTmS;
	private Integer peoplCnt;
	private Integer tripCnt;
	private Integer loadCnt;
	private Integer resumeCnt;
	private Integer cnflctCnt;
	private Integer failCnt;
	private Integer chargeStartCnt;
	private Integer chargeStopCnt;
	private Integer mStopCnt;
	private Integer mStopResumeCnt;
	private Integer callchangeOriginCnt;
	private Integer callchangeNewCnt;
	private Integer mCallCnclCnt;
	private Integer mStopResumeTimeoutCnt;
	private Integer mHolding;
	private Integer mEStopCnt;
	private Integer rStopCnt;
	private Integer rStopResumeCnt;

	@JsonIgnore
	private String fromPoiCnt;
	@JsonIgnore
	private String destPoiCnt;
	
	
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
	public String getDts() {
		return dts;
	}
	public void setDts(String dts) {
		this.dts = dts;
	}
	public Integer getDrKm() {
		return drKm;
	}
	public void setDrKm(Integer drKm) {
		this.drKm = drKm;
	}
	public Integer getDrTmS() {
		return drTmS;
	}
	public void setDrTmS(Integer drTmS) {
		this.drTmS = drTmS;
	}
	public Integer getPeoplCnt() {
		return peoplCnt;
	}
	public void setPeoplCnt(Integer peoplCnt) {
		this.peoplCnt = peoplCnt;
	}
	public Integer getTripCnt() {
		return tripCnt;
	}
	public void setTripCnt(Integer tripCnt) {
		this.tripCnt = tripCnt;
	}
	public Integer getLoadCnt() {
		return loadCnt;
	}
	public void setLoadCnt(Integer loadCnt) {
		this.loadCnt = loadCnt;
	}
	public Integer getResumeCnt() {
		return resumeCnt;
	}
	public void setResumeCnt(Integer resumeCnt) {
		this.resumeCnt = resumeCnt;
	}
	public Integer getCnflctCnt() {
		return cnflctCnt;
	}
	public void setCnflctCnt(Integer cnflctCnt) {
		this.cnflctCnt = cnflctCnt;
	}
	public Integer getFailCnt() {
		return failCnt;
	}
	public void setFailCnt(Integer failCnt) {
		this.failCnt = failCnt;
	}
	public Integer getChargeStartCnt() {
		return chargeStartCnt;
	}
	public void setChargeStartCnt(Integer chargeStartCnt) {
		this.chargeStartCnt = chargeStartCnt;
	}
	public Integer getChargeStopCnt() {
		return chargeStopCnt;
	}
	public void setChargeStopCnt(Integer chargeStopCnt) {
		this.chargeStopCnt = chargeStopCnt;
	}
	public Integer getmStopCnt() {
		return mStopCnt;
	}
	public void setmStopCnt(Integer mStopCnt) {
		this.mStopCnt = mStopCnt;
	}
	public Integer getmStopResumeCnt() {
		return mStopResumeCnt;
	}
	public void setmStopResumeCnt(Integer mStopResumeCnt) {
		this.mStopResumeCnt = mStopResumeCnt;
	}
	public Integer getCallchangeOriginCnt() {
		return callchangeOriginCnt;
	}
	public void setCallchangeOriginCnt(Integer callchangeOriginCnt) {
		this.callchangeOriginCnt = callchangeOriginCnt;
	}
	public Integer getCallchangeNewCnt() {
		return callchangeNewCnt;
	}
	public void setCallchangeNewCnt(Integer callchangeNewCnt) {
		this.callchangeNewCnt = callchangeNewCnt;
	}
	public Integer getmCallCnclCnt() {
		return mCallCnclCnt;
	}
	public void setmCallCnclCnt(Integer mCallCnclCnt) {
		this.mCallCnclCnt = mCallCnclCnt;
	}
	public Integer getmStopResumeTimeoutCnt() {
		return mStopResumeTimeoutCnt;
	}
	public void setmStopResumeTimeoutCnt(Integer mStopResumeTimeoutCnt) {
		this.mStopResumeTimeoutCnt = mStopResumeTimeoutCnt;
	}
	public Integer getmHolding() {
		return mHolding;
	}
	public void setmHolding(Integer mHolding) {
		this.mHolding = mHolding;
	}
	public Integer getmEStopCnt() {
		return mEStopCnt;
	}
	public void setmEStopCnt(Integer mEStopCnt) {
		this.mEStopCnt = mEStopCnt;
	}
	public Integer getrStopCnt() {
		return rStopCnt;
	}
	public void setrStopCnt(Integer rStopCnt) {
		this.rStopCnt = rStopCnt;
	}
	public Integer getrStopResumeCnt() {
		return rStopResumeCnt;
	}
	public void setrStopResumeCnt(Integer rStopResumeCnt) {
		this.rStopResumeCnt = rStopResumeCnt;
	}
	public String getFromPoiCnt() {
		return fromPoiCnt;
	}
	public void setFromPoiCnt(String fromPoiCnt) {
		this.fromPoiCnt = fromPoiCnt;
	}
	public String getDestPoiCnt() {
		return destPoiCnt;
	}
	public void setDestPoiCnt(String destPoiCnt) {
		this.destPoiCnt = destPoiCnt;
	}
	
	
	
}