package org.todomap.integrations.hitman.impl;

import java.io.StringWriter;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public class ResponseService {

	public JmsTemplate getSenderTemplate() {
		return senderTemplate;
	}

	public void setSenderTemplate(JmsTemplate senderTemplate) {
		this.senderTemplate = senderTemplate;
	}

	JmsTemplate senderTemplate;
	
	public void send(final IntegrationNotificationMessage message) {
		try {
			final JAXBContext jaxbContext = JAXBContext.newInstance(IntegrationNotificationMessage.class);
			final Marshaller marshaller = jaxbContext.createMarshaller();
			final StringWriter writer = new StringWriter();
			marshaller.marshal(message, writer);

			senderTemplate.send(new MessageCreator() {
				
				public Message createMessage(Session session) throws JMSException {
					TextMessage msg = session.createTextMessage();
					msg.setText(writer.toString());
					return msg;
				}
			});
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}
	
}
