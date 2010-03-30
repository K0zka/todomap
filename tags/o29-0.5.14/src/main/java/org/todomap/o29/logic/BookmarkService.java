package org.todomap.o29.logic;

import java.util.Date;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.todomap.o29.beans.Coordinate;

@Path("/bookmarks/")
@Produces("application/json")
public interface BookmarkService {
	
	@XmlRootElement(name="bookmark")
	class Bookmark {

		Date created;
		long id;
		int version;
		String text;
		Coordinate coordinate;

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
		public int getVersion() {
			return version;
		}
		public void setVersion(int version) {
			this.version = version;
		}
		public String getText() {
			return text;
		}
		public void setText(String text) {
			this.text = text;
		}
		public Coordinate getCoordinate() {
			return coordinate;
		}
		public void setCoordinate(Coordinate coordinate) {
			this.coordinate = coordinate;
		}
		
	}
	
	@Path("/bookmark/{id}")
	@POST
	void addBookmark(@PathParam("id") long itemId);
	
	@POST
	@Path("/unbookmark/{id}")
	void removeBookmark(@PathParam("id") long itemId);

	@GET
	@Path("/")
	@XmlElementWrapper(name="bookmarks")
	List<Bookmark> bookmarks();

	@GET
	@Path("/isbookmarked/")
	public boolean isBookmarked(long todoId);
}
