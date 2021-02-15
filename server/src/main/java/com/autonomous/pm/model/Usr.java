package com.autonomous.pm.model;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModelProperty;

import com.autonomous.pm.model.Do.TUsrGrp;
import com.autonomous.pm.model.Do.TUsrAth;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@NoArgsConstructor
@AllArgsConstructor(access=AccessLevel.PRIVATE)
@JsonInclude(value = Include.NON_EMPTY)
@Builder
public class Usr
{
	/* T_USR */
	private Long idUsr;
	@NotBlank(message = "ID는 필수 입력값입니다.")
	private String loginId;
	private String pwd;
	private String usrNm;
	private Long idUgrp;
	
	@ApiModelProperty(dataType = "java.lang.Integer")
	private Byte dFlg;	// 삭제
	private Byte activ;	// 사용가능여부: 1=사용중,0=disable
	
	/* T_USR_GRP */
	private String grpNm;
	
	/* T_USR_ATH */
	private Integer athCd;
	private Integer athLvl;
	private String athNm;
	
	
    private TUsrGrp usrGrp;
    private TUsrAth usrAth;
    
    public TUsrGrp getUsrGrp() {
		return usrGrp;
	}
    public TUsrAth getUsrAth() {
		return usrAth;
	}
	public Long getIdUsr() {
		return idUsr;
	}
	public void setIdUsr(Long idUsr) {
		this.idUsr = idUsr;
	}
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getUsrNm() {
		return usrNm;
	}
	public void setUsrNm(String usrNm) {
		this.usrNm = usrNm;
	}
	public Long getIdUgrp() {
		return idUgrp;
	}
	public void setIdUgrp(Long idUgrp) {
		this.idUgrp = idUgrp;
	}
	public Byte getdFlg() {
		return dFlg;
	}
	public void setdFlg(Byte dFlg) {
		this.dFlg = dFlg;
	}
	public Byte getActiv() {
		return activ;
	}
	public void setActiv(Byte activ) {
		this.activ = activ;
	}
	public String getGrpNm() {
		return grpNm;
	}
	public void setGrpNm(String grpNm) {
		this.grpNm = grpNm;
	}
	public Integer getAthCd() {
		return athCd;
	}
	public void setAthCd(Integer athCd) {
		this.athCd = athCd;
	}
	public Integer getAthLvl() {
		return athLvl;
	}
	public void setAthLvl(Integer athLvl) {
		this.athLvl = athLvl;
	}
	public String getAthNm() {
		return athNm;
	}
	public void setAthNm(String athNm) {
		this.athNm = athNm;
	}
	public void setUsrGrp(TUsrGrp usrGrp) {
		this.usrGrp = usrGrp;
	}
	public void setUsrAth(TUsrAth usrAth) {
		this.usrAth = usrAth;
	}
    
    
    
    
    
}