package com.autonomous.pm.domain.structure.report;

import com.autonomous.pm.domain.structure.Event;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EventReport {

	private String type;
	private Event data;

}
