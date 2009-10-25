package net.anzix.o29.beans;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.todomap.geocoder.Address;

@Entity
@Table(name="todo")
@XmlRootElement(name="todo")
@DiscriminatorValue("todo")
public class Todo extends BaseBean {
	@Embedded
	Coordinate location;
	@Column(nullable=false, name="shdsc")
	String shortDescr;
	@Column(nullable=false, name="dsc")
	@Lob
	String description;
	@Embedded
	Address address;

	public Coordinate getLocation() {
		return location;
	}
	public void setLocation(Coordinate location) {
		this.location = location;
	}
	public String getShortDescr() {
		return shortDescr;
	}
	public void setShortDescr(String shortDescr) {
		this.shortDescr = shortDescr;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
}