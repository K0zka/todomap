package net.anzix.o29.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement(name="rating")
public class Rating {
	@Id
	long id;
	
	@ManyToOne
	BaseBean bean;
	@ManyToOne
	User user;
	@Column
	short rate;
	@Column
	String comment;
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
