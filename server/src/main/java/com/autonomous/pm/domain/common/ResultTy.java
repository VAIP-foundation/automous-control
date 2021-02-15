package com.autonomous.pm.domain.common;

public enum ResultTy {
	ACK("Ack"), // Ack
	AUTHEN("Authentication"), // 인증
	JOIN("Join"),
	BYE("Bye"),
	CALL("Call"),
	STOP("Stop"),
	CALL_CHANGE("CallChange"),
	FLIGHT_INFO("FlightInfo"),
	DRIVING_INFO("DrivingInfo"),
	TRIP_INFO("TripInfo"),
	SENSOR_INFO("SensorInfo"),
	EVENT("Event"),
	ROUTE_INFO("RouteInfo"),
	NOTIFICATION("Notification")
	;
//	, FAILED("100") // 알수 없는 실패

	private final String type;

	private ResultTy(final String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return type.toString();
	}
}