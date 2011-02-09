package org.todomap.alertbox.resources;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.directory.shared.ldap.client.api.LdapConnection;

/**
 * TODO!
 * 
 * @author kocka
 * 
 */
@XmlRootElement(name = "ldap")
public class LdapResource extends BaseMonitorable {

	@XmlAttribute(name = "ssl")
	public boolean isSsl() {
		return ssl;
	}

	public void setSsl(boolean ssl) {
		this.ssl = ssl;
	}

	@XmlAttribute(name = "server")
	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	@XmlAttribute(name = "port")
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	boolean ssl;
	String server;
	int port = 389;

	@Override
	public final String getTypeId() {
		return "ldap";
	}

	@Override
	public String getName() {
		return "ldap://" + server + ":" + port;
	}

	@Override
	public StatusDescription check() throws Exception {
		LdapConnection connection = new LdapConnection(server, port, ssl);
		connection.bind();
		return null;
	}

}
