package com.autonomous.pm.domain.structure.ack;

import com.autonomous.pm.domain.common.ReasonCode;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Ack { // 일반 응답 메시지
	
	String omid;	// origin message id. origin으로부터 수신한 메시지의 mid 값. 어떠한 메시지에 대한 ACK인지 명시하는 용도. HTTP GET request와 같이 수신한 메시지의 mid 가 없는 경우 omid는 생략한다.
	String ocmd;	// origin command. 어떤 메시지의 Ack 인지 표기. 추후 운영,디버깅 편의성을 위함.(옵션)
	int result = 2;	// 결과 [1=실패, 2=정상]
	String reason;	// 실패 사유.result=2인 경우 생략(또는 null)
	
	public Ack(String omid, String ocmd, int result, String reason) {
		this.omid = omid;
		this.ocmd = ocmd;
		this.result = result;
		this.reason = reason;
	}
	
	public Ack(String omid, String ocmd, int result, ReasonCode reason) {
		this.omid = omid;
		this.ocmd = ocmd;
		this.result = result;
		this.reason = reason.toString();
	}
	
	public Ack(String omid, String ocmd) {
		this.omid = omid;
		this.ocmd = ocmd;
		this.result = 2;
	}
	
	public Ack() {
	}
	
}
