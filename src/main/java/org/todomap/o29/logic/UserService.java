package org.todomap.o29.logic;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.todomap.o29.beans.User;


@Path("/users/")
public interface UserService {
	
	@GET
	User getCurrentUser();
	
	@GET
	@Path("/user/{id}")
	User getUserById(@PathParam("id") long id);
	
	@GET
	@Path("userbyopenid/{url}")
	User getUserByOpenIdUrl(@PathParam("url") String name);
	
	void persist(User user);
}
