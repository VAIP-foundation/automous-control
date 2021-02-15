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

import com.autonomous.pm.component.validator.CustomCollectionValidator;
import com.autonomous.pm.domain.common.ResultCode;
import com.autonomous.pm.domain.common.ResultValue;
import com.autonomous.pm.model.Usr;
import com.autonomous.pm.model.Dto.ReqFromToDay;
import com.autonomous.pm.model.Dto.ReqFromToMonth;
import com.autonomous.pm.model.Dto.ResCnflctCnt;
import com.autonomous.pm.model.Dto.ResDrKmCnt;
import com.autonomous.pm.model.Dto.ResDrTmsCnt;
import com.autonomous.pm.model.Dto.ResFailCnt;
import com.autonomous.pm.model.Dto.ResMStopCnt;
import com.autonomous.pm.model.Dto.ResMeStopCnt;
import com.autonomous.pm.model.Dto.ResPeoplCnt;
import com.autonomous.pm.model.Dto.ResRStopCnt;
import com.autonomous.pm.model.Dto.ResStatSheet;
import com.autonomous.pm.model.Dto.ResTotal;
import com.autonomous.pm.model.Dto.ResTripCnt;
import com.autonomous.pm.service.LogServiceImpl;
import com.autonomous.pm.service.restful.StatServiceImpl;
import com.autonomous.pm.service.restful.VhclServiceImpl;
import com.autonomous.pm.web.controller.BaseRestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1")
public class AdminStatRestController extends BaseRestController {

    public static final Logger logger = LoggerFactory.getLogger(AdminStatRestController.class);

    @Autowired
    CustomCollectionValidator customCollectionValidator;

    @Autowired
    StatServiceImpl statService;
    
    @Autowired
    VhclServiceImpl vehicleService;
	@Autowired
	LogServiceImpl logService;


    /**
     * @param request
     * @param reqParam
     * @return
     * @throws BindException 
     */
    public ResponseEntity<?> getStatDistDailyLs(HttpServletRequest request,
    		@Valid @ModelAttribute ReqFromToDay reqParam,
    		BindingResult bindingResult) throws BindException {
        logger.info("getStatDistDailyLs() SearchParam={}", reqParam);
		logService.saveAccessLog(request);
        
        // 에러 체크
        if (bindingResult.hasErrors())
        	throw new BindException(bindingResult);
        
        // 통계-운행거리 조회 (일별)
        List<ResDrKmCnt> result = statService.getStatDistDailyLs(reqParam);
		
    	return new ResponseEntity<ResultValue>(new ResultValue(ResultCode.SUCCESS, result), HttpStatus.OK);
    }
    
    /**
     * @param request
     * @param reqParam
     * @return
     * @throws BindException 
     */
    public ResponseEntity<?> getStatDistMonthlyLs(HttpServletRequest request,
    		@Valid @ModelAttribute ReqFromToMonth reqParam,
    		BindingResult bindingResult) throws BindException {
        logger.info("getStatDistMonthlyLs() SearchParam={}", reqParam);
		logService.saveAccessLog(request);
        
        // 에러 체크
        if (bindingResult.hasErrors())
        	throw new BindException(bindingResult);
        
        List<ResDrKmCnt> result = statService.getStatDistMonthlyLs(reqParam);
        	
    	return new ResponseEntity<ResultValue>(new ResultValue(ResultCode.SUCCESS, result), HttpStatus.OK);
    }
    

    /**
     * @param request
     * @param reqParam
     * @return
     * @throws BindException 
     */
    public ResponseEntity<?> getStatDrvtmDailyLs(HttpServletRequest request,
    		@Valid @ModelAttribute ReqFromToDay reqParam,
    		BindingResult bindingResult) throws BindException {
        logger.info("getStatDrvtmDailyLs() SearchParam={}", reqParam);
		logService.saveAccessLog(request);
        
        if (bindingResult.hasErrors())
        	throw new BindException(bindingResult);
        
        List<ResDrTmsCnt> result = statService.getStatDrvtmDailyLs(reqParam);
		
    	return new ResponseEntity<ResultValue>(new ResultValue(ResultCode.SUCCESS, result), HttpStatus.OK);
    }
    
