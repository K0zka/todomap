package net.anzix.o29.logic;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

@Consumes("application/json")
@Path("/rating/")
public interface RatingService {
	@POST
	@Path("/add/{id}")
	void addRating(@PathParam("id") long id, @QueryParam("rating") short rating, @QueryParam("comment") String comment);
}
