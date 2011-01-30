package org.todomap.alertbox;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.apache.commons.io.FileUtils;
import org.todomap.alertbox.Monitorable.Status;
import org.todomap.alertbox.Monitorable.StatusDescription;

public class Monitor {

	public Monitor(final Notifier notifier, final History history) {
		super();
		this.notifier = notifier;
		this.history = history;
	}

	private static final Logger logger = Logger.getLogger(Monitor.class
			.getName());
	private static final File alertboxConfigDir = new File(
			System.getProperty("user.home"), ".alertbox");
	final BlockingQueue<Runnable> runnables = new ArrayBlockingQueue<Runnable>(
			1024);
	final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 10,
			30, TimeUnit.SECONDS, runnables);
	final HashMap<Monitorable, StatusDescription> statuses = new HashMap<Monitorable, Monitorable.StatusDescription>();
	final List<Monitorable> monitorables = new ArrayList<Monitorable>();
	final Notifier notifier;
	final History history;

	public List<Monitorable> getMonitorables() {
		refreshMonitorables();
		return monitorables;
	}

	private void refreshMonitorables() {
		final Collection<File> configFiles = FileUtils.listFiles(new File(
				alertboxConfigDir, "resources"), new String[] { "xml" }, true);
		final ArrayList<Monitorable> tobeRemoved = new ArrayList<Monitorable>();
		for (final File file : configFiles) {
			final String idFromFilename = idFromFilename(file);
			final long lastModified = file.lastModified();
			final Monitorable monitorable = findMonitorable(idFromFilename);
			if (monitorable == null) {
				//add monitorable
				addNewMonitorable(file, idFromFilename);
			} else if (lastModified > monitorable.lastChanged()){
				//replace
				try {
					monitorables.add(createMonitorable(file, idFromFilename));
					monitorables.remove(monitorable);
					statuses.remove(monitorable);
				} catch (JAXBException e) {
					logger.warning(e.getMessage());
				}
			}

		}
		for (final Monitorable mon : monitorables) {
			boolean found = false;
			for (final File file : configFiles) {
				if (mon.getId().equals(idFromFilename(file))) {
					found = true;
					break;
				}
			}
			if (!found) {
				tobeRemoved.add(mon);
			}
		}
		monitorables.removeAll(tobeRemoved);
	}

	private String idFromFilename(final File file) {
		return file.getAbsolutePath().replaceAll(File.separator, "_");
	}

	private Monitorable findMonitorable(final String idFromFilename) {
		for (final Monitorable mon : monitorables) {
			if (idFromFilename.equals(mon.getId())) {
				return mon;
			}
		}
		return null;
	}

	private void addNewMonitorable(File file, String idFromFilename) {
		try {
			final Monitorable monitorable = createMonitorable(file,
					idFromFilename);
			monitorables.add(monitorable);
			history.recordResourceMonitorStarted(monitorable);
		} catch (JAXBException e) {
			logger.warning(e.getMessage());
		}
	}

	private Monitorable createMonitorable(File file, String idFromFilename)
			throws JAXBException {
		final JAXBContext context = JAXBContext
				.newInstance("org.todomap.alertbox.resources");
		final Monitorable monitorable = (Monitorable) context
				.createUnmarshaller().unmarshal(file);

		monitorable.setId(idFromFilename);
		return monitorable;
	}

	public final StatusDescription checkFailSafe(final Monitorable monitorable) {
		try {
			return monitorable.check();
		} catch (Throwable t) {
			logger.fine(t.getMessage());
			return new StatusDescription(Status.Fail, t.getMessage());
		}
	}

	private class StatusCheckRunner implements Runnable {

		public StatusCheckRunner(final Monitorable monitorable) {
			super();
			this.monitorable = monitorable;
		}

		final Monitorable monitorable;

		@Override
		public void run() {
			StatusDescription result = checkFailSafe(monitorable);
			updateMonitorStatus(monitorable, result);
		}

	}

	final public void updateMonitorStatus(final Monitorable monitorable,
			final StatusDescription desc) {
		desc.lastFailed = history.getLastFail(monitorable);
		StatusDescription storedDescription = statuses.get(monitorable);
		if (notifier != null
				&& (storedDescription == null && (desc.getStatus() == Status.Fail || desc
						.getStatus() == Status.Warning))
				|| (storedDescription != null && storedDescription.getStatus() != desc
						.getStatus())) {
			notifier.notify(monitorable, desc);
			if (desc.getStatus() == Status.Ok) {
				history.recordOutageEnd(monitorable);
			} else {
				history.recordOutageStart(monitorable, desc);
			}
		}
		statuses.put(monitorable, desc);
	}

	public void updateAllMonitors() throws InterruptedException {
		for (Monitorable monitorable : getMonitorables()) {
			threadPoolExecutor.submit(new StatusCheckRunner(monitorable));
		}
	}

	public HashMap<Monitorable, StatusDescription> getStatuses() {
		return statuses;
	}

}
