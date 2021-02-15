package com.autonomous.pm.domain.structure.ack;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AckBody { // 일반 응답 메시지
	
	String type = "Ack";
	Ack data;
	
	public AckBody(Ack data) {
		this.data = data;
	}
	public AckBody(String type, Ack data) {
		this.type = type;
		this.data = data;
	}
	
}
