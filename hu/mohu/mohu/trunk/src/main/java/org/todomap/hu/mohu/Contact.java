package org.todomap.hu.mohu;

/**
 * Government agency contact info.
 * 
 * @author kocka
 *
 */
public class Contact {
	private String address;
	private String email;
	private String fax;
	private String name;
	private String phone;
	private String URL;

	public String getAddress() {
		return address;
	}

	public String getEmail() {
		return email;
	}

	public String getFax() {
		return fax;
	}

	public String getName() {
		return name;
	}

	public String getPhone() {
		return phone;
	}

	public String getURL() {
		return URL;
	}

	public void setAddress(final String address) {
		this.address = address;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public void setFax(final String fax) {
		this.fax = fax;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setPhone(final String phone) {
		this.phone = phone;
	}

	public void setURL(final String uRL) {
		URL = uRL;
	}
}
