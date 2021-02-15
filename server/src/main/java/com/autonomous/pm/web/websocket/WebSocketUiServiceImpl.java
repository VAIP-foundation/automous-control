package com.autonomous.pm.web.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.autonomous.pm.config.AppProperties;
import com.autonomous.pm.dao.TrpRawMapper;
import com.autonomous.pm.domain.common.ReasonCode;
import com.autonomous.pm.domain.structure.Event;
import com.autonomous.pm.domain.structure.Join;
import com.autonomous.pm.domain.structure.Notification;
import com.autonomous.pm.domain.structure.Notification.NotificationCode;
import com.autonomous.pm.domain.structure.Poi;
import com.autonomous.pm.domain.structure.TripInfo;
import com.autonomous.pm.domain.structure.ack.Ack;
import com.autonomous.pm.domain.structure.ack.AckBody;
import com.autonomous.pm.domain.structure.ack.AckNotification;
import com.autonomous.pm.domain.structure.mem.DrivingInfoMem;
import com.autonomous.pm.domain.structure.mem.EventMem;
import com.autonomous.pm.domain.structure.mem.RouteInfoMem;
import com.autonomous.pm.domain.structure.mem.SensorInfoMem;
import com.autonomous.pm.domain.structure.mem.TripInfoMem;
import com.autonomous.pm.domain.structure.push.DrivingInfoPush;
import com.autonomous.pm.domain.structure.push.EventPush;
import com.autonomous.pm.domain.structure.push.PushObject;
import com.autonomous.pm.domain.structure.push.RouteInfoPush;
import com.autonomous.pm.domain.structure.push.SensorInfoPush;
import com.autonomous.pm.domain.structure.push.TripInfoPush;
import com.autonomous.pm.domain.structure.upload.JoinUpload;
import com.autonomous.pm.memcache.MemDB;
import com.autonomous.pm.model.Do.TTrpRaw;
import com.autonomous.pm.model.Do.VVhcl;
import com.autonomous.pm.model.Dto.ResAdminPoi;
import com.autonomous.pm.service.restful.TerminalServiceImpl;
import com.autonomous.pm.service.restful.VhclServiceImpl;
import com.autonomous.pm.util.JwtUtil;
import com.autonomous.pm.vhcl.websocket.WebSocketPmRepository;
import com.autonomous.pm.vhcl.websocket.WebSocketPmTopic;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;


/**
 * WebSocketPmHandler에서 메세지를 받아 타입별로 큐에 저장한다.
 * 
 * @author daehi
 *
 */
@Slf4j
@Profile("!stomp")
@Component
public class WebSocketUiServiceImpl implements WebSocketUiService {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private static String connectURIPath = "/ws/v1/cntrlui/connect/";
    
    @Value("${vhcl.driving.able.sec:1200}")
	private Integer vhclDrivingAbleSec;
    
    @Value("${speed.kilometer.per.hour:5}")
    private Float defaultSpeed;
    
    @Autowired
    private WebSocketUiRepository repository;

    @Autowired
    private WebSocketUiRepository uiRepository;
    
    @Autowired
    private TrpRawMapper trpRawMapper;
    
    private int QINDEX_DrivingInfo;
    private int QINDEX_TripInfo;
    private int QINDEX_SensorInfo;
    private int QINDEX_Event;
    private int QINDEX_RouteInfo;
    
    @Autowired
    public WebSocketUiServiceImpl() {
    	QINDEX_DrivingInfo = AppProperties.instance().getPropertyInt("QINDEX.DrivingInfo");
    	QINDEX_TripInfo = AppProperties.instance().getPropertyInt("QINDEX.TripInfo");
    	QINDEX_SensorInfo = AppProperties.instance().getPropertyInt("QINDEX.SensorInfo");
    	QINDEX_Event = AppProperties.instance().getPropertyInt("QINDEX.Event");
    	QINDEX_RouteInfo = AppProperties.instance().getPropertyInt("QINDEX.RouteInfo");
    }
    
    
    public void join(WebSocketSession session, JoinUpload reqJoin) throws IOException {
        String term = getTermBySession(session);
		log.info("join >> term={}, reqJoin={}", term, reqJoin);
		
		Ack ack = new Ack(reqJoin.getData().getMid(), reqJoin.getType());
		
		// JWT 검증 시작
		Join joinData = reqJoin.getData();
		String token = joinData.getToken();
		
		// JWT 이 유효할 경우
		if ( JwtUtil.verify(token) ) {
			
			// Request 내 mid와 type을 이용하여 응답할 Ack를 생성한다.
			ack = new Ack(reqJoin.getData().getMid(), reqJoin.getType());
			
    		// Repository에 Topic을 Terminal로 설정(이미 존재시 생성하지 않는다)
    		WebSocketUiTopic topic = repository.setTopic(term);
    		
            topic.join(session);
            
        // JWT 가 유효하지 않을 경우
		} else {
    		ack = new Ack(reqJoin.getData().getMid(), reqJoin.getType(), 1, ReasonCode.INVALID_TOKEN);
			
		}
		// topic이 없으므로 직접 세션으로 전송한다.
		AckBody ackBody = new AckBody(ack);
		TextMessage ackMessage = new TextMessage(objectMapper.writeValueAsString(ackBody));
		session.sendMessage(ackMessage);
		
		// 해당 터미널에 메세지 전송
		sendMessage(term, true); // 해당터미널에, dirty 체크없이 가장 최근값 보내줌

    }
    
