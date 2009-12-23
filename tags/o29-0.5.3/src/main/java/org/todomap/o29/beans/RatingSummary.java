package org.todomap.o29.beans;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="ratingsum")
public class RatingSummary {
	long nrOfRatings;
	double average;
	public long getNrOfRatings() {
		return nrOfRatings;
	}
	public void setNrOfRatings(long nrOfRatings) {
		this.nrOfRatings = nrOfRatings;
	}
	public double getAverage() {
		return average;
	}
	public void setAverage(double average) {
		this.average = average;
	}
}
