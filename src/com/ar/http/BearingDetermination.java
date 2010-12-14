package com.ar.http;
/*!
* @author Rohan Balakrishnan
* \class BearingDetermination
* \brief Deal with calculation of Bearing and Distance
*/
public class BearingDetermination {
	public static double RadToDeg(double radians) {
		return radians * (180 / Math.PI);
	}

	public static double DegToRad(double degrees) {
		return degrees * (Math.PI / 180);
	}

	/*
	 * 
	 * @param lat1 Latitude of the user
	 * @param lon1 Longitude of the user
	 * @param lat2 Latitude of the POI
	 * @param lon2 Longitude of the POI
	 * 
	 * @return bearing Bearing in degrees clock-wise from true-North.
	 * 
	 * \brief Calculate bearing of POI from user position 
	 */
	public static double Bearing(double lat1, double long1, double lat2,
			double long2) {
		// Convert input values to radians
		lat1 = DegToRad(lat1);
		long1 = DegToRad(long1);
		lat2 = DegToRad(lat2);
		long2 = DegToRad(long2);

		double deltaLong = long2 - long1;

		double y = Math.sin(deltaLong) * Math.cos(lat2);
		double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
				* Math.cos(lat2) * Math.cos(deltaLong);
		double bearing = Math.atan2(y, x);
		return ConvertToBearing(RadToDeg(bearing));
	}
	/*
	 * 
	 * @param lat1 Latitude of the user
	 * @param lon1 Longitude of the user
	 * @param lat2 Latitude of the POI
	 * @param lon2 Longitude of the POI
	 * 
	 * @return distance Distance in meters
	 * 
	 * \brief Calculate distance of POI from user position 
	 */
	public static double Distance(double lat1, double lon1, double lat2,
			double lon2){

		double distance=6371*1000 * Math.acos( Math.cos( DegToRad(lat1) ) * Math.cos( DegToRad( lat2 ) ) * Math.cos( DegToRad( lon2 ) - DegToRad(lon1) ) + Math.sin( DegToRad(lat1) ) * Math.sin( DegToRad( lat2 ) ) );		
		return distance;
	}
	
	/*
	 * 
	 * @param degree Current degree bearing
	 * 
	 * @return bearing
	 * 
	 * \brief Convert degrees to bearing
	 */
	public static double ConvertToBearing(double deg) {
		return (deg + 360) % 360;
	}
}
