package org.todomap.spamegg;

public class User {
	public User() {
		super();
	}
	public User(String name, String ipAddress, String email, String URL, String userAgent) {
		super();
		this.name = name;
		this.ipAddress = ipAddress;
		this.email = email;
		this.URL = URL;
		this.userAgent = userAgent;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	String name;
	String ipAddress;
	String email;
	String URL;
	String userAgent;
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getURL() {
		return URL;
	}
	public void setURL(String uRL) {
		URL = uRL;
	}
	public String getUserAgent() {
		return userAgent;
	}
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
}
