package com.autonomous.pm.web.controller.v1;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.autonomous.pm.domain.common.ResultCode;
import com.autonomous.pm.domain.common.ResultValue;
import com.autonomous.pm.model.Dto.ReqAdminRate;
import com.autonomous.pm.model.Dto.ReqFromToDay;
import com.autonomous.pm.model.Dto.ResAdminRate;
import com.autonomous.pm.service.LogServiceImpl;
import com.autonomous.pm.service.restful.RateServiceImpl;
import com.autonomous.pm.web.controller.BaseRestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1")
public class AdminRateRestController extends BaseRestController {

	public static final Logger logger = LoggerFactory.getLogger(AdminRateRestController.class);

	@Autowired
	RateServiceImpl rateService;
	@Autowired
	LogServiceImpl logService;

	/**
	 * 
	 * @param request
	 * @param reqParam
	 * @return
	 * @throws BindException
	 */
	public ResponseEntity<?> getAdminRateLs(HttpServletRequest request, @Valid @ModelAttribute ReqAdminRate reqParam,
			BindingResult bindingResult) throws BindException {
		logger.info("getAdminRateLs() reqParam={}", reqParam);
		logService.saveAccessLog(request);

	    if (bindingResult.hasErrors())
	    	throw new BindException(bindingResult);
	    
		List<ResAdminRate> result = rateService.getAdminRateLs(reqParam);

		return new ResponseEntity<ResultValue>(new ResultValue(ResultCode.SUCCESS, result), HttpStatus.OK);
	}
}