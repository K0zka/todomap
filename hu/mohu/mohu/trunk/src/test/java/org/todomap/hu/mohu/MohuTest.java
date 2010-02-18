package org.todomap.hu.mohu;

import org.junit.Test;

public class MohuTest {
	@Test
	public void listContacts() throws Exception {
		Mohu mohu = new Mohu();
		mohu.listContacts("2377", "Örkény");
		mohu.listContacts("2400", "Dunaújváros");
		mohu.listContacts("6000", "Kecskemét");
		mohu.listContacts("6001", "Kecskemét");
	}

}
