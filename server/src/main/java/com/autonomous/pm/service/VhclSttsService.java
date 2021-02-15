package com.autonomous.pm.service;

import com.autonomous.pm.domain.structure.DrivingInfo;
import com.autonomous.pm.domain.structure.SensorInfo;
import com.autonomous.pm.domain.structure.TripInfo;
import com.autonomous.pm.model.Do.TVhclStts;
import com.autonomous.pm.model.Do.VVhcl;
import com.autonomous.pm.model.Dto.VhclStts;

public interface VhclSttsService {
	void insert(VhclStts vhclStts);
	void update(VVhcl vvhcl);
	void update(Long idV, TVhclStts vhclStts);
	void update(Long idV, TripInfo tripInfo);
	void update(Long idV, DrivingInfo drivingInfo);
	void update(Long idV, SensorInfo sensorInfo);
}
