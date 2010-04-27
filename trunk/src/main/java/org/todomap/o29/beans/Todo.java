package org.todomap.o29.beans;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.todomap.geocoder.Address;

@Entity
@Table(name="todo")
@XmlRootElement(name="todo")
@DiscriminatorValue("todo")
public class Todo extends BaseBean implements Translatable, Locatable {
	public TodoStatus getStatus() {
		return status;
	}
	public void setStatus(TodoStatus status) {
		this.status = status;
	}
	public TodoResolution getResolution() {
		return resolution;
	}
	public void setResolution(TodoResolution resolution) {
		this.resolution = resolution;
	}

	@Embedded
	Coordinate location;
	@Column(nullable=false, name="shdsc")
	String shortDescr;
	@Column(nullable=false, name="dsc")
	@Lob
	String description;
	@Embedded
	@Column(name="address")
	Address addr;

	@Column(name="status")
	@Enumerated(EnumType.STRING)
	TodoStatus status = TodoStatus.Open;
	@Column(name="resolution", nullable=true)
	@Enumerated(EnumType.STRING)
	TodoResolution resolution;
	
	@Column(name="closedate")
	Date closeDate;
	
	public Date getCloseDate() {
		return closeDate;
	}
	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
	}
	public List<Project> getProjects() {
		return projects;
	}
	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}
	
	@ManyToMany(mappedBy="todos")
	List<Project> projects;
	
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
	public Address getAddr() {
		return addr;
	}
	public void setAddr(Address addr) {
		this.addr = addr;
	}
	@Override
	public String getText() {
		return getDescription();
	}
	public void setLanguage(String language) {
		super.setLanguage(language);
	}
	@Override
	public String getName() {
		return getShortDescr();
	}
}
