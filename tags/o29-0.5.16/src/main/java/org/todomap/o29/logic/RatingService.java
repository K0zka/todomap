package org.todomap.o29.logic;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.todomap.o29.beans.Rating;
import org.todomap.o29.beans.RatingReport;
import org.todomap.o29.beans.RatingSummary;


@Consumes("application/json")
@Produces("application/json")
@Path("/rating/")
public interface RatingService {
	@POST
	@Path("/add/{id}")
	void addRating(@PathParam("id") long id, Rating rating);

	@GET
	@Path("/get/{id}")
	Rating getRating(@PathParam("id") long id);
	
	@GET
	@Path("/get/{id}/sum")
	RatingSummary getRatingSummary(@PathParam("id") long id);
	
	@GET
	@Path("/get/{id}/report")
	RatingReport getRatingReport(@PathParam("id") long id);

	@GET
	@Path("/get/{id}/report")
	RatingReport getAnonRatingReport(@PathParam("id") long id);

}
