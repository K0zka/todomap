package org.todomap.feed.beans;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="feed")
public class Feed {
	String title;
	String subtitle;
	String updated;
	String id;
	String rights;
	String generator;
	
}
