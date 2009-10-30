package org.todomap.o29.logic;

import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.todomap.o29.beans.Comment;


@Path("/comments")
public interface CommentService {
	@Path("/add")
	@POST
	void addComment(@FormParam("id") long id, @FormParam("text") String text);
	
	@Path("/{id}/get/{from},{count}")
	List<Comment> getComments(@PathParam("id") long id, @PathParam("from") @DefaultValue("0") long from, @PathParam("count") @DefaultValue("10") long count);
}
