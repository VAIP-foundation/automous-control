package com.autonomous.pm.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.autonomous.pm.config.AppConfig;
import com.autonomous.pm.util.GeoFence;

public class Environment {
	public static final Logger logger = LoggerFactory.getLogger(Environment.class);

	boolean v2xServerConnect = true;
	boolean useServiceLayer = true;
	boolean saveV2xRawPacket = false;

	boolean useTimeout = true;
	boolean useUpObst = true;
	boolean useUpstream= true;
	boolean useSpatDat = true;
	
	
	boolean debuguploadsysgamestat=true;

	String gpsFomular = "Vincenty";

	boolean geoFenceUse = true;


	boolean printSocketRcvHex = true;
	boolean printSocketSndHex = true;

	boolean vehicleLocationTrackingFromObd = true;

	int eventClearTime = 1000; // event 발생 후 수신이 없으면 해제로 판단하는 시간. 기본 1000ms
	int stopSendingEventClear = 5000; // event 해제 판단 후 더이상 data를 UI에 전송하지 않는 시간으 기준. 기본 5000ms

	long rasEventClearTime = 2000L;
	long rasStopSendingEventClear = 3000L;

	boolean flagWebsocketStreamPause = false;

	long engOffTimeSec = 10;

	long unregistVehicleTimeSec = 300;

	String controlServerDeviceId;

	long gameTime = 7200;
	boolean gameTimeCountDown = true;
	
	private long missionStopSendingEventClear = 2000L;
	
	private long pollingTimeDrivinginfo = 100L;

	private long pollingTimeTrafficinfo = 500L;

	private boolean realtimePushUse = true;
	
	private boolean debugWebsocket = false;
	
	private long sendingEventClear = 2000L;
	

	boolean v2xerrorpacketsaveuse = true;
	String v2xerrorpacketsavepath = "./v2xerrorpkt";

	boolean allowToAcceptRegReqDeliverInGamePlaying;
	
	private String scityServerUrl = "";
	

	int pvdVersion=2;

	static Environment self = new Environment();

	public static Environment instance() {
		return self;
	}

	public boolean isFlagWebsocketStreamPause() {
		return flagWebsocketStreamPause;
	}

	public void setFlagWebsocketStreamPause(boolean flagWebsocketStreamPause) {
		this.flagWebsocketStreamPause = flagWebsocketStreamPause;
	}


	public boolean isVehicleLocationTrackingFromObd() {
		return vehicleLocationTrackingFromObd;
	}


	public void setVehicleLocationTrackingFromObd(boolean vehicleLocationTrackingFromObd) {
		this.vehicleLocationTrackingFromObd = vehicleLocationTrackingFromObd;
	}

	public boolean isGeoFenceUse() {
		return geoFenceUse;
	}

	public void setGeoFenceUse(boolean geoFenceUse) {
		this.geoFenceUse = geoFenceUse;
	}



	public boolean isV2xServerConnect() {
		return v2xServerConnect;
	}

	public void setV2xServerConnect(boolean v2xServerConnect) {
		this.v2xServerConnect = v2xServerConnect;
	}

	public boolean isUseServiceLayer() {
		return useServiceLayer;
	}

	public void setUseServiceLayer(boolean useServiceLayer) {
		this.useServiceLayer = useServiceLayer;
	}

	public boolean isSaveV2xRawPacket() {
		return saveV2xRawPacket;
	}

	public void setSaveV2xRawPacket(boolean saveV2xRawPacket) {
		this.saveV2xRawPacket = saveV2xRawPacket;
	}


	public boolean isUseSpatDat() {
		return useSpatDat;
	}

	public void setUseSpatDat(boolean useSpatDat) {
		this.useSpatDat = useSpatDat;
	}


	public boolean isUseTimeout() {
		return useTimeout;
	}

	public void setUseTimeout(boolean useTimeout) {
		this.useTimeout = useTimeout;
	}

	public boolean isUseUpObst() {
		return useUpObst;
	}

	public void setUseUpObst(boolean useUpObst) {
		this.useUpObst = useUpObst;
	}


