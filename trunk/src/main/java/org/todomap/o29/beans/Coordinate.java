package org.todomap.o29.beans;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.xml.bind.annotation.XmlRootElement;

@Embeddable
@XmlRootElement(name="coordinate")
public class Coordinate {
	public Coordinate() {
		super();
	}
	public Coordinate(long longitude, long latitude) {
		super();
		this.longitude = longitude;
		this.latitude = latitude;
	}
	@Column
	double longitude;
	@Column
	double latitude;
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
}
