package net.anzix.o29.logic;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.xml.bind.annotation.XmlElementWrapper;

import net.anzix.o29.beans.Todo;
import net.anzix.o29.beans.TodoCollection;

@Path("/todos/")
@Produces("application/json")
public interface TodoService {

	@GET
	@Path("/todos/{nwx},{nwy},{sex},{sey}")
	@XmlElementWrapper(name="todos")
	List<Todo> getTodos(@PathParam("nwx") long nwx,
			@PathParam("nwy") long nwy, @PathParam("sex") long sex,
			@PathParam("sey") long sey);
	
	@PUT
	@Path("/new")
	void addTodo(Todo todo);
	
	@GET
	@Path("/all")
	TodoCollection getAllTodos();

}
