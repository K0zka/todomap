package org.todomap.alertbox.resources;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.net.smtp.SMTPClient;

@XmlRootElement(name="smtp")
public class SmtpResource extends BaseMonitorable {

	@XmlAttribute(name="host")
	String host;

	@Override
	public String getTypeId() {
		return "smtp";
	}

	@Override
	public String getName() {
		return host;
	}

	@Override
	public StatusDescription check() throws Exception {
		SMTPClient client = new SMTPClient();
		client.connect(host);
		client.help();
		return new StatusDescription(Status.Ok, client.getReplyString());
	}

}
