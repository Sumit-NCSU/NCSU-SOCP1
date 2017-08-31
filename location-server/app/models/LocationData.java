package models;

/**
 * @author sriva
 *
 */
public class LocationData {

	private String name;
	private long timestamp;
	private double latitude;
	private double longitude;
	private double distance;

	public LocationData() {
	}

	public LocationData(String name, long timestamp, double latitude, double longitude, double distance) {
		this.name = name;
		this.timestamp = timestamp;
		this.latitude = latitude;
		this.longitude = longitude;
		this.distance = distance;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	public double getDistance() {
		return distance;
	}
	
	public void setDistance(double distance) {
		this.distance = distance;
	}

	@Override
	public String toString() {
		return "LocationData [name=" + name + ", timestamp=" + timestamp + ", latitude=" + latitude + ", longitude="
				+ longitude + ", distance=" + distance + "]";
	}
}