    /**
     * @param request
     * @param reqParam
     * @return
     * @throws BindException 
     */
    public ResponseEntity<?> getStatDrvtmMonthlyLs(HttpServletRequest request,
    		@Valid @ModelAttribute ReqFromToMonth reqParam,
    		BindingResult bindingResult) throws BindException {
        logger.info("getStatDrvtmMonthlyLs() SearchParam={}", reqParam);
		logService.saveAccessLog(request);
        
        // 에러 체크
        if (bindingResult.hasErrors())
        	throw new BindException(bindingResult);
        
        // 통계-운행시간 조회 (월별)
        List<ResDrTmsCnt> result = statService.getStatDrvtmMonthlyLs(reqParam);
		
    	return new ResponseEntity<ResultValue>(new ResultValue(ResultCode.SUCCESS, result), HttpStatus.OK);
    }
    
    /**
     * @param request
     * @param reqParam
     * @return
     * @throws BindException 
     */
    @RequestMapping(value = "/admin/stat/users/daily/ls", method = RequestMethod.GET)
    public ResponseEntity<?> getStatUsersDailyLs(HttpServletRequest request,
    		@Valid @ModelAttribute ReqFromToDay reqParam,
    		BindingResult bindingResult) throws BindException {
        logger.info("getStatUsersDailyLs() SearchParam={}", reqParam);
		logService.saveAccessLog(request);
        
        if (bindingResult.hasErrors())
        	throw new BindException(bindingResult);
        
        List<ResPeoplCnt> result = statService.getStatUsersDailyLs(reqParam);
		
    	return new ResponseEntity<ResultValue>(new ResultValue(ResultCode.SUCCESS, result), HttpStatus.OK);
    }
    
    /**
     * @param request
     * @param reqParam
     * @return
     * @throws BindException 
     */
    public ResponseEntity<?> getStatUsersMonthlyLs(HttpServletRequest request,
    		@Valid @ModelAttribute ReqFromToMonth reqParam,
    		BindingResult bindingResult) throws BindException {
        logger.info("getStatUsersMonthlyLs() SearchParam={}", reqParam);
		logService.saveAccessLog(request);
        
        if (bindingResult.hasErrors())
        	throw new BindException(bindingResult);
        
        List<ResPeoplCnt> result = statService.getStatUsersMonthlyLs(reqParam);
		
    	return new ResponseEntity<ResultValue>(new ResultValue(ResultCode.SUCCESS, result), HttpStatus.OK);
    }
    
    /**
     * @param request
     * @param reqParam
     * @return
     * @throws BindException 
     */
    public ResponseEntity<?> getStatTripcntDailyLs(HttpServletRequest request,
    		@Valid @ModelAttribute ReqFromToDay reqParam,
    		BindingResult bindingResult) throws BindException {
        logger.info("getStatTripcntDailyLs() SearchParam={}", reqParam);
		logService.saveAccessLog(request);
        
        if (bindingResult.hasErrors())
        	throw new BindException(bindingResult);
        
        List<ResTripCnt> result = statService.getStatTripcntDailyLs(reqParam);
		
    	return new ResponseEntity<ResultValue>(new ResultValue(ResultCode.SUCCESS, result), HttpStatus.OK);
    }
    
    /**
     * @param request
     * @param reqParam
     * @return
     * @throws BindException 
     */
    public ResponseEntity<?> getStatTripcntMonthlyLs(HttpServletRequest request,
    		@Valid @ModelAttribute ReqFromToMonth reqParam,
    		BindingResult bindingResult) throws BindException {
        logger.info("getStatTripcntMonthlyLs() SearchParam={}", reqParam);
		logService.saveAccessLog(request);
        
        // 에러 체크
        if (bindingResult.hasErrors())
        	throw new BindException(bindingResult);
        
        // 통계-운행횟수 조회 (월별)
        List<ResTripCnt> result = statService.getStatTripcntMonthlyLs(reqParam);
		
    	return new ResponseEntity<ResultValue>(new ResultValue(ResultCode.SUCCESS, result), HttpStatus.OK);
    }

    /**
     * @param request
     * @param reqParam
     * @return
     * @throws BindException 
     */
    public ResponseEntity<?> getStatRstopcntDailyLs(HttpServletRequest request,
    		@Valid @ModelAttribute ReqFromToDay reqParam,
    		BindingResult bindingResult) throws BindException {
        logger.info("getStatRstopcntMonthlyLs() SearchParam={}", reqParam);
		logService.saveAccessLog(request);
        
        if (bindingResult.hasErrors())
        	throw new BindException(bindingResult);
        
        List<ResRStopCnt> result = statService.getStatRstopcntDailyLs(reqParam);
		
    	return new ResponseEntity<ResultValue>(new ResultValue(ResultCode.SUCCESS, result), HttpStatus.OK);
    }

