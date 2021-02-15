package com.autonomous.pm.web.controller.v1;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.autonomous.pm.component.validator.CustomCollectionValidator;
import com.autonomous.pm.domain.common.ResultCode;
import com.autonomous.pm.domain.common.ResultValue;
import com.autonomous.pm.domain.common.SearchParam;
import com.autonomous.pm.model.Dto.ReqAdminUsrAth;
import com.autonomous.pm.model.Dto.ResAdminUsrAth;
import com.autonomous.pm.service.LogServiceImpl;
import com.autonomous.pm.service.restful.UsrAthServiceImpl;
import com.autonomous.pm.web.controller.BaseRestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1")
public class AdminUsrAthRestController extends BaseRestController {

	public static final Logger logger = LoggerFactory.getLogger(AdminUsrAthRestController.class);

	@Autowired
	CustomCollectionValidator customCollectionValidator;

	@Autowired
	UsrAthServiceImpl usrAthService;
	@Autowired
	LogServiceImpl logService;

	/**
	 * 
	 * @param request
	 * @param reqParam
	 * @return
	 * @throws BindException
	 */
	public ResponseEntity<?> getAdminUsrAthLs(HttpServletRequest request, @RequestParam(required = false) String athNm) throws BindException {
		logger.info("getAdminUsrAthLs() athNm={}", athNm);
		logService.saveAccessLog(request);

		List<ResAdminUsrAth> result = usrAthService.getAdminUsrAthLs(athNm);

		return new ResponseEntity<ResultValue>(new ResultValue(ResultCode.SUCCESS, result), HttpStatus.OK);
	}

	/**
	 * 
	 * @param request
	 * @param reqParam
	 * @return
	 * @throws BindException
	 */
	public ResponseEntity<?> postAdminUsrAthLs(HttpServletRequest request, @Valid @RequestBody List<ReqAdminUsrAth> reqParam,
			BindingResult bindingResult) throws BindException {
		logger.info("postAdminUsrAthLs() reqParam={}", convertString(reqParam));
		logService.saveAccessLog(request);

		customCollectionValidator.validate(reqParam, bindingResult);
		if (bindingResult.hasErrors())
			throw new BindException(bindingResult);

		int result = usrAthService.postAdminUsrAthLs(reqParam);
		
		if ( result == -1 ) {
			return new ResponseEntity<ResultValue>(new ResultValue(ResultCode.USED_ELSEWHERE, null), HttpStatus.OK);
		} else {
			return new ResponseEntity<ResultValue>(new ResultValue(ResultCode.SUCCESS, null), HttpStatus.OK);
		}

	}

}