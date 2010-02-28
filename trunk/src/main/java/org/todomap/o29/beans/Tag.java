package org.todomap.o29.beans;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "tag")
@Entity
public class Tag {
	@Id
	@GeneratedValue
	long id;
	@Column(nullable = false, length = 2)
	String langcode;
	@Column(nullable = false, length = 64)
	String tag;

	@ManyToMany
	List<BaseBean> taggedBeans;

	@XmlTransient
	public List<BaseBean> getTaggedBeans() {
		return taggedBeans;
	}

	public void setTaggedBeans(List<BaseBean> taggedBeans) {
		this.taggedBeans = taggedBeans;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLangcode() {
		return langcode;
	}

	public void setLangcode(String langcode) {
		this.langcode = langcode;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
}
