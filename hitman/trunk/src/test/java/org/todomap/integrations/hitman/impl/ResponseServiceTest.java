package org.todomap.integrations.hitman.impl;

import org.junit.Test;
import org.unitils.UnitilsJUnit4;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBean;

@SpringApplicationContext("org/todomap/integrations/hitman/impl/ResponseServiceTestCtx.xml")
public class ResponseServiceTest extends UnitilsJUnit4 {
	
	@SpringBean("responseService")
	ResponseService responseService;
	
	@Test
	public void testSend() {
		IntegrationNotificationMessage message = new IntegrationNotificationMessage();
		message.setCustomMessage("Hello world!");
		message.setEndpointId("test");
		message.setMessageId("test");
		responseService.send(message);
	}
}
