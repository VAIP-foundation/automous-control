package com.autonomous.pm.memcache;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.autonomous.pm.auth.UsrDetailsImpl;
import com.autonomous.pm.auth.ajax.AjaxUserDetailsService;
import com.autonomous.pm.dao.DrvRawYymmMapper;
import com.autonomous.pm.dao.EvtHstMapper;
import com.autonomous.pm.dao.GrpPoiMapper;
import com.autonomous.pm.dao.IisAfsQueueMapper;
import com.autonomous.pm.dao.PoiGateMapMapper;
import com.autonomous.pm.dao.PoiMapper;
import com.autonomous.pm.dao.RouteHstMapper;
import com.autonomous.pm.dao.SnsrRawYymmMapper;
import com.autonomous.pm.dao.TrpRawMapper;
import com.autonomous.pm.dao.VhclMapper;
import com.autonomous.pm.domain.structure.Call;
import com.autonomous.pm.domain.structure.FlightInfo;
import com.autonomous.pm.domain.structure.mem.DrivingInfoMem;
import com.autonomous.pm.domain.structure.mem.EventMem;
import com.autonomous.pm.domain.structure.mem.RouteInfoMem;
import com.autonomous.pm.domain.structure.mem.SensorInfoMem;
import com.autonomous.pm.domain.structure.mem.TripInfoMem;
import com.autonomous.pm.model.CallPlanP1;
import com.autonomous.pm.model.CallPlanP2;
import com.autonomous.pm.model.JwtSession;
import com.autonomous.pm.model.Do.TEvtHst;
import com.autonomous.pm.model.Do.TGrpPoi;
import com.autonomous.pm.model.Do.TSnsrRawYymm;
import com.autonomous.pm.model.Do.TTrpRaw;
import com.autonomous.pm.model.Do.VPoiGate;
import com.autonomous.pm.model.Dto.DrvRawYymm;
import com.autonomous.pm.model.Dto.IisAfsQueue;
import com.autonomous.pm.model.Dto.PmAssgn;
import com.autonomous.pm.model.Dto.ResAdminPoi;
import com.autonomous.pm.model.Dto.RouteHst;
import com.autonomous.pm.service.CallServiceImpl;
import com.autonomous.pm.service.restful.PoiServiceImpl;
import com.autonomous.pm.service.restful.VhclServiceImpl;
import com.autonomous.pm.util.Environment;
import com.autonomous.pm.util.JwtUtil;

/**
 * 
 * 
 * @author promaker
 * @member public void loadTsignalMst(); public void loadRsuMst() {
 *         loadVehicleOptions()
 * 
 */
@Repository
public class MemDBInitializer {
	
	@Autowired VhclMapper vhclMapper;
	@Autowired PoiMapper poiMapper;
	@Autowired GrpPoiMapper grpPoiMapper;
	@Autowired PoiGateMapMapper poiGateMapMapper;
	
	@Autowired VhclServiceImpl vhclService;
	@Autowired PoiServiceImpl poiService;
	@Autowired CallServiceImpl callService;
	@Autowired AjaxUserDetailsService ajaxUserDetailsService;
	
	@Autowired DrvRawYymmMapper drvRawYymmMapper;
	@Autowired TrpRawMapper trpRawMapper;
	@Autowired SnsrRawYymmMapper snsrRawYymmMapper;
	@Autowired EvtHstMapper evtHstMapper;
	@Autowired RouteHstMapper routeHstMapper;
	@Autowired IisAfsQueueMapper iisAfsQueueMapper;
	
	

	public static final Logger logger = LoggerFactory.getLogger(MemDBInitializer.class);

	// =====================================================================

	void initVehicleOptions() {
		loadVehicleOptions();
	}

	public void loadVehicleOptions() {

	}


	/**
	 */
	void loadVVhcl() {
		logger.info("=========== loadVVhcl start ===========");
		vhclService.loadAll();
		if (logger.isDebugEnabled()) {
			logger.debug("loadVVhcl: " + MemDB.VHCL.toString());
		}
		logger.info("=========== loadVVhcl end(size:{}) ===========", MemDB.VHCL.selectAll().size());
	}
	
	/**
	 */
	void loadTPoi() {
		logger.info("=========== loadTPoi start ===========");
		poiService.loadAll();
		if (logger.isDebugEnabled()) {
			logger.debug("loadTPoi: " + MemDB.POI.toString());
		}
		logger.info("=========== loadTPoi end(size:{}) ===========", MemDB.POI.selectAll().size());
		
	}
	
