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
public class ResultMessage {

	private String resultCode = ResultCode.SUCCESS.toString();
	private String resultMessage = ResultCode.SUCCESS.name();
	private Object resultData = null;
	
	
	public ResultMessage(ResultCode resultCode, String resultMessage, Object data) {
		this.resultCode = resultCode.toString();
		this.resultMessage = resultMessage;
		this.resultData = data;
	}
	

}