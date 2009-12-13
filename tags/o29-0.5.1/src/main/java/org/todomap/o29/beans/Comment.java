package org.todomap.o29.beans;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name="cmnt")
@DiscriminatorValue("cmnt")
@XmlRootElement(name="comment")
public class Comment extends BaseBean implements Translatable {
	@ManyToOne
	@JoinColumn(name="bean_id")
	@XmlTransient
	BaseBean bean;
	public BaseBean getBean() {
		return bean;
	}

	public void setBean(BaseBean bean) {
		this.bean = bean;
	}

	@Column
	@Lob
	String text;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String getName() {
		return String.valueOf(getId());
	}
}
