package org.todomap.spamegg;

import java.io.IOException;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;

import org.junit.Test;

public abstract class SpamFilterTests {
	abstract SpamFilter getSpamFilter();

	/**
	 * Get all the spam from my google mail box and check them through the
	 * service, count the difference.
	 * 
	 * @throws MessagingException
	 * @throws IOException
	 * @throws SpamFilterException
	 */
	@Test
	public void testPerformance() throws MessagingException,
			SpamFilterException, IOException {
		final SpamFilter spamFilter = getSpamFilter();
		Session session = Session.getInstance(System.getProperties(),
				new Authenticator() {

					@Override
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(System
								.getProperty("mail.imaps.user"), System
								.getProperty("mail.imaps.password"));
					}
				});
		Store store = session.getStore();
		store.connect();
		Folder folder = store.getFolder("[Gmail]").getFolder("Spam");
		folder.open(Folder.READ_ONLY);
		Message[] messages = folder.getMessages();
		int hamCntr = 0;
		for (Message message : messages) {
			Address[] from = message.getFrom();
			Object content = message.getContent();
			String fromAddress = from[0].toString();
			if (spamFilter.isSpam(new Content(new User(fromAddress, "",
					fromAddress, "", ""), "", "", "email", content.toString()))) {
				hamCntr++;
			}
		}
		System.out.println("Total messages:" + folder.getMessageCount());
		System.out
				.println("gmail spam considered ham:"
						+ hamCntr);
	}

	@Test
	public void testIsSpam() throws SpamFilterException {
		final SpamFilter spamFilter = getSpamFilter();
		final Content spam = new Content(
				new User("John Doe", "127.0.0.1", "spamboy@hotmail.com",
						"http://localhost/XXX", "Spambot 1.0"),
				"-",
				"",
				"comment",
				"Win the lottery, buy cheap viagra online 500% off now from the president of Nigeria.");
		spamFilter.isSpam(spam);
		spamFilter.falseNegative(spam);
	}

	@Test
	public void testFalsePositive() throws SpamFilterException {
		final SpamFilter spamFilter = getSpamFilter();
		final Content notSpam = new Content(new User("Dummy Warhead",
				"127.0.0.1", "dummywarhead@hotmail.com",
				"http://dummywarhead.openid.net/", "Firefox"), "-", "",
				"comment", "Hello! This is a test.");
		spamFilter.isSpam(notSpam);
		spamFilter.falsePositive(notSpam);
	}

}
