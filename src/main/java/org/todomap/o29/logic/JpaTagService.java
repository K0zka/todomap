package org.todomap.o29.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.support.JpaDaoSupport;
import org.todomap.o29.beans.BaseBean;
import org.todomap.o29.beans.Tag;

public class JpaTagService extends JpaDaoSupport implements TagService {

	BaseService baseService;

	public BaseService getBaseService() {
		return baseService;
	}

	public void setBaseService(BaseService baseService) {
		this.baseService = baseService;
	}

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
	public void addTag(final long id, final String language, final String tag) {
		final BaseBean baseBean = baseService.getById(id);
		final Tag storedTag = getJpaTemplate().execute(new JpaCallback<Tag>() {

			@SuppressWarnings("unchecked")
			@Override
			public Tag doInJpa(final EntityManager entityManager)
					throws PersistenceException {
				final List<Tag> resultList = entityManager
						.createQuery(
								"select object(t) from "
										+ Tag.class.getName()
										+ " t where t.tag = :tag and t.langcode = :lang")
						.setParameter("tag", tag)
						.setParameter("lang", language).getResultList();
				if (resultList.isEmpty()) {
					Tag newTag = new Tag();
					newTag.setLangcode(language);
					newTag.setTag(tag);
					entityManager.persist(newTag);
					return newTag;
				} else {
					return resultList.get(0);
				}
			}
		});
		baseBean.getTags().add(storedTag);
		getJpaTemplate().persist(baseBean);
	}

	@Override
	public List<TagCloudElem> getTagCloud(final String language) {
		ArrayList<TagCloudElem> ret = new ArrayList<TagCloudElem>();
		for (final Tag tag : listTags(language)) {
			ret.add(getJpaTemplate().execute(new JpaCallback<TagCloudElem>() {

				@Override
				public TagCloudElem doInJpa(final EntityManager entityManager)
						throws PersistenceException {
					final Long result = (Long) entityManager.createQuery(
							"select count(*) from " + BaseBean.class.getName()
									+ " b where :tagid in tags").setParameter(
							"tagid", tag.getId()).getSingleResult();
					final TagCloudElem tagCloudElem = new TagCloudElem();
					tagCloudElem.setTag(tag);
					tagCloudElem.setWight(result);
					return tagCloudElem;
				}
			}));
		}
		return ret;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Tag> listTags(final String language, final String prefix) {
		final HashMap<String, String> params = new HashMap<String, String>();
		params.put("lang", language);
		params.put("tag", prefix.concat("%"));
		return getJpaTemplate().findByNamedParams(
				"select object(t) from " + Tag.class.getName()
						+ " t where t.langcode = :lang and t.tag like :tag",
				params);
	}

}
