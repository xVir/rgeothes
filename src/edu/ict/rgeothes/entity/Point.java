package edu.ict.rgeothes.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

import edu.ict.rgeothes.ApplicationContext;

public class Point extends Location {

	private double latitude;
	
	private double longitude;

	
	
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

	public Point(double latitude, double longitude) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public Point(double latitude, double longitude, Document beginDocument) {
		super(beginDocument);
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	@Override
	public String toString() {
		ToStringBuilder stringBuilder = new ToStringBuilder(this,
				ApplicationContext.getInstance().getToStringStyle());
		stringBuilder.append("latitude",latitude);
		stringBuilder.append("longitude",longitude);
		return stringBuilder.toString();
	}
	
	
}
