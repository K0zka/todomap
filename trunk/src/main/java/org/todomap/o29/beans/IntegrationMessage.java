package org.todomap.o29.beans;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@XmlRootElement(name="notification")
public class IntegrationMessage {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "integration_seq")
	@SequenceGenerator(name = "integration_seq", allocationSize = 1, initialValue = 0, sequenceName = "integration_seq")
	long id;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Date getReceived() {
		return received;
	}
	public void setReceived(Date received) {
		this.received = received;
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
	public Long getBeanId() {
		return beanId;
	}
	public void setBeanId(Long beanId) {
		this.beanId = beanId;
	}
	Date received;
	
	String endpointId;
	String messageId;
	String customMessage;
	@Transient
	Long beanId;

	public BaseBean getBean() {
		return bean;
	}
	public void setBean(BaseBean bean) {
		this.bean = bean;
	}
	@XmlTransient
	@ManyToOne()
	BaseBean bean;

}
