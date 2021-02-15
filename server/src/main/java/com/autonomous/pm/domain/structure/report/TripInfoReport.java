package com.autonomous.pm.domain.structure.report;

import com.autonomous.pm.domain.structure.TripInfo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TripInfoReport {

	private String type;
	private TripInfo data;

}
