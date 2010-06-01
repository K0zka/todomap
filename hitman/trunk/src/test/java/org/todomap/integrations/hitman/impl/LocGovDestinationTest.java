package org.todomap.integrations.hitman.impl;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBean;

@SpringApplicationContext("org/todomap/integrations/hitman/impl/LocGovDestinationTestCtx.xml")
public class LocGovDestinationTest extends UnitilsJUnit4 {
	
	@SpringBean("destination")
	LocGovDestination destination;
	
	@Test
	public void testSend() throws IOException {
		String message = IOUtils.toString(Thread.currentThread().getContextClassLoader().getResourceAsStream("org/todomap/integrations/hitman/impl/testevent1.xml"));
		destination.send(message);
	}
	
	
	
}