	public String getGpsFomular() {
		return gpsFomular;
	}

	public void setGpsFomular(String gpsFomular) {
		this.gpsFomular = gpsFomular.toLowerCase();
	}

	public int getEventClearTime() {
		return eventClearTime;
	}

	public void setEventClearTime(int eventClearTime) {
		this.eventClearTime = eventClearTime;
	}

	public int getStopSendingEventClear() {
		return stopSendingEventClear;
	}

	public void setStopSendingEventClear(int stopSendingEventClear) {
		this.stopSendingEventClear = stopSendingEventClear;
	}

	/**
	 */
	public long getRasEventClearTime() {
		return rasEventClearTime;
	}

	/**
	 */
	public void setRasEventClearTime(long rasEventClearTime) {
		this.rasEventClearTime = rasEventClearTime;
	}

	/**
	 */
	public long getRasStopSendingEventClear() {
		return rasStopSendingEventClear;
	}

	/**
	 */
	public void setRasStopSendingEventClear(long rasStopSendingEventClear) {
		this.rasStopSendingEventClear = rasStopSendingEventClear;
	}

	public boolean isPrintSocketRcvHex() {
		return printSocketRcvHex;
	}

	public void setPrintSocketRcvHex(boolean printSocketRcvHex) {
		this.printSocketRcvHex = printSocketRcvHex;
	}

	public boolean isPrintSocketSndHex() {
		return printSocketSndHex;
	}

	public void setPrintSocketSndHex(boolean printSocketSndHex) {
		this.printSocketSndHex = printSocketSndHex;
	}

	/**
	 */
	public long getEngOffTimeSec() {
		return engOffTimeSec;
	}

	/**
	 */
	public void setEngOffTimeSec(long engOffTimeSec) {
		this.engOffTimeSec = engOffTimeSec;
	}

	/**
	 */
	public String getControlServerDeviceId() {
		return controlServerDeviceId;
	}

	/**
	 */
	public void setControlServerDeviceId(String controlServerDeviceId) {
		this.controlServerDeviceId = controlServerDeviceId;
	}
	
	
	/**
	 */
	public long getUnregistVehicleTimeSec() {
		return unregistVehicleTimeSec;
	}
	/**
	 */
	public void setUnregistVehicleTimeSec(long unregRistVehicleTimeSec) {
		this.unregistVehicleTimeSec = unregRistVehicleTimeSec;
	}


	public long getGameTime() {
		return gameTime;
	}

	/**
	 */
	public void setGameTime(long gameTime) {
		this.gameTime = gameTime;
	}


	public boolean isGameTimeCountDown() {
		return gameTimeCountDown;
	}

	public void setGameTimeCountDown(boolean gameTimeCountDown) {
		this.gameTimeCountDown = gameTimeCountDown;
	}
	
	public boolean isDebuguploadsysgamestat() {
		return debuguploadsysgamestat;
	}

	public void setDebuguploadsysgamestat(boolean debuguploadsysgamestat) {
		this.debuguploadsysgamestat = debuguploadsysgamestat;
	}
	

	public long getMissionStopSendingEventClear() {
		return missionStopSendingEventClear;
	}

	/**
	 * @param missionStopSendingEventClear the missionStopSendingEventClear to set
	 */
	public void setMissionStopSendingEventClear(long missionStopSendingEventClear) {
		this.missionStopSendingEventClear = missionStopSendingEventClear;
	}
	
	/**
	 * @return the pollingTimeDrivinginfo
	 */
	public long getPollingTimeDrivinginfo() {
		return pollingTimeDrivinginfo;
	}

	/**
	 * @param pollingTimeDrivinginfo the pollingTimeDrivinginfo to set
	 */
	public void setPollingTimeDrivinginfo(long pollingTimeDrivinginfo) {
		this.pollingTimeDrivinginfo = pollingTimeDrivinginfo;
	}

	/**
	 * @return the pollingTimeTrafficinfo
	 */
	public long getPollingTimeTrafficinfo() {
		return pollingTimeTrafficinfo;
	}

