package com.autonomous.pm.model.Dto;

import lombok.ToString;

@ToString
public class ResMStopCnt {

	private Long idV;		// 차량ID
	private String vrn;		// 차량번호
	private String term;	// 터미널
	private int mStopCnt;	// 승객정지횟수
	
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
	public int getmStopCnt() {
		return mStopCnt;
	}
	public void setmStopCnt(int mStopCnt) {
		this.mStopCnt = mStopCnt;
	}
	
}