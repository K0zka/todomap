package org.todomap.o29.beans;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

@Entity
public class IntegrationMessage {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "integration_seq")
	@SequenceGenerator(name = "integration_seq", allocationSize = 1, initialValue = 0, sequenceName = "integration_seq")
	long id;

	@ManyToOne()
	BaseBean relatedTo;

	@Column
	Date received;
	@Column(name="sysid")
	String systemId;
	@Lob
	String message;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Date getReceived() {
		return received;
	}
	public void setReceived(Date received) {
		this.received = received;
	}
	public String getSystemId() {
		return systemId;
	}
	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

}
