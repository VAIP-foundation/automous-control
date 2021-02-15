package com.autonomous.pm.web.websocket;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import org.springframework.stereotype.Repository;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class WebSocketUiRepository {
	
    private HashMap<String, WebSocketUiTopic> topicMap;
    
    @Getter
    private Collection<WebSocketUiTopic> topics;

    public WebSocketUiRepository() {
    	topicMap = new HashMap<String, WebSocketUiTopic>();
    	
    	topics = Collections.unmodifiableCollection(topicMap.values());
    }
    
    public Collection<WebSocketUiTopic> getTopics() {
    	return topicMap.values();
    }
    
    public WebSocketUiTopic getTopic(String term) {
    	return topicMap.get(term);
    }
    
    public WebSocketUiTopic setTopic(String term){
    	if ( !topicMap.containsKey(term) ) {	// topicMap에 
    		WebSocketUiTopic topic = WebSocketUiTopic.create(term);
    		topicMap.putIfAbsent(term, topic);
    	}
    	return getTopic(term);
    }
    
    public void remove(WebSocketSession session, CloseStatus status, String term) {
    	log.info("Session remove >> session={}, status={}, term={}", session, status, term);
    	WebSocketUiTopic topic = getTopic(term);
    	if ( topic != null ) {
    		topic.bye(session);
    	}
    }
    
    /**
     * @param ack
     * @param term
     * @throws JsonProcessingException
     */
    public void publishMessageByTopic(Object ack, String term) throws JsonProcessingException {
    	WebSocketUiTopic topic = getTopic(term);
    	if ( topic != null ) {
    		topic.pub(ack);
    	}
    }
    
}