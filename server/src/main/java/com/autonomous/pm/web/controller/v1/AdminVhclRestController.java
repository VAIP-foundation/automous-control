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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.autonomous.pm.component.validator.CustomCollectionValidator;
import com.autonomous.pm.domain.common.ResultCode;
import com.autonomous.pm.domain.common.ResultMessage;
import com.autonomous.pm.domain.common.ResultValue;
import com.autonomous.pm.model.Usr;
import com.autonomous.pm.model.Dto.ReqAdminVhcl;
import com.autonomous.pm.model.Dto.ResAdminVhcl;
import com.autonomous.pm.service.LogServiceImpl;
import com.autonomous.pm.service.restful.UsrServiceImpl;
import com.autonomous.pm.service.restful.VhclServiceImpl;
import com.autonomous.pm.web.controller.BaseRestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1")
public class AdminVhclRestController extends BaseRestController {

	public static final Logger logger = LoggerFactory.getLogger(AdminVhclRestController.class);

	@Autowired
	CustomCollectionValidator customCollectionValidator;

	@Autowired
	UsrServiceImpl usrService;
	@Autowired
	VhclServiceImpl vhclService;
	@Autowired
	LogServiceImpl logService;

	/**
	 * 
	 * @param request
	 * @param reqParam
	 * @return
	 * @throws BindException
	 */
	public ResponseEntity<?> getAdminVhclLs(HttpServletRequest request,
			@RequestParam(name = "term", required = false) String term) throws BindException {
		logger.info("getAdminVhclLs() term={}", term);
		logService.saveAccessLog(request);

		List<ResAdminVhcl> result = vhclService.getAdminVhclLs(term);

		return new ResponseEntity<ResultValue>(new ResultValue(ResultCode.SUCCESS, result), HttpStatus.OK);
	}

	/**
	 * 
	 * @param request
	 * @param reqParam
	 * @return
	 * @throws BindException
	 */
	public ResponseEntity<?> postAdminVhclLs(HttpServletRequest request,
			@Valid @RequestBody List<ReqAdminVhcl> reqParam, BindingResult bindingResult) throws BindException {
		logger.info("postVhclLs() reqParam={}", convertString(reqParam));
		logService.saveAccessLog(request, convertString(reqParam));

		// 권한 체크
		String loginId = getUserId(request);
		Usr usr = usrService.findUsrByLoginId(loginId);
		if ( usr != null && usr.getAthLvl() < 5 ) {
			System.out.println("usr.getAthLvl()"+usr.getAthLvl());
			FieldError error = new FieldError("JWT", "Authentication", "Can not excute this API.");
			bindingResult.addError(error);
			throw new BindException(bindingResult);
		}
		
		// 에러 체크
		customCollectionValidator.validate(reqParam, bindingResult);
		if (bindingResult.hasErrors())
			throw new BindException(bindingResult);

		List<Long> dupleIdVs= vhclService.checkDuplicateByVrn(reqParam);
		if ( dupleIdVs.size() > 0 ) {
			String msg = "";	// idV 항목만 전달한다
			for(Long idV : dupleIdVs) {
				msg += idV + ",";
			}
			msg = msg.substring(0, msg.length()-1);
			
			return new ResponseEntity<ResultMessage>(new ResultMessage(ResultCode.DUPLICATED_VALUE, msg, null), HttpStatus.OK);
		}
		
		int result = vhclService.postAdminVhclLs(reqParam, bindingResult);
		if (result > 0) {
			return new ResponseEntity<ResultValue>(new ResultValue(ResultCode.SUCCESS, null), HttpStatus.OK);
		} else {
			return new ResponseEntity<ResultValue>(new ResultValue(ResultCode.FAILED, null), HttpStatus.OK);
		}

	}

}