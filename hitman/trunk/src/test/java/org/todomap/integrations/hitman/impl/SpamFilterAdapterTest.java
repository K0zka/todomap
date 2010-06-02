package org.todomap.integrations.hitman.impl;

import java.io.IOException;

import junitx.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.todomap.spamegg.LinkSleeveSpamFilter;
import org.unitils.UnitilsJUnit4;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBean;

@SpringApplicationContext("org/todomap/integrations/hitman/impl/SpamFilterAdapterTestCtx.xml")
public class SpamFilterAdapterTest extends UnitilsJUnit4 {

	@SpringBean("responseService")
	ResponseService responseService;
	
	@Test
	public void testFilter() throws IOException {
		SpamFilterAdapter adapter = new SpamFilterAdapter();
		adapter.setFilter(new LinkSleeveSpamFilter());
		String message = IOUtils.toString(Thread.currentThread().getContextClassLoader().getResourceAsStream("org/todomap/integrations/hitman/impl/testevent1.xml"));
		adapter.filter(message);
	}

	@Test
	public void testFilter_mock() throws IOException {
		SpamFilterAdapter adapter = new SpamFilterAdapter();
		FakeSpamFilter filter = new FakeSpamFilter();
		adapter.setFilter(filter);
		adapter.setResponseService(responseService);
		final String message = IOUtils.toString(Thread.currentThread().getContextClassLoader().getResourceAsStream("org/todomap/integrations/hitman/impl/testevent1.xml"));
		String result = adapter.filter(message);
		Assert.assertNotNull(result);
		
		filter.spam = true;
		
		result = adapter.filter(message);
		Assert.assertNull(result);
	}

}
