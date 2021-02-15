package com.autonomous.pm.dao;

import java.util.List;

import com.autonomous.pm.model.Do.TPoiGateMap;
import com.autonomous.pm.model.Do.VPoiGate;

public interface PoiGateMapMapper {

	List<VPoiGate> getAllPoiGateMapLs();
	List<TPoiGateMap> getPoiGateMapLs(Long idPoi);
	List<String> getGateByIdPoi(Long idPoi);
	
}