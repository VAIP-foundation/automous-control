package com.autonomous.pm.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.autonomous.pm.config.AppConfig;

/**
 * 
 * 
 * @author promaker
 *
 */
public class GeoFence {
	
	public static final Logger logger = LoggerFactory.getLogger(GeoFence.class);

	double lat1;
	double lon1;
	double lat2;
	double lon2;

	public GeoFence(double lat1,double lon1,double lat2,double lon2) {
		this.lat1 = lat1;
		this.lon1 = lon1;
		this.lat2 = lat2;
		this.lon2 = lon2;
	}
	
	public boolean isInRect(double lat,double lon) {
		if( lat1 <= lat && lat <= lat2 && lon1 <= lon && lon <= lon2) {
			if(logger.isDebugEnabled())
				logger.debug("GeoFence.isInRect()-TRUE:[{},{}] <= [{},{}] <= [{},{}]",lat1,lon1,lat,lon,lat2,lon2);
			return true;
		}
		if(logger.isDebugEnabled())
			logger.debug("GeoFence.isInRect()-FALSE:[{},{}] <= [{},{}] <= [{},{}]",lat1,lon1,lat,lon,lat2,lon2);
		return false;
	}
	
	public double getLat1() {
		return lat1;
	}

	public void setLat1(double lat1) {
		this.lat1 = lat1;
	}

	public double getLon1() {
		return lon1;
	}

	public void setLon1(double lon1) {
		this.lon1 = lon1;
	}

	public double getLat2() {
		return lat2;
	}

	public void setLat2(double lat2) {
		this.lat2 = lat2;
	}

	public double getLon2() {
		return lon2;
	}

	public void setLon2(double lon2) {
		this.lon2 = lon2;
	}

	public static GeoFence load() {
		double lat1 = AppConfig.instance().getPropertyDouble("kcity.geofence.lat1", 37.237567d);
		double lon1 = AppConfig.instance().getPropertyDouble("kcity.geofence.lon1", 126.770957d);
		double lat2 = AppConfig.instance().getPropertyDouble("kcity.geofence.lat2", 37.248281d);
		double lon2 = AppConfig.instance().getPropertyDouble("kcity.geofence.lon2", 126.775722d);
		GeoFence gf = new GeoFence(lat1,lon1,lat2,lon2);
		return gf;
	}
}
