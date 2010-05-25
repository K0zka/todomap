package org.todomap.o29.utils.integration;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.junit.Test;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.unitils.UnitilsJUnit4;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBean;

@SpringApplicationContext("org/todomap/o29/utils/integration/IntegrationMessageListenerTestCtx.xml")
public class IntegrationMessageListenerTest extends UnitilsJUnit4 {

	@SpringBean("template")
	JmsTemplate jmsTemplate;

	@SpringBean("listener")
	IntegrationMessageListener listener;

	@Test
	public void testListenerEndToEnd() throws InterruptedException {
		jmsTemplate.send(new MessageCreator() {

			@Override
			public Message createMessage(Session session) throws JMSException {
				return session
						.createTextMessage("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
								"<notification>" +
								"<customMessage>Hello world!</customMessage>" +
								"<date>2010-05-23T10:27:31.076+02:00</date>" +
								"<endpointId>test</endpointId>" +
								"<beanId>1</beanId>" +
								"<messageId>test</messageId>" +
								"</notification>");
			}
		});
		Thread.sleep(60000);
	}

}
