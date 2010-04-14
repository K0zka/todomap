package org.todomap.o29.beans;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

@Entity
public class AnonRating {
	@ManyToOne
	@JoinColumn(name = "bean_id", nullable = false)
	BaseBean bean;
	@Column
	String client;

	@Column(nullable = false, updatable = false)
	Date created = new Date();

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "anonrating_seq")
	@SequenceGenerator(name = "anonrating_seq", allocationSize = 1, initialValue = 0, sequenceName = "anonrating_seq")
	long id;

	@Column(nullable = false, name = "rate")
	short rate;

	@Column
	String userAgent;

	public BaseBean getBean() {
		return bean;
	}

	public String getClient() {
		return client;
	}

	public Date getCreated() {
		return created;
	}

	public long getId() {
		return id;
	}

	public short getRate() {
		return rate;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setBean(final BaseBean bean) {
		this.bean = bean;
	}

	public void setClient(final String client) {
		this.client = client;
	}

	public void setCreated(final Date created) {
		this.created = created;
	}

	public void setId(final long id) {
		this.id = id;
	}

	public void setRate(final short rate) {
		this.rate = rate;
	}

	public void setUserAgent(final String userAgent) {
		this.userAgent = userAgent;
	}

}