	/**
	 */
	void loadTGrpPoi() {
		logger.info("=========== loadTGrpPoi start ===========");
		List<TGrpPoi> grpPois = grpPoiMapper.getAllGrpPoiLs();
		grpPois.stream().forEach(p->{
			MemDB.GRP_POI.insertSafety(p.getIdGop(), p);
		});
		if (logger.isDebugEnabled()) {
			logger.debug("loadTGrpPoi: " + MemDB.GRP_POI.toString());
		}
		logger.info("=========== loadTGrpPoi end(size:{}) ===========", MemDB.GRP_POI.selectAll().size());
		
	}
	

	/**
	 */
	void loadTPoiGateMap() {
		logger.info("=========== loadTPoiGateMap start ===========");
		List<VPoiGate> gates = poiGateMapMapper.getAllPoiGateMapLs();
		gates.stream().forEach(g->{
			MemDB.GATE.insertSafety(g.getIdPoiGateMap(), g);
		});
		if (logger.isDebugEnabled()) {
			logger.debug("loadTPoiGateMap: " + MemDB.GATE.toString());
		}
		logger.info("=========== loadTPoiGateMap end(size:{}) ===========", MemDB.GATE.selectAll().size());
		
	}
	
	
	/**
	 */
	void loadCallPlanP1() {
		logger.info("=========== loadCallPlanP1 start ===========");
		List<CallPlanP1> callPlanList = callService.createP1CallList();
		for (CallPlanP1 cp1 : callPlanList) {
			MemDB.CALL_PLAN_P1.insertSafety(cp1.getIdCall(), cp1);	// P1 CALL 스케쥴의 key는 CALL의 ID로 한다.
		}
		if (logger.isDebugEnabled()) {
			logger.debug("loadCallSchedlueP1: " + MemDB.CALL_PLAN_P1.toString());
		}
		logger.info("=========== loadCallPlanP1 end(size:{}) ===========", MemDB.CALL_PLAN_P1.selectAll().size());
	}
	
	/**
	 */
	void loadCallPlanP2() {
		logger.info("=========== loadCallPlanP2 start ===========");
		List<CallPlanP2> callPlanList = callService.createP2CallList();
		
		for (CallPlanP2 cp2 : callPlanList) {
			MemDB.CALL_PLAN_P2.insertSafety(cp2.getSchdDate()+"_"+cp2.getFltId(), cp2);	// P2 CALL 스케쥴의 key는 스케쥴일자_항공ID 로 한다. // 20201022. kdh.
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("loadCallSchedlueP2: " + MemDB.CALL_PLAN_P2.toString());
		}
		logger.info("=========== loadCallPlanP2 end(size:{}) ===========", MemDB.CALL_PLAN_P2.selectAll().size());
	}
	/**
	 */
	void loadLastCall() {
		logger.info("=========== loadLastCall start ===========");
		List<PmAssgn> lastCallList = callService.createLastCallList();
		
		for (PmAssgn pa : lastCallList) {
			Call call = new Call();
			call.setCallid(pa.getCallId());
			call.setMid(null);
			call.setPrio(pa.getPri());
			call.setSub(null);
			if ( pa.getIdQueue() != null ) {
				IisAfsQueue queue = iisAfsQueueMapper.selectById(pa.getIdQueue());
				if ( queue != null ) {
					FlightInfo fi = queue.toFlightInfo();
					call.setFi(fi);
				}
			}
			if ( pa.getToPoi() != null ) {
				ResAdminPoi memPoi = MemDB.POI.select(pa.getToPoi());
				if ( memPoi != null ) {
					call.setTo(memPoi.toPoi());
				}
			}
			MemDB.LAST_CALL.insertSafety(pa.getIdV(), call);	// 차량별 마지막 call을 저장해놓는다. 
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("loadLastCall: " + MemDB.LAST_CALL.toString());
		}
		logger.info("=========== loadLastCall end(size:{}) ===========", MemDB.LAST_CALL.selectAll().size());
	}
	
