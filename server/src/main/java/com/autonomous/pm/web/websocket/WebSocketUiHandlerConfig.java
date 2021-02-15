package com.autonomous.pm.web.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

import com.autonomous.pm.vhcl.websocket.WebSocketPmHandler;
import com.autonomous.pm.vhcl.websocket.WebSocketPmHandlerConfig;


@Profile("!stomp,sockJS")
@Configuration
@EnableWebSocket
public class WebSocketUiHandlerConfig implements WebSocketConfigurer {

	public static final Logger logger = LoggerFactory.getLogger(WebSocketPmHandlerConfig.class);
//	private final int MAX_MESSAGE_SIZE = 1024 * 1024 * 16;			// 16Mb
//	private final Long MAX_SESSION_IDLE_TIMEOUT = 1 * 60 * 1000L;	// 1min
	
	@Autowired
	private WebSocketUiHandler socketHandler;
	
//	@Bean
//	public ServletServerContainerFactoryBean createWebSocketContainer() {
//		ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
//		container.setMaxTextMessageBufferSize(MAX_MESSAGE_SIZE);
//		container.setMaxBinaryMessageBufferSize(MAX_MESSAGE_SIZE);
//		container.setMaxSessionIdleTimeout(MAX_SESSION_IDLE_TIMEOUT);
//		container.setAsyncSendTimeout(MAX_SESSION_IDLE_TIMEOUT);
//		return container;
//	}
	
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		// INERFACE - 200. WebSocket 접속
		// 해당 endpoint로 handshake가 이루어진다.
		// use not .withSockJS();
		
    	String[] endPoints = {
			"/ws/connect",
			"/ws/v1/cntrlui/connect/T1",
			"/ws/v1/cntrlui/connect/T2"
		};
		
		registry
			.addHandler(socketHandler, endPoints)
//			.addHandler(socketHandler, "/ws/v1/cntrlui/**")
			.setAllowedOrigins("*");
		
	}

}