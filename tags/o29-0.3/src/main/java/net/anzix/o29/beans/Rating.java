package net.anzix.o29.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name="rating", uniqueConstraints={@UniqueConstraint(columnNames={"user_id", "bean_id"})})
@XmlRootElement(name="rating")
public class Rating {
	@Id
	long id;
	
	@ManyToOne
	@JoinColumn(name="bean_id", nullable=false)
	@XmlTransient
	BaseBean bean;
	@ManyToOne
	@JoinColumn(name="user_id", nullable=false)
	User user;
	@Column(nullable=false, name="rate")
	short rate;
	@Column
	String comment;
	@XmlTransient
	public BaseBean getBean() {
		return bean;
	}
	public void setBean(BaseBean bean) {
		this.bean = bean;
	}
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
}
