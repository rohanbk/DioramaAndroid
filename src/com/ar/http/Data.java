package com.ar.http;

import java.util.List;

/*!
 * @author Rohan Balakrishnan
 * \class Data
 * \brief Descriptor class for POI objects
 */
public class Data {

	private boolean morePages;
	private String nextPageKey;
	private String layar;
	private String errorString;
	private int errorCode;
	private List<POIFromObject> hotspots;

	public static class POIFromObject {
		private double lat;
		private double lon;
		private double distance;
		private String attribution;
		private int id;
		private String imageURL;
		private String line2;
		private String line3;
		private String line4;
		private String title;
		private int type;
		private float bearing;
		private int Angle_Offset;

		public int getAngle_Offset() {
			return Angle_Offset;
		}

		public void setAngle_Offset(int angleOffset) {
			Angle_Offset = angleOffset;
		}

		public void setLat(double lat) {
			this.lat = lat;
		}

		public void setLon(double lon) {
			this.lon = lon;
		}

		public void setAttribution(String attribution) {
			this.attribution = attribution;
		}

		public void setId(int id) {
			this.id = id;
		}

		public void setImageURL(String imageURL) {
			this.imageURL = imageURL;
		}

		public void setLine2(String line2) {
			this.line2 = line2;
		}

		public void setLine3(String line3) {
			this.line3 = line3;
		}

		public void setLine4(String line4) {
			this.line4 = line4;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public void setType(int type) {
			this.type = type;
		}

		public float getBearing() {
			return bearing;
		}

		public void setBearing(float bearing) {
			this.bearing = bearing;
		}

		public double getDistance() {
			return distance;
		}

		public void setDistance(double distance) {
			this.distance = distance;
		}

		public double getLat() {
			return lat;
		}

		public double getLon() {
			return lon;
		}

		public String getAttribution() {
			return attribution;
		}

		public int getId() {
			return id;
		}

		public String getImageURL() {
			return imageURL;
		}

		public String getLine2() {
			return line2;
		}

		public String getLine3() {
			return line3;
		}

		public String getLine4() {
			return line4;
		}

		public String getTitle() {
			return title;
		}

		public int getType() {
			return type;
		}

	}

	public boolean isMorePages() {
		return morePages;
	}

	public String getNextPageKey() {
		return nextPageKey;
	}

	public String getLayar() {
		return layar;
	}

	public String getErrorString() {
		return errorString;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public List<POIFromObject> getHotspots() {
		return hotspots;
	}

}