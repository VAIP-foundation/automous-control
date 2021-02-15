package com.autonomous.pm.domain.structure.report;

import com.autonomous.pm.domain.structure.DrivingInfo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DrivingInfoReport {

	private String type;
	private DrivingInfo data;

}
