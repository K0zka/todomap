package org.todomap.alertbox.resources;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="jvm")
public class JvmResource extends BaseMonitorable {

	@Override
	public String getName() {
		return "Local virtual machine";
	}

	@Override
	public StatusDescription check() throws Exception {
		long freeMemory = Runtime.getRuntime().freeMemory() / 1024 / 1024;
		long totalMemory = Runtime.getRuntime().totalMemory() / 1024 / 1024;
		long processors = Runtime.getRuntime().availableProcessors();
		String version = System.getProperties().getProperty("java.vm.version");
		return new StatusDescription(Status.Ok, "JVM: " + version + " CPU: " + processors
				+ " Memory:" + freeMemory + " MB free/ " + totalMemory
				+ " MB total");
	}

	@Override
	public String getTypeId() {
		return "jvm";
	}

}
