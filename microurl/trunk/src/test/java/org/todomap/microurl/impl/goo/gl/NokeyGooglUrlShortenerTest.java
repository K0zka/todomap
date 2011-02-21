package org.todomap.microurl.impl.goo.gl;

import org.todomap.microurl.UrlShortener;
import org.todomap.microurl.impl.UrlShortenerChecks;

public class NokeyGooglUrlShortenerTest extends UrlShortenerChecks {
	@Override
	public UrlShortener getUrlShortener() {
		GooglUlrShortener shortener = new GooglUlrShortener();
		return shortener;
	}
}
