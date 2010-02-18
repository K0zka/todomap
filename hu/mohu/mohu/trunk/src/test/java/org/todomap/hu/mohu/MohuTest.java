package org.todomap.hu.mohu;

import junit.framework.Assert;

import org.junit.Test;

public class MohuTest {
	@Test
	public void listContacts() throws Exception {
		Mohu mohu = new Mohu();
		mohu.listContacts("2377", "Örkény");
		mohu.listContacts("2400", "Dunaújváros");
		mohu.listContacts("6000", "Kecskemét");
		mohu.listContacts("6001", "Kecskemét");
		mohu.listContacts("3500", "Miskolc");

	}

	@Test
	public void testTricks() throws Exception {
		Mohu mohu = new Mohu();
		mohu.listContacts("9400", "Sopron");
		mohu.listContacts("9400", "Budapest VIII. kerület");
	}

	@Test
	public void testBuildContact() throws Exception {
		Mohu mohu = new Mohu();
		Contact contact = mohu
				.buildContactFromHtml("<div class=\"resultset resultset-last\">"
						+ "<div class=\"title\">Sopron Megyei Jogú Város Polgármesteri Hivatala</div>"
						+ "<div class=\"address\">9400 Sopron, Pf. 127. Sopron, Fő tér 1.</div>"
						+ "<div class=\"phone\">Telefon: (99) 515-100</div>"
						+ "<div class=\"fax\">Fax: (99) 311-445</div>"
						+ "</div>");
		Assert.assertEquals("Sopron Megyei Jogú Város Polgármesteri Hivatala",
				contact.getName());
		Assert.assertEquals("(99) 515-100", contact.getPhone());
	}

}
