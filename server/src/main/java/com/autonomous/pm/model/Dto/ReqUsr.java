package com.autonomous.pm.model.Dto;

import java.util.Date;

import com.autonomous.pm.model.Do.TUsr;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ReqUsr {

//	@NonNull
	private Long idUsr;
	private String loginId;
	private String pwd;
	private String usrNm;
	private Long idUgrp;
	private Date cDt;
	private Date mDt;
	private Date dDt;
	
	@ApiModelProperty(dataType = "java.lang.Integer")
	private Byte dFlg;
	
	private Byte activ;
	
	public Date getcDt() {
		return cDt;
	}
	public void setcDt(Date cDt) {
		this.cDt = cDt;
	}
	public Date getmDt() {
		return mDt;
	}
	public void setmDt(Date mDt) {
		this.mDt = mDt;
	}
	public Date getdDt() {
		return dDt;
	}
	public void setdDt(Date dDt) {
		this.dDt = dDt;
	}
	public Byte getdFlg() {
		return dFlg;
	}
	public void setdFlg(Byte dFlg) {
		this.dFlg = dFlg;
	}
	public Long getIdUsr() {
		return idUsr;
	}
	public String getLoginId() {
		return loginId;
	}
	public String getPwd() {
		return pwd;
	}
	public String getUsrNm() {
		return usrNm;
	}
	public Long getIdUgrp() {
		return idUgrp;
	}
	public Byte getActiv() {
		return activ;
	}
	
	public TUsr toEntity() {
		TUsr t = new TUsr();
		t.setIdUsr(this.idUsr);
		t.setLoginId(this.loginId);
		t.setPwd(this.pwd);
		t.setUsrNm(this.usrNm);
		t.setIdUgrp(this.idUgrp);
		t.setcDt(this.cDt);
		t.setmDt(this.mDt);
		t.setdDt(this.dDt);
		t.setdFlg(this.dFlg);
		t.setActiv(this.activ);
		return t;
	}
	
}