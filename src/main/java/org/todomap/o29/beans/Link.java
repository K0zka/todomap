package org.todomap.o29.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
public class Link {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "link_seq")
	@SequenceGenerator(name = "link_seq", allocationSize = 1, initialValue = 0, sequenceName = "link_seq")
	long id;
	@Column(nullable=true, name="descr")
	String desc;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@Column(nullable=false)
	String url;
}
