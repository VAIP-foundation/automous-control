package com.autonomous.pm.service.restful;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.autonomous.pm.dao.IisAfsQueueMapper;
import com.autonomous.pm.dao.MonitorMapper;
import com.autonomous.pm.dao.PmAssgnMapper;
import com.autonomous.pm.dao.TrpRawMapper;
import com.autonomous.pm.memcache.MemDB;
import com.autonomous.pm.model.CallPlanP1;
import com.autonomous.pm.model.Do.TGrpPoi;
import com.autonomous.pm.model.Do.TTrpRaw;
import com.autonomous.pm.model.Do.VPoiGate;
import com.autonomous.pm.model.Dto.IisAfsQueue;
import com.autonomous.pm.model.Dto.PmAssgn;
import com.autonomous.pm.model.Dto.ResEvent;
import com.autonomous.pm.model.Dto.ResPoi;
import com.autonomous.pm.model.Dto.ResTrip;
import com.autonomous.pm.model.Dto.ResVhcl;
import com.autonomous.pm.service.CallServiceImpl;
import com.autonomous.pm.util.DateUtil;
import com.autonomous.pm.util.MyUtil;
import com.autonomous.pm.vhcl.websocket.WebSocketPmServiceImpl;
import com.autonomous.pm.web.websocket.WebSocketUiRepository;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MonitorServiceImpl implements MonitorService {

	public static final Logger logger = LoggerFactory.getLogger(MonitorServiceImpl.class);

	@Autowired
	TerminalService terminalService;
	
	@Autowired
	CallServiceImpl callService;
	
	@Autowired
	WebSocketPmServiceImpl wsPmService;

	@Autowired
	WebSocketUiRepository wsUiRepository;

	@Autowired
	MonitorMapper monitorMapper;
	
	@Autowired
	TrpRawMapper trpRawMapper;
	
	@Autowired
	IisAfsQueueMapper iisAfsQueueMapper;
	
	@Autowired
	PmAssgnMapper pmAssgnMapper;
	
//	@Value("${ui.trp.start.Min:0}")	// UI에 보여줄 트립 목록 조회 시간
//	Integer uiTrpStartMin;
//	@Value("${ui.trp.end.Min:180}")	// UI에 보여줄 트립 목록 조회 시간
//	Integer uiTrpEndMin;
	
//	@Value("${ui.event.start.Min:0}")	// UI에 보여줄 이벤트 목록 조회 시간
//	Integer uiEventStartMin;
//	@Value("${ui.event.end.Min:180}")	// UI에 보여줄 이벤트 목록 조회 시간
//	Integer uiEventEndMin;
	

	@Override
	public List<String> getAllTerminal() {
		return terminalService.getAllTerminal();
	}


	// 정지
	@Override
	public int stopVehicle(String term, Long idV) {
		logger.debug("stopVehicle term={}, idV={}", term, idV);
		if (term == null) {
			logger.info("term is null");
			return 0;
		}
		
		callService.stop(term, idV);
		return 1;
	}

	// 재운행
	@Override
	public int resumeVehicle(String term, Long idV) {
		logger.debug("resumeVehicle term={}, idV={}" + term, idV);
		if (term == null) {
			logger.info("term is null");
			return 0;
		}
		if (idV == null) {
			logger.info("vrns is null or vrns size is zero");
			return 0;
		}
		
		callService.resume(term, idV);
		return 1;
	}

	// 회차
	@Override
	public int callVbase(String term, Long idV) {
		logger.debug("callP1PoiVehicle term={}, idV={}" + term + idV);
		if (term == null) {
			logger.info("term is null");
			return 0;
		}
		if (idV == null) {
			logger.info("vrns is null or vrns size is zero");
			return 0;
		}
		
		CallPlanP1 existCallP1 = MemDB.CALL_PLAN_P1.selectAll().stream()
			.filter(p1-> idV == p1.getIdV() )
			.filter(p1-> term.equals( p1.getTerm()) )
			.filter(p1-> "p1".equals( p1.getPri()) )
			.filter(p1 -> PmAssgn.CallTy.VBASE.toString().equals( p1.getCallTy()) )
			.findFirst().orElse(null);
		if ( existCallP1 != null ) {
			logger.info("already exist Vbase p1 Call");
			return -1;
		}
		
	
		try {
			callService.callVbase(term, idV);
		} catch (Exception e) {
			log.error(e.toString());
		}
		return 1;
	}

	// 긴급배정
	@Override
	public int callP1Vehicle(String term, Long idPoi, Integer repeat) {
		logger.debug("callP1Vehicle " + term);
		int result = 0;
		if (term == null) {
			logger.info("vrns is null or vrns size is zero");
			return result;
		}
		
		try {
			callService.addCallP1ByRepeat(term, idPoi, null, repeat);
			result = 1;
		} catch (Exception e) {
			log.error(e.toString());
		}
		return result;
	}
	
	// 긴급배정 취소
	@Override
	public int callP1Cancel(String term, Long idCall) {
		logger.debug("callP1Cancel {}, {}", term, idCall);
		int result = 0;
		if (term == null) {
			logger.info("vrns is null or vrns size is zero");
			return result;
		}
		
		try {
			callService.removeCallP1ByCancel(term, idCall);
			result = 1;
		} catch (Exception e) {
			log.error(e.toString());
		}
		return result;
	}

	@Override
	public List<ResVhcl> getVehicleLs(String term) {
		logger.debug("getVehicleLs " + term);
		if (term == null) {
			logger.info("vrns is null or vrns size is zero");
			return new ArrayList<ResVhcl>();
		}

		List<ResVhcl> reulsts = monitorMapper.getVehicleLs(term);
		
		/*
		
		 */
		reulsts.stream()
			.forEach(v->{
				if ( v.getDrvStts() == 0 || v.getDrvStts() == 1 || v.getDrvStts() == 3 || v.getDrvStts() == 20) {
					v.setDestPoi(null);
					v.setDestPoiNm(null);
					v.setFromPoi(null);
					v.setFromPoiNm(null);
				}
			})
			;
		
		return reulsts;
	}

	@Override
	public List<ResEvent> getEventLs(String term) {
		logger.debug("getEventLs " + term);
		if (term == null) {
			logger.info("vrns is null or vrns size is zero");
			return new ArrayList<ResEvent>();
		}
		return monitorMapper.getEventLs(term);
	}

	/**
	 */
	@Override
	public List<ResTrip> getTripLs(String term) {
		String tTy = MyUtil.getTypeByTerminal(term);	// "T1"->"ARV", "T2"->"DPT"
		Long idGop = MyUtil.getIdGopByTerminal(term);	// "T1"->1, "T2"->2
		
		
		List<ResTrip> step1List = new ArrayList<ResTrip>();
		List<ResTrip> step2List = new ArrayList<ResTrip>();
		List<ResTrip> step3List = new ArrayList<ResTrip>();
		List<ResTrip> step4List = new ArrayList<ResTrip>();
		
		List<IisAfsQueue> iisList = iisAfsQueueMapper.selectFlightSchTodayLs(tTy);
		iisList = callService.filterQueueByGateNo(iisList, term);	// 관리하는 gateNo만 이용하도록 filter
		
		List<ResTrip> tripList = trpRawMapper.selectTrpLs(term);
		List<PmAssgn> p1CallList = pmAssgnMapper.selectStandByP1Call(idGop);
		
		for ( int i=0;i<iisList.size();i++) {
			IisAfsQueue iis = iisList.get(i);
			ResTrip result = iis.toResTrip();
			
			VPoiGate poiGate = MemDB.GATE.selectAll().stream().filter(g->g.getGateNo().equals(iis.getGateNo())).findFirst().orElse(null);
			if ( poiGate != null ) {
				result.setGateNo(poiGate.getPoiNm());
			}
			
			step1List.add(result);
		}
		
		for ( int i=0;i<step1List.size();i++) {
			ResTrip iisTrip = step1List.get(i);
			step2List.add(iisTrip);
			
			for ( int j=0;j<tripList.size();j++) {
				
				ResTrip trp = tripList.get(j);
				if ( iisTrip.getFlghtnum().equals(trp.getFlghtnum()) ) {
					if ( iisTrip.getIdV() == null) { // 아직 병합되지 않았으면
						iisTrip.setIdV(trp.getIdV());
						iisTrip.setVrn(trp.getVrn());
						iisTrip.setGateNo(trp.getGateNo());
						iisTrip.setDts(trp.getDts());
						iisTrip.setTripStts(getTripStts(trp));
						iisTrip.setRepeatActiveCnt(trp.getRepeatActiveCnt());
						iisTrip.setRepeatReserveCnt(trp.getRepeatReserveCnt());
						tripList.remove(j);	// 지워준다.
						j--;	
						
					} else {	

						ResTrip iisTripNew = new ResTrip();
						BeanUtils.copyProperties(iisTrip, iisTripNew, "idV", "vrn", "gateNo", "dts", "tripStts", "repeatActiveCnt", "repeatReserveCnt");
						
						iisTripNew.setIdV(trp.getIdV());
						iisTripNew.setVrn(trp.getVrn());
						iisTripNew.setGateNo(trp.getGateNo());
						iisTripNew.setDts(trp.getDts());
						iisTripNew.setTripStts(getTripStts(trp));
						iisTripNew.setRepeatActiveCnt(trp.getRepeatActiveCnt());
						iisTripNew.setRepeatReserveCnt(trp.getRepeatReserveCnt());
						tripList.remove(j);	// 지워준다.
						j--;	// 리스트사이즈가 줄어든만큼 인덱스도 줄여준다.
						step2List.add(iisTripNew);
					}
					 
				}
			}
		}
		
		
		for ( int i=0;i<step2List.size();i++) {
			ResTrip result = step2List.get(i);
			step3List.add(result);
			
			ResTrip nextResult = null;
			if ( i<step2List.size()-1 ) {
				nextResult = step2List.get(i+1);
			}
			
			for ( int j=0;j<tripList.size();j++) {
				ResTrip trp = tripList.get(j);
				Long trpStartTime = trp.getDts().getTime();
				
				
				if ( trp.getActlDts() != null) {
					trpStartTime = trp.getActlDts().getTime();
				}
				
				if ( nextResult != null
						&& result.getActlDts().getTime() < trpStartTime
						&& nextResult.getActlDts().getTime() >= trpStartTime ) {
					
					trp.setTripStts(getTripStts(trp));
					step3List.add(trp);
					tripList.remove(j);	// 지워준다.
					j--;	
				}
			}
		}
		
		for ( int j=0;j<tripList.size();j++) {
			step3List.add(tripList.get(j));
		}
		
		
		for (int i=0; i<step3List.size();i++) {
			ResTrip result = step3List.get(i);
			step4List.add(result);
			if ( i<step3List.size()-1 ) {
				ResTrip nextResult = step3List.get(i+1);
				Long beforeTime = result.getActlDts() != null ? result.getActlDts().getTime() : result.getDts().getTime();
				Long nextTime = nextResult.getActlDts() != null ? nextResult.getActlDts().getTime() : nextResult.getDts().getTime();
				
				
				for ( int j=0;j<p1CallList.size();j++) {
					PmAssgn p1 = p1CallList.get(j);
					Date now = new Date();
					Long p1Time = now.getTime();	
					
					if ( beforeTime < p1Time && p1Time <= nextTime ){
						ResTrip rt = p1.toResTrip();
				    	rt.setTripStts(4);		
						step4List.add(rt);
						p1CallList.remove(j);	
						j--;	// 리스트사이즈가 줄어든만큼 인덱스도 줄여준다.
					}
				}
			}
			
		}
		
		// 5. 마무리 된 결과리스트 반환
		return step4List;
	}
	
	
	
	private Integer getTripStts(ResTrip trp) {
		Integer tripStts = null;
		
		String pri = trp.getPri();			
		
		Integer finRslt = trp.getFinRslt();	
		Integer rsn = trp.getRsn();			
		
		pri = pri == null ? "" : pri;
		rsn = rsn == null ? -1 : rsn;
		
		if ( finRslt == null ) {
			tripStts = 3;
			if ( "p1".equals(pri) ) {
				tripStts = 4;
			}
		} else if ( finRslt == 1 ) {
			tripStts = 1;
		} else if ( finRslt == 2 ) {
			tripStts = 2;
		}
		return tripStts;
	}
	
	

	@Override
	public List<ResPoi> getPoiLs(String term) {
		logger.debug("getEventLs " + term);
		if (term == null) {
			logger.info("vrns is null or vrns size is zero");
			return new ArrayList<ResPoi>();
		}

		return monitorMapper.getPoiLs(term);
	}

}