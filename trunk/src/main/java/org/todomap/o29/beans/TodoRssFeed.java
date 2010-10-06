package org.todomap.o29.beans;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class TodoRssFeed extends RssFeed {
	
	@ManyToOne
	Todo todo;
}
