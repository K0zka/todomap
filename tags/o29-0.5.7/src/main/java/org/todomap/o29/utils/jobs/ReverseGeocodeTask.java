package org.todomap.o29.utils.jobs;

import java.util.List;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.jpa.JpaTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.todomap.geocoder.GeoCodeException;
import org.todomap.geocoder.GeoCoder;
import org.todomap.geocoder.LatLng;
import org.todomap.o29.beans.Todo;

public class ReverseGeocodeTask extends TimerTask {

	private final class ReverseGeocodeTransaction extends
			TransactionCallbackWithoutResult {

		@SuppressWarnings("unchecked")
		@Override
		protected void doInTransactionWithoutResult(TransactionStatus status) {
			final List<Todo> unlocatedTodos = jpaTemplate
					.find("select OBJECT(todo) from " + Todo.class.getName()
							+ " todo where addr.country is null");
			for (final Todo todo : unlocatedTodos) {
				try {
					todo
							.setAddr(geoCoder.revert(new LatLng(todo
									.getLocation().getLatitude(), todo
									.getLocation().getLongitude())));
					jpaTemplate.persist(todo);
				} catch (GeoCodeException e) {
					logger.error("Could not reverse geocode todo: "
							+ todo.getId());
				}
			}
		}
	}

	private final static Logger logger = LoggerFactory
			.getLogger(ReverseGeocodeTask.class);

	GeoCoder geoCoder;
	JpaTemplate jpaTemplate;
	PlatformTransactionManager manager;

	@Override
	public void run() {
		logger.info("you can stop the tune but don't stop the beat");
		new TransactionTemplate(manager)
				.execute(new ReverseGeocodeTransaction());
	}

	public GeoCoder getGeoCoder() {
		return geoCoder;
	}

	public void setGeoCoder(GeoCoder geoCoder) {
		this.geoCoder = geoCoder;
	}

	public JpaTemplate getJpaTemplate() {
		return jpaTemplate;
	}

	public void setJpaTemplate(JpaTemplate jpaTemplate) {
		this.jpaTemplate = jpaTemplate;
	}

	public PlatformTransactionManager getManager() {
		return manager;
	}

	public void setManager(PlatformTransactionManager manager) {
		this.manager = manager;
	}

}
