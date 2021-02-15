package com.autonomous.pm.model.Dto;

import java.util.Date;

import lombok.Setter;
import lombok.ToString;

@Setter
@ToString
public class ResAdminLoginHst {

    private Long seq;		// SEQ,AI
    private Date loginDt;	// 로그인시각
    private String loginId;	// Login 사용자 ID
    private Long idUsr;		// Login 사용자 ID_USR
    private String peerIp;	// 접속 IP
    private String httpUa;	// HTTP UserAgent 필드 정보
    private String loginTy;	// 로그인/아웃 타입. 1:login, 2:logout
    private String loginTyNm;	// login, logout
    private Date cDt;		// 생성일시
    
	public Date getcDt() {
		return cDt;
	}
	public void setcDt(Date cDt) {
		this.cDt = cDt;
	}
	public Long getSeq() {
		return seq;
	}
	public Date getLoginDt() {
		return loginDt;
	}
	public String getLoginId() {
		return loginId;
	}
	public Long getIdUsr() {
		return idUsr;
	}
	public String getPeerIp() {
		return peerIp;
	}
	public String getHttpUa() {
		return httpUa;
	}
	public String getLoginTy() {
		return loginTy;
	}
	public String getLoginTyNm() {
		return loginTyNm;
	}
	
    
    
}