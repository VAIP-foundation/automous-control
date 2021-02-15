package com.autonomous.pm.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.autonomous.pm.dao.IisAfsQueueMapper;
import com.autonomous.pm.dao.PmAssgnMapper;
import com.autonomous.pm.dao.TrpRawMapper;
import com.autonomous.pm.domain.structure.Call;
import com.autonomous.pm.domain.structure.FlightInfo;
import com.autonomous.pm.domain.structure.Gps;
import com.autonomous.pm.domain.structure.Poi;
import com.autonomous.pm.domain.structure.Stop;
import com.autonomous.pm.domain.structure.mem.CallMem;
import com.autonomous.pm.domain.structure.mem.DrivingInfoMem;
import com.autonomous.pm.memcache.MemDB;
import com.autonomous.pm.model.CallPlanP1;
import com.autonomous.pm.model.CallPlanP2;
import com.autonomous.pm.model.Do.TGrpPoi;
import com.autonomous.pm.model.Do.TPmAssgn;
import com.autonomous.pm.model.Do.TPoiGateMap;
import com.autonomous.pm.model.Do.TTrpRaw;
import com.autonomous.pm.model.Do.VPoiGate;
import com.autonomous.pm.model.Do.VVhcl;
import com.autonomous.pm.model.Dto.IisAfsQueue;
import com.autonomous.pm.model.Dto.PmAssgn;
import com.autonomous.pm.model.Dto.ResAdminPoi;
import com.autonomous.pm.service.restful.PoiServiceImpl;
import com.autonomous.pm.util.DateUtil;
import com.autonomous.pm.util.GpsUtil;
import com.autonomous.pm.util.Guid;
import com.autonomous.pm.util.MyUtil;
import com.autonomous.pm.util.SecurityUtils;
import com.autonomous.pm.util.StringUtil;
import com.autonomous.pm.vhcl.websocket.WebSocketPmServiceImpl;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CallServiceImpl implements CallService {

	@Autowired PoiServiceImpl poiService;
	@Autowired CallChangeServiceImpl callChangeService;
	@Autowired WebSocketPmServiceImpl wsPmService;
	@Autowired SensorInfoServiceImpl sensorInfoService;
	
	
	@Autowired IisAfsQueueMapper iisAfsQueueMapper;
	@Autowired PmAssgnMapper pmAssgnMapper;
	@Autowired TrpRawMapper trpRawMapper;
	
	@Value("${pm.callplan.block.start.Min:-15}")
	private Integer pmCallBlockStartMin;
	@Value("${pm.callplan.block.end.Min:0}")
	private Integer pmCallBlockEndMin;
	
	@Value("${pm.callplan.start.Min:-35}")
	private Integer pmCallStartMin;
	@Value("${pm.callplan.end.Min:60}")
	private Integer pmCallEndMin;
	
	
	@Value("${pm.call.enable.hh:00:00-25:00}")
	private String pmCallEnableStringArray;
	
	
	
	public static final Logger logger = LoggerFactory.getLogger(CallServiceImpl.class);
	
	
	

	/**
	 */
	@Override
	public List<CallPlanP1> createP1CallList() {
		List<CallPlanP1> callPlanList = new ArrayList<CallPlanP1>();
		List<PmAssgn> callList = pmAssgnMapper.selectInitRows();
		
		callList.stream()
			.forEach(c->{
				callPlanList.add(new CallPlanP1(c));
			});
		
		return callPlanList;
	}
	
	
	/**
	 */
	@Override
	public List<CallPlanP2> createP2CallList() {
		List<CallPlanP2> callPlanList = new ArrayList<CallPlanP2>();
		
		List<IisAfsQueue> queueList = iisAfsQueueMapper.selectInitRows(pmCallStartMin, pmCallEndMin);
		log.info("Init queueList.size()" + queueList.size());
		
		updateDirtyP2CallList(queueList);
		
		log.info("Init Non-filter queueList.size()" + queueList.size());
		queueList = filterQueueByGateNo(queueList);
		log.info("Init Filtered queueList.size()" + queueList.size());
		
		queueList.stream()
			.forEach(q->{
				callPlanList.add(CallPlanP2.builder().queue(q).build());
			});
		
		log.info("Init callPlanList.size()" + callPlanList.size());
		return callPlanList;
	}
	
	/**
	 */
	public List<PmAssgn> createLastCallList() {
		return pmAssgnMapper.selectLastAll();
	}
	
	
	/**
	 */
	@Override
	public void updateCallPlanP1() {
		String today = DateUtil.getTodayFormat();
		log.info("updateCallPlanP1 today beforeSize={}, {}", today, MemDB.CALL_PLAN_P1.selectAll().size());	
		List<CallPlanP1> list = new ArrayList<CallPlanP1>();
		MemDB.CALL_PLAN_P1.selectAll().stream().forEach(p1->{
			if ( p1.getCsd().equals(today) ) {
				list.add(p1);
			}
		});
		
		// CallPlanP1 재등록
		MemDB.CALL_PLAN_P1.clearAll();
		for( CallPlanP1 p1 : list ) {
			MemDB.CALL_PLAN_P1.insertSafety(p1.getIdCall(), p1);
		}
		log.info("updateCallPlanP1 today afterSize={}, {}", today, MemDB.CALL_PLAN_P1.selectAll().size());	
	}
	
	/**
	 */
	@Override
	public void updateCallPlanP2() {

		int beforeSize = MemDB.CALL_PLAN_P2.selectAll().size();	
		
		List<CallPlanP2> newCallPlanList = new ArrayList<CallPlanP2>();
		List<IisAfsQueue> newQueueList = iisAfsQueueMapper.selectNewRowsAll(pmCallStartMin, pmCallEndMin);	
		
		
		updateDirtyP2CallList(newQueueList);
		
		callChangeService.checkCallChange(newQueueList);
		
		newQueueList = filterQueueByGateNo(newQueueList);
		
		newQueueList.stream()
			.forEach(q->{
				newCallPlanList.add(CallPlanP2.builder().queue(q).build());
			});
		int newCallPlanSize = newCallPlanList.size();	
		
		
		Date now = new Date();
		Date minDate = new Date(now.getTime() + pmCallStartMin*60*1000);
		List<CallPlanP2> beforeFilteredPlanList =
				MemDB.CALL_PLAN_P2.selectAll()
					.stream()
					.filter(p2-> {
						if (p2.getLastDateAndTime() != null ) {
							return p2.getLastDateAndTime().getTime() > minDate.getTime();	
						} else {
							return false;
						}
					})
					.collect(Collectors.toList());
		
		// 기존 Memory를 지운다.
		MemDB.CALL_PLAN_P2.clearAll();
		
		int beforeFilteredPlanSize = beforeFilteredPlanList.size();	
		
		for (CallPlanP2 cp2 : beforeFilteredPlanList) {
			MemDB.CALL_PLAN_P2.insertSafety(cp2.getSchdDate()+"_"+cp2.getFltId(), cp2);	
		}
		for (CallPlanP2 cp2 : newCallPlanList) {
			MemDB.CALL_PLAN_P2.insertSafety(cp2.getSchdDate()+"_"+cp2.getFltId(), cp2);	
		}
		
		int updatedPlanSize = MemDB.CALL_PLAN_P2.selectAll().size();	
		
	}
	
	
	/**
	 * @param queueList
	 * @return
	 */
	public List<IisAfsQueue> filterQueueByGateNo(List<IisAfsQueue> queueList) {
		List<IisAfsQueue> filteredList = new ArrayList<IisAfsQueue>();
		filteredList.addAll(filterQueueByGateNo(queueList, "T1"));
		filteredList.addAll(filterQueueByGateNo(queueList, "T2"));
		return filteredList;
	}
	public List<IisAfsQueue> filterQueueByGateNo(List<IisAfsQueue> queueList, String term) {
		log.info("##QUEUE_FILTER[ORG]## " + queueList.size());
		if (queueList.size() == 0) return queueList;
		
		queueList = queueList.stream()
			.filter(q->!"D".equals(q.getMsgType()))
			.filter(q->{
				String ty = MyUtil.getTypeByTerminal(term); 
				if ( ty != null ) {
					return ty.equals(q.gettTy()); 
				} else {
					return true;
				}
			})
			.filter(q->{
				boolean isExist = MemDB.GATE.selectAll()
						.stream()
						.filter( g-> term!=null ? term.equals(g.getTerm()) : true )
						.anyMatch(g-> g.getGateNo().equals(q.getGateNo()) );
				return isExist;
			}).collect(Collectors.toList());
		
		log.info("##QUEUE_FILTER[ARV]## " + queueList.stream().filter(f->f.gettTy().equals("ARV")).collect(Collectors.toList()).size() );
		log.info("##QUEUE_FILTER[DPT]## " + queueList.stream().filter(f->f.gettTy().equals("DPT")).collect(Collectors.toList()).size() );
		return queueList;
	}
	
	
	/**
	 */
	@Override
	public void updateDirtyP2CallList(List<IisAfsQueue> queueList) {
		if ( queueList.size() > 0 ) {
			iisAfsQueueMapper.updateDirtyList(queueList);
		}
		log.info("update dirty queueList.size()" + queueList.size());
	}

	
	/**
	 */
	@Override
	public Call getCall(Long idV) {
		log.info("getCall, idV={}", idV);
		VVhcl vVhcl = MemDB.VHCL.select(idV);
		
		if ( vVhcl == null ) {
			log.error("Vehicle is not regist. idV={}", idV);
			return null;
		}
		
		String term = vVhcl.getTerm();
		if (StringUtil.isEmpty(term)) {
			log.error("Vehicle must have Terminal. idV={}", idV);
			return null;
		}
		
		Call call = null;
		
		log.info("==================Check Call > Service Time==================");
		if ( !isTimeInService(pmCallEnableStringArray) ) {
			call = createBatLowCall(idV, term);	// getCall && saveCall
		}
		
		if ( call == null ) {
			log.info("==================Check Call > Low Batttery==================");
			if ( sensorInfoService.isBatLow(idV) ) {
				call = createBatLowCall(idV, term);	// getCall && saveCall
			}
		}
		
		// 2. P1콜이 있는지 체크
		if (call == null) {
			log.info("==================Check Call > P1==================");
			call = getCallP1(term, idV);	// getCall && saveCall
		}
		
		if (call == null) {
			log.info("==================Check Call > P2-Block==================");
			call = getCallP2ByBlock(term, idV);	// getCall && saveCall
		}
		
		if (call == null) {
			log.info("==================Check Call > P2-LastKnown==================");
			call = getCallP2ByLastKnown(term, idV);	// getCall && saveCall
		}
		
		if ( call == null ) {
			log.info("==================Check Call > Last Call==================");
			log.info("CallPlan P1, P2 is Empty... idV={}, term={}", idV, term);
			
			call = MemDB.LAST_CALL.select(idV);
			
			saveCallByCall(term, idV, call);
			
			log.info("Repeat send to LastCall... call={}", call);
			
			if ( call == null ) {
				log.error("CALL IS NULL");
				call = new Call();
			}
		} else {
			log.info("==================CALL SAVED==================");
			log.info("CALL SAVED, idV={}, Call={}" + idV + call);
			
			if ( !"batlow".equals(call.getSub()) ) {
				MemDB.LAST_CALL.insertSafety(idV, call);
			}
		}
		log.info( "\n"
				+ "==================SEND CALL==================\n"
				+ "== MemDB.CALL_PLAN_P1.size:{}    ==\n"
				+ "== MemDB.CALL_PLAN_P2.size:{}    ==\n"
				+ "== MemDB.LAST_CALL.select:{}     ==\n"
				+ "=============================================\n"
				, MemDB.CALL_PLAN_P1.selectAll().size()
				, MemDB.CALL_PLAN_P2.selectAll().size()
				, MemDB.LAST_CALL.select(idV));
		
		return call;
				
	}
	
	/**
	 */
	@Override
	public Call getCallP1(String term, Long idV) {
		Call call = null;
		CallPlanP1 cp1 = null;
		List<CallPlanP1> p1List = MemDB.CALL_PLAN_P1.selectAll();
		log.info("getCallP1() term={}, idV:={}", term, idV);
		log.info("p1List.size()={}, plList:={}", p1List.size(), p1List.toString());
		
		if ( p1List.size() > 0 ) {
			cp1 = p1List.stream()
					.sorted(Comparator.comparing(CallPlanP1::getIdCall))
					.filter(c -> term.equals(c.getTerm()) )
					.filter(c -> idV == c.getIdV() )
					.filter(c -> "3".equals(c.getCallTy()) )
					.findFirst()
					.orElse(null);
			
			if ( cp1 == null ) {
				cp1 = p1List.stream()
						.sorted(Comparator.comparing(CallPlanP1::getIdCall))
						.filter(c -> term.equals(c.getTerm()) )
						.filter(c -> idV == c.getIdV() )
						.findFirst()
						.orElse(null);
			}
			
			if ( cp1 == null ) {
				cp1 = p1List.stream()
						.sorted(Comparator.comparing(CallPlanP1::getIdCall))
						.filter(c -> term.equals(c.getTerm()) )
						.filter(c -> idV != c.getIdV() )
						.findFirst()
						.orElse(null);
			}
			
		}
		
		if ( cp1 != null ) {
			call = cp1.toCall();
			if ( cp1.getIdV() != null ) {	
				call.setSub("wait");
			}
			cp1.setCallStts(1);	
			
			MemDB.CALL_PLAN_P1.delete(cp1.getIdCall());
			saveCallP1BySend(term, idV, cp1);
		}
		
		return call;
	}
	
	@Override
	public Call getCallP2ByBlock(String term, Long idV) {
		Call call = null;
		
		if (MyUtil.isT1(term)) {
			log.info("callBlock Search: {}~{}", pmCallBlockStartMin, pmCallBlockEndMin);
			List<IisAfsQueue> blockQueueList = iisAfsQueueMapper.selectBlockRows(pmCallBlockStartMin, pmCallBlockEndMin);	
			log.info("callBlock QueueList.size():{}", blockQueueList.size());
			log.info("callBlock QueueList:{}", blockQueueList);
			
			
			List<CallPlanP2> newCallPlanList = new ArrayList<CallPlanP2>();
			
			blockQueueList = filterQueueByGateNo(blockQueueList, "T1");
			blockQueueList.stream()
				.forEach(q->{
					newCallPlanList.add(CallPlanP2.builder().queue(q).build());
				});
			
			if ( newCallPlanList.size() > 0 ) {
				CallPlanP2 cp2 = newCallPlanList.stream()
						.sorted((p1, p2) -> {
							VPoiGate g1 = MemDB.GATE.selectAll().stream().filter(g->g.getGateNo().equals(p1.getGateNo())).findFirst().orElse(null);
							VPoiGate g2 = MemDB.GATE.selectAll().stream().filter(g->g.getGateNo().equals(p2.getGateNo())).findFirst().orElse(null);
							Integer compare1 = g1 != null ? g1.getOrd() : Integer.MIN_VALUE;
							Integer compare2 = g2 != null ? g2.getOrd() : Integer.MIN_VALUE; 
							return Integer.compare( compare2, compare1 );	// ord 내림차순 정렬
						})
						.findFirst().orElse(null);
				if ( cp2 != null ) {
					call = cp2.toCall();
					saveCallP2(term, idV, cp2);
				}
			}
			
		}
		log.info("callBlock Return->{}", call);
		
		return call;
	}
	
	@Override
	public Call getCallP2ByLastKnown(String term, Long idV) {
		Call call = null;
		log.info("getCallP2ByLastKnown Request ->{},{}", term, idV);
		
		if (MyUtil.isT1(term)) {
			log.info("callLastKnown Search: {}~{}", pmCallStartMin, pmCallEndMin);
			List<IisAfsQueue> lastKnownQueueList = iisAfsQueueMapper.selectLastKnownRows(pmCallStartMin, pmCallEndMin);	
			log.info("callLastKnown QueueList.size():{}", lastKnownQueueList.size());
			log.info("callLastKnown QueueList:{}", lastKnownQueueList);
			
			if ( lastKnownQueueList.size() > 0 ) {
				
				List<CallPlanP2> newCallPlanList = new ArrayList<CallPlanP2>();
				
				lastKnownQueueList = filterQueueByGateNo(lastKnownQueueList, "T1");
				lastKnownQueueList.stream()
					.forEach(q->{
						newCallPlanList.add(CallPlanP2.builder().queue(q).build());
					});
				
				CallPlanP2 cp2_lasted = newCallPlanList.stream()
						.findFirst().orElse(null);
				
				if ( cp2_lasted != null ) {
					CallPlanP2 cp2 = newCallPlanList.stream()
							.filter(c -> term.equals(c.getTerm()) )
							.filter(c -> cp2_lasted.getLastDateAndTime().getTime() == c.getLastDateAndTime().getTime())
							.sorted((p1, p2) -> {
								VPoiGate g1 = MemDB.GATE.selectAll().stream().filter(g->g.getGateNo().equals(p1.getGateNo())).findFirst().orElse(null);
								VPoiGate g2 = MemDB.GATE.selectAll().stream().filter(g->g.getGateNo().equals(p2.getGateNo())).findFirst().orElse(null);
								Integer compare1 = g1 != null ? g1.getOrd() : Integer.MIN_VALUE;
								Integer compare2 = g2 != null ? g2.getOrd() : Integer.MIN_VALUE; 
								return Integer.compare( compare2, compare1 );	// ord 내림차순 정렬
							})
							.findFirst().orElse(null);
					
					if ( cp2 != null ) {
						call = cp2.toCall();
						saveCallP2(term, idV, cp2);
					}
				}
			}
			
		} else if (MyUtil.isT2(term)) {
			CallPlanP2 cp2 = new CallPlanP2();
			cp2.setPri("p2");	
			cp2.setTerm(term);				
			ResAdminPoi frmPoi = GpsUtil.findClosestPoi(idV);
			if ( frmPoi != null ) {
				cp2.setFrmPoi(frmPoi.getIdPoi());	
			} else {
				cp2.setFrmPoi(null);
			}
			
			ResAdminPoi toPoi = MemDB.POI.selectAll().stream()
					.filter(p->p.getTerm().equals(term))
					.filter(p->p.getPoiTy() == 2)	// Info
					.findFirst().orElse(null);
			if ( toPoi != null ) {
				cp2.setToPoi(toPoi.getIdPoi());
			}
			cp2.setcDt(DateUtil.nowToDate());
			call = cp2.toCall();
			saveCallP2(term, idV, cp2);
		}
		
		log.info("getCallP2ByLastKnown Return ->{}", call);
		return call;
	}
	
	@Override
	public Call getCallP2(String term, Long idV) {
		Call call = null;
		
		if (MyUtil.isT1(term)) {
			List<CallPlanP2> p2List = MemDB.CALL_PLAN_P2.selectAll();
			log.info("p2List.size()={}, p2List:={}", p2List.size(), p2List.toString());
			if ( p2List.size() > 0 ) {
				CallPlanP2 cp2_lasted = p2List.stream()
						.filter(c -> term.equals(c.getTerm()) )
						.sorted(Comparator.comparing(CallPlanP2::getLastDateAndTime))
						.findFirst().orElse(null);
				
				if ( cp2_lasted != null ) {
					CallPlanP2 cp2 = p2List.stream()
							.filter(c -> term.equals(c.getTerm()) )
							.filter(c -> cp2_lasted.getLastDateAndTime().getTime() == c.getLastDateAndTime().getTime())
							.sorted((p1, p2) -> {
								VPoiGate g1 = MemDB.GATE.selectAll().stream().filter(g->g.getGateNo().equals(p1.getGateNo())).findFirst().orElse(null);
								VPoiGate g2 = MemDB.GATE.selectAll().stream().filter(g->g.getGateNo().equals(p2.getGateNo())).findFirst().orElse(null);
								Integer compare1 = g1 != null ? g1.getOrd() : Integer.MIN_VALUE;
								Integer compare2 = g2 != null ? g2.getOrd() : Integer.MIN_VALUE; 
								return Integer.compare( compare2, compare1 );	// ord 내림차순 정렬
							})
							.findFirst().orElse(null);
					
					if ( cp2 != null ) {
						call = cp2.toCall();
						saveCallP2(term, idV, cp2);
					}
				}
			}
					
			
		} else if (MyUtil.isT2(term)) {
			CallPlanP2 cp2 = new CallPlanP2();
			cp2.setPri("p2");	
			cp2.setTerm(term);				
			ResAdminPoi frmPoi = GpsUtil.findClosestPoi(idV);
			if ( frmPoi != null ) {
				cp2.setFrmPoi(frmPoi.getIdPoi());	
			} else {
				cp2.setFrmPoi(null);
			}
			
			ResAdminPoi toPoi = MemDB.POI.selectAll().stream()
					.filter(p->p.getTerm().equals(term))
					.filter(p->p.getPoiTy() == 2)	// Info
					.findFirst().orElse(null);
			if ( toPoi != null ) {
				cp2.setToPoi(toPoi.getIdPoi());
			}
			cp2.setcDt(DateUtil.nowToDate());
			call = cp2.toCall();
			saveCallP2(term, idV, cp2);
		}
		
		return call;
	}
	
	
	/**
	 * @param term(mandatory)
	 * @param idV
	 */
	public void stop(String term, Long idV) {
		
		if (idV == null) {
			MemDB.VHCL.selectAll().stream().forEach(v->{
				Long _idV = v.getIdV();
				saveCallP0ByStop(term, _idV);
				wsPmService.stop(term, _idV, Stop.Prio.p0, Stop.Ty.STOP);
			});
		} else {
			saveCallP0ByStop(term, idV);
			wsPmService.stop(term, idV, Stop.Prio.p0, Stop.Ty.STOP);
		}
	}
	
	/**
	 * @param term(mandatory)
	 * @param idV(mandatory)
	 */
	public void resume(String term, Long idV) {
		saveCallP0ByResume(term, idV);
		
		wsPmService.resume(term, idV, Stop.Prio.p0, Stop.Ty.RESUME);
	}
	
	
	/**
	 * @param term
	 * @param idV
	 */
	private void saveCallP0ByStop(String term, Long idV) {
		saveCallP0(term, idV, PmAssgn.CallTy.STOP.toString());
	}
	/**
	 * @param term
	 * @param idV
	 */
	private void saveCallP0ByResume(String term, Long idV) {
		saveCallP0(term, idV, PmAssgn.CallTy.RESUME.toString());
	}
	/**
	 * @param term
	 * @param idV
	 */
	private void saveCallP0(String term, Long idV, String callTy) {
		Long idGop = null;
		Long frmPoi = null;
		if (idV!=null) {
			idGop = MemDB.VHCL.select(idV).getIdGop();
		} else if ( !StringUtil.isEmpty(term) ) {
			TGrpPoi grpPoi = MemDB.GRP_POI.selectAll().stream().filter(gp->gp.getTerm().equals(term)).findFirst().orElse(null);
			if ( grpPoi != null ) {
				idGop = grpPoi.getIdGop();
			}
		}
		String today = DateUtil.getTodayFormat();
		Integer subSeq = pmAssgnMapper.selectSubseqByToday(today)+1;	// 하루단위 seq값
		String callId = today + term + subSeq;
		
		if(idV!=null) {
			ResAdminPoi closestPoi = GpsUtil.findClosestPoi(idV);
			frmPoi = closestPoi != null ? closestPoi.getIdPoi() : null;
		}
		
		Date now = DateUtil.nowToDate();
		String cusr = SecurityUtils.currentUserName();
		
		PmAssgn pmAssgn = PmAssgn.builder()
				.idCall(null)	
			    .idV(idV)		
			    .idGop(idGop)	
			    .callId(callId)	
			    .callTy(callTy)
			    .csd(today)		
			    .subSeq(subSeq)	
			    .pri("p0")		
			    .rpt(1)			
			    .rptAct(1)		
			    .flc(null)		
			    .fln(null)		
			    .frmPoi(frmPoi)	
			    .toPoi(null)	
			    .callStts(2)	
			    .callRslt(1)	
			    .idTrip(null)	
			    .callDt(now)	
			    .callMthd("P")	
			    .idQueue(null)	
			    .cusr(cusr)		
			    .cDt(null)		
			    .mDt(null)		
				.build();
		pmAssgnMapper.insertSelective(pmAssgn);
	}
	
	/**
	 * @param term
	 * @param idV
	 */
	private void saveCallP1BySend(String term, Long idV, CallPlanP1 cp1) {
		Long idCall = cp1.getIdCall();
		Long idGop = null;
		
		if (idV!=null) {
			idGop = MemDB.VHCL.select(idV).getIdGop();
		} else if ( !StringUtil.isEmpty(term) ) {
			TGrpPoi grpPoi = MemDB.GRP_POI.selectAll().stream().filter(gp->gp.getTerm().equals(term)).findFirst().orElse(null);
			if ( grpPoi != null ) {
				idGop = grpPoi.getIdGop();
			}
		}
		
		Date now = DateUtil.nowToDate();
		PmAssgn pmAssgn = PmAssgn.builder()
				.idCall(idCall)				
			    .idV(idV)					
			    .idGop(idGop)				
			    .callStts(1)				
			    .callDt(now)				
				.build();
		
		if ( idCall != null ) {
			pmAssgnMapper.updateByPrimaryKeySelective(pmAssgn);
		}
	}
	
	/**
	 * @param term
	 * @param idV
	 */
	private void saveCallP2(String term, Long idV, CallPlanP2 cp2) {
		Long idGop = null;
		Long frmPoi = null;
		if (idV!=null) {
			idGop = MemDB.VHCL.select(idV).getIdGop();
		} else if ( !StringUtil.isEmpty(term) ) {
			TGrpPoi grpPoi = MemDB.GRP_POI.selectAll().stream().filter(gp->gp.getTerm().equals(term)).findFirst().orElse(null);
			if ( grpPoi != null ) {
				idGop = grpPoi.getIdGop();
			}
		}
		String today = DateUtil.getTodayFormat();
		Integer subSeq = pmAssgnMapper.selectSubseqByToday(today)+1;	// 하루단위 seq값
		String callId = today + term + subSeq;
		
		if(idV!=null) {
			ResAdminPoi closestPoi = GpsUtil.findClosestPoi(idV);
			frmPoi = closestPoi != null ? closestPoi.getIdPoi() : null;
		}
		
		Date now = DateUtil.nowToDate();
		String cusr = SecurityUtils.currentUserName();
		
		PmAssgn pmAssgn = PmAssgn.builder()
				.idCall(null)	
			    .idV(idV)		
			    .idGop(idGop)	
			    .callId(callId)	
			    .csd(today)		
			    .subSeq(subSeq)	
			    .pri("p2")		
			    .rpt(1)			
			    .rptAct(0)		
			    .flc(cp2.getFltCarr())		
			    .fln(cp2.getFltNo())		
			    .frmPoi(frmPoi)			
			    .toPoi(cp2.getToPoi())	
			    .callStts(PmAssgn.CallStts.SEND.toInteger())	
			    .callRslt(null)	
			    .idTrip(null)	
			    .callDt(now)	
			    .callMthd("G")	
			    .idQueue(cp2.getIdQueue())	
			    .cusr(cusr)		
			    .cDt(null)		
			    .mDt(null)		
				.build();
		pmAssgnMapper.insertSelective(pmAssgn);
	}
	
	
	/**
	 * @param term
	 * @param idV
	 */
	private void saveCallByCall(String term, Long idV, Call call) {
		Long idGop = null;
		Long frmPoi = null;
		if (idV!=null) {
			idGop = MemDB.VHCL.select(idV).getIdGop();
		} else if ( !StringUtil.isEmpty(term) ) {
			TGrpPoi grpPoi = MemDB.GRP_POI.selectAll().stream().filter(gp->gp.getTerm().equals(term)).findFirst().orElse(null);
			if ( grpPoi != null ) {
				idGop = grpPoi.getIdGop();
			}
		}
		String today = DateUtil.getTodayFormat();
		Integer subSeq = pmAssgnMapper.selectSubseqByToday(today)+1;	// 하루단위 seq값
		String callId = today + term + subSeq;
		
		if(idV!=null) {
			ResAdminPoi closestPoi = GpsUtil.findClosestPoi(idV);
			frmPoi = closestPoi != null ? closestPoi.getIdPoi() : null;
		}
		
		ResAdminPoi toPoi = MemDB.POI.selectAll().stream().filter(p->p.getPoiCd().equals(call.getTo().getPoicd())).findFirst().orElse(null);
		Long toIdPoi = (toPoi != null) ? toPoi.getIdPoi() : null;
		
		Date now = DateUtil.nowToDate();
		String cusr = SecurityUtils.currentUserName();
		
		PmAssgn pmAssgn = PmAssgn.builder()
				.idCall(null)	
			    .idV(idV)		
			    .idGop(idGop)	
			    .callId(callId)	
			    .csd(today)		
			    .subSeq(subSeq)	
			    .pri(call.getPrio())	
			    .rpt(1)			
			    .rptAct(0)		
			    .flc(null)		
			    .fln(null)		
			    .frmPoi(frmPoi)	
			    .toPoi(toIdPoi)	
			    .callStts(PmAssgn.CallStts.SEND.toInteger())	
			    .callRslt(null)	
			    .idTrip(null)	
			    .callDt(now)	
			    .callMthd("G")	
			    .idQueue(null)	
			    .cusr(cusr)		
			    .cDt(null)		
			    .mDt(null)		
				.build();
		pmAssgnMapper.insertSelective(pmAssgn);
	}
	
	
	/**
	 */
	public void updatePmAssgnByTrip(TTrpRaw trip) {
		log.debug(">>>>>>>>>trip: "+trip);
		PmAssgn pm = null;
		Long toPoi = null;
		Long fromPoi = null;
		Long idV = trip.getIdV();
		Integer finRslt = trip.getFinRslt();	
		boolean isStart = (finRslt == null);	
		
		// 도착지PoiCd를 통해 idPoi 획득
		String destPoiCd = trip.getDestPoiCd();
		if ( !StringUtil.isEmpty(destPoiCd) ) {
			ResAdminPoi poi = MemDB.POI.selectAll().stream()
					.filter(p->p.getPoiCd().equals(destPoiCd))
					.findFirst().orElse(null);
			if ( poi!=null) {
				toPoi = poi.getIdPoi();
			}
		}
		
		String fromPoiCd = trip.getFromPoiCd();
		if ( !StringUtil.isEmpty(fromPoiCd) ) {
			ResAdminPoi poi = MemDB.POI.selectAll().stream()
					.filter(p->p.getPoiCd().equals(fromPoiCd))
					.findFirst().orElse(null);
			if ( poi!=null) {
				fromPoi = poi.getIdPoi();
			}
		}
		
		
		pm = isStart ?
				pmAssgnMapper.selectWaitCallByTrip(idV, toPoi)
				: pmAssgnMapper.selectOngoingCallByTrip(idV, toPoi);
		
		
		if ( pm == null ) {
			log.info("Server Recieved Tripinfo. but Call is not found. idV={}", trip.getIdV());
		} else {
			
			if ( isStart ) {
				pm.setCallStts(PmAssgn.CallStts.SEND.toInteger());
				pm.setCallRslt(PmAssgn.CallRslt.ONGOING.toInteger());
				pm.setFrmPoi(fromPoi);
				pm.setIdTrip(trip.getIdTrip());
				if ( !PmAssgn.CallTy.RESERVE.toString().equals(pm.getCallTy())) {
					pm.setRptAct(pm.getRptAct()+1);
				}
				
			} else {
				pm.setCallStts(PmAssgn.CallStts.COMPLETE.toInteger());
				if ( finRslt == 1) {
					pm.setCallRslt(PmAssgn.CallRslt.ERROR.toInteger());
				} else if ( finRslt == 2) {
					pm.setCallRslt(PmAssgn.CallRslt.COMPLETE.toInteger());
				}
				
			}
			
			pmAssgnMapper.updateByPrimaryKeySelective(pm);
		}
		
	}
	
	
	
	
	/**
	 * @param term(mandatory)
	 * @param idV(mandatory)
	 * @throws Exception 
	 */
	public void callVbase(String term, Long idV) throws Exception {
		Long toPoi = MemDB.VHCL.select(idV).getvBase();
		log.info("callVbase term={}, idV={}, toPoi={}", term, idV, toPoi);
		addCallP1ByVbase(term, toPoi, idV, 1);
	}
	
	
	/**
	 * @param term
	 * @param toPoi
	 * @param idV
	 * @param repeat
	 * @throws Exception
	 */
	public void addCallP1ByRepeat(String term, Long toPoi, Long idV, Integer repeat) throws Exception {
		for ( int repeatAct=1;repeatAct<=repeat;repeatAct++) {
			addCallP1(term, toPoi, idV, repeat, repeatAct, PmAssgn.CallTy.RESERVE.toString());
		}
	}
	/**
	 * @param term
	 * @param toPoi
	 * @param idV
	 * @param repeat
	 * @throws Exception
	 */
	private void addCallP1ByVbase(String term, Long toPoi, Long idV, Integer repeat) throws Exception {
		addCallP1(term, toPoi, idV, repeat, null, PmAssgn.CallTy.VBASE.toString());
	}
	/**
	 * @param term(mandaotry)
	 * @param idPoi(mandaotry)
	 * @param idV
	 * @param repeat
	 * @throws Exception 
	 */
	public void addCallP1(String term, Long toPoi, Long idV, Integer repeat) throws Exception {
		addCallP1(term, toPoi, idV, repeat, null, null);
	}
	@Override
	public void addCallP1(String term, Long toPoi, Long idV, Integer repeat, Integer repeatAct, String callTy) throws Exception {
		log.info("addCallP1(term={}, toPoi={}, idV={}, repeat={}, repeatAct={}, callTy={})", term, toPoi, idV, repeat, repeatAct, callTy);
		if ( StringUtils.isEmpty(term) || toPoi == null) {
			log.error("addCallP1 must have parameter = [term, toPoi]");
			throw new Exception();
		}
		Long frmPoi = null;
		String today = DateUtil.getTodayFormat();
		repeat = repeat != null ? repeat : 1;			
		repeatAct = repeatAct != null ? repeatAct : 0;	
		
		Long idGop = MemDB.POI.select(toPoi).getIdGop();
		Integer subSeq = pmAssgnMapper.selectSubseqByToday(today)+1;	
		String callId = today + term + subSeq;
		
		Date now = DateUtil.nowToDate();
		String cusr = SecurityUtils.currentUserName();
		
		PmAssgn pmAssgn = PmAssgn.builder()
							.idCall(null)	
						    .idV(idV)		
						    .idGop(idGop)	
						    .callId(callId)	
						    .callTy(callTy)	
						    .csd(today)		
						    .subSeq(subSeq)	
						    .pri("p1")		
						    .rpt(repeat)	
						    .rptAct(repeatAct)		
						    .frmPoi(null)	
						    .toPoi(toPoi)	
						    .callStts(0)	
						    .callRslt(null)	
						    .callDt(now)	
						    .callMthd("G")	
						    .cusr(cusr)		
						    .build();
		
		CallPlanP1 callPlan = new CallPlanP1(pmAssgn);
		callPlan.setTerm(term);
		if ( repeat != null ) {
			callPlan.setRpt(repeat);
		}
		
		pmAssgnMapper.insert(pmAssgn);
		callPlan.setIdCall(pmAssgn.getIdCall());
		MemDB.CALL_PLAN_P1.insertSafety(pmAssgn.getIdCall(), callPlan);
		
	}
	
	public void removeCallP1ByCancel(String term, Long idCall) throws Exception {
		log.info("removeCallP1ByCancel(term={}, idCall={})", term, idCall);

		PmAssgn pmAssgn = PmAssgn.builder()
				.idCall(idCall)
				.callStts(2)	
				.callRslt(3)	
				.build();
		pmAssgnMapper.updateByPrimaryKeySelective(pmAssgn);
		MemDB.CALL_PLAN_P1.delete(idCall);
	}
	
	private Call createBatLowCall(Long idV, String term) {
		Call call = new Call();
		
		String uuid = Guid.genUUID(false);
		String sub = null;
		Poi toPoi = new Poi();
		
		
		call.setMid(null);
		call.setCallid(uuid);
		call.setPrio("p1");
		
		sub = "batlow";	
				
		if (term != null) {
			ResAdminPoi raPoi = MemDB.POI.selectAll().stream()
					.filter(p->term.equals(p.getTerm()))
					.filter(p->4==p.getPoiTy())
					.findAny().orElse(null);
			if ( raPoi != null ) {
				toPoi = raPoi.toPoi();
			} else {
				log.error("This terminal have not charging station, term={}", term);
			}
		}
		
		
		call.setSub(sub);
		call.setTo(toPoi);
		call.setFi(null);	
		
		saveCallByCall(term, idV, call);
		
		return call;
		
	}
	


	
	/**
	 * @param trip
	 */
	public void rollbackAtStart(TTrpRaw trip) {
		pmAssgnMapper.rollbackAtStart(trip);
	}
	
	private boolean isTimeInService(Integer startHH, Integer endHH) {
		Date dt = DateUtil.nowToDate();
		String hh = DateUtil.getUtcDatetimeByFormat(dt, "hh");
		Integer currentHH = Integer.parseInt(hh);
		log.debug("isTimeInService "+startHH + "<=" + currentHH + "<" + endHH);
		log.debug("isTimeInService "+( startHH <= currentHH && currentHH < endHH ));
		return ( startHH <= currentHH && currentHH < endHH );
	}
	
	/**
	 * @param pmCallEnableStringArray
	 * @return
	 */
	private static boolean isTimeInService(String pmCallEnableStringArray) {
		boolean isResult = false;
		Date dt = DateUtil.nowToDate();
		Integer currentHH = Integer.parseInt( DateUtil.getUtcDatetimeByFormat(dt, "hh") );
		Integer currentMi = Integer.parseInt( DateUtil.getUtcDatetimeByFormat(dt, "mi") );
		Integer currentMins = currentHH * 60 + currentMi;
		
		String[] arr = pmCallEnableStringArray.split(",");
		for ( String time : arr ) {
			
			Integer startHH = Integer.parseInt( time.split("-")[0].split(":")[0] );
			Integer startMi =  Integer.parseInt( time.split("-")[0].split(":")[1] );
			Integer startMins = startHH * 60 + startMi;
			
			Integer endHH = Integer.parseInt( time.split("-")[1].split(":")[0] );
			Integer endMi =  Integer.parseInt( time.split("-")[1].split(":")[1] );
			Integer endMins = endHH * 60 + endMi;
			
			if ( startMins <= currentMins && currentMins < endMins ) {
				isResult = true;
			};
			
		}
		
		try {
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return isResult;
	}
	
	
}
