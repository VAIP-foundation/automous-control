package com.autonomous.pm.model.Dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Setter;
import lombok.ToString;

@Setter
@ToString
public class ResAdminUsr {

	private int idUsr;		// 사용자ID
	private String loginId;	// 로그인ID
	private String usrNm;	// 사용자이름
//	private String pwd;		// 로그인암호
	private int idUgrp;		// 사용자그룹ID
	private String grpNm;	// 사용자그룹이름
	private int athCd;		// 권한코드
	private int athLvl;		// 권한레벨
	private String athNm;	// 권한명
	
	@ApiModelProperty(dataType = "java.lang.Integer")
	private Byte dFlg;		// 삭제여부
	
	public Byte getdFlg() {
		return dFlg;
	}
	public void setdFlg(Byte dFlg) {
		this.dFlg = dFlg;
	}
	public int getIdUsr() {
		return idUsr;
	}
	public String getLoginId() {
		return loginId;
	}
	public String getUsrNm() {
		return usrNm;
	}
	public int getIdUgrp() {
		return idUgrp;
	}
	public String getGrpNm() {
		return grpNm;
	}
	public int getAthCd() {
		return athCd;
	}
	public int getAthLvl() {
		return athLvl;
	}
	public String getAthNm() {
		return athNm;
	}
	
}