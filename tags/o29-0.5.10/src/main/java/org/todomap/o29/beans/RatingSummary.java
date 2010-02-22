package org.todomap.o29.beans;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="ratingsum")
public class RatingSummary {
	long nrOfRatings;
	Double average;
	public long getNrOfRatings() {
		return nrOfRatings;
	}
	public void setNrOfRatings(long nrOfRatings) {
		this.nrOfRatings = nrOfRatings;
	}
	public Double getAverage() {
		return average;
	}
	public void setAverage(Double average) {
		this.average = average;
	}
}
