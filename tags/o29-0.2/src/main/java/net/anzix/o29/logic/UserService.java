package net.anzix.o29.logic;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import net.anzix.o29.beans.User;

@Path("/users/")
public interface UserService {
	
	@GET
	@Path("/user/{id}")
	User getUserById(@PathParam("id") long id);
	
	@PUT
	@Path("/new")
	void addUser(User user);
}
