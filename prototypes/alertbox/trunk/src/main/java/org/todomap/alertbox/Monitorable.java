package org.todomap.alertbox;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

public interface Monitorable {
	enum Status {
		Ok, Warning, Fail
	}

	@XmlRootElement(name="status-description")
	class StatusDescription {
		public StatusDescription(final Status status, final String description) {
			super();
			this.status = status;
			this.description = description;
		}

		final Status status;
		final String description;
		Date lastChecked;
		Date lastFailed;

		public Status getStatus() {
			return status;
		}

		public String getDescription() {
			return description;
		}

		public Date getLastChecked() {
			return lastChecked;
		}

		public Date getLastFailed() {
			return lastFailed;
		}
	}

	public long lastChanged();
	String[] getTags();
	String getId();
	void setId(String id);
	String getDocUrl();
	String getTypeId();
	String getName();
	StatusDescription check() throws Exception;

}
