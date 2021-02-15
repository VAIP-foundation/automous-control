package com.autonomous.pm.domain.structure;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Join { // web socket 접속 시 가장 먼저 전송해야할 명령. 관제서버에 차량을 등록한다.
	
	private String mid;
	private String ts;		// 현재시각(UTC).yyyy-mm-ddThh:mi:ssZ ["2011-08-12T20:17:46.384Z"]
	
//	@JsonProperty("pm-as-key")
//	private String pmAsKey;	// 암호화된 인증 token
	private String token;	// 암호화된 인증 token
	
	private boolean contn;	// True=이전 trip 이어서 계속됨. False=일반적인 Join절차
							// Continue.
							// App down,네트워크 단절 등 앞서 TripInfo{ty='end'} 전송하지 않고
							// 비정상적으로 Trip이 종료된 경우, PM이 바로 재접속을시도했을때
							// contn 항목이 true 이면 서버는 이전 Trip을 이어서 운행 중인 것으로 판단.
	
}
