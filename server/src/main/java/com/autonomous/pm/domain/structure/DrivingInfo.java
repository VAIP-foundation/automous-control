package com.autonomous.pm.domain.structure;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DrivingInfo { // 주행정보 메세지
	
	private Date ts;		// 현재시각(UTC).yyyy-mm-ddThh:mi:ssZ ["2011-08-12T20:17:46.384Z"]
	private Float speed;	// 속도.km/h
	private Gps gps;		// 현재 위치와 운행 방위각
	private Integer stts;		// 운행상태 [1=탑승 대기, 2=주행 중, 3=충전 중, 4=승객에의한정지, 5=현장요원에의한정지, 6=관제서버에의한정지, 20=네트워크끊김]
	private boolean front;	// 주행방향 [True = 순방향 주행, False = 역방향 주행]
	private Integer swa;		// 조향각(steering wheel angle) [-540~540]
	private boolean brk;	// 브레이크 동작 여부 [True=ON, False=OFF]
	private Integer dtm;		// driving time. 현 trip의 주행 시간. 초단위
	private boolean auto;	// 자율주행 여부[True=자율주행, False=수동운행]
//	private float mrpm;		// motor rpm. 모터 회전 수
	
}
