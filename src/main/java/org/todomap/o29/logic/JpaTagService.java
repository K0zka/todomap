package org.todomap.o29.logic;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.support.JpaDaoSupport;
import org.todomap.o29.beans.Tag;

public class JpaTagService extends JpaDaoSupport implements TagService {

	@SuppressWarnings("unchecked")
	@Override
	public List<Tag> listTags(final String language) {
		return getJpaTemplate().executeFind(new JpaCallback<List<Tag>>() {

			@Override
			public List<Tag> doInJpa(EntityManager entityManager)
					throws PersistenceException {
				return entityManager.createQuery(
						"select object(t) from " + Tag.class.getName()
								+ " t where t.langcode = :lang").setParameter(
						"lang", language).getResultList();
			}
		});
	}

	@Override
	public void addTag(final long id, final String tag) {
		
	}
	
}