    /**
     * @param request
     * @param reqParam
     * @return
     * @throws BindException 
     */
    public ResponseEntity<?> getStatRstopcntMonthlyLs(HttpServletRequest request,
    		@Valid @ModelAttribute ReqFromToMonth reqParam,
    		BindingResult bindingResult) throws BindException {
        logger.info("getStatRstopcntDailyLs() SearchParam={}", reqParam);
		logService.saveAccessLog(request);
        
        if (bindingResult.hasErrors())
        	throw new BindException(bindingResult);
        
        List<ResRStopCnt> result = statService.getStatRstopcntMonthlyLs(reqParam);
		
    	return new ResponseEntity<ResultValue>(new ResultValue(ResultCode.SUCCESS, result), HttpStatus.OK);
    }
    
    
    /**
     * @param request
     * @param reqParam
     * @return
     * @throws BindException 
     */
    public ResponseEntity<?> getStatMestopcntDailyLs(HttpServletRequest request,
    		@Valid @ModelAttribute ReqFromToDay reqParam,
    		BindingResult bindingResult) throws BindException {
        logger.info("getStatMeStopCntMonthlyLs() SearchParam={}", reqParam);
		logService.saveAccessLog(request);
        
        if (bindingResult.hasErrors())
        	throw new BindException(bindingResult);
        
        List<ResMeStopCnt> result = statService.getStatMeStopCntDailyLs(reqParam);
		
    	return new ResponseEntity<ResultValue>(new ResultValue(ResultCode.SUCCESS, result), HttpStatus.OK);
    }

    /**
     * @param request
     * @param reqParam
     * @return
     * @throws BindException 
     */
    @RequestMapping(value = "/admin/stat/mestopcnt/monthly/ls", method = RequestMethod.GET)
    public ResponseEntity<?> getStatMeStopcntMonthlyLs(HttpServletRequest request,
    		@Valid @ModelAttribute ReqFromToMonth reqParam,
    		BindingResult bindingResult) throws BindException {
        logger.info("getStatMeStopCntDailyLs() SearchParam={}", reqParam);
		logService.saveAccessLog(request);
        
        if (bindingResult.hasErrors())
        	throw new BindException(bindingResult);
        
        List<ResMeStopCnt> result = statService.getStatMeStopCntMonthlyLs(reqParam);
		
    	return new ResponseEntity<ResultValue>(new ResultValue(ResultCode.SUCCESS, result), HttpStatus.OK);
    }
    
    
    /**
     * @param request
     * @param reqParam
     * @return
     * @throws BindException 
     */
    public ResponseEntity<?> getStatMStopcntDailyLs(HttpServletRequest request,
    		@Valid @ModelAttribute ReqFromToDay reqParam,
    		BindingResult bindingResult) throws BindException {
        logger.info("getStatMStopCntMonthlyLs() SearchParam={}", reqParam);
		logService.saveAccessLog(request);
        
        // 에러 체크
        if (bindingResult.hasErrors())
        	throw new BindException(bindingResult);
        
        // 통계-승객정지 조회 (일별)
        List<ResMStopCnt> result = statService.getStatMStopCntDailyLs(reqParam);
		
    	return new ResponseEntity<ResultValue>(new ResultValue(ResultCode.SUCCESS, result), HttpStatus.OK);
    }

    /**
     * 통계-승객정지 
     * @param reqParam
     * @return
     * @throws BindException 
     */
    public ResponseEntity<?> getStatMStopcntMonthlyLs(HttpServletRequest request,
    		@Valid @ModelAttribute ReqFromToMonth reqParam,
    		BindingResult bindingResult) throws BindException {
        logger.info("getStatMStopcntDailyLs() SearchParam={}", reqParam);
		logService.saveAccessLog(request);
        
        if (bindingResult.hasErrors())
        	throw new BindException(bindingResult);
        
        List<ResMStopCnt> result = statService.getStatMStopCntMonthlyLs(reqParam);
		
    	return new ResponseEntity<ResultValue>(new ResultValue(ResultCode.SUCCESS, result), HttpStatus.OK);
    }
    
