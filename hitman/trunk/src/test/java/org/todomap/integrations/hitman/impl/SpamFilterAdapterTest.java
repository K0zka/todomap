package org.todomap.integrations.hitman.impl;

import java.io.IOException;

import junitx.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.todomap.spamegg.Content;
import org.todomap.spamegg.LinkSleeveSpamFilter;
import org.todomap.spamegg.SpamFilter;
import org.todomap.spamegg.SpamFilterException;

public class SpamFilterAdapterTest {

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
		adapter.setFilter(new SpamFilter() {
			
			@Override
			public boolean isSpam(Content content) throws SpamFilterException {
				return false;
			}
			
			@Override
			public void falsePositive(Content content) throws SpamFilterException {
			}
			
			@Override
			public void falseNegative(Content content) throws SpamFilterException {
			}
		});
		String message = IOUtils.toString(Thread.currentThread().getContextClassLoader().getResourceAsStream("org/todomap/integrations/hitman/impl/testevent1.xml"));
		String filter = adapter.filter(message);
		Assert.assertNotNull(filter);
	}

}
