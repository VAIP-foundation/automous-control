package com.autonomous.pm.web.websocket;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.autonomous.pm.domain.common.ResultTy;
import com.autonomous.pm.domain.structure.TripInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
public class WebSocketUiTopic {
	
	public static final Logger wsFileLogger = LoggerFactory.getLogger("wsFileLogger");
	
    private String id;
    private String uuid;
    private String term;
    private Set<WebSocketSession> sessions = new HashSet<>();
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@Autowired
    private WebSocketUiRepository repository;
	
    // 채팅방 생성
    public static WebSocketUiTopic create(@NonNull String term) {
    	WebSocketUiTopic created = new WebSocketUiTopic();
    	created.id = term;
    	created.term = term;
        created.uuid = UUID.randomUUID().toString();
//        created.objectMapper.configure
        return created;
    }
    
    /**
     * 인터페이스type 에 따라 세션설정, 메세지 전송등을 분기처리한다.
     * @param session
     * @param type
     * @param ack
     * @param objectMapper
     * @throws JsonProcessingException
     */
    public void handleMessage(WebSocketSession session, ResultTy type, Object ack) throws JsonProcessingException {
    	log.info("session={}", session);
    	log.info("ack={}", ack);

    	switch (type) {
		case JOIN:
			join(session);
			pub(ack);
			break;
		case DRIVING_INFO:
			// TODO: DRIVING_INFO()
			pub(ack);
			break;
		case TRIP_INFO:
			// TODO: TRIP_INFO()
			pub(ack);
			break;
		case SENSOR_INFO:
			// TODO: SENSOR_INFO()
			pub(ack);
			break;
		case EVENT:
			// TODO: EVENT()
			pub(ack);
			break;
		case ROUTE_INFO:
			// TODO: ROUTE_INFO()
			pub(ack);
			break;
		default:
			break;
		}
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
    
    // 해당 토픽 내 등록된 세션이 있는지 확인한다(=join이 되어 있는지 확인한다)
    public boolean hasSession(WebSocketSession session) {
    	return sessions.contains(session);
    }

    
    public <T> void pub(T ack) throws JsonProcessingException {

    	/*
    	 * Date 형식이 1588122046213 에서 2020-04-29T01:03:03.213Z 형식으로 변경이 필요
    	 * -> spring.jackson.serialization.WRITE_DATES_AS_TIMESTAMPS=false 설정으로 해결함. 20200806. kdh.
    	 */
//    	DateTimeFormatter dd = new DateTimeFormatter(null, null, null, null, null, null, null);
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
    	sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
    	objectMapper.setDateFormat(sdf);
    	
        TextMessage message = new TextMessage(objectMapper.writeValueAsString(ack));
//        log.info("UI Topic:{} send-message:{}", this.getTerm(), message.getPayload());
        
        
        wsFileLogger.info("\n[UI/WS] term={}, sessions={}, payload={}", this.getTerm(), sessions.size(), message.getPayload());	// automous.pm.ws.{date}.log 에 저장되도록 설정
        sessions.parallelStream().forEach(session -> {
			try {
//				log.info("UI SEND term={}, MSG={}", this.term, message.getPayload().toString());
				if ( session.isOpen() ) {
					session.sendMessage(message);
				} else {
					bye(session);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				wsFileLogger.error("\n[UI/WS] term={}, sessions={}, payload={}", this.getTerm(), sessions.size(), message.getPayload());	// automous.pm.ws.{date}.log 에 저장되도록 설정
				wsFileLogger.error(e.toString());
			}
		});
    }
    
}
