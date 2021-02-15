package com.autonomous.pm.service.restful;

import org.springframework.web.socket.WebSocketSession;

public interface PingPongService {
	
	void addSession(WebSocketSession session);
	void removeSession(WebSocketSession session);
	void updateLastDataTime(WebSocketSession session);
}