    public void enter(String vrn) throws JsonProcessingException {
    	enter(vrn, false);
    }
    public void enter(String vrn, boolean isContn) throws JsonProcessingException {
    	VVhcl vhcl = MemDB.VHCL.selectAll().stream().filter(v->vrn.equals(v.getVrn())).findFirst().orElse(null);
        if ( vhcl != null ) {
        	Notification noti = new Notification();
        	noti.setIdV(vhcl.getIdV());
        	noti.setTs(new Date());
        	noti.setVrn(vrn);
        	if ( isContn ) {
        		noti.setCd(NotificationCode.ENTER_CONTN.toString());
        	} else {
        		noti.setCd(NotificationCode.ENTER.toString());
        	}
        	WebSocketUiTopic uiTopic = uiRepository.getTopic(vhcl.getTerm());
        	if ( uiTopic!=null) {
        		uiTopic.pub(new AckNotification(noti));
        	}
        }
    }
    public void leave(String vrn) throws JsonProcessingException {
    	// UI의 topic에 pub 한다.
        VVhcl vhcl = MemDB.VHCL.selectAll().stream().filter(v->vrn.equals(v.getVrn())).findFirst().orElse(null);
        if ( vhcl != null ) {
        	WebSocketUiTopic uiTopic = uiRepository.getTopic(vhcl.getTerm());
        	if ( uiTopic!=null) {
        		Notification noti = new Notification();
        		noti.setIdV(vhcl.getIdV());
        		noti.setTs(new Date());
        		noti.setVrn(vrn);
        		noti.setCd(NotificationCode.LEAVE.toString());
        		uiTopic.pub(new AckNotification(noti));
        	}
        }
    }
    
    public void disconnect(String vrn) throws JsonProcessingException {
    	// UI의 topic에 pub 한다.
        VVhcl vhcl = MemDB.VHCL.selectAll().stream().filter(v->vrn.equals(v.getVrn())).findFirst().orElse(null);
        if ( vhcl != null ) {
        	WebSocketUiTopic uiTopic = uiRepository.getTopic(vhcl.getTerm());
        	if ( uiTopic!=null) {
        		Notification noti = new Notification();
        		noti.setIdV(vhcl.getIdV());
        		noti.setTs(new Date());
        		noti.setVrn(vrn);
        		noti.setCd(NotificationCode.UNREACHABLE.toString());
        		uiTopic.pub(new AckNotification(noti));
        	}
        }
    }
    
    @Autowired
	WebSocketUiRepository wsUiRepository;
    @Autowired
	WebSocketPmRepository wsPmRepository;
	@Autowired
	TerminalServiceImpl terminalService;
	@Autowired
	VhclServiceImpl vhclService;
	
