package org.todomap.prototypes.ajaxcleanup.services.timer;

import org.todomap.prototypes.ajaxcleanup.channel.Messenger;

public class TimeService {
	public TimeService(Messenger messenger) {
		super();
		this.messenger = messenger;
		thread = new TimeThread(messenger);
	}
	final Messenger messenger;
	final TimeThread thread;
	public void init() {
		thread.start();
	}
	public void shutdown() {
		thread.shutdown();
	}
}
