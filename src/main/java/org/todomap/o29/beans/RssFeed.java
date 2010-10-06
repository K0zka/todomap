package org.todomap.o29.beans;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="rssfeed")
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING, name = "type", length = 8)
@Inheritance(strategy = InheritanceType.JOINED)
public class RssFeed extends BaseBean {

	@Override
	public String getName() {
		return null;
	}
	
	public String getFeedUrl() {
		return feedUrl;
	}

	public void setFeedUrl(String feedUrl) {
		this.feedUrl = feedUrl;
	}

	public List<RssEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<RssEntry> entries) {
		this.entries = entries;
	}

	@Column
	String feedUrl;

	@OneToMany(mappedBy="feed")
	List<RssEntry> entries;

}
