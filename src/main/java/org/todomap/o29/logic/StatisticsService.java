package org.todomap.o29.logic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.xml.bind.annotation.XmlRootElement;

import org.todomap.geocoder.Address;

@Path("/statistics/")
public interface StatisticsService {
	@XmlRootElement(name="totals")
	class Totals implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 6607167688265633780L;
		int nrOfUsers;
		int nrOfProjects;
		int nrOfActiveProjects;
		public int getNrOfUsers() {
			return nrOfUsers;
		}
		public void setNrOfUsers(int nrOfUsers) {
			this.nrOfUsers = nrOfUsers;
		}
		public int getNrOfProjects() {
			return nrOfProjects;
		}
		public void setNrOfProjects(int nrOfProjects) {
			this.nrOfProjects = nrOfProjects;
		}
		public int getNrOfActiveProjects() {
			return nrOfActiveProjects;
		}
		public void setNrOfActiveProjects(int nrOfActiveProjects) {
			this.nrOfActiveProjects = nrOfActiveProjects;
		}
		public int getNrOfNewsposts() {
			return nrOfNewsposts;
		}
		public void setNrOfNewsposts(int nrOfNewsposts) {
			this.nrOfNewsposts = nrOfNewsposts;
		}
		public int getNrOfTodos() {
			return nrOfTodos;
		}
		public void setNrOfTodos(int nrOfTodos) {
			this.nrOfTodos = nrOfTodos;
		}
		public int getNrOfUnresolvedTodos() {
			return nrOfUnresolvedTodos;
		}
		public void setNrOfUnresolvedTodos(int nrOfUnresolvedTodos) {
			this.nrOfUnresolvedTodos = nrOfUnresolvedTodos;
		}
		int nrOfNewsposts;
		int nrOfTodos;
		int nrOfUnresolvedTodos;
	}

	@XmlRootElement(name="area")
	class Area {
		String name;
		int size;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getSize() {
			return size;
		}
		public void setSize(int size) {
			this.size = size;
		}
		public List<Area> getSubAreas() {
			return subAreas;
		}
		public void setSubAreas(List<Area> subAreas) {
			this.subAreas = subAreas;
		}
		List<Area> subAreas = new ArrayList<StatisticsService.Area>();
	}
	
	@GET
	@Path("/totals")
	public Totals getTotals();

	@POST
	@Path("/heatmap")
	public List<Area> getHeatMap(Address address);

}
