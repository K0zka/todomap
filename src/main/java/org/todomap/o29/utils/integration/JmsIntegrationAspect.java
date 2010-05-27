package org.todomap.o29.utils.integration;

import java.io.StringWriter;
import java.lang.reflect.Method;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.StreamMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.MarshalException;
import javax.xml.bind.Marshaller;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.support.JpaDaoSupport;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.todomap.o29.beans.BaseBean;
import org.todomap.o29.beans.User;

/**
 * Send the updated/new/deleted data out on the wire.
 * 
 * @author kocka
 */
public final class JmsIntegrationAspect extends JpaDaoSupport implements MethodInterceptor {

	private final static Logger logger = LoggerFactory
			.getLogger(JmsIntegrationAspect.class);

	private final JmsTemplate jmsTemplate;
	private final String[] triggerMethods = new String[] { "add", "delete",
			"remove", "update", "persist" };

	public JmsIntegrationAspect(final JmsTemplate jmsTemplate) {
		super();
		this.jmsTemplate = jmsTemplate;
	}

	private boolean ignoreMessage(final MethodInvocation invocation) {
		final String methodName = invocation.getMethod().getName();
		for (final String prefix : triggerMethods) {
			if (methodName.startsWith(prefix)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public Object invoke(final MethodInvocation invocation) throws Throwable {
		final Object result = invocation.proceed();

		if (!ignoreMessage(invocation)) {

			final Object[] arguments = invocation.getArguments();
			final JAXBContext jaxbContext = JAXBContext
					.newInstance(Invocation.class.getPackage().getName() + ":"
							+ BaseBean.class.getPackage().getName());
			final Marshaller marshaller = jaxbContext.createMarshaller();
			final StringWriter writer = new StringWriter();
			final Method method = invocation.getMethod();
			final Invocation inv = new Invocation(method.getDeclaringClass()
					.getName(), method.getName(), arguments, result, getUser());
			try {
				marshaller.marshal(inv, writer);
			} catch (final MarshalException marshalException) {
				logger.error("Message could not be serialized",
						marshalException);
			}
			final String message = writer.toString();
			jmsTemplate.send(new MessageCreator() {

				@Override
				public Message createMessage(final Session session)
						throws JMSException {
					final StreamMessage streamMessage = session
							.createStreamMessage();
					streamMessage.writeString(message);
					logger.debug("message created");
					return streamMessage;
				}
			});
			logger.debug("created message: " + message);
		}
		return result;
	}

	private User getUser() {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication == null || !authentication.isAuthenticated()) {
			return null;
		} else {
			return getJpaTemplate().execute(new JpaCallback<User>() {

				@Override
				public User doInJpa(EntityManager manager)
						throws PersistenceException {
					return (User) manager.createQuery("select object(u) from "+User.class.getName()+" u where openIdUrl = ?").setParameter(1, authentication.getName()).getSingleResult();
				}
			});
		}
	}
}
