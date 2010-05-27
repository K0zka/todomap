package org.todomap.integrations.hitman.impl;

import java.util.ArrayList;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.StreamMessage;

import org.todomap.integrations.hitman.Destination;
import org.todomap.integrations.hitman.Filter;

public class EventListener implements MessageListener {

	List<Filter> filters = new ArrayList<Filter>();
	List<Destination> destinations = new ArrayList<Destination>();

	public void onMessage(Message message) {
		try {
			String msg = ((StreamMessage)message).readString();

			for (Filter filter : filters) {
				if(msg == null) {
					break;
				}
				msg = filter.filter(msg);
			}

			for (Destination destination : destinations) {
				destination.send(msg);
			}

		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<Filter> getFilters() {
		return filters;
	}

	public void setFilters(List<Filter> filters) {
		this.filters = filters;
	}

	public List<Destination> getDestinations() {
		return destinations;
	}

	public void setDestinations(List<Destination> destinations) {
		this.destinations = destinations;
	}

}
