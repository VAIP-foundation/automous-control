package com.autonomous.pm.domain.structure.push;

public class PushObject {
	public enum Type {
		DRVINFO("DrivingInfo"),
		SENSORINFO("SensorInfo"),
		TRIPINFO("TripInfo"),
		EVENT("Event"),
		ROUTEINFO("RouteInfo");
		
		private final String code;
		private Type(final String code) {
			this.code = code;
		}
		public String toString() {
			return code.toString();
		}
	}

	private String type;
	private Object data;

	public String getType() {
		return this.type;
	}
	public void setType(String t) {
		this.type = t;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
	public PushObject(Type t, Object o) {
		this.type = t.toString();
		this.data = o;
	}

}
