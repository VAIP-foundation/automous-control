package com.autonomous.pm.domain.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
//@JsonInclude(value = Include.NON_EMPTY)
@JsonInclude(value = Include.NON_NULL)
public class ResultValue {

	private String 	  	 resultCode = ResultCode.SUCCESS.toString();
	private ResultCode 	 resultMessage = ResultCode.SUCCESS;
	

	private Object 	     resultData = null;
	
	
	public ResultValue(ResultCode resultCode, Object data) {
		this.resultCode = resultCode.toString();
		this.resultMessage = resultCode;
		this.resultData = data;
	}
	

}