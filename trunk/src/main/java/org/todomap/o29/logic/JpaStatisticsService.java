package org.todomap.o29.logic;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.support.JpaDaoSupport;
import org.todomap.o29.beans.Project;
import org.todomap.o29.beans.ProjectRssEntry;
import org.todomap.o29.beans.Todo;
import org.todomap.o29.beans.User;

public class JpaStatisticsService extends JpaDaoSupport implements StatisticsService {

	@Override
	public Totals getTotals() {
		return getJpaTemplate().execute(new JpaCallback<Totals>() {

			@Override
			public Totals doInJpa(EntityManager entityManager)
					throws PersistenceException {
				final Totals totals = new Totals();

				totals.setNrOfTodos(getCount(entityManager, "select count(t) from "+Todo.class.getName()+" t"));
				totals.setNrOfUnresolvedTodos(getCount(entityManager, "select count(t) from "+Todo.class.getName()+" t where t.status = 'Open'"));
				totals.setNrOfProjects(getCount(entityManager, "select count(p) from "+Project.class.getName()+" p"));
				totals.setNrOfActiveProjects(getCount(entityManager, "select count(p) from "+Project.class.getName()+" p where p.status = 'Active'"));
				totals.setNrOfUsers(getCount(entityManager, "select count(u) from "+User.class.getName()+" u"));
				totals.setNrOfNewsposts(getCount(entityManager, "select count(r) from "+ProjectRssEntry.class.getName()+" r"));

				return totals;
			}

			private int getCount(final EntityManager entityManager, String query) {
				final Long result = (Long) entityManager.createQuery(query).getSingleResult();
				return result.intValue();
			}
		});
	}
	
}