	/**
	 * @param pollingTimeTrafficinfo the pollingTimeTrafficinfo to set
	 */
	public void setPollingTimeTrafficinfo(long pollingTimeTrafficinfo) {
		this.pollingTimeTrafficinfo = pollingTimeTrafficinfo;
	}
	
	public String getV2xerrorpacketsavepath() {
		return v2xerrorpacketsavepath;
	}

	public void setV2xerrorpacketsavepath(String v2xerrorpacketsavepath) {
		this.v2xerrorpacketsavepath = v2xerrorpacketsavepath;
	}

	public boolean isV2xerrorpacketsaveuse() {
		return v2xerrorpacketsaveuse;
	}

	public void setV2xerrorpacketsaveuse(boolean v2xerrorpacketsaveuse) {
		this.v2xerrorpacketsaveuse = v2xerrorpacketsaveuse;
	}
	
	public boolean isRealtimePushUse() {
		return realtimePushUse;
	}

	public void setRealtimePushUse(boolean realtimePushUse) {
		this.realtimePushUse = realtimePushUse;
	}
	
	public boolean isDebugWebsocket() {
		return debugWebsocket;
	}

	public void setDebugWebsocket(boolean debugWebsocket) {
		this.debugWebsocket = debugWebsocket;
	}
	
	/**
	 */
	public boolean isAllowToAcceptRegReqDeliverInGamePlaying() {
		return allowToAcceptRegReqDeliverInGamePlaying;
	}

	/**
	 */
	public void setAllowToAcceptRegReqDeliverInGamePlaying(boolean allowToAcceptRegReqDeliverInGamePlaying) {
		this.allowToAcceptRegReqDeliverInGamePlaying = allowToAcceptRegReqDeliverInGamePlaying;
	}
	
	/**
	 */
	public String getScityServerUrl() {
		return scityServerUrl;
	}

	/**
	 */
	public void setScityServerUrl(String scityServerUrl) {
		this.scityServerUrl = scityServerUrl;
	}
	
	

	public int getPvdVersion() {
		return pvdVersion;
	}

	public void setPvdVersion(int pvdVersion) {
		this.pvdVersion = pvdVersion;
	}

