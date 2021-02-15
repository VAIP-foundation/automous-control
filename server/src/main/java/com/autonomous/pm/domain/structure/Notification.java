package com.autonomous.pm.domain.structure;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Notification { // 차량 상태 변경 시 UI에 전달한다
	public enum NotificationCode {
		ENTER("1001"),			// 1001 Enter알림(PM-Join 시)
		ENTER_CONTN("1002"),	// 1002 Enter알림(PM-Join.contn 시)
		LEAVE("2001"),			// 2001 Leave 알림(PM-Bye 시)
		UNREACHABLE("3001");	// 3001 Disconnect 알림(네트워크 연결 비정상 단절 시)
		
		private final String code;
		private NotificationCode(final String code) {
			this.code = code;
		}
		public String toString() {
			return code.toString();
		}
	}
	
	private Date ts;		// 현재시각(UTC).yyyy-mm-ddThh:mi:ssZ ["2011-08-12T20:17:46.384Z"]
	private Long idV;
	private String vrn;
	private String cd;
	
}