    /**
     * @param request
     * @param reqParam
     * @return
     * @throws BindException 
     */
    public ResponseEntity<?> getStatCrshDailyLs(HttpServletRequest request,
    		@Valid @ModelAttribute ReqFromToDay reqParam,
    		BindingResult bindingResult) throws BindException {
        logger.info("getStatCrshDailyLs() SearchParam={}", reqParam);
		logService.saveAccessLog(request);
        
        if (bindingResult.hasErrors())
        	throw new BindException(bindingResult);
        
        List<ResCnflctCnt> result = statService.getStatCrshDailyLs(reqParam);
		
    	return new ResponseEntity<ResultValue>(new ResultValue(ResultCode.SUCCESS, result), HttpStatus.OK);
    }
    
    /**
     * @param request
     * @param reqParam
     * @return
     * @throws BindException 
     */
    public ResponseEntity<?> getStatCrshMonthlyLs(HttpServletRequest request,
    		@Valid @ModelAttribute ReqFromToMonth reqParam,
    		BindingResult bindingResult) throws BindException {
        logger.info("getStatCrshMonthlyLs() SearchParam={}", reqParam);
		logService.saveAccessLog(request);
        
        // 에러 체크
        if (bindingResult.hasErrors())
        	throw new BindException(bindingResult);
        
        // 통계-추돌 조회 (월별)
        List<ResCnflctCnt> result = statService.getStatCrshMonthlyLs(reqParam);
		
    	return new ResponseEntity<ResultValue>(new ResultValue(ResultCode.SUCCESS, result), HttpStatus.OK);
    }

    
    /**
     * @param request
     * @param reqParam
     * @return
     * @throws BindException 
     */
    public ResponseEntity<?> getStatFailDailyLs(HttpServletRequest request,
    		@Valid @ModelAttribute ReqFromToDay reqParam,
    		BindingResult bindingResult) throws BindException {
        logger.info("getStatFailDailyLs() SearchParam={}", reqParam);
		logService.saveAccessLog(request);
        
        if (bindingResult.hasErrors())
        	throw new BindException(bindingResult);
        
        List<ResFailCnt> result = statService.getStatFailDailyLs(reqParam);
		
    	return new ResponseEntity<ResultValue>(new ResultValue(ResultCode.SUCCESS, result), HttpStatus.OK);
    }
    
    /**
     * @param request
     * @param reqParam
     * @return
     * @throws BindException 
     */
    public ResponseEntity<?> getStatFailMonthlyLs(HttpServletRequest request,
    		@Valid @ModelAttribute ReqFromToMonth reqParam,
    		BindingResult bindingResult) throws BindException {
        logger.info("getStatFailMonthlyLs() SearchParam={}", reqParam);
		logService.saveAccessLog(request);
        
        if (bindingResult.hasErrors())
        	throw new BindException(bindingResult);
        
        List<ResFailCnt> result = statService.getStatFailMonthlyLs(reqParam);
		
    	return new ResponseEntity<ResultValue>(new ResultValue(ResultCode.SUCCESS, result), HttpStatus.OK);
    }   
    
    /**
     * @param request
     * @param reqParam
     * @return
     * @throws BindException 
     */
    public ResponseEntity<?> getStatTotalDailyLs(HttpServletRequest request,
    		@Valid @ModelAttribute ReqFromToDay reqParam,
    		BindingResult bindingResult) throws BindException {
        logger.info("getStatTotalDailyLs() SearchParam={}", reqParam);
		logService.saveAccessLog(request);
        
        if (bindingResult.hasErrors())
        	throw new BindException(bindingResult);
        
        List<ResTotal> result = statService.getStatTotalDailyLs(reqParam);
		
    	return new ResponseEntity<ResultValue>(new ResultValue(ResultCode.SUCCESS, result), HttpStatus.OK);
    }
    
