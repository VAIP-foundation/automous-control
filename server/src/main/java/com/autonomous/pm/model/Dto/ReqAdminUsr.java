package com.autonomous.pm.model.Dto;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.autonomous.pm.model.Do.TUsr;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Setter;
import lombok.ToString;

@Setter
@ToString
public class ReqAdminUsr {

	private Long idUsr; // 사용자ID [ID 존재시 해당 사용자에 update. 미 존재시 신규SEQ로 insert]
	
	@NotNull(message = "로그인ID(loginId) 은 필수 입력 값입니다.")
	private String loginId; // 로그인ID
	
	@NotNull(message = "사용자이름(usrNm) 은 필수 입력 값입니다.")
	private String usrNm; // 사용자이름
	
//	@NotNull(message = "로그인암호(pwd) 은 필수 입력 값입니다.")
	private String pwd; // 로그인암호
	
	@NotNull(message = "사용자그룹ID(idUgrp) 은 필수 입력 값입니다.")	
	private Long idUgrp; // 사용자그룹ID
	
	@NotNull(message = "삭제여부(dFlg) 은 필수 입력 값입니다.")
	@ApiModelProperty(dataType = "java.lang.Integer")
	private Byte dFlg; // 삭제여부 [0=정상,1=삭제]
	
	public TUsr toEntity() {
		TUsr usr = new TUsr();
		usr.setIdUsr(this.idUsr);
		usr.setLoginId(this.loginId);
		usr.setUsrNm(this.usrNm);
		usr.setPwd(this.pwd);
		usr.setIdUgrp(this.idUgrp);
		usr.setdFlg(this.dFlg);
//		usr.setActiv(null);
		return usr;
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

	public String getUsrNm() {
		return usrNm;
	}

	public String getPwd() {
		return pwd;
	}

	public Long getIdUgrp() {
		return idUgrp;
	}
}