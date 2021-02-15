package com.autonomous.pm.domain.structure;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Bye {	// websocket 접속 해제 시 서버에 정상 끊김 절차를 위해 전송한다.
	
	private String mid;
	private String ts;	// 현재시각(UTC).yyyy-mm-ddThh:mi:ssZ ["2011-08-12T20:17:46.384Z"]
	private int reason;	// 종료사유
	
}
