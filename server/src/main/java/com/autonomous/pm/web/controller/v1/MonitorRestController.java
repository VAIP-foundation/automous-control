package com.autonomous.pm.web.controller.v1;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.autonomous.pm.component.validator.CustomCollectionValidator;
import com.autonomous.pm.domain.common.ResultCode;
import com.autonomous.pm.domain.common.ResultValue;
import com.autonomous.pm.domain.common.SearchParam;
import com.autonomous.pm.model.Usr;
import com.autonomous.pm.model.Do.TTrpRaw;
import com.autonomous.pm.model.Dto.ResEvent;
import com.autonomous.pm.model.Dto.ResPoi;
import com.autonomous.pm.model.Dto.ResTrip;
import com.autonomous.pm.model.Dto.ResAdminPoi;
import com.autonomous.pm.service.LogServiceImpl;
import com.autonomous.pm.service.restful.MonitorServiceImpl;
import com.autonomous.pm.service.restful.TerminalServiceImpl;
import com.autonomous.pm.service.restful.VhclServiceImpl;
import com.autonomous.pm.model.Dto.ResVhcl;
import com.autonomous.pm.util.MyUtil;
import com.autonomous.pm.util.StringUtil;
import com.autonomous.pm.web.controller.BaseRestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1")
public class MonitorRestController extends BaseRestController {

	public static final Logger logger = LoggerFactory.getLogger(MonitorRestController.class);

	@Autowired
	CustomCollectionValidator customCollectionValidator;

	@Autowired
	TerminalServiceImpl terminalService;

	@Autowired
	MonitorServiceImpl monitorService;

	@Autowired
	VhclServiceImpl vehicleService;
	@Autowired
	LogServiceImpl logService;

	/**
	 * 
	 * @param request
	 * @param reqParam
	 * @return
	 * @throws BindException
	 */
	public ResponseEntity<?> getStop(HttpServletRequest request, @PathVariable(name = "terminal", required = true) String terminal,
			@ModelAttribute SearchParam reqParam, BindingResult bindingResult) throws BindException {

		Long idV = reqParam.getIdV();
		logger.info("getStop() terminal={}, idV={}", terminal, idV);
		logService.saveAccessLog(request);

		String term = terminalService.checkTerminal(bindingResult, terminal);

		if (term == null) {
			FieldError e = new FieldError("RequestParameter", "term", "Terminal은 필수 입력 값입니다.");
			bindingResult.addError(e);
		}

		if (bindingResult.hasErrors())
			throw new BindException(bindingResult);

		int result = monitorService.stopVehicle(term, idV);
		if (result == 0) {
			return new ResponseEntity<ResultValue>(new ResultValue(ResultCode.FAILED, null), HttpStatus.OK);
		} else {
			return new ResponseEntity<ResultValue>(new ResultValue(ResultCode.SUCCESS, null), HttpStatus.OK);
		}
	}

	/**
	 * 
	 * @param request
	 * @param reqParam
	 * @return
	 * @throws BindException
	 */
	public ResponseEntity<?> getResumeDtl(HttpServletRequest request, @PathVariable(name = "terminal", required = true) String terminal,
			@ModelAttribute SearchParam reqParam, BindingResult bindingResult) throws BindException {

		Long idV = reqParam.getIdV();
		logger.info("getResumeDtl() terminal={}, idV={}", terminal, idV);
		logService.saveAccessLog(request);

		String term = terminalService.checkTerminal(bindingResult, terminal);

		if (term == null) {
			FieldError e = new FieldError("RequestParameter", "term", "Terminal은 필수 입력 값입니다.");
			bindingResult.addError(e);
		}
		if (idV == null) {
			FieldError e = new FieldError("RequestParameter", "idV", "idV는 필수 입력 값입니다.");
			bindingResult.addError(e);
		} 

		if (bindingResult.hasErrors())
			throw new BindException(bindingResult);

		int result = monitorService.resumeVehicle(term, idV);
		if (result == 0) {
			return new ResponseEntity<ResultValue>(new ResultValue(ResultCode.FAILED, null), HttpStatus.OK);
		} else {
			return new ResponseEntity<ResultValue>(new ResultValue(ResultCode.SUCCESS, null), HttpStatus.OK);
		}
	}

	/**
	 * 
	 * @param request
	 * @param reqParam
	 * @return
	 * @throws BindException
	 */
	public ResponseEntity<?> getCallP1PoiDtl(HttpServletRequest request, @PathVariable(name = "terminal", required = true) String terminal,
			@ModelAttribute SearchParam reqParam, BindingResult bindingResult) throws BindException {

		Long idV = reqParam.getIdV();
		logger.info("getCallP1PoiDtl() terminal={}, idV={}", terminal, idV);
		logService.saveAccessLog(request);

		String term = terminalService.checkTerminal(bindingResult, terminal);

		if (term == null) {
			FieldError e = new FieldError("RequestParameter", "term", "Terminal은 필수 입력 값입니다.");
			bindingResult.addError(e);
		}
		if (idV == null) {
			FieldError e = new FieldError("RequestParameter", "idV", "idV는 필수 입력 값입니다.");
			bindingResult.addError(e);
		} 

		if (bindingResult.hasErrors())
			throw new BindException(bindingResult);

		int reseumeResult = monitorService.resumeVehicle(term, idV);
		
		int callResult = monitorService.callVbase(term, idV); // 0:실패, 1:성공
		
		if (reseumeResult == 0 || callResult == 0) {
			return new ResponseEntity<ResultValue>(new ResultValue(ResultCode.FAILED, null), HttpStatus.OK);
		} else if (callResult == -1) {
			return new ResponseEntity<ResultValue>(new ResultValue(ResultCode.DUPLICATED_VALUE, null), HttpStatus.OK);
		} else {
			return new ResponseEntity<ResultValue>(new ResultValue(ResultCode.SUCCESS, null), HttpStatus.OK);
		}
	}

