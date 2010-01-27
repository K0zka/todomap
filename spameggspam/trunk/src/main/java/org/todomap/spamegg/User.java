package org.todomap.spamegg;

public class User {
	private String email;
	private String ipAddress;
	private String name;
	private String url;
	private String userAgent;

	public User() {
		super();
	}

	public User(final String name, final String ipAddress, final String email,
			final String url, final String userAgent) {
		super();
		this.name = name;
		this.ipAddress = ipAddress;
		this.email = email;
		this.url = url;
		this.userAgent = userAgent;
	}

	public String getEmail() {
		return email;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public String getName() {
		return name;
	}

	public String getURL() {
		return url;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public void setIpAddress(final String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setURL(final String uRL) {
		url = uRL;
	}

	public void setUserAgent(final String userAgent) {
		this.userAgent = userAgent;
	}
}
