package com.autonomous.pm.vhcl.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.autonomous.pm.config.AppProperties;
import com.autonomous.pm.core.queue.GlobalQueue;
import com.autonomous.pm.domain.common.ReasonCode;
import com.autonomous.pm.domain.common.ResultTy;
import com.autonomous.pm.domain.structure.Call;
import com.autonomous.pm.domain.structure.CallChange;
import com.autonomous.pm.domain.structure.Event;
import com.autonomous.pm.domain.structure.Join;
import com.autonomous.pm.domain.structure.Poi;
import com.autonomous.pm.domain.structure.Stop;
import com.autonomous.pm.domain.structure.Vhcl;
import com.autonomous.pm.domain.structure.ack.Ack;
import com.autonomous.pm.domain.structure.ack.AckBody;
import com.autonomous.pm.domain.structure.ack.AckCallChange;
import com.autonomous.pm.domain.structure.ack.AckStop;
import com.autonomous.pm.domain.structure.mem.DrivingInfoMem;
import com.autonomous.pm.domain.structure.mem.EventMem;
import com.autonomous.pm.domain.structure.mem.RouteInfoMem;
import com.autonomous.pm.domain.structure.mem.SensorInfoMem;
import com.autonomous.pm.domain.structure.mem.TripInfoMem;
import com.autonomous.pm.domain.structure.push.DrivingInfoPush;
import com.autonomous.pm.domain.structure.report.DrivingInfoReport;
import com.autonomous.pm.domain.structure.report.EventReport;
import com.autonomous.pm.domain.structure.report.RouteInfoReport;
import com.autonomous.pm.domain.structure.report.SensorInfoReport;
import com.autonomous.pm.domain.structure.report.TripInfoReport;
import com.autonomous.pm.domain.structure.upload.ByeUpload;
import com.autonomous.pm.domain.structure.upload.JoinUpload;
import com.autonomous.pm.memcache.MemDB;
import com.autonomous.pm.model.Do.VVhcl;
import com.autonomous.pm.model.Dto.ResAdminPoi;
import com.autonomous.pm.model.Dto.VhclStts;
import com.autonomous.pm.service.CmdHstServiceImpl;
import com.autonomous.pm.service.TripInfoServiceImpl;
import com.autonomous.pm.service.VhclSttsServiceImpl;
import com.autonomous.pm.service.restful.PingPongServiceImpl;
import com.autonomous.pm.util.Guid;
import com.autonomous.pm.util.JwtUtil;
import com.autonomous.pm.web.websocket.WebSocketUiRepository;
import com.autonomous.pm.web.websocket.WebSocketUiServiceImpl;
import com.autonomous.pm.web.websocket.WebSocketUiTopic;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;


/**
 * 
 * @author daehi
 *
 */
