package com.autonomous.pm.service;

import java.util.List;

import com.autonomous.pm.domain.structure.mem.DrivingInfoMem;

public interface DrivingInfoService {
	
	void insertInfo(DrivingInfoMem info);
	void insertInfo(List<DrivingInfoMem> infos);
	
	
}
