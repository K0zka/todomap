package org.todomap.alertbox;

import org.todomap.alertbox.Monitorable.StatusDescription;

public interface Notifier {
	public void start() throws Exception;
	void notify(Monitorable monitorable, StatusDescription statusDescription);
	public void stop();
}
