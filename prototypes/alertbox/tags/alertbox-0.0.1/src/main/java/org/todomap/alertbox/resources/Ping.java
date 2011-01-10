package org.todomap.alertbox.resources;

import java.net.InetAddress;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ping")
public class Ping extends BaseMonitorable {

	String host;
	int timeOut = 3000;
	@XmlAttribute(name = "timeout")
	public int getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}

	public Integer getWarn() {
		return warn;
	}

	public void setWarn(Integer warn) {
		this.warn = warn;
	}

	Integer warn = 1000;

	@Override
	public String getTypeId() {
		return "ping";
	}

	@Override
	public String getName() {
		return host;
	}

	@Override
	public StatusDescription check() throws Exception {
		try {
			final InetAddress address = InetAddress.getByName(host);
			final long start = System.currentTimeMillis();
			final boolean reachable = address.isReachable(timeOut);
			final long end = System.currentTimeMillis();
			return new StatusDescription(reachable ? Status.Ok : Status.Fail, (end - start) + "ms");
		} catch (Exception e) {
			return new StatusDescription(Status.Fail, e.toString());
		}
	}

	@XmlAttribute(name = "host")
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

}
