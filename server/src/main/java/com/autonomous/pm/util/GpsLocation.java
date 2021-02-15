package com.autonomous.pm.util;

public class GpsLocation {
	public double LAT = 0; // 8 byte,latitude
	public double LON = 0; // 8 byte,longitude
	
	public GpsLocation() {}
	
	public GpsLocation(double lat,double lon) {
		LAT = lat;
		LON = lon;
	}
	
	public double getLAT() {
		return LAT;
	}
	public void setLAT(double lAT) {
		LAT = lAT;
	}
	public double getLON() {
		return LON;
	}
	public void setLON(double lON) {
		LON = lON;
	}
	
	public String toString() {
		return String.format("LAT:%13.9f,LON:%13.9f", getLAT(),getLON());
	}
	
}
