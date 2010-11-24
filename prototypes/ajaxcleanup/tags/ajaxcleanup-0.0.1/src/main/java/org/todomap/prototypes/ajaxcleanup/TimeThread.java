package org.todomap.prototypes.ajaxcleanup;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.eclipse.jetty.websocket.WebSocket.Outbound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeThread extends Thread {

	final ArrayList<Outbound> outbounds = new ArrayList<Outbound>();
	
	final static Logger logger = LoggerFactory.getLogger(TimeThread.class);
	
	@Override
	public void run() {
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		while(true) {
			try {
				final Object object = new Object();
				synchronized(object) {
					object.wait(1000);
				}
			} catch (InterruptedException e) {
				logger.debug(e.getMessage(),e);
			}
			final String dateStr = sdf.format(new Date());
			for(final Outbound outbound : outbounds) {
				try {
					outbound.sendMessage("<update channel=\"time\">"+dateStr+"</update>");
				} catch (IOException e) {
					logger.error(e.getMessage(),e);
				}
			}
		}
		
	}
	
	public void add(final Outbound outbound) {
		outbounds.add(outbound);
	}
	
	public void remove(final Outbound outbound) {
		outbounds.remove(outbound);
	}
	
}
