package org.todomap.o29.logic;

import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.xml.bind.annotation.XmlElementWrapper;

import org.todomap.o29.beans.Comment;


@Path("/comments/")
public interface CommentService {
	@Path("/add")
	@POST
	void addComment(@FormParam("id") long id, @FormParam("text") String text);

	@GET
	@Path("/get/{id}")
	@XmlElementWrapper(name="comments")
	List<Comment> getComments(@PathParam("id") long id);
}
