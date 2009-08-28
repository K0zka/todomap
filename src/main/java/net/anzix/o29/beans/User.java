package net.anzix.o29.beans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name="o29user")
@XmlRootElement(name="user")
public class User extends BaseBean {
	String displayName;
	@Column(nullable=true, length=64)
	String email;

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
}
