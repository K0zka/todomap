package org.todomap.o29.beans;

import org.todomap.geocoder.Address;

public interface Locatable {
	Coordinate getLocation();
	Address getAddr();
}
