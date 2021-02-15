package com.autonomous.pm.model.Dto;

import lombok.ToString;

@ToString
public class ResMeStopCnt {

	private Long idV;		// 차량ID
	private String vrn;		// 차량번호
	private String term;	// 터미널
	private int meStopCnt;	// 원격정지횟수
	
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
	public int getMeStopCnt() {
		return meStopCnt;
	}
	public void setMeStopCnt(int meStopCnt) {
		this.meStopCnt = meStopCnt;
	}
	
}