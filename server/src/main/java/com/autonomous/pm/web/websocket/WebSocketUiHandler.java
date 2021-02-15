package com.autonomous.pm.web.websocket;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.autonomous.pm.domain.common.ReasonCode;
import com.autonomous.pm.domain.common.ResultTy;
import com.autonomous.pm.domain.structure.Join;
import com.autonomous.pm.domain.structure.ack.Ack;
import com.autonomous.pm.domain.structure.ack.AckBody;
import com.autonomous.pm.domain.structure.upload.JoinUpload;
import com.autonomous.pm.util.JwtUtil;
import com.autonomous.pm.vhcl.websocket.WebSocketPmServiceImpl;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Profile("!stomp")
@Component
public class WebSocketUiHandler extends TextWebSocketHandler {

	private ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);
	
    private final ObjectMapper objectMapper;
    private final WebSocketUiRepository repository;

    @Autowired
    private WebSocketUiServiceImpl uiService;
    
    @Autowired
    public WebSocketUiHandler(ObjectMapper objectMapper, WebSocketUiRepository repository) {
    	this.objectMapper = objectMapper;
    	this.repository = repository;
    }

	@SuppressWarnings("unchecked")
	@Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {

        String payload = message.getPayload();
        String term = uiService.getTermBySession(session);
        log.info("term={}, payload={}", term, payload);
        
        try {
        	HashMap<String, String> jsonMap = objectMapper.readValue(payload, HashMap.class);
        	log.info("jsonMap : {}", jsonMap);
        	
        	String type = jsonMap.get("type");
        	
        	if ( ResultTy.JOIN.toString().equals(type) ) {
        		JoinUpload reqJoin = objectMapper.readValue(payload, JoinUpload.class);
        		uiService.join(session, reqJoin);
	    	}
        	
		} catch (JsonParseException e) {
			log.error(e.toString());
		} catch (JsonMappingException e) {
			log.error(e.toString());
		} catch (IOException e) {
			log.error(e.toString());
		}

    }

	@Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    	log.info("WebSocket UI Connected Session={}", session);
    }
	
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    	log.info("WebSocket UI Closed Session={}", session);
    	String terminal = uiService.getTermBySession(session);
        repository.remove(session, status, terminal);
    }
    
}