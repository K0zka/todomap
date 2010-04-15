package org.todomap.o29.logic;

import org.apache.log4j.Logger;
import org.todomap.minigeoip.GeoipResolver;

public class GeoIpLogic {

	private final static Logger logger = Logger.getLogger(GeoIpLogic.class);

	public void init() throws Exception {
		if (updateOnStartup) {
			logger.info("Updating geoip data");
			if (updateInThread) {
				new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							resolver.update();
						} catch (final Exception e) {
							logger.warn("Could not update geoip", e);
						}
					}
				}).start();
			} else {
				resolver.update();
			}
		}
	}

	GeoipResolver resolver;
	boolean updateOnStartup;
	boolean updateInThread;

	public GeoipResolver getResolver() {
		return resolver;
	}

	public void setResolver(GeoipResolver resolver) {
		this.resolver = resolver;
	}

	public boolean isUpdateOnStartup() {
		return updateOnStartup;
	}

	public void setUpdateOnStartup(boolean updateOnStartup) {
		this.updateOnStartup = updateOnStartup;
	}

	public boolean isUpdateInThread() {
		return updateInThread;
	}

	public void setUpdateInThread(boolean updateInThread) {
		this.updateInThread = updateInThread;
	}
}
