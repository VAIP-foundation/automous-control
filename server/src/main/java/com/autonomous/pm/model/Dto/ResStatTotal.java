package com.autonomous.pm.model.Dto;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@RequiredArgsConstructor
public class ResStatTotal
{
	
	private Long idV;	// 차량ID
	private String vrn;	// 차량번호
	private String term;// 터미널
//	private String dts;	// 일자
	private int drKm;	// 운행거리
	private int drTmS;	// 운행시간
	private int tripCnt;	// 운행횟수
	private int rStopCnt;	// 긴급정지
	private int mStopCnt;	// 수동정지
	private int meStopCnt;	// 긴급정지
	private int cnflctCnt;	// 고장추돌
	private int failCnt;	// 고장
	private int peoplCnt;	// 이용자수
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
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
//	public String getDts() {
//		return dts;
//	}
//	public void setDts(String dts) {
//		this.dts = dts;
//	}
	public int getDrKm() {
		return drKm;
	}
	public void setDrKm(int drKm) {
		this.drKm = drKm;
	}
	public int getDrTmS() {
		return drTmS;
	}
	public void setDrTmS(int drTmS) {
		this.drTmS = drTmS;
	}
	public int getTripCnt() {
		return tripCnt;
	}
	public void setTripCnt(int tripCnt) {
		this.tripCnt = tripCnt;
	}
	public int getrStopCnt() {
		return rStopCnt;
	}
	public void setrStopCnt(int rStopCnt) {
		this.rStopCnt = rStopCnt;
	}
	public int getCnflctCnt() {
		return cnflctCnt;
	}
	public void setCnflctCnt(int cnflctCnt) {
		this.cnflctCnt = cnflctCnt;
	}
	public int getFailCnt() {
		return failCnt;
	}
	public void setFailCnt(int failCnt) {
		this.failCnt = failCnt;
	}
	public int getPeoplCnt() {
		return peoplCnt;
	}
	public void setPeoplCnt(int peoplCnt) {
		this.peoplCnt = peoplCnt;
	}
	public int getmStopCnt() {
		return mStopCnt;
	}
	public void setmStopCnt(int mStopCnt) {
		this.mStopCnt = mStopCnt;
	}
	public int getMeStopCnt() {
		return meStopCnt;
	}
	public void setMeStopCnt(int meStopCnt) {
		this.meStopCnt = meStopCnt;
	}
	
    
}