package net.anzix.o29.beans;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name="o29user")
@XmlRootElement(name="user")
public class User extends BaseBean {
	@Column(name="displayname")
	String displayName;
	@Column(nullable=true, length=64)
	String email;
	@OneToMany()
	List<BaseBean> bookmarks;
	@Embedded
	Coordinate homeLoc;
	@Column
	short homeZoomLevel;
	
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

	public short getHomeZoomLevel() {
		return homeZoomLevel;
	}

	public void setHomeZoomLevel(short homeZoomLevel) {
		this.homeZoomLevel = homeZoomLevel;
	}
}
