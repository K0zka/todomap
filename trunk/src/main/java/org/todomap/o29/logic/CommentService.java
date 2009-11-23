package org.todomap.o29.logic;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.xml.bind.annotation.XmlElementWrapper;

import org.todomap.o29.beans.Comment;


@Path("/comments/")
@Produces("application/json")
public interface CommentService {
	@Path("/add/{id}")
	@POST
	void addComment( @PathParam("id") long id, String comment);

	@GET
	@Path("/get/{id}")
	@XmlElementWrapper(name="comments")
	List<Comment> getComments(@PathParam("id") long id);
}
