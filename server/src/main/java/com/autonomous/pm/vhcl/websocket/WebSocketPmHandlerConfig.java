package com.autonomous.pm.vhcl.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;


@Profile("!stomp,sockJS")
@Configuration
@EnableWebSocket
public class WebSocketPmHandlerConfig implements WebSocketConfigurer {
	
	public static final Logger logger = LoggerFactory.getLogger(WebSocketPmHandlerConfig.class);
	private final int MAX_MESSAGE_SIZE = 1024 * 1024 * 16;			// 16Mb
//	private final Long MAX_SESSION_IDLE_TIMEOUT = 1 * 60 * 1000L;	// 1min
	private final Long MAX_SESSION_IDLE_TIMEOUT = 24 * 60 * 60 * 1000L;	// 1day
	
	
	@Autowired
    private WebSocketPmHandler socketHandler;

	@Bean
	public ServletServerContainerFactoryBean createWebSocketContainer() {
		ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
		container.setMaxTextMessageBufferSize(MAX_MESSAGE_SIZE);
		container.setMaxBinaryMessageBufferSize(MAX_MESSAGE_SIZE);
		container.setMaxSessionIdleTimeout(MAX_SESSION_IDLE_TIMEOUT);
		container.setAsyncSendTimeout(MAX_SESSION_IDLE_TIMEOUT);
		return container;
	}

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		// INERFACE - 200. WebSocket 접속
		// 해당 endpoint로 handshake가 이루어진다.
		// use not .withSockJS();
		registry
			.addHandler(socketHandler, "/ws/v1/connect/**")
			.setAllowedOrigins("*");
//			.withSockJS()
		
	}

}