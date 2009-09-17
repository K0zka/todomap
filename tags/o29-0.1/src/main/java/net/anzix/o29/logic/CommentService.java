package net.anzix.o29.logic;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("/comments/")
public interface CommentService {
	@Path("add")
	@POST
	void addComment(@FormParam("id") long id, @FormParam("text") String text);
}
