package com.autonomous.pm.domain.structure.report;

import com.autonomous.pm.domain.structure.RouteInfo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RouteInfoReport {

	private String type;
	private RouteInfo data;

}