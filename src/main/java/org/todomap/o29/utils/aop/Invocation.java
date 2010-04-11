/**
 * 
 */
package org.todomap.o29.utils.aop;

import java.util.Date;

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "invocation")
class Invocation {
	public Invocation() {
		super();
	}

	public Invocation(String interfaceName, String methodName,
			Object[] arguments, Object result) {
		super();
		this.interfaceName = interfaceName;
		this.methodName = methodName;
		this.arguments = arguments;
		this.result = result;
		this.callTime = new Date();
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	String interfaceName;

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	@XmlElementWrapper(name = "arguments")
	public Object[] getArguments() {
		return arguments;
	}

	public Date getCallTime() {
		return callTime;
	}

	public void setCallTime(Date callTime) {
		this.callTime = callTime;
	}

	public void setArguments(Object[] arguments) {
		this.arguments = arguments;
	}

	String methodName;
	Object[] arguments;
	Object result;
	Date callTime;
}