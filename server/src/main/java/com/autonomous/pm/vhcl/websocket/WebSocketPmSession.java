package com.autonomous.pm.vhcl.websocket;

import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@ToString
@Slf4j
@Component
public class WebSocketPmSession {
	
	private WebSocketSession webSocketSession;
	
	private Long lastDataTime;		// 마지막 데이터 수신 시간
	private Long lastPingTime;		// 마지막 핑 전송 시간
	private Long lastPongTime;		// 마지막 퐁 수신 시간
	
	private boolean isUnreachable = false;	// 현재 네트워크가 연결 안되고 있는 상태인지 여부

	public WebSocketPmSession() {
	}
	
	public WebSocketPmSession(WebSocketSession webSocketSession) {
		this.webSocketSession = webSocketSession;
	}
	
	public WebSocketSession getWebSocketSession() {
		return webSocketSession;
	}
	public void setWebSocketSession(WebSocketSession webSocketSession) {
		this.webSocketSession = webSocketSession;
	}
	
	// lastDataTime
	public Long getLastDataTime() {
		return lastDataTime;
	}
	public void setLastDataTime(Long lastDataTime) {
		this.lastDataTime = lastDataTime;
	}
	public void setLastDataTime(Date lastDataDate) {
		this.setLastDataTime(lastDataDate.getTime());
	}
	public void setLastDataTimeByNow() {
		this.setLastDataTime(new Date());
	}
	
	// lastPingTime
	public Long getLastPingTime() {
		return lastPingTime;
	}
	public void setLastPingTime(Long lastPingTime) {
		this.lastPingTime = lastPingTime;
	}
	public void setLastPingTime(Date lastPingDate) {
		this.setLastPingTime(lastPingDate.getTime());
	}
	public void setLastPingTimeByNow() {
		this.setLastPingTime(new Date());
	}
	
	// lastPongTime
	public Long getLastPongTime() {
		return lastPongTime;
	}
	public void setLastPongTime(Long lastPongTime) {
		this.lastPongTime = lastPongTime;
	}
	public void setLastPongTime(Date lastPongDate) {
		this.setLastPongTime(lastPongDate.getTime());
	}
	public void setLastPongTimeByNow() {
		this.setLastPongTime(new Date());
	}
	
	public boolean isUnreachable() {
		return isUnreachable;
	}
	public void setUnreachable(boolean isUnreachable) {
		this.isUnreachable = isUnreachable;
	}

	@Override
	public String toString() {
		return "WebSocketPmSession [webSocketSession=" + webSocketSession + ", lastDataTime=" + lastDataTime
				+ ", lastPingTime=" + lastPingTime + ", lastPongTime=" + lastPongTime + "]";
	}

	
	
}