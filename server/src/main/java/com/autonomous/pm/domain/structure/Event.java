package com.autonomous.pm.domain.structure;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Event { // PM 측에서 발생한 event
	public enum EventCode {
		REDRIVE("1001"),				// 1001 운행재개
		CRASH("1101"),					// 1101 충돌
		TROUBLE("1201"),				// 1201 고장보고
		CHARGE_START("1202"),			// 1202 충전 시작
		CHARGE_FIN("1203"),				// 1203 충전 완료
		STOP_START_USER("1301"),		// 1301 수동정지(by승객).
		STOP_FIN_USER("1302"),			// 1302 수동정지-재기동(by승객)
		CHANGE_GATE_DEP_ORIGIN("1303"),	// 1303 출국장 게이트변경: 기존 목적지 선택
		CHANGE_GATE_DEP_OTHER("1304"),	// 1304 출국장 게이트변경: 변경 목적지 선택
		CHANGE_GATE_DEP_CANCEL("1305"),	// 1305 출국장 게이트변경: CALL 취소, 하차 선택
		STOP_FIN_TIMEOUT("1306"),		// 1306 수동정지-재기동(by시간초과)
		STOP_START_ADMIN("1401"),		// 1401 수동정지(by관리자)
		STOP_FIN_ADMIN("1402"),			// 1402 수동정지-재기동(by관리자)	@deprecated
		REMOTE_STOP("1501"),			// 1501 원격정지
		REMOTE_RESUME("1502"),			// 1502 원격재운행
		UNREACHABLE("2001"),			// 2001 네트워크끊김
		JOIN("2002"),					// 2002 차량접속
		BYE("2003");					// 2003 차량접속해제
		
		private final String code;
		private EventCode(final String code) {
			this.code = code;
		}
		public String toString() {
			return code.toString();
		}
	}
	
	private Date ts;		// 현재시각(UTC).yyyy-mm-ddThh:mi:ssZ ["2011-08-12T20:17:46.384Z"]
	private String evtcd;	// event code
	private String failcd;	// 고장코드. evtcd='고장보고' 일때 사용. 그외 null 
	
}
