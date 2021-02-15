package com.autonomous.pm.web.websocket;

import java.io.IOException;

import org.springframework.web.socket.WebSocketSession;

import com.autonomous.pm.domain.structure.upload.JoinUpload;

interface WebSocketUiService {
    
    void join(WebSocketSession session, JoinUpload reqJoin) throws IOException;
    
}