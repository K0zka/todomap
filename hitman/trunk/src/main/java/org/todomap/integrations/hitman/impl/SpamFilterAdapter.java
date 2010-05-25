package org.todomap.integrations.hitman.impl;

import org.springframework.jms.core.JmsTemplate;
import org.todomap.integrations.hitman.Filter;
import org.todomap.spamegg.Content;
import org.todomap.spamegg.SpamFilter;
import org.todomap.spamegg.SpamFilterException;
import org.todomap.spamegg.User;

public class SpamFilterAdapter implements Filter {

	JmsTemplate senderTemplate;
	SpamFilter filter;

	public SpamFilter getFilter() {
		return filter;
	}

	public void setFilter(SpamFilter filter) {
		this.filter = filter;
	}

	public String filter(String message) {
		try {
			if(filter.isSpam(new Content(new User("", "", "", "", ""), "", "", "comment", message))) {
				return null;
			} else {
				return message;
			}
		} catch (SpamFilterException e) {
			throw new RuntimeException(e);
		}
	}

}
