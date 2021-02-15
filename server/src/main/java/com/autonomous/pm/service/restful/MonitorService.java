package com.autonomous.pm.service.restful;

import java.util.List;

import com.autonomous.pm.model.Dto.ResEvent;
import com.autonomous.pm.model.Dto.ResPoi;
import com.autonomous.pm.model.Dto.ResTrip;
import com.autonomous.pm.model.Dto.ResVhcl;

public interface MonitorService {
	
	List<String> getAllTerminal();
	int stopVehicle(String terminal, Long idV);
	int resumeVehicle(String terminal, Long idV);
	int callVbase(String term, Long idV);
	int callP1Vehicle(String terminal, Long idPoi, Integer repeat);
	int callP1Cancel(String term, Long idCall);
	List<ResVhcl> getVehicleLs(String terminal);
	List<ResEvent> getEventLs(String terminal);
	List<ResTrip> getTripLs(String terminal);
	List<ResPoi> getPoiLs(String terminal);
	
}
