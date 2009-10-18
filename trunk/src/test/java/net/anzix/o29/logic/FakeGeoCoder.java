package net.anzix.o29.logic;

import org.todomap.geocoder.Address;
import org.todomap.geocoder.GeoCodeException;
import org.todomap.geocoder.GeoCoder;
import org.todomap.geocoder.LatLng;

public class FakeGeoCoder implements GeoCoder{

	@Override
	public LatLng geocode(Address address) throws GeoCodeException {
		return null;
	}

	@Override
	public Address revert(LatLng loc) throws GeoCodeException {
		final Address address = new Address();
		address.setTown("Budapest");
		address.setState("Budapest");
		address.setCountry("HU");
		return address;
	}

}
