package com.autonomous.pm.domain.structure;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Gps {	// 위치 정보
	
	private Double lat;		// 위도(WGS84)
	private Double lng;		// 경도(WGS84)
	private Integer hd;		// 방위각(0~359)
	
}
