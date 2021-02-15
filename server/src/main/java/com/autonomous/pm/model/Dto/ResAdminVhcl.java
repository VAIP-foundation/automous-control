package com.autonomous.pm.model.Dto;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class ResAdminVhcl {

	private int idV;		// 차량ID [차량ID,SEQ]
	private String vrn;	// 차량번호
	private String stts;	// 상태 [1=정상,2=고장]
	private String activ;	// 사용가능여부 [1=사용중,0=disable]
	private int vBase;		// 회차장소 [차량ID,SEQ]
	private String vBaseNm;		// 회차장소 명 [poiNm]
	private String vclr;		// 차량색상
	private int idGop;		// 권역ID [Group Of POI  ID,SEQ]
	private String gopCd;		// 권역CODE [권역CODE.PM차량과 공유한다.ex: T1WI,T2EO]
	private String gopNm;	// 권역명 ["권역명.PM차량과 공유한다.ex:T1 West 입국장"]
	private String term;	// 터미널 [T1=터미널1,T2=터미널2]
	
	@ApiModelProperty(dataType = "java.lang.Integer")
	private Byte dFlg;		// 삭제여부 [0=정상,1=삭제]
	
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
	public String getStts() {
		return stts;
	}
	public void setStts(String stts) {
		this.stts = stts;
	}
	public String getActiv() {
		return activ;
	}
	public void setActiv(String activ) {
		this.activ = activ;
	}
	public int getvBase() {
		return vBase;
	}
	public void setvBase(int vBase) {
		this.vBase = vBase;
	}
	public String getVclr() {
		return vclr;
	}
	public void setVclr(String vclr) {
		this.vclr = vclr;
	}
	public int getIdGop() {
		return idGop;
	}
	public void setIdGop(int idGop) {
		this.idGop = idGop;
	}
	public String getGopCd() {
		return gopCd;
	}
	public void setGopCd(String gopCd) {
		this.gopCd = gopCd;
	}
	public String getGopNm() {
		return gopNm;
	}
	public void setGopNm(String gopNm) {
		this.gopNm = gopNm;
	}
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public Byte getdFlg() {
		return dFlg;
	}
	public void setdFlg(Byte dFlg) {
		this.dFlg = dFlg;
	}
	public String getvBaseNm() {
		return vBaseNm;
	}
	public void setvBaseNm(String vBaseNm) {
		this.vBaseNm = vBaseNm;
	}
	
	
}