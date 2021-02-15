package com.autonomous.pm.service;

import java.util.List;

import com.autonomous.pm.domain.structure.mem.TripInfoMem;
import com.autonomous.pm.model.Do.TTrpRaw;

public interface TripInfoService {
	
	void insertInfo(TripInfoMem info);
	void insertInfo(TTrpRaw info);
	
	void insertInfo(List<TripInfoMem> info);
	
	
}
