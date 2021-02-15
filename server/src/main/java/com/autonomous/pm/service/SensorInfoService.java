package com.autonomous.pm.service;

import java.util.List;

import com.autonomous.pm.domain.structure.mem.SensorInfoMem;

public interface SensorInfoService {
	
	void insertInfo(SensorInfoMem info);
	void insertInfo(List<SensorInfoMem> info);
	boolean isBatLow(Long idV);
	
}
