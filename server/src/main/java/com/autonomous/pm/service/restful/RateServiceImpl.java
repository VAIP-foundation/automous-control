package com.autonomous.pm.service.restful;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autonomous.pm.dao.RateMapper;
import com.autonomous.pm.model.Do.TCsSurveyHst;
import com.autonomous.pm.model.Dto.ReqAdminRate;
import com.autonomous.pm.model.Dto.ReqFromToDay;
import com.autonomous.pm.model.Dto.ResAdminRate;

@Service
public class RateServiceImpl implements RateService {

	public static final Logger logger = LoggerFactory.getLogger(RateServiceImpl.class);

	@Autowired
	RateMapper rateMapper;

	@Override
	public List<ResAdminRate> getAdminRateLs(ReqAdminRate reqParam) {
		return rateMapper.getAdminRateLs(reqParam);
	}

	@Override
	public int postAdminRateLs(TCsSurveyHst reqParam) {
		return rateMapper.postAdminRateLs(reqParam);
	}
	
}
