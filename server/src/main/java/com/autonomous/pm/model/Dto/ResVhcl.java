package com.autonomous.pm.model.Dto;

import lombok.ToString;

@ToString
public class ResVhcl {

	private int idV;			// 차량ID(차량코드)
	private String vrn;			// 차량번호
	private String vclr;		// 차량색상
	private Long fromPoi;		// 출발지 POI ID
	private String fromPoiNm;	// 출발지 POI 명
	private Long destPoi;		// 도착지 POI ID
	private String destPoiNm;	// 도착지 POI 명
	private int drvStts;		// 차량상태코드
	private String drvSttsNm;	// 차량상태명
	private Long vBase;			// 회차장소ID
	private String vBaseNm;		// 회차장소명
	public int getIdV() {
		return idV;
	}
	public void setIdV(int idV) {
		this.idV = idV;
	}
	public String getVrn() {
		return vrn;
	}
	public void setVrn(String vrn) {
		this.vrn = vrn;
	}
	public String getVclr() {
		return vclr;
	}
	public void setVclr(String vclr) {
		this.vclr = vclr;
	}
	public Long getFromPoi() {
		return fromPoi;
	}
	public void setFromPoi(Long fromPoi) {
		this.fromPoi = fromPoi;
	}
	public String getFromPoiNm() {
		return fromPoiNm;
	}
	public void setFromPoiNm(String fromPoiNm) {
		this.fromPoiNm = fromPoiNm;
	}
	public Long getDestPoi() {
		return destPoi;
	}
	public void setDestPoi(Long destPoi) {
		this.destPoi = destPoi;
	}
	public String getDestPoiNm() {
		return destPoiNm;
	}
	public void setDestPoiNm(String destPoiNm) {
		this.destPoiNm = destPoiNm;
	}
	public int getDrvStts() {
		return drvStts;
	}
	public void setDrvStts(int drvStts) {
		this.drvStts = drvStts;
	}
	public String getDrvSttsNm() {
		return drvSttsNm;
	}
	public void setDrvSttsNm(String drvSttsNm) {
		this.drvSttsNm = drvSttsNm;
	}
	public Long getvBase() {
		return vBase;
	}
	public void setvBase(Long vBase) {
		this.vBase = vBase;
	}
	public String getvBaseNm() {
		return vBaseNm;
	}
	public void setvBaseNm(String vBaseNm) {
		this.vBaseNm = vBaseNm;
	}
	
}