package org.todomap.alertbox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.todomap.alertbox.Monitorable.Status;
import org.todomap.alertbox.Monitorable.StatusDescription;

public class Monitor {

	public Monitor(final Notifier notifier) {
		super();
		this.notifier = notifier;
	}

	final BlockingQueue<Runnable> runnables = new ArrayBlockingQueue<Runnable>(
			1024);
	final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 10,
			30, TimeUnit.SECONDS, runnables);
	final HashMap<Monitorable, StatusDescription> statuses = new HashMap<Monitorable, Monitorable.StatusDescription>();
	final List<Monitorable> monitorables = new ArrayList<Monitorable>();
	final Notifier notifier;

	public List<Monitorable> getMonitorables() {
		return monitorables;
	}

	public final StatusDescription checkFailSafe(final Monitorable monitorable) {
		try {
			return monitorable.check();
		} catch (Throwable t) {
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
		StatusDescription storedDescription = statuses.get(monitorable);
		if (notifier != null
				&& (storedDescription == null && (desc.getStatus() == Status.Fail || desc
						.getStatus() == Status.Warning))
				|| (storedDescription != null && storedDescription.getStatus() != desc
						.getStatus())) {
			notifier.notify(monitorable, desc);
		}
		statuses.put(monitorable, desc);
	}

	public void updateAllMonitors() throws InterruptedException {
		for (Monitorable monitorable : monitorables) {
			threadPoolExecutor.submit(new StatusCheckRunner(monitorable));
		}
	}

	public HashMap<Monitorable, StatusDescription> getStatuses() {
		return statuses;
	}

}
