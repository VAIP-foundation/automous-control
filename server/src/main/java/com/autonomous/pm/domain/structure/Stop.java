package com.autonomous.pm.domain.structure;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Stop {	// PM차량을 정지 명령용 구조체
	public enum Prio { p0, p1 }
	public enum Ty { STOP, RESUME }
	
	private String mid;	// message id. 메시지의 발송자가 발번한 unique한 id 값.UUID format. HTTP GET/POST 의 경우 사용하지 않고 WebSocket기반 메시지에서만 사용한다.
	private Prio prio;// 우선순위 정보
						// 'p0' = 반드시 바로 수행해야하는 CALL 명령. PM차량은 수행 중인 CALL을 무조건 취소하고 현재 수신한 CALL을 수행한다.
						// 'p1' = 다음에 수행 할 CALL 명령. 수행 중인 CALL 이 있으면 완료 후 수행한다. 관리자에 의해 수동으로 부여된 CALL 명령.
	private Ty ty;	// Stop/Resume toggle. 'STOP'= stop정지, 'RESUME'= resume 재기동.
	
	public Stop(String mid, Prio prio, Ty ty) {
		this.mid = mid;
		this.prio = prio;
		this.ty = ty;
	}
	public Stop(Prio prio, Ty ty) {
		this.prio = prio;
		this.ty = ty;
	}
	public Stop() {
	}
}
