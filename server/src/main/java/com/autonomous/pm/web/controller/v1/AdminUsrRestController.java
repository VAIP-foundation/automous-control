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
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
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
import com.autonomous.pm.model.Usr;
import com.autonomous.pm.model.Dto.ReqAdminUsr;
import com.autonomous.pm.model.Dto.ResAdminGop;
import com.autonomous.pm.model.Dto.ResAdminUsr;
import com.autonomous.pm.service.LogServiceImpl;
import com.autonomous.pm.service.restful.UsrServiceImpl;
import com.autonomous.pm.web.controller.BaseRestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1")
public class AdminUsrRestController extends BaseRestController {

	public static final Logger logger = LoggerFactory.getLogger(AdminUsrRestController.class);

	@Autowired
	CustomCollectionValidator customCollectionValidator;

	@Autowired
	UsrServiceImpl usrService;
	@Autowired
	LogServiceImpl logService;

	/**
	 * 
	 * @param request
	 * @param reqParam
	 * @return
	 * @throws BindException
	 */
	public ResponseEntity<?> getAdminUsrLs(HttpServletRequest request,
			@RequestParam(required = false) Integer idUgrp,
			@RequestParam(required = false) String usrNm) throws BindException {
		logger.info("getAdminUsrLs() idUgrp={}, usrNm={}", idUgrp, usrNm);
		logService.saveAccessLog(request);

		List<ResAdminUsr> result = usrService.getAdminUsrLs(idUgrp, usrNm);

		return new ResponseEntity<ResultValue>(new ResultValue(ResultCode.SUCCESS, result), HttpStatus.OK);
	}

	/**
	 * 
	 * @param request
	 * @param reqParam
	 * @return
	 * @throws BindException
	 */
	public ResponseEntity<?> postAdminUsrLs(HttpServletRequest request, @Valid @RequestBody List<ReqAdminUsr> reqParam,
			BindingResult bindingResult) throws BindException {
		logger.info("postAdminUsrLs() reqParam={}", convertString(reqParam));
		logService.saveAccessLog(request, convertString(reqParam));

		String loginId = getUserId(request);
		Usr usr = usrService.findUsrByLoginId(loginId);
		if ( usr != null && usr.getAthLvl() < 5 ) {
			System.out.println("usr.getAthLvl()"+usr.getAthLvl());
			FieldError error = new FieldError("JWT", "Authentication", "Can not excute this API.");
			bindingResult.addError(error);
			throw new BindException(bindingResult);
		}
		
		
		
		customCollectionValidator.validate(reqParam, bindingResult);
		if (bindingResult.hasErrors())
			throw new BindException(bindingResult);

		usrService.postAdminUsrLs(reqParam, bindingResult);
		return new ResponseEntity<ResultValue>(new ResultValue(ResultCode.SUCCESS, null), HttpStatus.OK);

	}

}