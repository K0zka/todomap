package org.todomap.o29.logic;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.todomap.o29.beans.Rating;


@Consumes("application/json")
@Path("/rating/")
public interface RatingService {
	@POST
	@Path("/add/{id}")
	void addRating(@PathParam("id") long id, Rating rating);

	@GET
	@Path("/get/{id}")
	Rating getRating(@PathParam("id") long id);
}
