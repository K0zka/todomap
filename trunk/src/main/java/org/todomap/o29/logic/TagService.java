package org.todomap.o29.logic;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.todomap.o29.beans.Tag;

@Path("/tags/")
public interface TagService {
	@GET
	@Path("/list/{lang}")
	List<Tag> listTags(@PathParam("lang") String language);
	
	@POST
	@Path("/add/{id}")
	public void addTag(@PathParam("id") long id, String tag);
}
