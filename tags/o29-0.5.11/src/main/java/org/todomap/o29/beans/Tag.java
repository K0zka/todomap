package org.todomap.o29.beans;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "tag")
@Entity
public class Tag {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tag_seq")
	@SequenceGenerator(name = "tag_seq", allocationSize = 1, initialValue = 0, sequenceName = "tag_seq")
	long id;
	@Column(nullable = false, length = 2)
	String langcode;
	@Column(nullable = false, length = 64, name="tag")
	String name;

	@ManyToMany(mappedBy="tags")
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