@Slf4j
@Profile("!stomp")
@Component
public class WebSocketPmServiceImpl implements WebSocketPmService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private static String connectURIPath = "/ws/v1/connect/";
    
    @Autowired
    private WebSocketPmRepository repository;
    
    @Autowired
    private WebSocketUiServiceImpl uiService;
    
    @Autowired
    private CmdHstServiceImpl cmdHstService;
    
    @Autowired
    private VhclSttsServiceImpl vhclSttsService;
    
    @Autowired
    private TripInfoServiceImpl tripInfoService;
    
    @Autowired
    private PingPongServiceImpl pingPongService;
    
    private int QINDEX_DrivingInfo;
    private int QINDEX_TripInfo;
    private int QINDEX_SensorInfo;
    private int QINDEX_Event;
    private int QINDEX_RouteInfo;
    
    @Autowired
    public WebSocketPmServiceImpl() {
    	QINDEX_DrivingInfo = AppProperties.instance().getPropertyInt("QINDEX.DrivingInfo");
    	QINDEX_TripInfo = AppProperties.instance().getPropertyInt("QINDEX.TripInfo");
    	QINDEX_SensorInfo = AppProperties.instance().getPropertyInt("QINDEX.SensorInfo");
    	QINDEX_Event = AppProperties.instance().getPropertyInt("QINDEX.Event");
    	QINDEX_RouteInfo = AppProperties.instance().getPropertyInt("QINDEX.RouteInfo");
    }
    
    public void fail(WebSocketSession session, String type, String reason) throws IOException {
        Ack ack = new Ack();
        ack.setOcmd(type);
        ack.setResult(1);
        ack.setReason(reason);
        TextMessage ackMessage = new TextMessage(objectMapper.writeValueAsString(new AckBody(ack)));
        session.sendMessage(ackMessage);
    }
    
    public void disconnect(WebSocketSession session) throws IOException {
    	String vrn = getVrnBySession(session);
    	log.info("disconnect >> vrn={}", vrn);
		
		WebSocketPmTopic topic = repository.getTopic(vrn);
		
		if ( topic != null && topic.hasSession(session) ) {
			
			try {
				
				uiService.disconnect(vrn);
			
				VVhcl vhcl = MemDB.VHCL.selectAll().stream().filter(v->v.getVrn().equals(vrn)).findFirst().orElse(null);
				if (vhcl!= null) {
					if ( vhcl.getDrvStts() != 3		
						&& vhcl.getDrvStts() != 0	
							) {
						vhcl.setDrvStts(20);	// 네트워크끊김
						MemDB.VHCL.insertSafety(vhcl.getIdV(), vhcl);	// 상태값 다시 저장
						vhclSttsService.update(vhcl);
					}
				}
				
				tripInfoService.updateFinishOnBye(vrn);
				
				
		    	// --- Disconnect 발생시 EVENT 등록, kdh. 200726. ---
				Event evt = new Event();
				evt.setEvtcd(Event.EventCode.UNREACHABLE.toString());
				evt.setTs(new Date());
				EventReport eventReport = new EventReport();
				eventReport.setData(evt);
				event(session, eventReport);
				// --- END ---
			
			} catch (Exception e) {
				log.error(e.toString());
				
			} finally {
				
				topic.bye(session);		// 해당 topic에서 session을 해제한다.
				pingPongService.removeSession(session);	// 네트워크 상태 확인 Memory 에서 제거한다.
			}
		}
    }
    
    public void join(WebSocketSession session, JoinUpload reqJoin) throws IOException {
        String vrn = getVrnBySession(session);
		log.info("join >> vrn={}, reqJoin={}", vrn, reqJoin);
		
		Ack ack = new Ack();
		try {
			// JWT 검증 시작
			Join join = reqJoin.getData();
			String token = join.getToken();
			
			// JWT 이 유효할 경우
			if ( JwtUtil.verify(token) ) {
				
				ack = new Ack(reqJoin.getData().getMid(), reqJoin.getType());
				
	    		WebSocketPmTopic topic = repository.setTopic(vrn);
	    		
	    		topic.removeSessionAll();
	    		
	    		if ( !topic.hasSession(session) ) {
	    			topic.join(session);	
	    			pingPongService.addSession(session);	
	    		}
	    		
	    		if ( reqJoin.getData().isContn() ) {
	    			uiService.enter(vrn);
	    		} else {
	    			uiService.enter(vrn, true);
	    		}
	    		
	    		VVhcl vhcl = MemDB.VHCL.selectAll().stream().filter(v->v.getVrn().equals(vrn)).findFirst().orElse(null);
	    		vhcl.setDrvStts(0);	
	    		MemDB.VHCL.insertSafety(vhcl.getIdV(), vhcl);	
	    		
	    		vhclSttsService.update(vhcl);
	    		
	    		if ( join.isContn() ) {
	    			tripInfoService.updateStartOnContinueJoin(vrn);
	    		}
	    		
	    		
	    		try {
	    			Event evt = new Event();
	    			evt.setEvtcd(Event.EventCode.JOIN.toString());
	    			evt.setTs(new Date());
	    			EventReport eventReport = new EventReport();
	    			eventReport.setData(evt);
	    			log.info("session => {}", session.toString());
	    			event(session, eventReport);
				} catch (Exception e) {
					log.error(e.toString());
					log.error("Event insert Error:" + e.toString());
				}
	    		
	    		
	            
			} else {
	    		ack = new Ack(reqJoin.getData().getMid(), reqJoin.getType(), 1, ReasonCode.INVALID_TOKEN);
	    		ack.setResult(1);
	    		ack.setReason(ReasonCode.INVALID_TOKEN.toString());
			}
			
		} catch (Exception e) {
			log.error(e.toString());
			ack = new Ack(reqJoin.getData().getMid(), reqJoin.getType(), 1, ReasonCode.INTERNAL_SERVER_ERROR);
    		ack.setResult(1);
    		ack.setReason(ReasonCode.INTERNAL_SERVER_ERROR.toString());
    		
		} finally {
			// 직접 해당 세션에 전송한다.
			TextMessage ackMessage = new TextMessage(objectMapper.writeValueAsString(new AckBody(ack)));
			session.sendMessage(ackMessage);
		}
		
		
		
    }
    
    /**
     */
    @Override
    public void bye(WebSocketSession session, ByeUpload reqBye) throws IOException {
    	String vrn = getVrnBySession(session);
    	log.info("bye >> vrn={}, reqBye={}", vrn, reqBye);
		
		Ack ack = new Ack(reqBye.getData().getMid(), reqBye.getType());
		
		bye(session);
		
		// 직접 해당 세션에 전송한다.
		TextMessage ackMessage = new TextMessage(objectMapper.writeValueAsString(new AckBody(ack)));
		session.sendMessage(ackMessage);
    }
    
    /**
     * @param session
     * @throws IOException
     */
    public void bye(WebSocketSession session) throws IOException {
    	String vrn = getVrnBySession(session);
    	log.info("bye >> vrn={}", vrn);
		
		WebSocketPmTopic topic = repository.getTopic(vrn);
		
		if ( topic != null ) {
			try {
				
				uiService.leave(vrn);
			
				VVhcl vhcl = MemDB.VHCL.selectAll().stream().filter(v->v.getVrn().equals(vrn)).findFirst().orElse(null);
				if (vhcl!= null) {
					if ( vhcl.getDrvStts() != 3 ) {	// 현재 상태가 3:충전중 일때는 bye 메시지가 발생하여도 초기화 하지 않는다
						vhcl.setDrvStts(0);	// 초기화
						MemDB.VHCL.insertSafety(vhcl.getIdV(), vhcl);	// 상태값 다시 저장
						vhclSttsService.update(vhcl);
					}
				}
				
				// Trip종료, Call상태종료 상태값을 바꿔준다.
				tripInfoService.updateFinishOnBye(vrn);
				
				
		    	// --- BYE 발생시 EVENT 등록, kdh. 200726. ---
				Event evt = new Event();
				evt.setEvtcd(Event.EventCode.BYE.toString());
				evt.setTs(new Date());
				EventReport eventReport = new EventReport();
				eventReport.setData(evt);
				event(session, eventReport);
				// --- END ---
				
			} catch (Exception e) {
				log.error(e.toString());
				
			} finally {
				topic.bye(session);		// 해당 topic에서 session을 해제한다.
				pingPongService.removeSession(session);	// 네트워크 상태 확인 Memory 에서 제거한다.
			}
		}
		
    }
    
    public void stop(String terminal, String vrn, Stop.Prio prio, Stop.Ty ty) {
        List<String> vrns = getVrnsByTerminal(terminal);
        log.info("terminals={}, vrns={}", terminal.toString(), vrns.toString());
		
		Stop stop = new Stop(prio, ty);
		stop.setMid(Guid.genUUID(false));
		AckStop ackStop = new AckStop(ResultTy.STOP.toString(), stop);
		
		WebSocketPmTopic topic = repository.getTopic(vrn);
		
		try {
			if ( topic != null ) {
				topic.send(ackStop);
				
				VVhcl vhcl = MemDB.VHCL.selectAll().stream().filter(v->vrn.equals(v.getVrn())).findFirst().orElse(null);
				
				cmdHstService.insertCmdHst(vrn, "STOP", "G", "/api/v1/"+ terminal + "/resume?idV=" + vhcl.getIdV(), ackStop.toString(), stop.getMid(), null);
			}
		} catch (JsonProcessingException e) {
			log.error(e.toString());
		}
    }
    
    public void stop(String terminal, Long idV, Stop.Prio prio, Stop.Ty ty) {
    	log.info("stop, terminals={}, idV={}", terminal.toString(), idV);
    	VVhcl vhcl = MemDB.VHCL.select(idV);
    	String vrn = vhcl.getVrn();
    	
    	stop(terminal, vrn, prio, ty);
    }
    
    public void stopAll(String terminal, Stop.Prio prio, Stop.Ty ty) {
        List<String> vrns = getVrnsByTerminal(terminal);
		log.info("stopAll, terminals={}, vrns={}", terminal.toString(), vrns.toString());
		
		vrns.forEach(vrn->{
			stop(terminal, vrn, prio, ty);
		});
    }
    
    public void resume(String terminal, String vrn, Stop.Prio prio, Stop.Ty ty) {
		log.info("resume, terminals={}, vrn={}", terminal.toString(), vrn);
		
		// Request 내 mid와 type을 이용하여 응답할 Ack를 생성한다.
		Stop stop = new Stop(prio, ty);
		stop.setMid(Guid.genUUID(false));
		AckStop ackStop = new AckStop(ResultTy.STOP.toString(), stop);
		
		WebSocketPmTopic topic = repository.getTopic(vrn);
		
		try {
			if ( topic != null ) {
				topic.send(ackStop);
				
				VVhcl vhcl = MemDB.VHCL.selectAll().stream().filter(v->vrn.equals(v.getVrn())).findFirst().orElse(null);
				
				// T_CMD_HST에 저장
				cmdHstService.insertCmdHst(vrn, "RESUME", "G", "/api/v1/"+ terminal + "/resume?idV=" + vhcl.getIdV(), ackStop.toString(), stop.getMid(), null);
			}
		} catch (JsonProcessingException e) {
			log.error(e.toString());
		}
		
    }
    
    public void resume(String terminal, Long idV, Stop.Prio prio, Stop.Ty ty) {
    	log.info("resume, terminals={}, idV={}", terminal.toString(), idV);
    	VVhcl vhcl = MemDB.VHCL.select(idV);
    	String vrn = vhcl.getVrn();
    	
    	resume(terminal, vrn, prio, ty);
    }
    
    

    public void callChange(String vrn, CallChange callChange) {
		log.info("reqCallChange, vrn={}, callChange={}", vrn, callChange);
		
		AckCallChange ackBody = new AckCallChange(ResultTy.CALL_CHANGE.toString(), callChange);
		
		try {
			WebSocketPmTopic topic = repository.getTopic(vrn);
			if ( topic != null ) {
				topic.send(ackBody);
				
				// T_CMD_HST에 저장
				cmdHstService.insertCmdHst(vrn, "CALLCHANGE", "P", null, ackBody.toString(), callChange.getMid(), null);
			}
		} catch (JsonProcessingException e) {
			log.error(e.toString());
		}
    }
    
    public void callChange(Long idV, CallChange callChange) {
		log.info("reqCallChange, idV={}, callChange={}", idV, callChange);
		VVhcl vhcl = MemDB.VHCL.select(idV);
		String vrn = vhcl.getVrn();
		
		callChange(vrn, callChange);
	}
    
    
    public void drivingInfo(Long idV, DrivingInfoReport drivingInfoReport) {
        DrivingInfoMem drivingInfoMem = new DrivingInfoMem(idV, drivingInfoReport.getData());
		try {
			GlobalQueue.INSTANCE.getPmMeseageQ(QINDEX_DrivingInfo).put(drivingInfoMem);
			
			MemDB.DRV.insertSafety(idV, drivingInfoMem);
            uiService.sendMessage();
		} catch (InterruptedException e) {
			log.error(e.toString());
		}
		
    }

    public void tripInfo(Long idV, TripInfoReport tripInfoReport) {
        TripInfoMem tripInfoMem = new TripInfoMem(idV, tripInfoReport.getData());
		try {
			GlobalQueue.INSTANCE.getPmMeseageQ(QINDEX_TripInfo).put(tripInfoMem);
			
			// UI에 메세지를 전송한다.
			MemDB.TRP.insertSafety(idV, tripInfoMem);
			uiService.sendMessage();
		} catch (InterruptedException e) {
			log.error(e.toString());
		}
			
    }

    public void sensorInfo(Long idV, SensorInfoReport sensorInfoReport) {
//        log.info("sensorInfoReport={}", sensorInfoReport);
        SensorInfoMem sensorInfoMem = new SensorInfoMem(idV, sensorInfoReport.getData());
		try {
			GlobalQueue.INSTANCE.getPmMeseageQ(QINDEX_SensorInfo).put(sensorInfoMem);
			
			MemDB.SNSR.insertSafety(idV, sensorInfoMem);
			uiService.sendMessage();
		} catch (InterruptedException e) {
			log.error(e.toString());
		}
			
    }
    
    public void event(WebSocketSession session, EventReport eventReport) {
    	String vrn = getVrnBySession(session);
    	VVhcl vhcl = getVhcl(vrn);
    	Long idV = vhcl.getIdV();
    	event(idV, eventReport);
    }
    
    public void event(Long idV, EventReport eventReport) {
        EventMem eventMem = new EventMem(idV, eventReport.getData());
		try {
			GlobalQueue.INSTANCE.getPmMeseageQ(QINDEX_Event).put(eventMem);
			

			String evtCd = eventReport.getData().getEvtcd();
			if ( Event.EventCode.CHARGE_START.toString().equals( evtCd ) ) {
				VVhcl vhcl = MemDB.VHCL.select(idV);
				if (vhcl!= null) {
					vhcl.setDrvStts(3);	// 충전중
					MemDB.VHCL.insertSafety(vhcl.getIdV(), vhcl);	// 상태값 다시 저장
					vhclSttsService.update(vhcl);
				}
				
			} else if ( Event.EventCode.UNREACHABLE.toString().equals( evtCd ) ) {
				VVhcl vhcl = MemDB.VHCL.select(idV);
				if (vhcl!= null) {
					vhcl.setDrvStts(20);	// 네트워크연결끊김
					MemDB.VHCL.insertSafety(vhcl.getIdV(), vhcl);	// 상태값 다시 저장
					vhclSttsService.update(vhcl);
				}
				
			} else if ( Event.EventCode.BYE.toString().equals( evtCd ) ) {
				VVhcl vhcl = MemDB.VHCL.select(idV);
				if (vhcl!= null) {
					vhcl.setDrvStts(0);	// 운행이력없음
					MemDB.VHCL.insertSafety(vhcl.getIdV(), vhcl);	// 상태값 다시 저장
					vhclSttsService.update(vhcl);
				}
			}
			
		} catch (InterruptedException e) {
			log.error("event: {}",e.toString());
		}
		
    }
    
    public void routeInfo(Long idV, RouteInfoReport routeInfoReport) {
		
        RouteInfoMem routeInfoMem = new RouteInfoMem(idV, routeInfoReport.getData());
		try {
			GlobalQueue.INSTANCE.getPmMeseageQ(QINDEX_RouteInfo).put(routeInfoMem);
			
			MemDB.RTI.insertSafety(idV, routeInfoMem);
			uiService.sendMessage();
		} catch (InterruptedException e) {
			log.error(e.toString());
		}
    }
    
    
    /**
     */
    public String getVrnBySession(WebSocketSession session) {
    	String path = session.getUri().getPath();
        String vrn = path.replace(connectURIPath, "");
    	return vrn;
    }
    
    public VVhcl getVhcl(String vrn) {
    	return MemDB.VHCL.selectAll().stream().filter(v -> vrn.equals(v.getVrn()) ).findFirst().orElse(null);
    }
    
    private List<String> getVrnsByTerminal(String terminal){
    	List<String> vrns = new ArrayList<String>();
    	
    	MemDB.VHCL.selectAll().stream().forEach(v -> {
			if ( terminal.equals(v.getTerm()) ) {
				vrns.add(v.getVrn());
			}
    	});
    	
    	return vrns;
    }

}