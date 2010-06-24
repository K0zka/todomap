package org.todomap.o29.logic;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.todomap.o29.beans.Link;
import org.todomap.o29.beans.User;


@Path("/users/")
public interface UserService {
	
	@GET
	User getCurrentUser();
	
	@GET
	@Path("/user/{id}")
	User getUserById(@PathParam("id") long id);
	
	@POST
	@Path("/user/link/add")
	long addUserLink(Link link);
	
	@POST
	@Path("/user/link/remove")
	long removeLink(long id);

	@GET
	@Path("/user/link/list")
	List<Link> listUserLinks();

	@GET
	@Path("userbyopenid/{url}")
	User getUserByOpenIdUrl(@PathParam("url") String name);
	
	void persist(User user);
}
