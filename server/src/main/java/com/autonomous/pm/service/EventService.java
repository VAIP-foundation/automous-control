package com.autonomous.pm.service;

import java.util.List;

import com.autonomous.pm.domain.structure.mem.EventMem;

public interface EventService {
	
	void insertInfo(EventMem info);
	void insertInfo(List<EventMem> info);
	
}
