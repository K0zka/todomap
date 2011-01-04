package org.todomap.alertbox;

import org.todomap.alertbox.Monitorable.StatusDescription;

public interface Notifier {
	void notify(StatusDescription statusDescription);
}
