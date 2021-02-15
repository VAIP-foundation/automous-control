package com.autonomous.pm.dao;

import java.util.List;

import com.autonomous.pm.model.Do.TTrpRaw;
import com.autonomous.pm.model.Dto.ResEvent;
import com.autonomous.pm.model.Dto.ResPoi;
import com.autonomous.pm.model.Dto.ResVhcl;

public interface MonitorMapper {
	
	List<ResVhcl> getVehicleLs(String term);
	
	List<ResEvent> getEventLs(String term);
	
	List<ResPoi> getPoiLs(String term);
	
	List<TTrpRaw> getTripLs(String term);
	
}