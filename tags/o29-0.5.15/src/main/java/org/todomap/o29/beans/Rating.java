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
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "rating", uniqueConstraints = { @UniqueConstraint(columnNames = {
		"user_id", "bean_id" }) })
@XmlRootElement(name = "rating")
public class Rating {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rating_seq")
	@SequenceGenerator(name = "rating_seq", allocationSize = 1, initialValue = 0, sequenceName = "rating_seq")
	long id;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	@ManyToOne
	@JoinColumn(name = "bean_id", nullable = false)
	BaseBean bean;
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	User user;
	@Column(nullable = false, name = "rate")
	short rate;
	@Column
	String comment;
	@Column(insertable = true, updatable = false)
	Date created = new Date();

	@XmlTransient
	public BaseBean getBean() {
		return bean;
	}

	public void setBean(BaseBean bean) {
		this.bean = bean;
	}

	@XmlTransient
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public short getRate() {
		return rate;
	}

	public void setRate(short rate) {
		this.rate = rate;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@XmlElement(name = "userid")
	@Transient
	public long getUserId() {
		return user == null ? -1 : user.getId();
	}

	@XmlElement(name = "beanid")
	@Transient
	public long getBeanId() {
		return bean == null ? -1 : bean.getId();
	}
}
