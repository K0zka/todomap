package org.todomap.o29.logic;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/anon/rating/")
public interface AnonimousRatingService {
	@POST
	@Path("/rate/{id}/{value}")
	public void rate(@PathParam("id") long id, @PathParam("value") short value);
}
