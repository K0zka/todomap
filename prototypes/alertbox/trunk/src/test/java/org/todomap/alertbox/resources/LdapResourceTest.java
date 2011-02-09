package org.todomap.alertbox.resources;

import org.apache.directory.shared.ldap.client.api.exception.InvalidConnectionException;
import org.junit.Test;


public class LdapResourceTest {

	@Test
	public void testBind() throws Exception {
		LdapResource resource = new LdapResource();
		resource.setServer("directory.d-trust.de");
		resource.check();
	}

	@Test(expected=InvalidConnectionException.class)
	public void testBind_fail() throws Exception {
		LdapResource resource = new LdapResource();
		resource.setServer("notexisting.example.net");
		resource.check();
	}

}
