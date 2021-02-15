package com.autonomous.pm.domain.structure.ack;

import com.autonomous.pm.domain.structure.CallChange;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AckCallChange { // 일반 응답 메시지
	
	String type = "Ack";
	CallChange data;
	
	public AckCallChange(CallChange data) {
		this.data = data;
	}
	public AckCallChange(String type, CallChange data) {
		this.type = type;
		this.data = data;
	}
	
}