	public void load() {
		setV2xServerConnect(AppConfig.instance().getPropertyBool("debug.useV2Xconnect", true));
		setUseServiceLayer(AppConfig.instance().getPropertyBool("debug.SVCLAYER", true));
		setSaveV2xRawPacket(AppConfig.instance().getPropertyBool("debug.SAVE_V2X_RAW_PAKECT", false));
		setUseSpatDat(AppConfig.instance().getPropertyBool("debug.useSpatDat", true));
		setUseTimeout(AppConfig.instance().getPropertyBool("debug.useTimeout", true));
		setGpsFomular(AppConfig.instance().getProperty("gps.distance.formula", "Haversine"));
		setEventClearTime(AppConfig.instance().getPropertyInt("event.cleartime", 1000));
		setStopSendingEventClear(AppConfig.instance().getPropertyInt("event.stopsending", 4000));

		setRasEventClearTime(AppConfig.instance().getPropertyInt("event.ras.cleartime", 2000));
		setRasStopSendingEventClear(AppConfig.instance().getPropertyInt("event.ras.stopsending", 3000));
		setMissionStopSendingEventClear(AppConfig.instance().getPropertyInt("event.mis.stopsending", 2000));

		setPollingTimeDrivinginfo(AppConfig.instance().getPropertyInt("websocket.pollingtime.drivinginfo", 100));

		setPollingTimeTrafficinfo(AppConfig.instance().getPropertyInt("websocket.pollingtime.trafficinfo", 500));
		
		setRealtimePushUse(AppConfig.instance().getPropertyBool("websocket.realtime.pushuse", false));
		
		setDebugWebsocket(AppConfig.instance().getPropertyBool("debug.websocket", false));
		
		
		
		setPrintSocketRcvHex(AppConfig.instance().getPropertyBool("debug.v2x.printsocketrcvhex", true));
		setPrintSocketSndHex(AppConfig.instance().getPropertyBool("debug.v2x.printsocketsndhex", true));

		setEngOffTimeSec(AppConfig.instance().getPropertyInt("engoffperiod", 10));

		setControlServerDeviceId(AppConfig.instance().getProperty("ctrlserver.deviceid", "123456"));

		setGameTime(AppConfig.instance().getPropertyInt("bgtask.use.gametimer.time", 7200));

		setGameTimeCountDown(AppConfig.instance().getPropertyBool("bgtask.use.gametimer.time.countdown", true));
		
		setDebuguploadsysgamestat(AppConfig.instance().getPropertyBool("debug.upload.sysgamestat", true));
		
		setV2xerrorpacketsavepath(AppConfig.instance().getProperty("v2x.errorpkt.save.path", v2xerrorpacketsavepath));
		setV2xerrorpacketsaveuse(AppConfig.instance().getPropertyBool("v2x.errorpkt.save.use", true));
		
		setUnregistVehicleTimeSec(AppConfig.instance().getPropertyInt("bgtask.unregistvehice.gap.min", 60)*60);	// 분단위 값이므로 초단위로 변환하여 저장한다. 	
		
		setAllowToAcceptRegReqDeliverInGamePlaying(AppConfig.instance().getPropertyBool("regreqdeliveringameplaying.accept.allow", false));
		
		setScityServerUrl(AppConfig.instance().getProperty("scity.server.url","http://localhost:5000"));
		
		
		setPvdVersion(AppConfig.instance().getPropertyInt("scity.pvd.version",2));

		logger.info("DEBUG OPTIONS: debug.useV2Xconnect:             :{}", isV2xServerConnect());
		logger.info("DEBUG OPTIONS: debug.SVCLAYER:                  :{}", isUseServiceLayer());
		logger.info("DEBUG OPTIONS: debug.SAVE_V2X_RAW_PAKECT:       :{}", isSaveV2xRawPacket());
		logger.info("DEBUG OPTIONS: debug.useSpatDat:                :{}", isUseSpatDat());
		logger.info("DEBUG OPTIONS: debug.useTimeout:                :{}", isUseTimeout());
                                                                     
		logger.info("DEBUG OPTIONS: debug.v2x.printsocketrcvhex:     :{}", isPrintSocketRcvHex());
		logger.info("DEBUG OPTIONS: debug.v2x.printsocketsndhex:     :{}", isPrintSocketSndHex());
		logger.info("DEBUG OPTIONS: debug.upload.sysgamestat:        :{}", isDebuguploadsysgamestat());
                                                                     
		logger.info("OPTIONS: gps.distance.formula:                  :[{}] ", getGpsFomular());
                                                                     
		logger.info("SETTING: event.ras.cleartime                    :{} ", getRasEventClearTime());
		logger.info("SETTING: event.ras.stopsending                  :{} ", getRasStopSendingEventClear());
		logger.info("SETTING: engoffperiod                           :{} ", getEngOffTimeSec());
		logger.info("SETTING: ctrlserver.deviceid                    :{} ", getControlServerDeviceId());
		logger.info("SETTING: bgtask.use.gametimer.time              :{} ", getGameTime());
		logger.info("SETTING: bgtask.use.gametimer.time.countdown    :{} ", isGameTimeCountDown());
		logger.info("OPTIONS: v2x.errorpkt.save.use                  :{} ", isV2xerrorpacketsaveuse());
		logger.info("SETTING: v2x.errorpkt.save.path                 :{} ", getV2xerrorpacketsavepath());
		logger.info("SETTING: bgtask.unregistvehice.gap.min          :{} ", getUnregistVehicleTimeSec());
		logger.info("SETTING: regreqdeliveringameplaying.accept.allow:{} ", isAllowToAcceptRegReqDeliverInGamePlaying());
		logger.info("SETTING: scity.server.url                       :{} ", getScityServerUrl());
		logger.info("SETTING: scity.pvd.version                      :{} ", getPvdVersion());
		
	}



	







}
