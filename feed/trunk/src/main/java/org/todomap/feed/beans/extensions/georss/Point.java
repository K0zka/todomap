package org.todomap.feed.beans.extensions.georss;

import java.util.List;

import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="point", namespace="http://www.georss.org/georss")
public class Point {
	@XmlList()
	List<String> value;
}
