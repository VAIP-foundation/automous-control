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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.autonomous.pm.component.validator.CustomCollectionValidator;
import com.autonomous.pm.domain.common.ResultCode;
import com.autonomous.pm.domain.common.ResultValue;
import com.autonomous.pm.model.Dto.ReqAdminPoi;
import com.autonomous.pm.model.Dto.ResAdminGop;
import com.autonomous.pm.model.Dto.ResAdminPoi;
import com.autonomous.pm.service.LogServiceImpl;
import com.autonomous.pm.service.restful.PoiServiceImpl;
import com.autonomous.pm.web.controller.BaseRestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1")
public class AdminPoiRestController extends BaseRestController {

	public static final Logger logger = LoggerFactory.getLogger(AdminPoiRestController.class);

	@Autowired
	CustomCollectionValidator customCollectionValidator;

	@Autowired
	PoiServiceImpl poiService;
	@Autowired
	LogServiceImpl logService;

	/**
	 * 
	 * @param request
	 * @param reqParam
	 * @return
	 * @throws BindException
	 */
	@RequestMapping(value = "/admin/poi/ls", method = RequestMethod.GET)
	public ResponseEntity<?> getAdminPoiLs(HttpServletRequest request,
			@RequestParam(name = "term", required = false) String term) throws BindException {
		logger.info("getAdminPoiLs() term={}", term);
		logService.saveAccessLog(request);

		List<ResAdminPoi> result = poiService.getAdminPoiLs(term);

		return new ResponseEntity<ResultValue>(new ResultValue(ResultCode.SUCCESS, result), HttpStatus.OK);
	}

	/**
	 * 
	 * @param request
	 * @param reqParam
	 * @return
	 * @throws BindException
	 */
	public ResponseEntity<?> postAdminPoiLs(HttpServletRequest request, @Valid @RequestBody List<ReqAdminPoi> reqParam,
			BindingResult bindingResult) throws BindException {
		logger.info("postAdminPoiLs() reqParam={}", convertString(reqParam));
		logService.saveAccessLog(request, convertString(reqParam));

		// 에러 체크
		customCollectionValidator.validate(reqParam, bindingResult);
		if (bindingResult.hasErrors())
			throw new BindException(bindingResult);

		int result = poiService.postAdminPoiLs(reqParam);
		if (result > 0) {
			return new ResponseEntity<ResultValue>(new ResultValue(ResultCode.SUCCESS, null), HttpStatus.OK);
		} else {
			return new ResponseEntity<ResultValue>(new ResultValue(ResultCode.FAILED, null), HttpStatus.OK);
		}

	}

	/**
	 * 
	 * @param request
	 * @param reqParam
	 * @return
	 * @throws BindException
	 */
	public ResponseEntity<?> getAdminGopLs(HttpServletRequest request,
			@RequestParam(name = "term", required = false) String term) throws BindException {
		logger.info("getAdminGopLs() term={}", term);
		logService.saveAccessLog(request);

		List<ResAdminGop> result = poiService.getAdminGopLs(term);

		return new ResponseEntity<ResultValue>(new ResultValue(ResultCode.SUCCESS, result), HttpStatus.OK);
	}
}