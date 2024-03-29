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

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

@Entity
@Table(name="cmnt")
@DiscriminatorValue("cmnt")
@XmlRootElement(name="comment")
@Indexed
public class Comment extends BaseBean implements Translatable {
	@ManyToOne
	@JoinColumn(name="bean_id")
	@XmlTransient
	BaseBean bean;

	@XmlTransient
	public BaseBean getBean() {
		return bean;
	}

	public void setBean(BaseBean bean) {
		this.bean = bean;
	}

	@Column
	@Lob
	@Field(store=Store.COMPRESS)
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
