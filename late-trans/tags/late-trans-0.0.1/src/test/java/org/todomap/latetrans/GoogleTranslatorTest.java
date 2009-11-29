package org.todomap.latetrans;

import junit.framework.Assert;

import org.junit.Test;

public class GoogleTranslatorTest {

	@Test
	public void testGetLanguage() {
		GoogleTranslator translator = new GoogleTranslator();
		Assert.assertEquals("hu", translator.getLanguage("Jó napot!"));
		Assert.assertEquals("es", translator.getLanguage("Buenos dias!"));
		Assert.assertEquals("en", translator.getLanguage("Good morning!"));
		Assert.assertEquals("de", translator.getLanguage("Guten Tag!"));
		Assert.assertEquals("sk", translator.getLanguage("Dobre da!"));
	}
	
}
