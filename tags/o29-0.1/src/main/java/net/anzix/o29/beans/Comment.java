package net.anzix.o29.beans;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name="cmnt")
@DiscriminatorValue("cmnt")
@XmlRootElement(name="comment")
public class Comment extends BaseBean {
	@ManyToOne
	BaseBean bean;
	@Column
	String text;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
