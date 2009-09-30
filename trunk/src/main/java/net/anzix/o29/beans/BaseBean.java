package net.anzix.o29.beans;

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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
@DiscriminatorColumn(discriminatorType=DiscriminatorType.STRING, name="type", length=8)
@Table(name="base")
public abstract class BaseBean {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="base_seq")
	@SequenceGenerator(name="base_seq", allocationSize=1, initialValue=0, sequenceName="base_seq")
	long id;
	@OneToMany(fetch=FetchType.LAZY)
	List<Comment> comments;
	@OneToMany(fetch=FetchType.LAZY)
	List<Rating> ratings;
	
	@Transient
	RatingSummary ratingSummary;

	@Column(updatable=false, nullable=false)
	Date created = new Date();
	
	@OneToMany(fetch=FetchType.LAZY)
	List<Attachment> attachments;
	public RatingSummary getRatingSummary() {
		return ratingSummary;
	}
	public void setRatingSummary(RatingSummary ratingSummary) {
		this.ratingSummary = ratingSummary;
	}
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
	public List<Comment> getComments() {
		return comments;
	}
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	public List<Rating> getRatings() {
		return ratings;
	}
	public void setRatings(List<Rating> ratings) {
		this.ratings = ratings;
	}
}
