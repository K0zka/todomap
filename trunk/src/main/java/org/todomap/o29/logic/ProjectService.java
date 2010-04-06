package org.todomap.o29.logic;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.todomap.o29.beans.Project;

@Path("/project/")
public interface ProjectService {
	@POST
	@Path("/add")
	public long add(Project project);
	@POST
	@Path("/update")
	public void update(Project project);
	@GET
	@Path("/get/{id}")
	public Project getById(long id);
	
}
