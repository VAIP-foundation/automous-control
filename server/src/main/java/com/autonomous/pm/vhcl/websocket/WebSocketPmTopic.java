package com.autonomous.pm.vhcl.websocket;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;

import org.springframework.web.socket.PingMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.autonomous.pm.memcache.MemDB;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
public class WebSocketPmTopic {
    private String id;
    private String uuid;
    private String vrn;
    private Set<WebSocketSession> sessions = new HashSet<>();
    
    private ObjectMapper objectMapper = new ObjectMapper();
	    
    // 채팅방 생성
    public static WebSocketPmTopic create(@NonNull String vrn) {
    	WebSocketPmTopic created = new WebSocketPmTopic();
    	created.id = vrn;
    	created.vrn = vrn;
        created.uuid = UUID.randomUUID().toString();
        return created;
    }
    
    public void join(WebSocketSession session) {
    	if ( !sessions.contains(session) ) {
    		sessions.add(session);
    	}
    }
    
    public void bye(WebSocketSession session) {
    	if ( sessions.contains(session) ) {
    		sessions.remove(session);
    	}
    }
    
    /**
     */
    public void removeSessionAll() {
    	
    	ArrayList<WebSocketSession> sessionsList = new ArrayList<WebSocketSession>();
    	sessions.stream().forEach(s->sessionsList.add(s));
    	for ( WebSocketSession s : sessionsList ) {
    		bye(s);
    		MemDB.WS_PM_SESSION.delete(s.getId());
    	}
    	
    }
    
    public boolean hasSession(WebSocketSession session) {
    	return sessions.contains(session);
    }

    public <T> void send(T ack) throws JsonProcessingException {
    	/*
    	 */
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
    	sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
    	objectMapper.setDateFormat(sdf);
    	
    	
        TextMessage message = new TextMessage(objectMapper.writeValueAsString(ack));
        log.info("PM Topic:{} send-message:{}", this.getVrn(), message.getPayload());
        sessions.parallelStream().forEach(session -> {
			try {
				session.sendMessage(message);
			} catch (IOException e) {
				log.error(e.toString());
			}
		});
    }
    
    public <T> void ping() throws JsonProcessingException {
        PingMessage message = new PingMessage();
        sessions.parallelStream().forEach(session -> {
			try {
				WebSocketPmSession wsPmSession = MemDB.WS_PM_SESSION.select(session.getId());
//				if ( wsPmSession.is)
				session.sendMessage(message);
		    	wsPmSession.setLastPingTimeByNow();
		    	MemDB.WS_PM_SESSION.insertSafety(session.getId(), wsPmSession);
			} catch (IOException e) {
				log.error(e.toString());
			}
		});
    }
}
