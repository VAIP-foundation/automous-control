package com.autonomous.pm.domain.structure;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Call {	// PM차량이 승객 수송 서비스를 수행하도록 명령 구조체
	private String mid;		// message id. 메시지의 발송자가 발번한 unique한 id 값.UUID format. HTTP GET/POST 의 경우 사용하지 않고 WebSocket기반 메시지에서만 사용한다.
	private String callid;	// call id. call 을 구분할 수 있는 unique한 id 값.UUID format
	private String prio;	// 우선순위 정보
							// 'p0' = 반드시 바로 수행해야하는 CALL 명령. PM차량은 수행 중인 CALL을 무조건 취소하고 현재 수신한 CALL을 수행한다.
							// 'p1' = 다음에 수행 할 CALL 명령. 수행 중인 CALL 이 있으면 완료 후 수행한다. 관리자에 의해 수동으로 부여된 CALL 명령.
							// 'p2' = 다음에 수행 할 CALL 명령. 시스템에 의해 자동으로 부여되는 CALL 명령
	private String sub;		// "batlow" = 상황에 따라 부가 정보를 전달하기 위함. 없는 경우 null. "batlow" = battery low, 충전소 이동 명령
							// "wait" = 이동 후 대기(회차에서 쓰임)
	private Poi to;			// 목적지. PM이 이동할 POI
	private FlightInfo fi;	// 편명 정보.  p='p2' 일 경우에만 사용 가능. 그외 null.
}