	/**
	 */
	void loadDrivingInfo() {
		logger.info("=========== loadDrivingInfo start ===========");
		List<DrvRawYymm> drvRaws = drvRawYymmMapper.selectLastAll();
		for (DrvRawYymm drvRaw : drvRaws) {
			DrivingInfoMem wsi = new DrivingInfoMem();
			wsi.setDataByEntity(drvRaw);
			MemDB.DRV.insertSafety(wsi.getIdV(), wsi);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("loadDrivingInfo: " + MemDB.DRV.toString());
		}
		logger.info("=========== loadDrivingInfo end(size:{}) ===========", MemDB.DRV.selectAll().size());
	}
	
	
	/**
	 */
	void loadTripInfo() {
		logger.info("=========== loadTripInfo start ===========");
		List<TTrpRaw> trpRaws = trpRawMapper.selectLastAll();
		for (TTrpRaw trpRaw : trpRaws) {
			TripInfoMem wti = new TripInfoMem();
			wti.setDataByEntity(trpRaw);
			MemDB.TRP.insertSafety(wti.getIdV(), wti);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("loadTripInfo: " + MemDB.TRP.toString());
		}
		logger.info("=========== loadTripInfo end(size:{}) ===========", MemDB.TRP.selectAll().size());
	}
	
	
	/**
	 */
	void loadSensorInfo() {
		logger.info("=========== loadSensorInfo start ===========");
		List<TSnsrRawYymm> snsrRaws = snsrRawYymmMapper.selectLastAll();
		for (TSnsrRawYymm snsrRaw : snsrRaws) {
			SensorInfoMem wsi = new SensorInfoMem();
			wsi.setDataByEntity(snsrRaw);
			MemDB.SNSR.insertSafety(wsi.getIdV(), wsi);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("loadSensorInfo: " + MemDB.SNSR.toString());
		}
		logger.info("=========== loadSensorInfo end(size:{}) ===========", MemDB.SNSR.selectAll().size());
	}
	
	
	/**
	 */
	void loadEvent() {
		logger.info("=========== loadEvent start ===========");
		List<TEvtHst> evtHsts = evtHstMapper.selectLastAll();
		for (TEvtHst evtHst : evtHsts) {
			EventMem we = new EventMem();
			we.setDataByEntity(evtHst);
			MemDB.EVT.insertSafety(we.getIdV(), we);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("loadEvent: " + MemDB.EVT.toString());
		}
		logger.info("=========== loadEvent end(size:{}) ===========", MemDB.EVT.selectAll().size());
	}
	
	
	/**
	 */
	void loadRouteInfo() {
		logger.info("=========== loadRouteInfo start ===========");
		List<RouteHst> routeHsts = routeHstMapper.selectLastAll();
		for (RouteHst routeHst : routeHsts) {
			RouteInfoMem wri = new RouteInfoMem();
			wri.setDataByEntity(routeHst);
			MemDB.RTI.insertSafety(wri.getIdV(), wri);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("loadRouteInfo: " + MemDB.RTI.toString());
		}
		logger.info("=========== loadRouteInfo end(size:{}) ===========", MemDB.RTI.selectAll().size());
	}
	
	/**
	 */
	void loadTestToken() {
		String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiMSIsImlzcyI6IklDTiIsImlkIjoiYWRtaW4iLCJleHAiOjE5MDAyOTYwNjd9.uODGrlbBkQR7dp36CDMGRNVNbotLnQ9gUpK2AYNf6n0";
		DecodedJWT jwt = JwtUtil.tokenToJwt(token);
		String username = jwt.getClaim("id").asString();
		UsrDetailsImpl userDetails = (UsrDetailsImpl) ajaxUserDetailsService.loadUserByUsername(username);
		
		JwtSession jwtSession = JwtSession.builder()
								.token(token)
								.type("1")
								.userDetails(userDetails)
								.accessToken(token)
								.build();
		
		MemDB.JWT.insertSafety(token, jwtSession);
	}


	
	

	// =====================================================================
	@PostConstruct
	public void init() {
		self = this;

		logger.info("MemDBInitializer.init()");
		Environment.instance().load();
		
		// MASTER 정보 load
		loadVVhcl();
		loadTPoi();
		loadTGrpPoi();
		loadTPoiGateMap();
		
		// call schedule 정보 load
		loadCallPlanP1();
		loadCallPlanP2();
		loadLastCall();
		
		// DATA 정보 load
		loadDrivingInfo();
		loadTripInfo();
		loadSensorInfo();
		loadEvent();
		loadRouteInfo();
		
	}

	// =====================================================================
	MemDBInitializer() {
	}

	/**
	 * Single-tone
	 */
	static MemDBInitializer self;

	public static MemDBInitializer instance() {
		return self;
	}
	// =====================================================================

}
