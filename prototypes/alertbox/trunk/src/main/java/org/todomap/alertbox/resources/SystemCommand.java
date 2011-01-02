package org.todomap.alertbox.resources;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.io.IOUtils;

@XmlRootElement(name="system")
public class SystemCommand extends BaseMonitorable {

	String command;

	@Override
	public String getTypeId() {
		return "commandline";
	}

	@Override
	public String getName() {
		return command;
	}

	@Override
	public StatusDescription check() throws Exception {
		final Process exec = Runtime.getRuntime().exec(command);
		exec.waitFor();
		final String output = IOUtils.toString(exec.getInputStream());
		return new StatusDescription(exec.exitValue() == 0 ? Status.Ok
				: Status.Fail, output);
	}

	@XmlAttribute(name="command")
	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

}