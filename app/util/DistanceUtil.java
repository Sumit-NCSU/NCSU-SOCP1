package util;

/**
 * code referenced for calculating distance: https://github.com/jasonwinn/haversine/blob/master/Haversine.java
 *
 */
public class DistanceUtil {

	private static final int EARTH_RADIUS = 6371; // Km

	public static double findDistance(double startLat, double startLong, double endLat, double endLong) {
		double dLat = Math.toRadians((endLat - startLat));
		double dLong = Math.toRadians((endLong - startLong));

		startLat = Math.toRadians(startLat);
		endLat = Math.toRadians(endLat);

		double a = Math.pow(Math.sin(dLat / 2), 2)
				+ Math.cos(startLat) * Math.cos(endLat) * Math.pow(Math.sin(dLong / 2), 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		return EARTH_RADIUS * c;
	}

}
