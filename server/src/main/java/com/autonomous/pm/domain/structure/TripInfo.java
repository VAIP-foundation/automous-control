package com.autonomous.pm.domain.structure;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TripInfo {	// TRIP의 시작/종료 보고
	
	private Date ts;		// 현재시각(UTC).yyyy-mm-ddThh:mi:ssZ ["2011-08-12T20:17:46.384Z"]
	private Poi to;	// 목적지 정보
	private Integer ty;	// start/end [1=start. TRIP출발보고, 2=end. TRIP종료보고]
	private Integer result;	// 결과.운행완료 - 정상적으로 목적지까지 승객 수송 완료. 운행실패 - 운행이 취소되어 운행 비정상 완료. '사유' 항목에 보조 정보 참조 [1=운행실패 , 2=운행완료]
	private Integer reason; // 운행실패 시: '1'= 탑승객취소: 탑승객에 의해 운행 취소 , '2' = 탑승객이 일시 하차 후 미탑승에 의해 운행 비정상 완료, '3'= 고장: 승객 수송 도중 고장 발생에 의해 비정상 완료, '4'= 회차: 회차 명령에 의해, '5'= 원격정지]
	private String flghtnum;	// flight number. 항공편명. 출국장 전용. 출국장에서 승객을 Info->Gate로 운송 시에 사용.  출국장 게이트 변경 시 관제로부터 알림 서비스를 받기 위해 관제에 등록하는 용도. null 이면 출국장 게이트 변경 알림 동작 안함. [KE123]
	private Integer dist;	// 주행거리(m단위). "ty"=2 (end) 일때만 사용. 그외 null.
	
}