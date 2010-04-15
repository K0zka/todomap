package org.todomap.o29.logic;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.support.JpaDaoSupport;
import org.todomap.o29.beans.Project;

public class JpaProjectService extends JpaDaoSupport implements ProjectService {

	UserService userService;
	
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Override
	public long add(Project project) {
		getJpaTemplate().persist(project);
		return project.getId();
	}

	@Override
	public Project getById(final long id) {
		return getJpaTemplate().execute(new JpaCallback<Project>() {

			@Override
			public Project doInJpa(EntityManager entityManager)
					throws PersistenceException {
				return entityManager.find(Project.class, id);
			}
		});
	}

	@Override
	public void update(Project project) {
		getJpaTemplate().persist(project);
	}

}
