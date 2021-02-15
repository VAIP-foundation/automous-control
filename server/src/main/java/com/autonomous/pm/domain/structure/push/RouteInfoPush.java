package com.autonomous.pm.domain.structure.push;

import org.springframework.beans.BeanUtils;

import com.autonomous.pm.domain.structure.RouteInfo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RouteInfoPush extends RouteInfo {	// UI PUSH를 위한 구조체. 일부 항목을 추가하였다.
	
	private Long idV;	// 차량ID
	private String vrn;	// 차량번호
	
	public RouteInfoPush() {
	}
	
	public RouteInfoPush(Long idV, String vrn, RouteInfo routeInfo) {
		this.setIdV(idV);
		this.setVrn(vrn);
		BeanUtils.copyProperties(routeInfo, this);
	}
}