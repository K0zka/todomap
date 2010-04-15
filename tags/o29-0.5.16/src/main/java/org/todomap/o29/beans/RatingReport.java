package org.todomap.o29.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ratingReport")
public class RatingReport {

	@XmlRootElement(name = "item")
	public static class RatingReportItem {
		short value;
		public short getValue() {
			return value;
		}
		public void setValue(short value) {
			this.value = value;
		}
		public long getCount() {
			return count;
		}
		public void setCount(long count) {
			this.count = count;
		}
		long count;
	}

	List<RatingReportItem> items = new ArrayList<RatingReportItem>();

	public List<RatingReportItem> getItems() {
		return items;
	}
}
