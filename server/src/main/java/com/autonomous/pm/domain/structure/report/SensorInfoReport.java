package com.autonomous.pm.domain.structure.report;

import com.autonomous.pm.domain.structure.SensorInfo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SensorInfoReport {

	private String type;
	private SensorInfo data;

}
