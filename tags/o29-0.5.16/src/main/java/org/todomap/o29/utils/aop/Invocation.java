/**
 * 
 */
package org.todomap.o29.utils.aop;

import java.util.Date;

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "invocation")
class Invocation {
	Object[] arguments;

	Date callTime;

	String interfaceName;

	String methodName;

	Object result;

	public Invocation() {
		//non-arg constructor needed by jaxb.
	}
	
	public Invocation(final String interfaceName, final String methodName,
			final Object[] arguments, final Object result) {
		super();
		this.interfaceName = interfaceName;
		this.methodName = methodName;
		this.arguments = arguments;
		this.result = result;
		this.callTime = new Date();
	}

	@XmlElementWrapper(name = "arguments")
	public Object[] getArguments() {
		return arguments;
	}

	public Date getCallTime() {
		return callTime;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public String getMethodName() {
		return methodName;
	}

	public Object getResult() {
		return result;
	}

	public void setArguments(final Object[] arguments) {
		this.arguments = arguments;
	}

	public void setCallTime(final Date callTime) {
		this.callTime = callTime;
	}

	public void setInterfaceName(final String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public void setMethodName(final String methodName) {
		this.methodName = methodName;
	}

	public void setResult(final Object result) {
		this.result = result;
	}
}