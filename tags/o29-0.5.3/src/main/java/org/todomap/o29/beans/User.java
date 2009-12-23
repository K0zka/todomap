package org.todomap.o29.beans;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name="o29user")
@XmlRootElement(name="user")
public class User extends BaseBean {
	@Column(name="openidurl",nullable=false, unique=true, updatable=false,length=128)
	String openIdUrl;
	@Column(name="displayname")
	String displayName;
	@Column(nullable=true, length=64)
	String email;
	@OneToMany()
	@XmlTransient
	List<BaseBean> bookmarks;
	@Embedded
	Coordinate homeLoc;
	@Column
	Short homeZoomLevel;
	
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@XmlTransient
	public List<BaseBean> getBookmarks() {
		return bookmarks;
	}

	public void setBookmarks(List<BaseBean> bookmarks) {
		this.bookmarks = bookmarks;
	}

	public Coordinate getHomeLoc() {
		return homeLoc;
	}

	public void setHomeLoc(Coordinate homeLoc) {
		this.homeLoc = homeLoc;
	}

	public Short getHomeZoomLevel() {
		return homeZoomLevel;
	}

	public void setHomeZoomLevel(Short homeZoomLevel) {
		this.homeZoomLevel = homeZoomLevel;
	}

	public String getOpenIdUrl() {
		return openIdUrl;
	}

	public void setOpenIdUrl(String openIdUrl) {
		this.openIdUrl = openIdUrl;
	}

	@Override
	public String getName() {
		return getName() == null ? String.valueOf(getId()) : getName();
	}

}
