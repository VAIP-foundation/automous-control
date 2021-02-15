package com.autonomous.pm.domain.structure;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RouteInfo { // 이동경로
	
	private Date ts;			// 현재시각(UTC).yyyy-mm-ddThh:mi:ssZ ["2011-08-12T20:17:46.384Z"]
	private Location from;		// 출발 POI의 좌표
	private Location[] points;	// 경유할 지점 목록. 경로가 휘어지거나 꺾이는 경우 필요함.
								// 순서가 중요함. from~to 에 이르는 위치의 순서여야 함."고장코드(토르)"sheet 에 정의 요망.
	private Location to;		// 도착 POI의 좌표 
	
}