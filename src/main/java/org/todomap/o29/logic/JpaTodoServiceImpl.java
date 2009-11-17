package org.todomap.o29.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import org.springframework.orm.jpa.support.JpaDaoSupport;
import org.todomap.geocoder.GeoCodeException;
import org.todomap.geocoder.GeoCoder;
import org.todomap.geocoder.LatLng;
import org.todomap.o29.beans.Coordinate;
import org.todomap.o29.beans.Todo;

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
	public List<Todo> getTodos(double northEastLng, double northEastLat, double southWestLng, double southWestLat) {
		// TODO: Still not correct implementation, but works until I get a GIS
		// up and running.
		return getJpaTemplate().find(
				"select a from " + Todo.class.getName() + " a "
						+ "where a.location.longitude between ? and ? "
						+ "and a.location.latitude between ? and ?",
				(double) min(southWestLng, northEastLng), (double) max(southWestLng, northEastLng),
				(double) min(southWestLat, northEastLat), (double) max(southWestLat, northEastLat)
				);
	}

	@Override
	public void addTodo(final Todo todo) {
		try {
			todo.setAddr(geoCoder.revert(new LatLng(todo.getLocation()
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
						+ " todo where todo.addr.country = :country "
						+ "and todo.addr.town = :town "
						+ "and todo.addr.state = :state "
						+ "order by todo.created desc ", params);
	}

	static double min(final double a, final double b) {
		return a < b ? a : b;
	}

	static double max(final double a, final double b) {
		return a > b ? a : b;
	}

	@Override
	public void saveTodo(final Todo todo) {
		Todo byId = getById(todo.getId());
		byId.setDescription(todo.getDescription());
		if(todo.getLocation() != null) {
			byId.setLocation(todo.getLocation());
			try {
				todo.setAddr(geoCoder.revert(new LatLng(todo.getLocation().getLatitude(), todo.getLocation().getLongitude())));
			} catch (GeoCodeException e) {
				logger.warn(e.getMessage(), e);
			}
		}
		getJpaTemplate().persist(byId);
	}

}
