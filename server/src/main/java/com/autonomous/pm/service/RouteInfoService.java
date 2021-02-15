package com.autonomous.pm.service;

import java.util.List;

import com.autonomous.pm.domain.structure.mem.RouteInfoMem;

public interface RouteInfoService {
	
	void insertInfo(RouteInfoMem info);
	void insertInfo(List<RouteInfoMem> info);
	
}
