package org.todomap.microurl.impl.is.gd;

import org.todomap.microurl.UrlShortener;
import org.todomap.microurl.impl.UrlShortenerChecks;

public class IsgdUrlShortenerTest extends UrlShortenerChecks {
	@Override
	public UrlShortener getUrlShortener() {
		return new IsgdUrlShortener();
	}
}
