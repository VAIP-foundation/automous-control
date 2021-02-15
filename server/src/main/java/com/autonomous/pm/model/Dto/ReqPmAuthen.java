package com.autonomous.pm.model.Dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.ToString;

@ToString
public class ReqPmAuthen {

	@ApiModelProperty(dataType = "String", value = "vrn 인증 패스워드")
	String password;	// vrn 인증 패스워드
	
	@ApiModelProperty(dataType = "String", value = "vrn 인증 키")
	String authentication_key;	// vrn 인증 키

	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getAuthentication_key() {
		return authentication_key;
	}
	public void setAuthentication_key(String authentication_key) {
		this.authentication_key = authentication_key;
	}
	
}