    /**
     * Send Message to UI WebSocket
     * for All terminals
     */
    public void sendMessage() {
    	List<String> terms = terminalService.getAllTerminal();	// T1, T2
    	// terminal을 순서대로 돌며 수행.
		terms.stream().forEach(term->{
			sendMessage(term, false);
		});
    }
    /**
     * @param term
     * @param unCheckDirty
     */
    public void sendMessage(String term, boolean unCheckDirty) {
    	
		List<VVhcl> vhcls = MemDB.VHCL.selectAll();
		List<PushObject> uiRealData = new ArrayList<PushObject>();
		
		
			
		WebSocketUiTopic topic = wsUiRepository.getTopic(term);
		if ( topic != null ) {
			
			vhcls.stream()
				.filter(v -> term.equals(v.getTerm()))
				.filter(v -> {
					String vrn = v.getVrn();
					WebSocketPmTopic pmTopic = wsPmRepository.getTopic(vrn);
					boolean hasSession = false;
					if ( pmTopic != null ) {
						for (WebSocketSession ss : pmTopic.getSessions()) {
							if ( ss.getUri().getPath().endsWith(vrn) ) {
								hasSession = true;	// topic 의 세션 내 차량번호가 존재시 filter에 포함됨. 그 외는 전송예외대상
							}
						}
					}
					
					if ( hasSession == false ) {
						EventMem evtMem = MemDB.EVT.select(v.getIdV());
						if ( evtMem != null ) {
							if ( Event.EventCode.UNREACHABLE.toString().equals( evtMem.getData().getEvtcd() ) ){
								hasSession = true;
							}
						}
					}
					
					return hasSession;
					
				})	// vrn이
				.forEach(v->{
						
					Long idV = v.getIdV();
					String vrn = v.getVrn();
					
					// DrivingInfo 조회
					try {
						DrivingInfoMem drv = MemDB.DRV.select(idV);
						if ( drv != null && (unCheckDirty || !drv.isDirty()) ) {
							
							DrivingInfoPush drvPush = new DrivingInfoPush(idV, vrn, drv.getData());
//						drvPush.calcEvtCd(defaultSpeed);
							drvPush.calcEvtCd();
							uiRealData.add(new PushObject(PushObject.Type.DRVINFO, drvPush));
							
							drv.setDirty(true);	// dirty 갱신
							MemDB.DRV.insertSafety(idV, drv);
						}
						
					} catch (Exception e) {
						log.error("DrivingInfoPushError:{}", e.toString());
					}
						
				
					// TripInfo 조회
					try {
						
						TripInfoMem trp = MemDB.TRP.select(idV);
						if ( trp != null && (unCheckDirty || !trp.isDirty()) ) {
							TripInfo tripInfo = trp.getData();
							
							if ( tripInfo.getTo() != null) {
								Poi to = tripInfo.getTo();
								ResAdminPoi raPoi = MemDB.POI.selectAll().stream()
										.filter(p->p.getPoiCd().equals(to.getPoicd()))
										.findFirst().orElse(null);
								if ( raPoi != null ) {
									to.setPoinm(raPoi.getPoiNm());
								}
							}
							
							TripInfoPush trpPush = new TripInfoPush(idV, vrn, tripInfo);
							if ( tripInfo.getTy() == 1) {
								ResAdminPoi closestPoi = trp.getCloestPoi();
								trpPush.setFrom(closestPoi.toPoi());
							} else if ( tripInfo.getTy() == 2) {
								TTrpRaw lastTrpRaw = trpRawMapper.selectLastByIdV(trp.getIdV());
								ResAdminPoi fromPoi = MemDB.POI.selectAll().stream()
										.filter(p->p.getPoiCd().equals(lastTrpRaw.getFromPoiCd()))
										.findFirst().orElse(null);
								if (fromPoi!=null) {
									trpPush.setFrom(fromPoi.toPoi());
								}
								if ( trpPush.getTo() == null ) {	// 도착 시 목적지 정보를 null로 올려주는 경우가 있을 경우 빈 구조체라도 생성하여 전달
									trpPush.setTo(new Poi());
								}
							}
							
							uiRealData.add(new PushObject(PushObject.Type.TRIPINFO, trpPush));
							
							trp.setDirty(true);	// dirty 갱신
							MemDB.TRP.insertSafety(idV, trp);
						}
					} catch (Exception e) {
						log.error("TripInfoPushError:{}", e.toString());
					}
				
					
					// SensorInfo 조회
					try {
						SensorInfoMem snsr = MemDB.SNSR.select(idV);
						if ( snsr != null && (unCheckDirty || !snsr.isDirty()) ) {
							
							SensorInfoPush snsrPush = new SensorInfoPush(idV, vrn, snsr.getData(), vhclDrivingAbleSec);
							uiRealData.add(new PushObject(PushObject.Type.SENSORINFO, snsrPush));
							
							snsr.setDirty(true);	// dirty 갱신
							MemDB.SNSR.insertSafety(idV, snsr);
						}

					} catch (Exception e) {
						log.error("SensorInfoPush-Error: {}", e.toString());
					}
				
					
					// Event 조회
					try {
						EventMem evt = MemDB.EVT.select(idV);
						if ( evt != null && (unCheckDirty || !evt.isDirty()) ) {
							
							EventPush evtPush = new EventPush(idV, vrn, evt.getData());
							uiRealData.add(new PushObject(PushObject.Type.EVENT, evtPush));
							
							evt.setDirty(true);	// dirty 갱신
							MemDB.EVT.insertSafety(idV, evt);
						}

					} catch (Exception e) {
						log.error("EventPush-Error: {}", e.toString());
					}
					
					
					
					// RouteInfo 조회
					try {
						RouteInfoMem rti = MemDB.RTI.select(idV);
						if ( rti != null && (unCheckDirty || !rti.isDirty()) ) {
							
							RouteInfoPush rtiPush = new RouteInfoPush(idV, vrn, rti.getData());
							uiRealData.add(new PushObject(PushObject.Type.ROUTEINFO, rtiPush));
							
							rti.setDirty(true);	// dirty 갱신
							MemDB.RTI.insertSafety(idV, rti);
						}

					} catch (Exception e) {
						log.error("RouteInfoPush-Error: {}", e.toString());
					}
				});
		
//			log.info("=============uiRealData.size()"+uiRealData.size());
			if ( uiRealData.size() > 0 ) {
				try {
					// topic에 데이터 전달
					topic.pub(uiRealData);
				} catch (JsonProcessingException e) {
					log.error("UI Topic Error: {}", e.toString());
				}
			}
		}
    	
    }
    
    public String getTermBySession(WebSocketSession session) {
    	String path = session.getUri().getPath();
    	String terminal = path.replace(connectURIPath, "");
    	return terminal;
    }
    

}