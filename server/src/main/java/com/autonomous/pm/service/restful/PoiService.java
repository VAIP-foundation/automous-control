package com.autonomous.pm.service.restful;

import java.util.List;

import com.autonomous.pm.domain.structure.Poi;
import com.autonomous.pm.model.Dto.ReqAdminPoi;
import com.autonomous.pm.model.Dto.ResAdminGop;
import com.autonomous.pm.model.Dto.ResAdminPoi;

public interface PoiService {
	
	List<ResAdminPoi> getAdminPoiLs(String term);
	
	int postAdminPoiLs(List<ReqAdminPoi> reqParam);
	
	List<ResAdminGop> getAdminGopLs(String term);

	ResAdminPoi getPoiByGateNo(String gateNo);

	ResAdminPoi getPoiByClosestGPS(Long lng, Long lat);
	
}
