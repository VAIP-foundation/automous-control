package com.autonomous.pm.domain.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(value = Include.NON_EMPTY)
public class SearchParam {
	
	String idUsr;	
	
	String loginId;	
	
	String usrNm;	
	
	String pwd;	
	
	String year;
	String month;
	String day;
	
	String fromDate;	
	String toDate;		
	
	Long idV;	
	String vrn;	
	String password;	
	String authentication_key;	

	String term;	
	Integer idUgrp;	

	String grpNm;	
	String athNm;	

	Integer star;	
	Integer idPoi;	
	Integer repeat;	
	
	
	@Override
	public String toString() {
		return "SearchParam [" + (idUsr != null ? "idUsr=" + idUsr + ", " : "")
				+ (loginId != null ? "loginId=" + loginId + ", " : "") + (usrNm != null ? "usrNm=" + usrNm + ", " : "")
				+ (pwd != null ? "pwd=" + pwd + ", " : "") + (year != null ? "year=" + year + ", " : "")
				+ (month != null ? "month=" + month + ", " : "") + (day != null ? "day=" + day + ", " : "")
				+ (fromDate != null ? "fromDate=" + fromDate + ", " : "")
				+ (toDate != null ? "toDate=" + toDate + ", " : "") + (idV != null ? "idV=" + idV + ", " : "")
				+ (vrn != null ? "vrn=" + vrn + ", " : "") + (password != null ? "password=" + password + ", " : "")
				+ (authentication_key != null ? "authentication_key=" + authentication_key + ", " : "")
				+ (term != null ? "term=" + term + ", " : "") + (idUgrp != null ? "idUgrp=" + idUgrp + ", " : "")
				+ (grpNm != null ? "grpNm=" + grpNm + ", " : "") + (athNm != null ? "athNm=" + athNm + ", " : "")
				+ (star != null ? "star=" + star + ", " : "") + (idPoi != null ? "idPoi=" + idPoi + ", " : "")
				+ (repeat != null ? "repeat=" + repeat : "") + "]";
	}
	
};
