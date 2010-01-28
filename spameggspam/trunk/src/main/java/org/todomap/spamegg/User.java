package org.todomap.spamegg;

/**
 * Informations about the author of the content.
 * 
 * @author kocka
 */
public final class User {
	private final String email;
	private final String ipAddress;
	private final String name;
	private final String url;
	private final String userAgent;

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
}
