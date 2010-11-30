package org.todomap.prototypes.ajaxcleanup.services.timer;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.todomap.prototypes.ajaxcleanup.channel.Messenger;
import org.todomap.prototypes.ajaxcleanup.messages.UpdateMessage;

public class TimeThread extends Thread {

	public TimeThread(final Messenger messenger) {
		super("TIMER");
		this.messenger = messenger;
	}

	boolean terminating = false;
	final Messenger messenger;
	
	final static Logger logger = LoggerFactory.getLogger(TimeThread.class);

	@Override
	public void run() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		while(!terminating) {
			try {
				final Object object = new Object();
				synchronized(object) {
					object.wait(1000);
				}
				messenger.message(new UpdateMessage("time", format.format(new Date())));
			} catch (InterruptedException e) {
				logger.debug(e.getMessage(),e);
			}
		}
		
	}
	
	void shutdown() {
		terminating = true;
	}
	
}
