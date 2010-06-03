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
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.todomap.o29.beans.BaseBean;
import org.todomap.o29.beans.IntegrationMessage;
import org.todomap.o29.logic.BaseService;

public class IntegrationMessageListener extends JpaDaoSupport implements
		MessageListener {

	PlatformTransactionManager txManager;
	BaseService baseService;

	public BaseService getBaseService() {
		return baseService;
	}

	public void setBaseService(BaseService baseService) {
		this.baseService = baseService;
	}

	public PlatformTransactionManager getTxManager() {
		return txManager;
	}

	public void setTxManager(PlatformTransactionManager txManager) {
		this.txManager = txManager;
	}

	private final static Logger logger = LoggerFactory
			.getLogger(IntegrationMessageListener.class);

	@Override
	public void onMessage(final Message message) {
		new TransactionTemplate(txManager)
				.execute(new TransactionCallback<Object>() {

					@Override
					public Object doInTransaction(TransactionStatus arg0) {
						try {
							final String text = ((TextMessage) message)
									.getText();
							final JAXBContext jaxbContext = JAXBContext
									.newInstance(Invocation.class.getPackage()
											.getName()
											+ ":"
											+ BaseBean.class.getPackage()
													.getName());
							Unmarshaller unmarshaller = jaxbContext
									.createUnmarshaller();
							IntegrationMessage integrationMessage = (IntegrationMessage) unmarshaller
									.unmarshal(new StringReader(text));
							integrationMessage.setBean(baseService.getById(integrationMessage.getBeanId()));
							getJpaTemplate().persist(integrationMessage);
							
							//perform actions
							action(integrationMessage);
							
						} catch (JMSException e) {
							logger.error("Could not handle message:" + message);
						} catch (JAXBException e) {
							logger.error("Could not handle message:" + message);
						}
						return null;
					}
				});
	}

	void action(IntegrationMessage message) {
		/*
		 * Not much logic here, could be refactored to classes once it is needed.
		 */
		if("org.todomap.spamfilter".equals(message.getEndpointId())) {
			message.getBean().setSpamChecked(true);
			getJpaTemplate().persist(message.getBean());
		}
	}
	
}
