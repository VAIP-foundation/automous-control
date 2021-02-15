package com.autonomous.pm.web.controller.v1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.autonomous.pm.component.validator.CustomCollectionValidator;
import com.autonomous.pm.domain.common.ResultCode;
import com.autonomous.pm.domain.common.ResultValue;
import com.autonomous.pm.memcache.MemDB;
import com.autonomous.pm.model.KeyValue;
import com.autonomous.pm.service.CommonServiceImpl;
import com.autonomous.pm.service.LogServiceImpl;
import com.autonomous.pm.util.StringUtil;
import com.autonomous.pm.web.controller.BaseRestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1")
public class CommonRestController extends BaseRestController {

	public static final Logger logger = LoggerFactory.getLogger(CommonRestController.class);

	@Autowired
	CustomCollectionValidator customCollectionValidator;

	@Autowired
	LogServiceImpl logService;
	
    @Autowired
    CommonServiceImpl commonService;

	/**
	 * 
	 * @param request
	 * @param reqParam
	 * @return
	 * @throws BindException
	 */
	public ResponseEntity<?> getCommonKeylist(HttpServletRequest request, @RequestParam(name = "cat") String cat) {
		logger.info("getCommonKeylist() cat:" + cat);
		logService.saveAccessLog(request);
		
		if (StringUtil.isEmpty(cat)) {
			return new ResponseEntity<ResultValue>(new ResultValue(ResultCode.INVALID_INPUT_FIELD, null), HttpStatus.OK);
		}

		List<KeyValue> result = commonService.getCommonKeylist(cat);

		return new ResponseEntity<ResultValue>(new ResultValue(ResultCode.SUCCESS, result), HttpStatus.OK);
	}
	
	/**
	 * Memory DB 현재 값 조회
	 */
	public ResponseEntity<?> getCommonMemDBLs(HttpServletRequest request, @RequestParam(name = "db", required = false) String db) {
		logger.info("getCommonMemDBLs() db:" + db);
		logService.saveAccessLog(request);


		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("VHCL", MemDB.VHCL.selectAll());
		map.put("POI", MemDB.POI.selectAll());
		map.put("GRP_POI", MemDB.GRP_POI.selectAll());
		map.put("GATE", MemDB.GATE.selectAll());
		map.put("DRV", MemDB.DRV.selectAll());
		try {
			map.put("TRP", MemDB.TRP.selectAll());
		} catch (NullPointerException e) {
			map.put("TRP", new ArrayList<String>());
		}
		map.put("SNSR", MemDB.SNSR.selectAll());
		map.put("EVT", MemDB.EVT.selectAll());
		map.put("RTI", MemDB.RTI.selectAll());
		map.put("CALL_PLAN_P1", MemDB.CALL_PLAN_P1.selectAll());
		map.put("CALL_PLAN_P2", MemDB.CALL_PLAN_P2.selectAll());
		map.put("LAST_CALL", MemDB.LAST_CALL.selectAll());
		map.put("WS_PM_SESSION", MemDB.WS_PM_SESSION.selectAll());
		map.put("JWT", MemDB.JWT.selectAll());
		
		if ( !StringUtil.isEmpty(db) ) {
			Object memdb = map.get(db);
			map.clear();
			map.put(db, memdb);
		}
		
		return new ResponseEntity<ResultValue>(new ResultValue(ResultCode.SUCCESS, map), HttpStatus.OK);
	}

}