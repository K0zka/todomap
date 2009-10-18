package net.anzix.o29.logic;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import net.anzix.o29.beans.Coordinate;
import net.anzix.o29.beans.Todo;

@Path("/todos/")
@Produces("application/json")
public interface TodoService {

	@XmlRootElement(name = "todo-sum")
	public class TodoSummary {
		long id;
		String shortDescr;
		Coordinate location;

		@XmlElement(name = "id")
		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		@XmlElement(name = "descr")
		public String getShortDescr() {
			return shortDescr;
		}

		public void setShortDescr(String shortDescr) {
			this.shortDescr = shortDescr;
		}

		@XmlElement(name = "location")
		public Coordinate getLocation() {
			return location;
		}

		public void setLocation(Coordinate location) {
			this.location = location;
		}
	}

	@GET
	@Path("/area/{nex},{ney},{swx},{swy}")
	@XmlElementWrapper(name = "todos")
	List<Todo> getTodos(@PathParam("nex") double nex,
			@PathParam("ney") double ney, @PathParam("swx") double swx,
			@PathParam("swy") double swy);

	@GET
	@Path("/area.sht/{nex},{ney},{swx},{swy}")
	@XmlElementWrapper(name = "todos")
	List<TodoSummary> getTodoSums(@PathParam("nex") double nex,
			@PathParam("ney") double ney, @PathParam("swx") double swx,
			@PathParam("swy") double swy);

	@PUT
	@Path("/new")
	@Consumes("application/json")
	@Produces("application/json")
	void addTodo(Todo todo);

	@GET
	@Path("/all")
	@XmlElementWrapper(name = "todos")
	List<Todo> getAllTodos();

	@GET
	@Path("/byid/{id}")
	Todo getById(@PathParam("id") long id);

	@GET
	@Path("/byloc/{countrycode}/{state}/{town}")
	@XmlElementWrapper(name = "todos")
	List<Todo> getByLocation(@PathParam("countrycode") String countryCode,
			@PathParam("state") String state, @PathParam("town") String town);

}
