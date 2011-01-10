package org.todomap.alertbox.notifiers;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.todomap.alertbox.Monitorable;
import org.todomap.alertbox.Monitorable.StatusDescription;
import org.todomap.alertbox.Notifier;

@XmlRootElement(name = "xmpp")
public class JabberNotifier implements Notifier {

	private String serviceName = "gmail.com";
	private String host = "talk.google.com";
	private int port = 5222;
	private String username;
	private String password;
	private String[] addresses;

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	private XMPPConnection connection = null;

	@Override
	public void start() throws XMPPException {
		final ConnectionConfiguration config = new ConnectionConfiguration(
				host, port, serviceName);

		connection = new XMPPConnection(config);
		connection.connect();
		connection.login(username, password);

	}

	@Override
	public void notify(Monitorable monitorable,
			StatusDescription statusDescription) {
		for (final String address : addresses) {
			final Chat chat = connection.getChatManager().createChat(address,
					null);
			try {
				chat.sendMessage(monitorable.getName() + " "
						+ statusDescription.getStatus() + "\n"
						+ statusDescription.getDescription());
			} catch (XMPPException e) {

			}
		}
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	@XmlAttribute(name = "port")
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	@XmlAttribute(name = "user", required = true)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@XmlAttribute(name = "pwd", required = true)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@XmlElement(name = "to")
	public String[] getAddresses() {
		return addresses;
	}

	public void setAddresses(String[] addresses) {
		this.addresses = addresses;
	}

}
