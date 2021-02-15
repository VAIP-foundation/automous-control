package com.autonomous.pm.service;

import java.util.List;

import com.autonomous.pm.domain.structure.FlightInfo;
import com.autonomous.pm.domain.structure.mem.TripInfoMem;

public interface FlightService {
	
	List<FlightInfo> getFlightSchLs(String vrn);
	
}
