package org.todomap.o29.beans;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * External rss feed for a project.
 * 
 * @author kocka
 */
@Entity
@Table(name="projectrssfeed")
public class ProjectRssFeed extends RssFeed {

	@ManyToOne
	Project project;
	
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

}
