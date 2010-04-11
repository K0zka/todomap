package org.todomap.o29.utils.aop;

import java.io.StringWriter;
import java.lang.reflect.Method;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.StreamMessage;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.MarshalException;
import javax.xml.bind.Marshaller;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.todomap.o29.beans.BaseBean;

/**
 * Send the updated/new/deleted data out on the wire.
 * 
 * @author kocka
 */
public final class JmsIntegrationAspect implements MethodInterceptor {

	private final static Logger logger = LoggerFactory
			.getLogger(JmsIntegrationAspect.class);

	private final JmsTemplate jmsTemplate;
	private final String[] triggerMethods = new String[] { "add", "delete",
			"remove", "update", "persist" };

	public JmsIntegrationAspect(final JmsTemplate jmsTemplate) {
		super();
		this.jmsTemplate = jmsTemplate;
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
					.getName(), method.getName(), arguments, result);
			try {
				marshaller.marshal(inv, writer);
			} catch (final MarshalException marshalException) {
				logger.error("Message could not be serialized", marshalException);
			}
			final String message = writer.toString();
			jmsTemplate.send(new MessageCreator() {

				@Override
				public Message createMessage(final Session session)
						throws JMSException {
					final StreamMessage streamMessage = session
							.createStreamMessage();
					streamMessage.writeBytes(message.getBytes());
					logger.debug("message created");
					return streamMessage;
				}
			});
			logger.debug("created message: " + message);
		}
		return result;
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
}
