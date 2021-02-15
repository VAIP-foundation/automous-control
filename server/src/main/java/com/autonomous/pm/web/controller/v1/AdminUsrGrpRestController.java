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
import com.autonomous.pm.model.Dto.ReqAdminUsrGrp;
import com.autonomous.pm.model.Dto.ResAdminUsrGrp;
import com.autonomous.pm.service.LogServiceImpl;
import com.autonomous.pm.service.restful.UsrGrpServiceImpl;
import com.autonomous.pm.web.controller.BaseRestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1")
public class AdminUsrGrpRestController extends BaseRestController {

	public static final Logger logger = LoggerFactory.getLogger(AdminUsrGrpRestController.class);

	@Autowired
	CustomCollectionValidator customCollectionValidator;

	@Autowired
	UsrGrpServiceImpl usrGrpService;
	@Autowired
	LogServiceImpl logService;

	/**
	 * 
	 * @param request
	 * @param reqParam
	 * @return
	 * @throws BindException
	 */
	public ResponseEntity<?> getAdminUsrGrpLs(HttpServletRequest request, @RequestParam(required = false) String grpNm) throws BindException {
		logger.info("getAdminUsrGrpLs() grpNm={}", grpNm);
		logService.saveAccessLog(request);

		List<ResAdminUsrGrp> result = usrGrpService.getAdminUsrGrpLs(grpNm);

		return new ResponseEntity<ResultValue>(new ResultValue(ResultCode.SUCCESS, result), HttpStatus.OK);
	}

	/**
	 * 
	 * @param request
	 * @param reqParam
	 * @return
	 * @throws BindException
	 */
	public ResponseEntity<?> postAdminGroupLs(HttpServletRequest request, @Valid @RequestBody List<ReqAdminUsrGrp> reqParam,
			BindingResult bindingResult) throws BindException {
		logger.info("postAdminGroupLs() reqParam={}", convertString(reqParam));
		logService.saveAccessLog(request, convertString(reqParam));

		// 에러 체크
		customCollectionValidator.validate(reqParam, bindingResult);
		if (bindingResult.hasErrors())
			throw new BindException(bindingResult);

		// 사용자 그룹 등록/수정/삭제
		// T_USR_GRP를 삭제하려 하였으나 T_USR에서 이미 사용중일때
		int result = usrGrpService.postAdminUsrGrpLs(reqParam);
		if ( result == -1 ) {
			return new ResponseEntity<ResultValue>(new ResultValue(ResultCode.USED_ELSEWHERE, null), HttpStatus.OK);
		} else {
			return new ResponseEntity<ResultValue>(new ResultValue(ResultCode.SUCCESS, null), HttpStatus.OK);
		}

	}

}