	/**
	 * 
	 * @param request
	 * @param reqParam
	 * @return
	 * @throws BindException
	 */
	public ResponseEntity<?> getCallP1Dtl(HttpServletRequest request, @PathVariable(name = "terminal", required = true) String terminal,
			@RequestParam(required = true) Long idPoi,
			@RequestParam(required = true) Integer repeat) throws BindException {

		logger.info("getCallP1Dtl() terminal={}, idPoi={}, repeat={}", terminal, repeat);
		logService.saveAccessLog(request);

		FieldError e = terminalService.checkTerminal(terminal);
		if (e != null) {
			throw new BindException(terminal, "terminal");
		}

		int result = monitorService.callP1Vehicle(terminal, idPoi, repeat); // 0:실패, 1:성공
		if (result == 0) {
			return new ResponseEntity<ResultValue>(new ResultValue(ResultCode.FAILED, null), HttpStatus.OK);
		} else {
			return new ResponseEntity<ResultValue>(new ResultValue(ResultCode.SUCCESS, null), HttpStatus.OK);
		}
		
	}

	/**
	 * 
	 * @param request
	 * @param reqParam
	 * @return
	 * @throws BindException
	 */
	public ResponseEntity<?> getCallP1CancelDtl(HttpServletRequest request, @PathVariable(name = "terminal", required = true) String terminal,
			@RequestParam(required = true) Long idCall) throws BindException {

		logger.info("getCallP1Dtl() terminal={}, idCall={}", terminal, idCall);
		logService.saveAccessLog(request);

		FieldError e = terminalService.checkTerminal(terminal);
		if (e != null) {
			throw new BindException(terminal, "terminal");
		}

		int result = monitorService.callP1Cancel(terminal, idCall); // 0:실패, 1:성공
		if (result == 0) {
			return new ResponseEntity<ResultValue>(new ResultValue(ResultCode.FAILED, null), HttpStatus.OK);
		} else {
			return new ResponseEntity<ResultValue>(new ResultValue(ResultCode.SUCCESS, null), HttpStatus.OK);
		}
		
	}

	/**
	 * 
	 * @param request
	 * @param reqParam
	 * @return
	 * @throws BindException
	 */
	public ResponseEntity<?> getVehicleT1Ls(HttpServletRequest request) throws BindException {
		String term = "T1";
		logger.info("getVehicleLs() terminal={}", term);
		logService.saveAccessLog(request);

		return getVehicleLs(request, term);
	}

	public ResponseEntity<?> getVehicleT2Ls(HttpServletRequest request) throws BindException {
		String term = "T2";
		logger.info("getVehicleLs() terminal={}", term);
		logService.saveAccessLog(request);

		return getVehicleLs(request, term);
	}

	private ResponseEntity<?> getVehicleLs(HttpServletRequest request, String terminal) throws BindException {
		logger.info("getVehicleLs() terminal={}", terminal);

		List<ResVhcl> result = monitorService.getVehicleLs(terminal);
		return new ResponseEntity<ResultValue>(new ResultValue(ResultCode.SUCCESS, result), HttpStatus.OK);
	}

	/**
	 * 
	 * @param request
	 * @param reqParam
	 * @return
	 * @throws BindException
	 */
	public ResponseEntity<?> getEventLs(HttpServletRequest request, @PathVariable(name = "terminal", required = true) String terminal) throws BindException {
		logger.info("getEventLs() terminal={}", terminal);
		logService.saveAccessLog(request);

		List<ResEvent> result = null;
		if( MyUtil.isT1(terminal) || MyUtil.isT2(terminal) ) {
			result = monitorService.getEventLs(terminal);
		}
		return new ResponseEntity<ResultValue>(new ResultValue(ResultCode.SUCCESS, result), HttpStatus.OK);
	}

	/**
	 * 
	 * @param request
	 * @param reqParam
	 * @return
	 * @throws BindException
	 */
	public ResponseEntity<?> getTripLs(HttpServletRequest request, @PathVariable(name = "terminal", required = true) String terminal) throws BindException {
		logger.info("getTripLs() terminal={}", terminal);
		logService.saveAccessLog(request);


		List<ResTrip> result = null;
		if( MyUtil.isT1(terminal) || MyUtil.isT2(terminal) ) {
			result = monitorService.getTripLs(terminal);
		}
		return new ResponseEntity<ResultValue>(new ResultValue(ResultCode.SUCCESS, result), HttpStatus.OK);
	}

	/**
	 * 
	 * @param request
	 * @param reqParam
	 * @return
	 * @throws BindException
	 */
	public ResponseEntity<?> getPoiLs(HttpServletRequest request, @PathVariable(name = "terminal", required = true) String terminal) throws BindException {
		logger.info("getPoiLs() terminal={}", terminal);
		logService.saveAccessLog(request);

		
		List<ResPoi> result = null;
		if( MyUtil.isT1(terminal) || MyUtil.isT2(terminal) ) {
			result = monitorService.getPoiLs(terminal);
		}
		return new ResponseEntity<ResultValue>(new ResultValue(ResultCode.SUCCESS, result), HttpStatus.OK);
	}

}