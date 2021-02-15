package com.autonomous.pm.vhcl.websocket;

import java.io.IOException;
import java.util.List;

import org.springframework.web.socket.WebSocketSession;

import com.autonomous.pm.domain.structure.CallChange;
import com.autonomous.pm.domain.structure.Call;
import com.autonomous.pm.domain.structure.Poi;
import com.autonomous.pm.domain.structure.Stop;
import com.autonomous.pm.domain.structure.report.DrivingInfoReport;
import com.autonomous.pm.domain.structure.report.EventReport;
import com.autonomous.pm.domain.structure.report.RouteInfoReport;
import com.autonomous.pm.domain.structure.report.SensorInfoReport;
import com.autonomous.pm.domain.structure.report.TripInfoReport;
import com.autonomous.pm.domain.structure.upload.ByeUpload;
import com.autonomous.pm.domain.structure.upload.JoinUpload;

interface WebSocketPmService {
    
    void join(WebSocketSession session, JoinUpload reqJoin) throws IOException;
    
    void bye(WebSocketSession session, ByeUpload reqBye) throws IOException;
    
    /**
     */
    void stop(String terminal, String vrn, Stop.Prio prio, Stop.Ty ty);
    void stop(String terminal, Long idV, Stop.Prio prio, Stop.Ty ty);
    void stopAll(String terminal, Stop.Prio prio, Stop.Ty ty);
    
    void resume(String terminal, String vrn, Stop.Prio prio, Stop.Ty ty);
    void resume(String terminal, Long idV, Stop.Prio prio, Stop.Ty ty);

    
    void callChange(String vrn, CallChange callChange);
    void callChange(Long idV, CallChange callChange);
    
    void drivingInfo(Long vId, DrivingInfoReport drivingInfoReport);

    void tripInfo(Long vId, TripInfoReport tripInfoReport);

    void sensorInfo(Long vId, SensorInfoReport sensorInfoReport);
    
    void event(Long vId, EventReport eventReport);
    
    void routeInfo(Long vId, RouteInfoReport routeInfoReport);
    
    String getVrnBySession(WebSocketSession session);

}