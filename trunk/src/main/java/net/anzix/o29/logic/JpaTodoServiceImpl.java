package net.anzix.o29.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.anzix.o29.beans.Coordinate;
import net.anzix.o29.beans.Todo;

import org.springframework.orm.jpa.support.JpaDaoSupport;
import org.todomap.geocoder.GeoCodeException;
import org.todomap.geocoder.GeoCoder;
import org.todomap.geocoder.LatLng;

public class JpaTodoServiceImpl extends JpaDaoSupport implements TodoService {

	GeoCoder geoCoder;

	@SuppressWarnings("unchecked")
	@Override
	public List<Todo> getAllTodos() {
		return getJpaTemplate().find(
				"select a from " + Todo.class.getName() + " a");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Todo> getTodos(double nex, double ney, double swx, double swy) {
		// TODO: Still not correct implementation, but works until I get a GIS
		// up and running.
		return getJpaTemplate()
				.find(
						"select a from "
								+ Todo.class.getName()
								+ " a "
								+ "where a.location.longitude between ? and ? and a.location.latitude between ? and ? order by created desc",
						(double) nex, (double) swx, (double) ney, (double) swy);
	}

	@Override
	public void addTodo(final Todo todo) {
		try {
			todo.setAddress(geoCoder.revert(new LatLng(todo.getLocation()
					.getLatitude(), todo.getLocation().getLongitude())));
		} catch (GeoCodeException e) {
			logger.error("Could not reverse-geocode location");
		}
		getJpaTemplate().persist(todo);
	}

	@Override
	public Todo getById(long id) {
		return getJpaTemplate().find(Todo.class, id);
	}

	@Override
	public List<TodoSummary> getTodoSums(double nex, double ney, double swx,
			double swy) {
		final ArrayList<TodoSummary> ret = new ArrayList<TodoSummary>();
		for (final Object row : getJpaTemplate()
				.find(
						"select a.id, a.shortDescr, a.location from "
								+ Todo.class.getName()
								+ " a "
								+ "where a.location.longitude between ? and ? and a.location.latitude between ? and ?",
						(double) ney, (double) swy, (double) nex, (double) swx)) {
			final TodoSummary todoSummary = new TodoSummary();
			todoSummary.setId((Long) ((Object[]) row)[0]);
			todoSummary.setShortDescr((String) ((Object[]) row)[1]);
			todoSummary.setLocation((Coordinate) ((Object[]) row)[2]);
			ret.add(todoSummary);
		}
		return ret;
	}

	public GeoCoder getGeoCoder() {
		return geoCoder;
	}

	public void setGeoCoder(GeoCoder geoCoder) {
		this.geoCoder = geoCoder;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Todo> getByLocation(String countryCode, String state,
			String town) {
		final HashMap<String, String> params = new HashMap<String, String>();
		params.put("state", state);
		params.put("country", countryCode);
		params.put("town", state);
		return getJpaTemplate().findByNamedParams(
				"select todo from " + Todo.class.getName()
						+ " todo where todo.address.country = :country "
						+ "and todo.address.town = :town " 
						+ "and todo.address.state = :state "
						+ "order by todo.created desc ", params);
	}

}
