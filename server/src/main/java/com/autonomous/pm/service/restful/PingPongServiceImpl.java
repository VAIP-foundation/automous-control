package com.autonomous.pm.service.restful;

import java.io.IOException;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.PingMessage;
import org.springframework.web.socket.WebSocketSession;

import com.autonomous.pm.domain.structure.Event;
import com.autonomous.pm.domain.structure.report.EventReport;
import com.autonomous.pm.memcache.MemDB;
import com.autonomous.pm.model.Do.VVhcl;
import com.autonomous.pm.service.TripInfoServiceImpl;
import com.autonomous.pm.util.DateUtil;
import com.autonomous.pm.vhcl.websocket.WebSocketPmRepository;
import com.autonomous.pm.vhcl.websocket.WebSocketPmServiceImpl;
import com.autonomous.pm.vhcl.websocket.WebSocketPmSession;
import com.autonomous.pm.web.websocket.WebSocketUiRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PingPongServiceImpl implements PingPongService {
	
	public static final Logger wsFileLogger = LoggerFactory.getLogger("wsFileLogger");
    
	@Value("${session.last.data.check.sec:10}")	// 세션체크 시 마지막 데이터 확인 시간 기준
	private long sessionLastDataCheckSec;
	@Value("${session.unreached.sec:10}")		// 세션체크 시 unreached 시간 기준
	private long sessionUnreachedSec;
	@Value("${session.disconnected.sec:1800}")	// 세션체크 시 disconnected 시간 기준
	private long sessionDisconnectedSec;
	
	
	@Autowired
    private WebSocketPmRepository wsPmRepository;
	@Autowired
    private WebSocketUiRepository wsUiRepository;
	
	
	@Autowired
    private WebSocketPmServiceImpl wsPmService;
	
	@Autowired
    private TripInfoServiceImpl tripInfoService;
    
	@Override
	public void addSession(WebSocketSession session) {
    	WebSocketPmSession wsPmSession = new WebSocketPmSession(session);
    	wsPmSession.setLastDataTimeByNow();
    	wsPmSession.setLastPingTimeByNow();
    	wsPmSession.setLastPongTimeByNow();
    	
		MemDB.WS_PM_SESSION.insertSafety(session.getId(), wsPmSession);
	}

	@Override
	public void removeSession(WebSocketSession session) {
		MemDB.WS_PM_SESSION.delete(session.getId());
	}

	/**
     * @param session
     * @throws IOException
     */
	@Override
    public void updateLastDataTime(WebSocketSession session) {
    	WebSocketPmSession wsPmSession = MemDB.WS_PM_SESSION.select(session.getId());
    	if ( wsPmSession != null ) {
	    	wsPmSession.setLastDataTimeByNow();
	    	wsPmSession.setLastPingTimeByNow();
	    	wsPmSession.setLastPongTimeByNow();
	    	MemDB.WS_PM_SESSION.insertSafety(session.getId(), wsPmSession);
    	}
    }
	
	/**
     */
    public void updateLastPingTime(WebSocketSession session) {
    	WebSocketPmSession wsPmSession = MemDB.WS_PM_SESSION.select(session.getId());
    	if ( wsPmSession != null ) {
	    	wsPmSession.setLastPingTimeByNow();
	    	MemDB.WS_PM_SESSION.insertSafety(session.getId(), wsPmSession);
    	}
    }
	
	/**
     */
    public void updateLastPongTime(WebSocketSession session) {
    	WebSocketPmSession wsPmSession = MemDB.WS_PM_SESSION.select(session.getId());
    	
    	

		log.info(">>PingPongTest:: Recv Pong= " + session.getUri() + " / " + DateUtil.getTimeFormat(new Date()));
    	wsPmSession.setLastPongTimeByNow();
		wsPmSession.setUnreachable(false);	
    	MemDB.WS_PM_SESSION.insertSafety(session.getId(), wsPmSession);
    }
	
	
    public void sendPing() {
    	log.info(">>PingPongTest:: WS_PM_SESSION SIZE=" + MemDB.WS_PM_SESSION.selectAll().size());
    	
    	
    	StringBuffer sb = new StringBuffer();
    	sb.append("\n==================[SESSION CHECK]==================");
		wsPmRepository.getTopics().stream().forEach(topic -> {
			sb.append("\n[PM] topic=\"").append(topic.getVrn() ).append("\", sessions=").append(topic.getSessions().size());
		});
		wsUiRepository.getTopics().stream().forEach(topic -> {
			sb.append("\n[UI] topic=\"").append(topic.getTerm()).append("\", sessions=").append(topic.getSessions().size());
		});
    	sb.append("\n===================================================");
    	wsFileLogger.info(sb.toString());
    	
    	
    	
    	MemDB.WS_PM_SESSION.selectAll().stream().forEach(wsPmSession->{
    		WebSocketSession session = wsPmSession.getWebSocketSession();
    		
    		
    		log.info(">>PingPongTest:: WS_PM_SESSION STATUS="
    				+ "/sessionCount=" + MemDB.WS_PM_SESSION.selectAll().size()
    				+ "/uri" + session.getUri()
    				+ "now:" + new Date().getTime()/1000%1000
    				+ "/lastDataTime:" + wsPmSession.getLastDataTime()/1000%1000
					+ "/lastPingTime:"+ wsPmSession.getLastPingTime()/1000%1000
					+ "/lastPongTime:"+ wsPmSession.getLastPongTime()/1000%1000
					+ "/isNeedCheck:" + isNeedCheck(session)
					+ "/isDisconnectedNetwork:" + isDisconnectedNetwork(session)
					+ "/isUnreachableNetwork:" + isUnreachableNetwork(session)
					+ "/isUnreachable:"+ wsPmSession.isUnreachable()
					);
    		
			if ( !isNeedCheck(session) ) {
				wsPmSession.setUnreachable(false);	
		    	MemDB.WS_PM_SESSION.insertSafety(session.getId(), wsPmSession);
		    	
			} else {
				log.info(">>PingPongTest:: isNeedCheck");
				if ( isDisconnectedNetwork(session) ) {
					log.info(">>PingPongTest:: isDisconnectedNetwork");
					try {
						wsPmService.bye(session);
					} catch (IOException e) {
						log.error(e.toString());
					}
					
				} else {
					
					if ( isUnreachableNetwork(session) ) {
						log.info(">>PingPongTest:: isUnreachableNetwork");
						
						if ( !wsPmSession.isUnreachable() ) {
							
							Event event = new Event();
							event.setTs(new Date());
							event.setEvtcd("2001");	// unreachable
							
							EventReport eventReport = new EventReport();
							eventReport.setData(event);
							eventReport.setType("Event");
							
							wsPmService.event(session, eventReport);
							
							wsPmSession.setUnreachable(true);
							MemDB.WS_PM_SESSION.insertSafety(session.getId(), wsPmSession);
							
							String vrn = wsPmService.getVrnBySession(session);
							VVhcl vhcl = MemDB.VHCL.selectAll().stream().filter(v->vrn.equals(v.getVrn())).findFirst().orElse(null);
							vhcl.setDrvStts(20);
							MemDB.VHCL.insertSafety(vhcl.getIdV(), vhcl);
							
							tripInfoService.updateFinishOnBye(vrn);
						}
					}
					
					try {
						PingMessage message = new PingMessage();
						if ( session.isOpen() ) {
							session.sendMessage(message);
							updateLastPingTime(session);
							log.info(">>PingPongTest:: Send Ping= " + session.getUri() + " / " + DateUtil.getTimeFormat(new Date()));
						} else {
							removeSession(session);
						}
					} catch (IOException e) {
						log.error(e.toString());
					}
				}
				
			}
    	});
    }
   
    
    public void checkSession() {
    	sendPing();
    }
    
    
	/**
	 * @return
	 */
	private boolean isNeedCheck(WebSocketSession session) {
		WebSocketPmSession wsPmSession = MemDB.WS_PM_SESSION.select(session.getId());
		
		boolean result = false;
		Date now = new Date();
		Long diffTime = now.getTime() - wsPmSession.getLastDataTime();
		if ( diffTime > sessionLastDataCheckSec*1000 ) {	
			result = true;									
		}
		return result;
	}
	
	
	/**
	 * @return
	 */
	boolean isUnreachableNetwork(WebSocketSession session) {
		WebSocketPmSession wsPmSession = MemDB.WS_PM_SESSION.select(session.getId());
		
		boolean result = false;
		Long lastPingTime = wsPmSession.getLastPingTime();
		Long lastPongTime = wsPmSession.getLastPongTime();
		if ( lastPingTime != null && lastPongTime != null ) {
			Long diffTime = lastPingTime - lastPongTime;
			
			if (diffTime > sessionUnreachedSec*1000) {	
				result = true;							
			}
		}
		return result;
	}
	
	
	/**
	 * @return
	 */
	boolean isDisconnectedNetwork(WebSocketSession session) {
		WebSocketPmSession wsPmSession = MemDB.WS_PM_SESSION.select(session.getId());
		
		boolean result = false;
		Long lastPingTime = wsPmSession.getLastPingTime();
		Long lastPongTime = wsPmSession.getLastPongTime();
		if ( lastPingTime != null && lastPongTime != null ) {
			Long diffTime = lastPingTime - lastPongTime;
			
			if (diffTime > sessionDisconnectedSec*1000) {	
				result = true;								
			}
		}
		return result;
	}


}