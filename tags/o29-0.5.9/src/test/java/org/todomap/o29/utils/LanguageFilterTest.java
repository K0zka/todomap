package org.todomap.o29.utils;

import java.util.Locale;

import javax.servlet.ServletException;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

public class LanguageFilterTest {

	@Test
	public void testGetLocaleFromRequest() throws ServletException {
		LanguageFilter filter = new LanguageFilter();
		filter.init(null);
		Assert.assertEquals(new Locale("en","UK"),filter.getLocaleFromRequest(makeRequest("en.example.com")));
		Assert.assertEquals(new Locale("hu","HU"),filter.getLocaleFromRequest(makeRequest("hu.example.com")));
		Assert.assertEquals(new Locale("en","UK"),filter.getLocaleFromRequest(makeRequest("example.com")));
		Assert.assertEquals(new Locale("en","UK"),filter.getLocaleFromRequest(makeRequest("localhost")));
	}

	private MockHttpServletRequest makeRequest(String serverName) {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setServerName(serverName);
		return request;
	}

	@Test
	public void testDoFilter() throws ServletException {
		LanguageFilter filter = new LanguageFilter();
		filter.init(null);
	}


}
