package org.todomap.o29.beans;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="ratingsum")
public class RatingSummary {
	long nrOfRatings;
	Double average;
	long nrOfAnonRatings;
	public long getNrOfAnonRatings() {
		return nrOfAnonRatings;
	}
	public void setNrOfAnonRatings(long nrOfAnonRatings) {
		this.nrOfAnonRatings = nrOfAnonRatings;
	}
	Double anonAverage;
	public Double getAnonAverage() {
		return anonAverage;
	}
	public void setAnonAverage(Double anonAverage) {
		this.anonAverage = anonAverage;
	}
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
