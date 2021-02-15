package com.autonomous.pm.domain.common;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ResultObject {

	private String type;
	private Object data;

	public ResultObject(ResultTy resultTy, Object data) {
		this.type = resultTy.toString();
		this.data = data;
	}
}
