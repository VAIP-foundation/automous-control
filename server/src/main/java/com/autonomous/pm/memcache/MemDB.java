/**
 * 
 */
package com.autonomous.pm.memcache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.autonomous.pm.domain.structure.Call;
import com.autonomous.pm.domain.structure.mem.DrivingInfoMem;
import com.autonomous.pm.domain.structure.mem.EventMem;
import com.autonomous.pm.domain.structure.mem.RouteInfoMem;
import com.autonomous.pm.domain.structure.mem.SensorInfoMem;
import com.autonomous.pm.domain.structure.mem.TripInfoMem;
import com.autonomous.pm.memcache.base.MemEntity;
import com.autonomous.pm.model.CallPlanP1;
import com.autonomous.pm.model.CallPlanP2;
import com.autonomous.pm.model.JwtSession;
import com.autonomous.pm.model.LoginInfo;
import com.autonomous.pm.model.Do.TGrpPoi;
import com.autonomous.pm.model.Do.VPoiGate;
import com.autonomous.pm.model.Do.VVhcl;
import com.autonomous.pm.model.Dto.ResAdminPoi;
import com.autonomous.pm.vhcl.websocket.WebSocketPmSession;

/**
 * @author promaker
 *
 */
public class MemDB {
	
	public static final Logger logger = LoggerFactory.getLogger(MemDB.class);

	/**
	 */
	public static MemEntity<VVhcl> VHCL = new MemEntity<VVhcl>(1);

	/**
	 */
	public static MemEntity<ResAdminPoi> POI = new MemEntity<ResAdminPoi>(1);

	/**
	 */
	public static MemEntity<TGrpPoi> GRP_POI = new MemEntity<TGrpPoi>(1);

	/**
	 */
	public static MemEntity<VPoiGate> GATE = new MemEntity<VPoiGate>(1);
	
	
	/**
	 */
	public static MemEntity<DrivingInfoMem> DRV = new MemEntity<DrivingInfoMem>(1);
	
	/**
	 */
	public static MemEntity<TripInfoMem> TRP = new MemEntity<TripInfoMem>(1);
	
	/**
	 */
	public static MemEntity<SensorInfoMem> SNSR = new MemEntity<SensorInfoMem>(1);
	
	/**
	 */
	public static MemEntity<EventMem> EVT = new MemEntity<EventMem>(1);
	
	/**
	 */
	public static MemEntity<RouteInfoMem> RTI = new MemEntity<RouteInfoMem>(1);
	
	/**
	 */
	public static MemEntity<CallPlanP1> CALL_PLAN_P1 = new MemEntity<CallPlanP1>(1);
	
	/**
	 */
	public static MemEntity<CallPlanP2> CALL_PLAN_P2 = new MemEntity<CallPlanP2>(1);
	
	
	/**
	 */
	public static MemEntity<Call> LAST_CALL = new MemEntity<Call>(1);
	
	
	/**
	 */
	public static MemEntity<WebSocketPmSession> WS_PM_SESSION = new MemEntity<WebSocketPmSession>(1);
	
	/**
	 */
	public static MemEntity<JwtSession> JWT = new MemEntity<JwtSession>(1);
	
	
	/**
	 */
	public static MemEntity<LoginInfo> LOGIN_INFO = new MemEntity<LoginInfo>(1);

	
}
