package org.todomap.o29.logic;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.todomap.geocoder.Address;
import org.todomap.o29.beans.Coordinate;
import org.todomap.o29.beans.Todo;

@Path("/todos/")
@Produces("application/json")
public interface TodoService {

	@XmlRootElement
	public abstract class Flag {

		Coordinate location;

		@XmlElement(name = "location")
		public Coordinate getLocation() {
			return location;
		}

		public void setLocation(Coordinate location) {
			this.location = location;
		}

	}

	@XmlRootElement(name = "group-sum")
	public class TodoGroup extends Flag {
		String name;

		int nrOfIssues;

		Address address;

		@XmlElement(name = "name")
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		@XmlElement(name = "nrOfIssues")
		public int getNrOfIssues() {
			return nrOfIssues;
		}

		public void setNrOfIssues(int nrOfIssues) {
			this.nrOfIssues = nrOfIssues;
		}

		@XmlElement(name = "address")
		public Address getAddress() {
			return address;
		}

		public void setAddress(Address address) {
			this.address = address;
		}
	}

	@XmlRootElement(name = "todo-sum")
	public class TodoSummary extends Flag {
		long id;
		String shortDescr;

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

	@GET
	@Path("/area/{level}/{nex},{ney},{swx},{swy}")
	@XmlElementWrapper(name = "groups")
	List<TodoGroup> getTodoGroupsFromArea(
			@PathParam("level") final String level,
			@PathParam("nex") double nex, @PathParam("ney") double ney,
			@PathParam("swx") double swx, @PathParam("swy") double swy);

	@POST
	@Path("/new")
	@Consumes("application/json")
	@Produces("application/json")
	Todo addTodo(Todo todo);

	@POST
	@Path("/update")
	@Consumes("application/json")
	@Produces("application/json")
	Todo saveTodo(Todo todo);

	@GET
	@Path("/all")
	@XmlElementWrapper(name = "todos")
	List<Todo> getAllTodos();

	@GET
	@Path("/byid/{id}")
	Todo getById(@PathParam("id") long id);

	@GET
	@Path("/shortbyid/{id}")
	Todo getShortTodoById(@PathParam("id") long id);

	@GET
	@Path("/byloc/{countrycode}/{state}/{town}")
	@XmlElementWrapper(name = "todos")
	List<Todo> getByLocation(@PathParam("countrycode") String countryCode,
			@PathParam("state") String state, @PathParam("town") String town);

}
