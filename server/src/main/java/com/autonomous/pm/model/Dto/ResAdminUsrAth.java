package com.autonomous.pm.model.Dto;

import lombok.Setter;
import lombok.ToString;

@Setter
@ToString
public class ResAdminUsrAth {

	private Integer athCd; // 권한코드
	private Integer athLvl;	// 권한레벨
	private String athNm;	// 권한명
	private String dscrpt;	// 권한에 대한 설명
//	private String cDt;	// 생성일시
//	private String mDt;	// 수정일시
	private Byte dFlg;
	
	public Integer getAthCd() {
		return athCd;
	}
	public Integer getAthLvl() {
		return athLvl;
	}
	public String getAthNm() {
		return athNm;
	}
	public String getDscrpt() {
		return dscrpt;
	}
	public Byte getdFlg() {
		return dFlg;
	}
	public void setdFlg(Byte dFlg) {
		this.dFlg = dFlg;
	}
	
}