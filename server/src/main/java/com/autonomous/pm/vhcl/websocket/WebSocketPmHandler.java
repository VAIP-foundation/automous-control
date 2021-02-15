package com.autonomous.pm.vhcl.websocket;

import java.io.IOException;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.autonomous.pm.domain.common.ResultTy;
import com.autonomous.pm.domain.structure.ack.Ack;
import com.autonomous.pm.domain.structure.ack.AckBody;
import com.autonomous.pm.domain.structure.report.DrivingInfoReport;
import com.autonomous.pm.domain.structure.report.EventReport;
import com.autonomous.pm.domain.structure.report.RouteInfoReport;
import com.autonomous.pm.domain.structure.report.SensorInfoReport;
import com.autonomous.pm.domain.structure.report.TripInfoReport;
import com.autonomous.pm.domain.structure.upload.ByeUpload;
import com.autonomous.pm.domain.structure.upload.JoinUpload;
import com.autonomous.pm.model.Do.VVhcl;
import com.autonomous.pm.service.CmdHstServiceImpl;
import com.autonomous.pm.service.restful.PingPongServiceImpl;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Profile("!stomp")
@Component
public class WebSocketPmHandler extends TextWebSocketHandler {


    public static final Logger wsFileLogger = LoggerFactory.getLogger("wsFileLogger");
    
    private final ObjectMapper objectMapper;
    private final WebSocketPmRepository repository;

    @Autowired
    private WebSocketPmServiceImpl pmService;
    @Autowired
    private PingPongServiceImpl pingPongService;
    @Autowired
    private CmdHstServiceImpl cmdHstService;    
    
    @Autowired
    public WebSocketPmHandler(ObjectMapper objectMapper, WebSocketPmRepository repository) {
    	this.objectMapper = objectMapper;
    	this.repository = repository;
    }

    /**
    
     */
	@SuppressWarnings("unchecked")
	@Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
		
        String payload = message.getPayload();
        String vrn = pmService.getVrnBySession(session);
        VVhcl vhcl = pmService.getVhcl(vrn);
        
        wsFileLogger.info("\n[PM/WS] vrn={}, payload={}", vrn, payload);	// automous.pm.ws.{date}.log 에 저장되도록 설정
        
        try {
        	HashMap<String, String> jsonMap = objectMapper.readValue(payload, HashMap.class);
        	String type = jsonMap.get("type");
        	Long idV;
        	if (vhcl == null) {
        		pmService.fail(session, type, "VHCL_NOT_FOUND");
        		
        	} else {
        		pingPongService.updateLastDataTime(session);
        		
        		idV = vhcl.getIdV();
        	
	        	/* 201. JOIN */
	        	if ( ResultTy.JOIN.toString().equals(type) ) {
	        		JoinUpload reqJoin = objectMapper.readValue(payload, JoinUpload.class);
	        		pmService.join(session, reqJoin);
	        		
	    		/* 202. BYE */
	        	} else if ( ResultTy.BYE.toString().equals(type) ) {
	        		ByeUpload reqBye = objectMapper.readValue(payload, ByeUpload.class);
	        		pmService.bye(session, reqBye);
	        	}
	        	
        		WebSocketPmTopic topic = repository.getTopic(vrn);
                if ( topic != null && topic.hasSession(session) ) {
                
		    		/* 208. DrivingInfo */
			    	if ( ResultTy.DRIVING_INFO.toString().equals(type) ) {
			    		DrivingInfoReport drivingInfoReport = objectMapper.readValue(payload, DrivingInfoReport.class);
		        		pmService.drivingInfo(idV, drivingInfoReport);
		        		
		    		/* 209. TripInfo */
			    	} else if ( ResultTy.TRIP_INFO.toString().equals(type) ) {
			    		TripInfoReport tripInfoReport = objectMapper.readValue(payload, TripInfoReport.class);
			    		pmService.tripInfo(idV, tripInfoReport);
			    		
		    		/* 210. SensorInfo */
			    	} else if ( ResultTy.SENSOR_INFO.toString().equals(type) ) {
			    		SensorInfoReport sensorInfoReport = objectMapper.readValue(payload, SensorInfoReport.class);
			    		pmService.sensorInfo(idV, sensorInfoReport);
			    		
		    		/* 211. Event */
			    	} else if ( ResultTy.EVENT.toString().equals(type) ) {
			    		EventReport eventReport = objectMapper.readValue(payload, EventReport.class);
			    		pmService.event(idV, eventReport);
			    		
		    		/* 212. RouteInfo */
			    	} else if ( ResultTy.ROUTE_INFO.toString().equals(type) ) {
			    		RouteInfoReport routeInfoReport = objectMapper.readValue(payload, RouteInfoReport.class);
			    		pmService.routeInfo(idV, routeInfoReport);
			    		
			    	/* Ack*/	
			    	} else if ( ResultTy.ACK.toString().equals(type) ) {
			    		AckBody ackBody = objectMapper.readValue(payload, AckBody.class);
			    		// T_CMD_HST에 저장
			     		cmdHstService.updateCmdHstByMid(ackBody.getData().getOmid(), ackBody.toString());
			    	} 
                } else {
                	log.info("topic is null or has not session");
                }
                
	    	}
        	
		} catch (JsonParseException e) {
			log.error(e.toString());
		} catch (JsonMappingException e) {
			log.error(e.toString());
		} catch (IOException e) {
			log.error(e.toString());
		} finally {
		}

    }

	@Override
    protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
		String vrn = pmService.getVrnBySession(session);
        pingPongService.updateLastPongTime(session);	// 네트워크 상태 확인 Memory 에 추가한다.
	}
	
	@Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		wsFileLogger.info("Socket PM Connected Session={}", session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    	wsFileLogger.info("Socket PM Closed session={}", session);
        pmService.disconnect(session);
    }
    
}