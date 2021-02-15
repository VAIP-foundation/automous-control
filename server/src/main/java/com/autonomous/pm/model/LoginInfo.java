package com.autonomous.pm.model;

import java.util.Date;

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
public class LoginInfo
{
	private String loginId;
	private Date lastLoginDt;
	private Date lastFailDt;
	private Integer failCount = 0;
	
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public Date getLastLoginDt() {
		return lastLoginDt;
	}
	public void setLastLoginDt(Date lastLoginDt) {
		this.lastLoginDt = lastLoginDt;
	}
	public Date getLastFailDt() {
		return lastFailDt;
	}
	public void setLastFailDt(Date lastFailDt) {
		this.lastFailDt = lastFailDt;
	}
	public Integer getFailCount() {
		return failCount;
	}
	public void setFailCount(Integer failCount) {
		this.failCount = failCount;
	}
}