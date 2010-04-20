package org.todomap.o29.beans;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * External rss feed for a project.
 * 
 * @author kocka
 */
@Entity
@Table(name="rssfeed")
public class ProjectRssFeed {

	@Id
	long id;
	
	@Column
	String feedUrl;

	@OneToMany(mappedBy="feed")
	List<ProjectRssEntry> entries;

	@ManyToOne
	Project project;
	
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFeedUrl() {
		return feedUrl;
	}

	public void setFeedUrl(String feedUrl) {
		this.feedUrl = feedUrl;
	}

	public List<ProjectRssEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<ProjectRssEntry> entries) {
		this.entries = entries;
	}
}
