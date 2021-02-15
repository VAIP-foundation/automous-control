package com.autonomous.pm.vhcl.websocket;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Repository;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class WebSocketPmRepository {
	
    private Map<String, WebSocketPmTopic> topicMap;
    
    @Getter
    private Collection<WebSocketPmTopic> topics;

    public WebSocketPmRepository() {
    	topicMap = new HashMap<String, WebSocketPmTopic>();
    	topics = Collections.unmodifiableCollection(topicMap.values());
    }
    
    // Repository에 VRN 생성(이미 존재시 생성하지 않는다)
    public WebSocketPmTopic setTopic(String vrn){
		
		WebSocketPmTopic topic = new WebSocketPmTopic();
		topic.setId(vrn);
		topic.setVrn(vrn);
		topic.setUuid(UUID.randomUUID().toString());
		topicMap.putIfAbsent(vrn, topic);
		
    	return getTopic(vrn);
    }
    
    public WebSocketPmTopic getTopic(String vrn) {
        return topicMap.get(vrn);
    }
    
    public Collection<WebSocketPmTopic> getTopics() {
        return topicMap.values();
    }
    
    public void remove(WebSocketSession session, CloseStatus status, String vrn) {
    	// vrn을 기준으로 Topic을 찾아 세션을 없애준다.
    	log.info("Session remove >> session={}, status={}, vrn={}", session, status, vrn);
    	WebSocketPmTopic topic = getTopic(vrn);
    	if ( topic != null ) {	// join 하지 않았을 경우 null이므로 bye할 필요 없다.
    		topic.bye(session);
    	}
    }
    
}