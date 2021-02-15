package com.autonomous.pm.auth;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.autonomous.pm.model.Do.TUsr;

import lombok.ToString;

@ToString
public class VhclDetailsImpl extends User {

	private String detail; 

	private String rankNm;

	public VhclDetailsImpl(String id, String password, List<GrantedAuthority> authorities) {
		super(id, password, authorities);
	}

	public VhclDetailsImpl(TUsr member, List<GrantedAuthority> authorities) {
		super(member.getLoginId(), member.getPwd(), authorities);
		this.detail = member.getUsrNm();
//		this.rankNm = member.getcRankNm();
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