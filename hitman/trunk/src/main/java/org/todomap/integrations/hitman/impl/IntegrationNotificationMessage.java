package org.todomap.integrations.hitman.impl;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "notification")
public class IntegrationNotificationMessage {
	String endpointId;
	String messageId;
	String customMessage;
	Long beanId;

	public Long getBeanId() {
		return beanId;
	}

	public void setBeanId(Long beanId) {
		this.beanId = beanId;
	}

	public String getEndpointId() {
		return endpointId;
	}

	public void setEndpointId(String endpointId) {
		this.endpointId = endpointId;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getCustomMessage() {
		return customMessage;
	}

	public void setCustomMessage(String customMessage) {
		this.customMessage = customMessage;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	Date date = new Date();
}
