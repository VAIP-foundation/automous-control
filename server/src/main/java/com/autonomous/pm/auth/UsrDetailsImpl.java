package com.autonomous.pm.auth;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.autonomous.pm.model.Usr;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UsrDetailsImpl extends User {

	private Long idUsr;
	private String loginId;
	private String detail; 
	
    private String usrNm;
    private String grpNm;
    private String athNm;
    private Integer athLvl;
    private Integer athCd;
    
    @NonNull
    @JsonProperty("pm_as_key")
    String pmAsKey;
    
    @JsonProperty("expire_time")
    String expireTime;

	public UsrDetailsImpl(String id, List<GrantedAuthority> authorities) {
		super(id, "", authorities);
	}

	public UsrDetailsImpl(Usr member, List<GrantedAuthority> authorities) {
		super(member.getLoginId(), member.getPwd(), authorities);
		this.detail = member.getUsrNm();
		this.idUsr = member.getIdUsr();
		this.loginId = member.getLoginId();
		
		this.usrNm = member.getUsrNm();
		this.grpNm = member.getGrpNm();
		this.athNm = member.getAthNm();
		this.athLvl = member.getAthLvl();
		this.athCd = member.getAthCd();
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

//	public String getRankNm() {
//		return rankNm;
//	}

//	public void setRankNm(String rankNm) {
//		this.rankNm = rankNm;
//	}
}