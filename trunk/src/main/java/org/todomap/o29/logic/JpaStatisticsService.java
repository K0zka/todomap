package org.todomap.o29.logic;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.support.JpaDaoSupport;
import org.todomap.geocoder.Address;
import org.todomap.o29.beans.Project;
import org.todomap.o29.beans.ProjectRssEntry;
import org.todomap.o29.beans.Todo;
import org.todomap.o29.beans.User;

public class JpaStatisticsService extends JpaDaoSupport implements
		StatisticsService {

	@Override
	public Totals getTotals() {
		return getJpaTemplate().execute(new JpaCallback<Totals>() {

			@Override
			public Totals doInJpa(EntityManager entityManager)
					throws PersistenceException {
				final Totals totals = new Totals();

				totals.setNrOfTodos(getCount(entityManager,
						"select count(t) from " + Todo.class.getName() + " t"));
				totals.setNrOfUnresolvedTodos(getCount(entityManager,
						"select count(t) from " + Todo.class.getName()
								+ " t where t.status = 'Open'"));
				totals.setNrOfProjects(getCount(entityManager,
						"select count(p) from " + Project.class.getName()
								+ " p"));
				totals.setNrOfActiveProjects(getCount(entityManager,
						"select count(p) from " + Project.class.getName()
								+ " p where p.status = 'Active'"));
				totals.setNrOfUsers(getCount(entityManager,
						"select count(u) from " + User.class.getName() + " u"));
				totals.setNrOfNewsposts(getCount(
						entityManager,
						"select count(r) from "
								+ ProjectRssEntry.class.getName() + " r"));

				return totals;
			}

			private int getCount(final EntityManager entityManager, String query) {
				final Long result = (Long) entityManager.createQuery(query)
						.getSingleResult();
				return result.intValue();
			}
		});
	}

	enum ViewLevel {
		Country, State, Town
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Area> getHeatMap(final Address address) {
		return getJpaTemplate().executeFind(new JpaCallback<List<Area>>() {

			@Override
			public List<Area> doInJpa(final EntityManager entityManager)
					throws PersistenceException {
				final ViewLevel level = getViewLevel(address);
				final StringBuilder jpaql = new StringBuilder(
						"select count(t), ");
				switch(level) {
				case Country:
					jpaql.append(" t.addr.country ");
					break;
				case State:
					jpaql.append(" t.addr.country, t.addr.state");
					break;
				case Town:
					jpaql.append(" t.addr.country, t.addr.state, t.addr.town ");
				}
				jpaql.append(" from ").append(Todo.class.getName())
						.append(" t ");
				switch(level) {
				case Country:
					jpaql.append(" ");
					break;
				case State:
					jpaql.append(" where t.addr.country = :country ");
					break;
				case Town:
					jpaql.append(" where t.addr.country = :country and state = :state");
					break;
				}
				jpaql.append(" group by ");
				switch(level) {
				case Country:
					jpaql.append(" t.addr.country ");
					break;
				case State:
					jpaql.append(" t.addr.country, t.addr.state ");
					break;
				case Town:
					jpaql.append(" t.addr.country, t.addr.state, t.addr.town ");
					break;
				}
				Query query = entityManager.createQuery(jpaql.toString());
				switch(level) {
				case State:
					query.setParameter("country", address.getCountry());
					break;
				case Town:
					query.setParameter("country", address.getCountry());
					query.setParameter("state", address.getState());
				}
				List<Object[]> resultList = query.getResultList();
				ArrayList<StatisticsService.Area> ret = new ArrayList<StatisticsService.Area>();
				for(Object[] result : resultList) {
					Area area = new Area();
					area.setSize(((Long)result[0]).intValue());
					
					switch(level) {
					case Country:
						area.setName((String)result[1]);
						break;
					case State:
						area.setName((String)result[2]);
						break;
					case Town:
						area.setName((String)result[3]);
						break;
					}
					ret.add(area);
				}
				return ret;
			}

		});
	}

	ViewLevel getViewLevel(Address address) {
		boolean townBlank = StringUtils.isBlank(address.getTown());
		boolean stateBlank = StringUtils.isBlank(address.getState());
		boolean countryBlank = StringUtils.isBlank(address.getCountry());
		if(townBlank && ! stateBlank) {
			return ViewLevel.Town;
		}
		if(stateBlank && !countryBlank) {
			return ViewLevel.State;
		}
		return ViewLevel.Country;
	}

}
