package com.autonomous.pm.model.Dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class ResRStopCnt {

	private Long idV;	// 차량ID
	private String vrn;	// 차량번호
	private String term;// 터미널
//	private String dts;	// 일자
	private int rStopCnt;	// 원격정지횟수
	
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
//	public String getDts() {
//		return dts;
//	}
//	public void setDts(String dts) {
//		this.dts = dts;
//	}
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public int getrStopCnt() {
		return rStopCnt;
	}
	public void setrStopCnt(int rStopCnt) {
		this.rStopCnt = rStopCnt;
	}
	
	
	
}