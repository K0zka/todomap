package org.todomap.o29.beans;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.search.annotations.Field;

@Entity
@XmlRootElement(name = "project")
@DiscriminatorValue("project")
public class Project extends BaseBean {

	@Column(name = "name", nullable = false, length = 128)
	@Field
	String name;

	public List<Todo> getTodos() {
		return todos;
	}

	public void setTodos(List<Todo> todos) {
		this.todos = todos;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ManyToMany()
	@JoinTable(name = "project_todo", joinColumns = { @JoinColumn(name = "todo_id", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(name = "project_id", referencedColumnName = "id") })
	List<Todo> todos;

	@Lob
	@Column(name = "descr")
	String description;

	@Override
	@Field
	public String getName() {
		return name;
	}

	public ProjectStatus getStatus() {
		return status;
	}

	public void setStatus(ProjectStatus status) {
		this.status = status;
	}

	@Enumerated(EnumType.STRING)
	@Column(name="status")
	@Field
	ProjectStatus status;

	@OneToMany(mappedBy="project")
	List<ProjectRssFeed> rssfeeds;

	public List<ProjectRssFeed> getRssfeeds() {
		return rssfeeds;
	}

	public void setRssfeeds(List<ProjectRssFeed> rssfeeds) {
		this.rssfeeds = rssfeeds;
	}

}
