package com.autonomous.pm.service.restful;

import java.util.List;

import com.autonomous.pm.model.Do.TCsSurveyHst;
import com.autonomous.pm.model.Dto.ReqAdminRate;
import com.autonomous.pm.model.Dto.ReqFromToDay;
import com.autonomous.pm.model.Dto.ResAdminRate;

public interface RateService {

	List<ResAdminRate> getAdminRateLs(ReqAdminRate reqParam);
	int postAdminRateLs(TCsSurveyHst reqParam);
	
}