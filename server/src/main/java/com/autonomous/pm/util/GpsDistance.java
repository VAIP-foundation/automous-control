package com.autonomous.pm.util;


/**
 * 
 * 두 GPS 위치의 거리를 계산하는 class
 * 
 * 계산 공식은 2가지를 선택적으로 사용할 수 있으며
 * 
 *  Vincenty, Haversine 두개 알고리즘 중 하나를 선택할 수 있다.
 * 
 * @author promaker
 *
 */
public class GpsDistance {
	public enum FormulaType {
		Vincenty, Haversine
	};

	public static FormulaType formula = FormulaType.Haversine;

	/**
	 * 
	 * 
	 * 
	 * @param lat1
	 * @param lon1
	 * @param lat2
	 * @param lon2
	 * @return
	 */
	public static Double getDistance(double lat1, double lon1, double lat2, double lon2) {
		if (formula == FormulaType.Haversine) {
			return _getDistance_Haversine(lat1, lon1, lat2, lon2);
		} else {
			return _getDistance_Vincenty(lat1, lon1, lat2, lon2);
		}
	}
	
	public static Double getDistance(GpsLocation loc1,GpsLocation loc2) {
		
		if(loc1==null||loc2==null)
			return null;
		
		if (formula == FormulaType.Haversine) {
			return _getDistance_Haversine(loc1.getLAT(), loc1.getLON(), loc2.getLAT(), loc2.getLON());
		} else {
			return _getDistance_Vincenty(loc1.getLAT(), loc1.getLON(), loc2.getLAT(), loc2.getLON());
		}
	}

	public static final double R = 6372.8; // In kilometers

	/**
	 * Haversine 방정식
	 * m 단위 거리
	 * 
	 * @param lat1
	 * @param lon1
	 * @param lat2
	 * @param lon2
	 * @return
	 */
	static double _getDistance_Haversine(double lat1, double lon1, double lat2, double lon2) {
		double dLat = Math.toRadians(lat2 - lat1);
		double dLon = Math.toRadians(lon2 - lon1);
		lat1 = Math.toRadians(lat1);
		lat2 = Math.toRadians(lat2);

		double a = Math.pow(Math.sin(dLat / 2), 2) + Math.pow(Math.sin(dLon / 2), 2) * Math.cos(lat1) * Math.cos(lat2);
		double c = 2 * Math.asin(Math.sqrt(a));
		return R * c * 1000.0;
	}

	/**
	 * 
	 * Vincenty 방정식
	 * m 단위 거리
	 * 
	 * @param lat1
	 * @param lon1
	 * @param lat2
	 * @param lon2
	 * @return
	 */
	static double _getDistance_Vincenty(double lat1, double lon1, double lat2, double lon2) {
		double a = 6378137, b = 6356752.314245, f = 1 / 298.257223563;
		double L = Math.toRadians(lon2 - lon1);
		double U1 = Math.atan((1 - f) * Math.tan(Math.toRadians(lat1)));
		double U2 = Math.atan((1 - f) * Math.tan(Math.toRadians(lat2)));
		double sinU1 = Math.sin(U1), cosU1 = Math.cos(U1);
		double sinU2 = Math.sin(U2), cosU2 = Math.cos(U2);
		double cosSqAlpha;
		double sinSigma;
		double cos2SigmaM;
		double cosSigma;
		double sigma;

		double lambda = L, lambdaP, iterLimit = 100;
		do {
			double sinLambda = Math.sin(lambda), cosLambda = Math.cos(lambda);
			sinSigma = Math.sqrt((cosU2 * sinLambda) * (cosU2 * sinLambda)
					+ (cosU1 * sinU2 - sinU1 * cosU2 * cosLambda) * (cosU1 * sinU2 - sinU1 * cosU2 * cosLambda));
			if (sinSigma == 0) {
				return 0;
			}

			cosSigma = sinU1 * sinU2 + cosU1 * cosU2 * cosLambda;
			sigma = Math.atan2(sinSigma, cosSigma);
			double sinAlpha = cosU1 * cosU2 * sinLambda / sinSigma;
			cosSqAlpha = 1 - sinAlpha * sinAlpha;
			cos2SigmaM = cosSigma - 2 * sinU1 * sinU2 / cosSqAlpha;

			double C = f / 16 * cosSqAlpha * (4 + f * (4 - 3 * cosSqAlpha));
			lambdaP = lambda;
			lambda = L + (1 - C) * f * sinAlpha
					* (sigma + C * sinSigma * (cos2SigmaM + C * cosSigma * (-1 + 2 * cos2SigmaM * cos2SigmaM)));

		} while (Math.abs(lambda - lambdaP) > 1e-12 && --iterLimit > 0);

		if (iterLimit == 0) {
			return 0;
		}

		double uSq = cosSqAlpha * (a * a - b * b) / (b * b);
		double A = 1 + uSq / 16384 * (4096 + uSq * (-768 + uSq * (320 - 175 * uSq)));
		double B = uSq / 1024 * (256 + uSq * (-128 + uSq * (74 - 47 * uSq)));
		double deltaSigma = B * sinSigma * (cos2SigmaM + B / 4 * (cosSigma * (-1 + 2 * cos2SigmaM * cos2SigmaM)
				- B / 6 * cos2SigmaM * (-3 + 4 * sinSigma * sinSigma) * (-3 + 4 * cos2SigmaM * cos2SigmaM)));

		double s = b * A * (sigma - deltaSigma);

		return s;
	}
}
