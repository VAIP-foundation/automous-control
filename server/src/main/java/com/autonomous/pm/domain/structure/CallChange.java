package com.autonomous.pm.domain.structure;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CallChange {	// CALL 변경
	
	private String mid;		// message id. 메시지의 발송자가 발번한 unique한 id 값.UUID format. HTTP GET/POST 의 경우 사용하지 않고 WebSocket기반 메시지에서만 사용한다.
	private Call call;		// 새로운 call 을 위한 정보. Call.p='p0' call.mid 생
							// 략 또는 null value.
							// Call.to 에 의해 새로운 Poi를 인지해야한다. 바뀐
							// 게이트가 타 권역인 경우 새 Call의 Poi는 회차
							// /Info 지점으로 push된다.
	private FlightInfo fi;
	private Poi newdst;		// new destination. 승객이 최종 도착할 POI.
							// 1. 동일 권역이라면: Call.to == newdst .
							// 2. 다른 권역이라면: Call.to != newdst .
							// Call.to == Poi("INFO") 이고 newdst ==
							// Poi( new_gate ) .
							// 이때 Poi( new_gate ) 은 DB 에 저장되어 있는 않
							// 은 POI이므로
							// { term=null, grp=null, gnm=null, pcd=null,
							// pnm="Gate101", ty="gate", lat=null, lng=null}
	
}
