package org.todomap.o29.utils.security;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.support.JpaDaoSupport;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

public class JpaPersistentTokenRepository extends JpaDaoSupport implements
		PersistentTokenRepository {

	private final static Logger logger = LoggerFactory
			.getLogger(JpaPersistentTokenRepository.class);

	@Override
	public void createNewToken(PersistentRememberMeToken token) {
		RememberMeToken persistentToken = new RememberMeToken();
		persistentToken.setDate(token.getDate());
		persistentToken.setSeries(token.getSeries());
		persistentToken.setTokenValue(token.getTokenValue());
		persistentToken.setUsername(token.getUsername());
		getJpaTemplate().persist(persistentToken);
	}

	@Override
	public PersistentRememberMeToken getTokenForSeries(String seriesId) {
		RememberMeToken persistentToken = getJpaTemplate().find(
				RememberMeToken.class, seriesId);
		return new PersistentRememberMeToken(persistentToken.getUsername(),
				persistentToken.getSeries(), persistentToken.getTokenValue(),
				persistentToken.getDate());
	}

	@Override
	public void removeUserTokens(final String username) {
		getJpaTemplate().execute(new JpaCallback<Object>() {

			@Override
			public Object doInJpa(EntityManager entityManager)
					throws PersistenceException {
				int updated = entityManager.createQuery(
						"delete from " + RememberMeToken.class.getName()
								+ " where username = :username").setParameter(
						"username", username).executeUpdate();
				logger.debug("User " + username + " removed " + updated
						+ " tokens");
				return null;
			}
		});
	}

	@Override
	public void updateToken(final String series, final String tokenValue,
			final Date lastUsed) {
		final RememberMeToken persistentToken = getJpaTemplate().find(
				RememberMeToken.class, series);
		persistentToken.setTokenValue(tokenValue);
		persistentToken.setDate(lastUsed);
		getJpaTemplate().persist(persistentToken);
	}

}
