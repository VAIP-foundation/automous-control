package com.autonomous.pm.vhcl.controller.v1;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.autonomous.pm.auth.VhclDetailsImpl;
import com.autonomous.pm.component.validator.CustomCollectionValidator;
import com.autonomous.pm.domain.common.ResultCode;
import com.autonomous.pm.domain.common.ResultObject;
import com.autonomous.pm.domain.common.ResultTy;
import com.autonomous.pm.domain.common.ResultValue;
import com.autonomous.pm.domain.common.SearchParam;
import com.autonomous.pm.domain.structure.Authentication;
import com.autonomous.pm.domain.structure.Call;
import com.autonomous.pm.domain.structure.FlightInfo;
import com.autonomous.pm.domain.structure.Poi;
import com.autonomous.pm.domain.structure.ack.Ack;
import com.autonomous.pm.memcache.MemDB;
import com.autonomous.pm.model.Do.TCsSurveyHst;
import com.autonomous.pm.model.Do.VVhcl;
import com.autonomous.pm.model.Dto.ReqPmAuthen;
import com.autonomous.pm.model.Dto.ReqRate;
import com.autonomous.pm.service.CallServiceImpl;
import com.autonomous.pm.service.CmdHstServiceImpl;
import com.autonomous.pm.service.FlightServiceImpl;
import com.autonomous.pm.service.LogServiceImpl;
import com.autonomous.pm.service.restful.RateServiceImpl;
import com.autonomous.pm.service.restful.VhclServiceImpl;
import com.autonomous.pm.util.JwtUtil;
import com.autonomous.pm.util.MyUtil;
import com.autonomous.pm.web.controller.BaseRestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1")
public class VhclRestController extends BaseRestController {

    public static final Logger logger = LoggerFactory.getLogger(VhclRestController.class);

    @Autowired
    CustomCollectionValidator customCollectionValidator;
    
    @Autowired
    CallServiceImpl callService;
    
    @Autowired
	FlightServiceImpl flightService;
    @Autowired
	RateServiceImpl rateService;
    @Autowired
	VhclServiceImpl vhclService;
	@Autowired
	LogServiceImpl logService;
    
    @Autowired
    CmdHstServiceImpl cmdHstService;

    // 101. 인증
    public ResponseEntity<?> getAuthentication(HttpServletRequest request, @RequestParam("vid") String vrn, @RequestBody ReqPmAuthen reqParam) {
    	logger.info("getAuthentication() vrn:{}, RequestParam:{}", vrn, reqParam);
    	logService.saveAccessLog(request, reqParam.toString());

        if ( !vhclService.checkVrnPwd(vrn, reqParam.getPassword()) ) {
            return new ResponseEntity<ResultValue>(new ResultValue(ResultCode.NOT_MATCH_PWD, null), HttpStatus.OK);
        }
        
        
        VhclDetailsImpl details = new VhclDetailsImpl(vrn, reqParam.getPassword(), AuthorityUtils.createAuthorityList("1"));	// TODO: 권한레벨지정
        String token = JwtUtil.createAccessToken(details);
        DecodedJWT jwt = JwtUtil.tokenToJwt(token);
        String expireTime = JwtUtil.remainExpiresDateByISO8601(jwt);
        VVhcl vhcl = MemDB.VHCL.selectAll().stream().filter(v->vrn.equals(v.getVrn())).findAny().orElse(null);
        String term = null;
        String hall = null;
        if ( vhcl != null ) {
        	term = vhcl.getTerm();
        	if ( MyUtil.isT1(term) ) {
        		hall = "ARV";
        	} else if ( MyUtil.isT2(term) ) {
        		hall = "DPT";
        	}
        }
        
        Authentication auth = new Authentication();
        auth.setToken(token);
        auth.setExprtm(expireTime);
        auth.setTerm(term);
        auth.setHall(hall);
        
        return new ResponseEntity<Object>(new ResultObject(ResultTy.AUTHEN, auth), HttpStatus.OK);
    }
    
    // 102. CALL 요청
    public ResponseEntity<?> getCall(HttpServletRequest request, @PathVariable("vrn") String vrn) {
    	logger.info("getCall() vrn:" + vrn);
    	logService.saveAccessLog(request);

        if ( vrn == null || "".equals(vrn) ) {
            return new ResponseEntity<ResultValue>(new ResultValue(ResultCode.NOT_NULL_FIELD, null), HttpStatus.OK);
        }
        
        VVhcl vhcl = MemDB.VHCL.selectAll().stream().filter(v->vrn.equals(v.getVrn())).findFirst().orElse(null);
        if ( vhcl == null || vhcl.getIdV() == null ) {
        	return new ResponseEntity<ResultValue>(new ResultValue(ResultCode.NOT_NULL_FIELD, null), HttpStatus.OK);
        }
        
        Call call = callService.getCall(vhcl.getIdV());
        
		// T_CMD_HST에 저장
 		cmdHstService.insertCmdHstByGetCall(vrn, call);
     		
        return new ResponseEntity<Object>(new ResultObject(ResultTy.CALL, call), HttpStatus.OK);
    }
    
    // 103. 비행스케쥴 요청
    public ResponseEntity<?> getFlightSchLs(HttpServletRequest request, @PathVariable("vrn") String vrn) {
    	logger.info("getFlightSchLs() vrn:" + vrn);
    	logService.saveAccessLog(request);

        if ( vrn == null || "".equals(vrn) ) {
            return new ResponseEntity<ResultValue>(new ResultValue(ResultCode.NOT_NULL_FIELD, null), HttpStatus.OK);
        }
        
        List<FlightInfo> result = flightService.getFlightSchLs(vrn);
    	
        return new ResponseEntity<Object>(new ResultObject(ResultTy.FLIGHT_INFO, result), HttpStatus.OK);
    }
    
    // 108. 서비스 만족도 조사
    public ResponseEntity<?> postRate(HttpServletRequest request, @RequestBody ReqRate reqRate) throws BindException {
    	Integer star = reqRate.getStar();
    	logger.info("getFlightSchedule() star:" + star);
    	logService.saveAccessLog(request, reqRate.toString());
    	Ack ack = new Ack();
    	
    	String vrn = getUserId(request);
    	VVhcl vhcl = MemDB.VHCL.selectAll().stream().filter(v->v.getVrn().equals(vrn)).findFirst().orElse(null);
    	if ( vhcl == null ) {
    		ack.setResult(1);	// 1:fail
        	ack.setReason("VRN_NOT_FOUND");
    	} else if ( star == null || star < 1 || star > 10 ) {
        	ack.setResult(1);	// 1:fail
        	ack.setReason("INVALID_INPUT_FIELD");
        } else {
    		Long idV = vhcl.getIdV();
    		TCsSurveyHst tCsSurveyHst = new TCsSurveyHst();
    		tCsSurveyHst.setIdV(idV);
    		tCsSurveyHst.setStars(star);
    		rateService.postAdminRateLs(tCsSurveyHst);
        	
        }
        
        return new ResponseEntity<Object>(new ResultObject(ResultTy.ACK, ack), HttpStatus.OK);
    }

}