    /**
     * @param request
     * @param reqParam
     * @return
     * @throws BindException 
     */
    public ResponseEntity<?> getStatTotalMonthlyLs(HttpServletRequest request,
    		@Valid @ModelAttribute ReqFromToMonth reqParam,
    		BindingResult bindingResult) throws BindException {
        logger.info("getStatTotalMonthlyLs() SearchParam={}", reqParam);
		logService.saveAccessLog(request);
        
        // 에러 체크
        if (bindingResult.hasErrors())
        	throw new BindException(bindingResult);
        
        // 통계-차량별 집계 목록 조회 (월별)
        List<ResTotal> result = statService.getStatTotalMonthlyLs(reqParam);
		
    	return new ResponseEntity<ResultValue>(new ResultValue(ResultCode.SUCCESS, result), HttpStatus.OK);
    }  
    
    /**
     * 통계-엑셀다운로드용 목록 조회(일별)
     * @param request
     * @param reqParam
     * @return
     * @throws BindException 
     */
    public ResponseEntity<?> getStatExcelDailyLs(HttpServletRequest request,
    		@Valid @ModelAttribute ReqFromToDay reqParam,
    		BindingResult bindingResult) throws BindException {
        logger.info("getStatExcelDailyLs() SearchParam={}", reqParam);
		logService.saveAccessLog(request);
        
        // 에러 체크
        if (bindingResult.hasErrors())
        	throw new BindException(bindingResult);
        
        // 통계-엑셀다운로드용 목록 조회 (일별)
        List<ResStatSheet> result = statService.getStatExcelDailyLs(reqParam);
		
    	return new ResponseEntity<ResultValue>(new ResultValue(ResultCode.SUCCESS, result), HttpStatus.OK);
    }  
    
    /**
     * @param request
     * @param reqParam
     * @return
     * @throws BindException 
     */
    public ResponseEntity<?> getStatExcelMonthlyLs(HttpServletRequest request,
    		@Valid @ModelAttribute ReqFromToMonth reqParam,
    		BindingResult bindingResult) throws BindException {
        logger.info("getStatExcelMonthlyLs() SearchParam={}", reqParam);
		logService.saveAccessLog(request);
        
        if (bindingResult.hasErrors())
        	throw new BindException(bindingResult);
        
        List<ResStatSheet> result = statService.getStatExcelMonthlyLs(reqParam);
		
    	return new ResponseEntity<ResultValue>(new ResultValue(ResultCode.SUCCESS, result), HttpStatus.OK);
    }
    
    /**
     * @param request
     * @param reqParam
     * @return
     * @throws BindException 
     */
    public ResponseEntity<?> getStatRawEventLs(HttpServletRequest request,
    		@Valid @ModelAttribute ReqFromToDay reqParam,
    		BindingResult bindingResult) throws BindException {
        logger.info("getStatRawEventLs() SearchParam={}", reqParam);
		logService.saveAccessLog(request);
        
        if (bindingResult.hasErrors())
        	throw new BindException(bindingResult);
        
        List<ResStatSheet> result = statService.getStatRawEventLs(reqParam);
		
    	return new ResponseEntity<ResultValue>(new ResultValue(ResultCode.SUCCESS, result), HttpStatus.OK);
    }
    
    /**
     * @param request
     * @param reqParam
     * @return
     * @throws BindException 
     */
    public ResponseEntity<?> getStatRawTripLs(HttpServletRequest request,
    		@Valid @ModelAttribute ReqFromToDay reqParam,
    		BindingResult bindingResult) throws BindException {
        logger.info("getStatRawEventLs() SearchParam={}", reqParam);
		logService.saveAccessLog(request);
        
        if (bindingResult.hasErrors())
        	throw new BindException(bindingResult);
        
        List<ResStatSheet> result = statService.getStatRawTripLs(reqParam);
		
    	return new ResponseEntity<ResultValue>(new ResultValue(ResultCode.SUCCESS, result), HttpStatus.OK);
    }
    
    /**
     * @param request
     * @param reqParam
     * @return
     * @throws BindException 
     */
    public ResponseEntity<?> getStatRawCallLs(HttpServletRequest request,
    		@Valid @ModelAttribute ReqFromToDay reqParam,
    		BindingResult bindingResult) throws BindException {
        logger.info("getStatRawEventLs() SearchParam={}", reqParam);
		logService.saveAccessLog(request);
        
        if (bindingResult.hasErrors())
        	throw new BindException(bindingResult);
        
        List<ResStatSheet> result = statService.getStatRawCallLs(reqParam);
		
    	return new ResponseEntity<ResultValue>(new ResultValue(ResultCode.SUCCESS, result), HttpStatus.OK);
    }
}