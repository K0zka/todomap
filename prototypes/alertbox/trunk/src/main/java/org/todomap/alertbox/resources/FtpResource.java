package org.todomap.alertbox.resources;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.net.ftp.FTPClient;

@XmlRootElement(name="ftp")
public class FtpResource extends BaseMonitorable{

	String server;
	int port = 21;
	String user;
	String password;

	@Override
	public final String getTypeId() {
		return "ftp";
	}

	@Override
	public String getName() {
		return server + " ftp check";
		
	}

	@Override
	public StatusDescription check() throws Exception {
		FTPClient client = new FTPClient();
		try{
			client.connect(server, port);
			if(user!= null) {
				client.login(user, password);
			}
			return new StatusDescription(Status.Ok, client.listHelp());
			
		} finally {
			if(client.isConnected()) {
				client.disconnect();
			}
		}
	}

	@XmlAttribute(name="server")
	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	@XmlAttribute(name="port")
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	@XmlAttribute(name="user")
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	@XmlAttribute(name="pwd")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
