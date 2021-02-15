package com.autonomous.pm.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.autonomous.pm.domain.structure.DrivingInfo;
import com.autonomous.pm.domain.structure.Gps;
import com.autonomous.pm.domain.structure.Location;
import com.autonomous.pm.domain.structure.mem.DrivingInfoMem;
import com.autonomous.pm.memcache.MemDB;
import com.autonomous.pm.model.Do.VVhcl;
import com.autonomous.pm.model.Dto.ResAdminPoi;

public class GpsUtil {
	public static final Logger logger = LoggerFactory.getLogger(GpsUtil.class);
	
//	@Value("${speed.kilometer.per.hour:5}")
	private static Double speedKilometerPerHour = 5d;
	
	/**
	 * 좌표간 거리(km)를 계산해준다.
	 * @param lat1
	 * @param lng1
	 * @param lat2
	 * @param lng2
	 * @return (double) kilometers
	 */
	public static double distance(double lat1, double lng1, double lat2, double lng2) {

		double earthRadius = 6371; // in kilometer, change to 3958.75 for miles output

	    double dLat = Math.toRadians(lat2-lat1);
	    double dLng = Math.toRadians(lng2-lng1);

	    double sindLat = Math.sin(dLat / 2);
	    double sindLng = Math.sin(dLng / 2);

	    double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
	        * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));

	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

	    double dist = earthRadius * c;

	    return dist; // output distance, in kilometer
	}
	
	/**
	 * 좌표배열의 각 거리(km)를 계산하여 합산한다.
	 * @param locations
	 * @return (double) kilometers
	 */
	public static double distanceByLocations(Location[] locations) {
		double totalDistance = 0;
		for ( int i=1; i<locations.length; i++ ) {
			Location before = locations[i-1];
			Location after = locations[i];
			double distance = distance(before.getLat(), before.getLng(), after.getLat(), after.getLng());
			totalDistance += distance;
		}
	    return totalDistance; // output distance, in kilometer
	}
	

	/**
	 * 거리(km)를 입력받아 소요시간을 계산한다.
	 * @param locations
	 * @return
	 */
	public static double kilometersToSeconds(double kilometers) {
		double sec = 0;
		if ( kilometers != 0) {
			sec = kilometers/speedKilometerPerHour/60/60;
		}
		return sec;
	}
	
	
	/**
	 * GPS정보를 입력 받아 가장 가까운 POI 정보를 찾는다.
	 * @param ty
	 * @return
	 */
	public static ResAdminPoi findClosestPoi(Long idV) {
		VVhcl vhcl = MemDB.VHCL.select(idV);
		ResAdminPoi closestPoi = null;
		Long idGop = vhcl.getIdGop();
		
		closestPoi = MemDB.POI.selectAll().stream()
				.filter(p->p.getIdGop() == idGop)
				.filter(p->p.getPoiTy() == 4)
				.findFirst().get();	// GPS정보가 없을 경우 충전소를 디폴트로 보냄
		
		// 최근 주행정보(GPS)가 있을 경우 해당 데이터를 기반으로 검색
		DrivingInfoMem dim = MemDB.DRV.select(idV);
		if ( dim != null && dim.getData() != null ) {
			DrivingInfo di = dim.getData();
			Gps lastGps = di.getGps();
			if ( lastGps != null ) {
				closestPoi = findClosestPoi(idGop, lastGps);
			}
		}
		return closestPoi;
	}
	public static ResAdminPoi findClosestPoi(Long idGop, Gps gps) {
		return findClosestPoi(idGop, Double.valueOf(gps.getLat().toString()), Double.valueOf(gps.getLng().toString()));
	}
	public static ResAdminPoi findClosestPoi(Long idGop, Double lat, Double lng) {
		ResAdminPoi closestPoi = new ResAdminPoi();
		double closestDistance = Double.MAX_VALUE;
		for (ResAdminPoi p : MemDB.POI.selectAll()) {
			if (p.getIdGop() == idGop) {
				double distance = distance(lat, lng, p.getLat(), p.getLng());
				if ( closestDistance > distance ) {
					closestPoi = p;
					closestDistance = distance;
				};
			}
		}
		return closestPoi;
	}
}
