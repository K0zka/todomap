package org.todomap.o29.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.support.JpaDaoSupport;
import org.todomap.geocoder.Address;
import org.todomap.geocoder.GeoCodeException;
import org.todomap.geocoder.GeoCoder;
import org.todomap.geocoder.LatLng;
import org.todomap.o29.beans.Coordinate;
import org.todomap.o29.beans.Todo;
import org.todomap.o29.utils.HtmlUtil;

public class JpaTodoServiceImpl extends JpaDaoSupport implements TodoService {

	GeoCoder geoCoder;

	TranslatorService translatorService;

	UserService userService;

	int groupingThreshold = 6;

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public TranslatorService getTranslatorService() {
		return translatorService;
	}

	public void setTranslatorService(TranslatorService translatorService) {
		this.translatorService = translatorService;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Todo> getAllTodos() {
		return getJpaTemplate().find(
				"select a from " + Todo.class.getName() + " a");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Todo> getTodos(double northEastLng, double northEastLat,
			double southWestLng, double southWestLat) {
		// TODO: Still not correct implementation, but works until I get a GIS
		// up and running.
		return getJpaTemplate().find(
				"select a from " + Todo.class.getName() + " a "
						+ "where a.location.longitude between ? and ? "
						+ "and a.location.latitude between ? and ?",
				(double) min(southWestLng, northEastLng),
				(double) max(southWestLng, northEastLng),
				(double) min(southWestLat, northEastLat),
				(double) max(southWestLat, northEastLat));
	}

	@Override
	public Todo addTodo(final Todo todo) {
		try {
			todo.setAddr(geoCoder.revert(new LatLng(todo.getLocation()
					.getLatitude(), todo.getLocation().getLongitude())));
		} catch (GeoCodeException e) {
			logger.error("Could not reverse-geocode location");
		}
		todo.setDescription(HtmlUtil.cleanup(todo.getDescription()));
		todo.setCreator(userService.getCurrentUser());
		translatorService.updateLanguage(todo);
		todo.setCreator(userService.getCurrentUser());
		getJpaTemplate().persist(todo);
		return todo;
	}

	@Override
	public Todo getById(long id) {
		return getJpaTemplate().find(Todo.class, id);
	}

	@Override
	public List<TodoSummary> getTodoSums(final double nex, final double ney,
			final double swx, final double swy) {

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

	@SuppressWarnings("unchecked")
	TodoGroup getGroupByAddress(final Address address) {
		final TodoGroup group = new TodoGroup();

		getJpaTemplate().execute(new JpaCallback() {

			@Override
			public Object doInJpa(final EntityManager em)
					throws PersistenceException {
				final String country = address.getCountry();
				final String state = address.getState();
				final String town = address.getTown();
				// TODO: average is not a perfect method. We could just
				// use google GEO.
				final Query query = em
						.createQuery("select avg(t.location.longitude), avg(t.location.latitude), count(t) from "
								+ Todo.class.getName()
								+ " t "
								+ " where t.addr.country = :country "
								+ (state == null ? ""
										: " and t.addr.state = :state")
								+ (town == null ? ""
										: " and t.addr.town = :town "));
				query.setParameter("country", country);
				if (state != null) {
					query.setParameter("state", state);
				}
				if (town != null) {
					query.setParameter("town", town);
				}

				final Object[] result = (Object[]) query.getSingleResult();
				group.setLocation(new Coordinate((Double) result[0],
						(Double) result[1]));
				group.setNrOfIssues(((Long) result[2]).intValue());

				return null;
			}
		});
		group.setAddress(address);
		return group;
	}

	private static final Map<String, String> levels = new HashMap<String, String>();
	static {
		levels.put("country", "x.addr.country");
		levels.put("state", "x.addr.country, x.addr.state");
		levels.put("town", "x.addr.country, x.addr.state, x.addr.town");
	}

	@SuppressWarnings("unchecked")
	public List<TodoGroup> getTodoGroupsFromArea(final String level,
			final double nex, final double ney, final double swx,
			final double swy) {

		return (List<TodoGroup>) getJpaTemplate().execute(new JpaCallback() {

			@Override
			public Object doInJpa(EntityManager em) throws PersistenceException {
				final Query groupQuery = em.createQuery("select distinct "
						+ levels.get(level) + " from " + Todo.class.getName()
						+ " x where x.location.longitude between ? and ? "
						+ "and x.location.latitude between ? and ? ");
				groupQuery.setParameter(1, ney);
				groupQuery.setParameter(2, swy);
				groupQuery.setParameter(3, nex);
				groupQuery.setParameter(4, swx);

				final ArrayList<TodoGroup> ret = new ArrayList<TodoGroup>();
				final List resultList = groupQuery.getResultList();
				for (final Object resultObj : resultList) {
					final Address addr = new Address();
					buildAddress(resultObj, addr);
					ret.add(getGroupByAddress(addr));
				}

				return ret;
			}

			private void buildAddress(final Object resultObj, final Address addr) {
				if (resultObj instanceof String) {
					addr.setCountry((String) resultObj);
				} else if (resultObj instanceof Object[]) {
					addr.setCountry((String) ((Object[]) resultObj)[0]);
					addr.setState((String) ((Object[]) resultObj)[1]);
					if (((Object[]) resultObj).length > 2) {
						addr.setTown((String) ((Object[]) resultObj)[2]);
					}
				}
			}
		});

	}

	public GeoCoder getGeoCoder() {
		return geoCoder;
	}

	public void setGeoCoder(GeoCoder geoCoder) {
		this.geoCoder = geoCoder;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Todo> getByLocation(final String countryCode,
			final String state, final String town) {
		final HashMap<String, String> params = new HashMap<String, String>();
		if (state != null) {
			params.put("state", state);
		}
		params.put("country", countryCode);
		if (town != null) {
			params.put("town", state);
		}
		return getJpaTemplate()
				.findByNamedParams(
						"select todo from "
								+ Todo.class.getName()
								+ " todo where todo.addr.country = :country "
								+ (town == null ? ""
										: "and todo.addr.town = :town ")
								+ (state == null ? ""
										: "and todo.addr.state = :state ")
								+ "order by todo.created desc ", params);
	}

	static double min(final double a, final double b) {
		return a < b ? a : b;
	}

	static double max(final double a, final double b) {
		return a > b ? a : b;
	}

	public Todo saveTodo(final Todo todo) {
		final Todo byId = getById(todo.getId());
		byId.setDescription(todo.getDescription());
		if (todo.getLocation() != null) {
			byId.setLocation(todo.getLocation());
			try {
				todo.setAddr(geoCoder.revert(new LatLng(todo.getLocation()
						.getLatitude(), todo.getLocation().getLongitude())));
			} catch (GeoCodeException e) {
				logger.warn(e.getMessage(), e);
			}
		}
		translatorService.updateLanguage(byId);
		getJpaTemplate().persist(byId);
		return byId;
	}

	@Override
	public Todo getShortTodoById(final long id) {
		final Todo todo = getById(id);
		/*
		 * This long boilerplate code is actually cloning the Todo. It is no
		 * good here. TODO: addr?
		 */
		final Todo ret = new Todo();
		ret.setDescription(HtmlUtil.getFirstParagraph(todo.getText()));
		if (todo.getAddr() != null) {
			final Address addr = new Address();
			addr.setAddress(todo.getAddr().getAddress());
			addr.setCountry(todo.getAddr().getCountry());
			addr.setState(todo.getAddr().getState());
			addr.setTown(todo.getAddr().getTown());
			ret.setAddr(addr);
		}
		ret.setId(todo.getId());
		ret.setLanguage(todo.getLanguage());
		final Coordinate location = todo.getLocation();
		ret.setLocation(new Coordinate(location.getLatitude(), location
				.getLongitude()));
		ret.setVersion(todo.getVersion());
		ret.setShortDescr(todo.getShortDescr());
		return ret;
	}

}