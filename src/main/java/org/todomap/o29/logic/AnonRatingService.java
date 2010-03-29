package org.todomap.o29.logic;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.todomap.o29.beans.RatingSummary;

@Path("/anon/rating/")
public interface AnonRatingService {
	@POST
	@Path("/rate/{id}/{value}")
	public RatingSummary addRate(@PathParam("id") long id, @PathParam("value") short value);
}
