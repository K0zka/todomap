package org.todomap.o29.utils.integration;

import java.io.StringReader;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.jpa.support.JpaDaoSupport;
import org.todomap.o29.beans.BaseBean;
import org.todomap.o29.beans.IntegrationMessage;

public class IntegrationMessageListener extends JpaDaoSupport implements MessageListener {

	private final static Logger logger = LoggerFactory
			.getLogger(IntegrationMessageListener.class);

	@Override
	public void onMessage(final Message message) {
		try {
			final String text = ((TextMessage) message).getText();
			final JAXBContext jaxbContext = JAXBContext
					.newInstance(Invocation.class.getPackage().getName() + ":"
							+ BaseBean.class.getPackage().getName());
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			IntegrationMessage integrationMessage = (IntegrationMessage) unmarshaller.unmarshal(new StringReader(text));
			getJpaTemplate().persist(integrationMessage);
		} catch (JMSException e) {
			logger.error("Could not handle message:" + message);
		} catch (JAXBException e) {
			logger.error("Could not handle message:" + message);
		}
	}

}
