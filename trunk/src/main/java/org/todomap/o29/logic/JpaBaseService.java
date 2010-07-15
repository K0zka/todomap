package org.todomap.o29.logic;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.util.Version;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.support.JpaDaoSupport;
import org.todomap.o29.beans.BaseBean;
import org.todomap.o29.beans.User;

public class JpaBaseService extends JpaDaoSupport implements BaseService {

	private final Logger logger = LoggerFactory.getLogger(JpaBaseService.class);

	public void init() throws InterruptedException {
		getJpaTemplate().execute(new JpaCallback<Object>() {

			@Override
			public Object doInJpa(EntityManager manager)
					throws PersistenceException {
				FullTextEntityManager fullTextEntityManager = Search
						.getFullTextEntityManager(manager);
				try {
					fullTextEntityManager.createIndexer(BaseBean.class)
							.startAndWait();
				} catch (InterruptedException e) {
					logger.error(e.getMessage(), e);
				}
				return null;
			}
		});
	}

	UserService userService;

	@Override
	public BaseBean getById(final long id) {
		return getJpaTemplate().find(BaseBean.class, id);
	}

	@Override
	public BaseBean removebyId(long id) {
		final User currentUser = userService.getCurrentUser();
		final BaseBean bean = getById(id);
		final String openIdUrl = bean.getCreator().getOpenIdUrl();
		if (openIdUrl != null && openIdUrl.equals(currentUser.getOpenIdUrl())) {
			getJpaTemplate().remove(bean);
		}
		return bean;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Override
	public List<BaseBean> list(final String countryCode, final Date day) {
		return getJpaTemplate().execute(new JpaCallback<List<BaseBean>>() {

			@SuppressWarnings("unchecked")
			@Override
			public List<BaseBean> doInJpa(final EntityManager entityManager)
					throws PersistenceException {
				return entityManager
						.createQuery(
								"select object (b) from "
										+ BaseBean.class.getName()
										+ " b where date_trunc('day',b.created) = :day")
						.setParameter("day", day).getResultList();
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Date> listActiveDays(String countryCode) {
		return getJpaTemplate().executeFind(new JpaCallback<List<Date>>() {

			@Override
			public List<Date> doInJpa(final EntityManager entityManager)
					throws PersistenceException {
				// TODO: this runs on postgresql only
				return entityManager.createNativeQuery(
						"SELECT distinct date_trunc('day',created) from base")
						.getResultList();
			}
		});
	}

	@Override
	public List<BaseBean> search(final String freeText) {
		return getJpaTemplate().execute(new JpaCallback<List<BaseBean>>() {

			@SuppressWarnings("unchecked")
			@Override
			public List<BaseBean> doInJpa(EntityManager em)
					throws PersistenceException {
				// TODO Auto-generated method stub
				final FullTextEntityManager fullTextEntityManager = org.hibernate.search.jpa.Search
						.getFullTextEntityManager(em);
				final MultiFieldQueryParser parser = new MultiFieldQueryParser(
						Version.LUCENE_29, new String[] {"text","description","shortDescr","displayName"},
						new StandardAnalyzer(Version.LUCENE_29));
				try {
					final org.apache.lucene.search.Query query = parser
							.parse(freeText);
					javax.persistence.Query persistenceQuery = fullTextEntityManager
							.createFullTextQuery(query, BaseBean.class);
					return persistenceQuery.getResultList();
				} catch (ParseException e) {
					logger.warn(e.getMessage(), e);
				}

				return null;
			}
		});
	}

}
