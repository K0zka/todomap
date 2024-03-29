package org.todomap.o29.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Store;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING, name = "type", length = 8)
@Table(name = "base")
@XmlRootElement(name = "base")
@Indexed
public abstract class BaseBean implements Nameable {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "base_seq")
	@SequenceGenerator(name = "base_seq", allocationSize = 1, initialValue = 0, sequenceName = "base_seq")
	@DocumentId
	long id;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "bean")
	List<Comment> comments;
	@OneToMany(fetch = FetchType.LAZY)
	List<Rating> ratings;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "o29user_base", 
			joinColumns = @JoinColumn(referencedColumnName = "ID", name = "bookmarks_id"), 
			inverseJoinColumns = @JoinColumn(name = "o29user_id"))
	List<User> listeners = new ArrayList<User>();

	public List<User> getListeners() {
		return listeners;
	}

	public void setListeners(List<User> listeners) {
		this.listeners = listeners;
	}

	public List<IntegrationMessage> getMessages() {
		return messages;
	}

	public void setMessages(List<IntegrationMessage> messages) {
		this.messages = messages;
	}

	@ManyToMany()
	@JoinTable(name = "base_tag", joinColumns = @JoinColumn(name = "base_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
	@IndexedEmbedded
	List<Tag> tags;

	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	@Column(updatable = false, nullable = false)
	@Field(store = Store.YES)
	Date created = new Date();

	@Version
	@Column(nullable = false)
	int version = 0;

	@ManyToOne
	User creator;

	@Column
	@Field
	String language;

	@Column()
	Boolean spamChecked;

	@OneToMany
	List<IntegrationMessage> messages;

	public Boolean getSpamChecked() {
		return spamChecked;
	}

	public void setSpamChecked(Boolean spamChecked) {
		this.spamChecked = spamChecked;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "attachedTo")
	List<Attachment> attachments;

	@XmlTransient
	public List<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@XmlTransient
	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	@XmlTransient
	public List<Rating> getRatings() {
		return ratings;
	}

	public void setRatings(List<Rating> ratings) {
		this.ratings = ratings;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@XmlTransient
	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
}
