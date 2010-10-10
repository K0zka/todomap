package org.todomap.o29.logic.rss;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.support.JpaDaoSupport;
import org.todomap.feed.Reader;
import org.todomap.feed.beans.Channel;
import org.todomap.feed.beans.Item;
import org.todomap.feed.beans.Rss;
import org.todomap.o29.beans.RssEntry;
import org.todomap.o29.beans.RssFeed;

public class RssService extends JpaDaoSupport {

	private final static Logger logger = LoggerFactory
			.getLogger(RssService.class);

	public void updateAllRss() {
		getJpaTemplate().execute(new JpaCallback<Object>() {

			@Override
			public Object doInJpa(EntityManager manager)
					throws PersistenceException {

				@SuppressWarnings("unchecked")
				final List<RssFeed> rssFeeds = manager.createQuery(
						"select object(rss) from " + RssFeed.class.getName()
								+ " rss ").getResultList();

				for (RssFeed feed : rssFeeds) {
					updateRssFeed(feed);
				}

				return null;
			}

		});
	}

	void updateRssFeed(final RssFeed feed) {
		Reader reader = new Reader();
		Rss rss;
		try {
			rss = reader.read(feed.getFeedUrl());
			
			for (final Channel channel : rss.getChannels()) {
				for (final Item item : channel.getItems()) {
					getJpaTemplate().execute(new JpaCallback<Object>() {

						@Override
						public Object doInJpa(final EntityManager manager)
								throws PersistenceException {
							final Long cnt = (Long) manager.createQuery("select count(item) from "+RssEntry.class.getName()+" item where item.guid = :guid").setParameter("guid", item.getGuid()).getSingleResult();
							if(cnt == 0) {
								RssEntry entry = new RssEntry();
								entry.setFeed(feed);
								entry.setGuid(item.getGuid());
								entry.setLink(item.getLink());
								entry.setTitle(item.getTitle());
								manager.persist(entry);
							}
							return null;
						}
					});
				}
			}
		} catch (IOException e) {
			logger.error(
					"Could not fetch feed " + feed.getId() + " "
							+ feed.getFeedUrl(), e);
		}
	}

}
