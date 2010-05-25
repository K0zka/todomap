package org.todomap.integrations.hitman.impl;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.todomap.integrations.hitman.Destination;
import org.w3c.dom.Document;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class LocGovDestination extends JdbcDaoSupport implements Destination {

	private String templateDir;
	private MailSender mailSender;
	private String from;

	class Address {
		String name;
		String email;
	}
	
	public MailSender getMailSender() {
		return mailSender;
	}

	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public void send(String message) {
		try {
			Document dom = Utils.parse(message);

			String interfaceName = Utils.getXpathValue(
					"/invocation/interfaceName/text()", dom);
			String methodName = Utils.getXpathValue(
					"/invocation/methodName/text()", dom);
			if (!"org.todomap.o29.logic.TodoService".equals(interfaceName)
					|| !"addTodo".equals(methodName)) {
				return;
			}

			Configuration config = new Configuration();
			config.setTemplateLoader(new ClassTemplateLoader(LocGovDestination.class, ""));
			Template template = config.getTemplate("firstnotification.fm");
			StringWriter stringWriter = new StringWriter();
			HashMap<String, String> data = new HashMap<String, String>();
			data.put("lat", Utils.getXpathValue("/invocation/result/location/latitude/text()", dom));
			data.put("long", Utils.getXpathValue("/invocation/result/location/longitude/text()", dom));
			data.put("id", Utils.getXpathValue("/invocation/result/id/text()", dom));
			data.put("created", Utils.getXpathValue("/invocation/result/created/text()", dom));
			data.put("created", Utils.getXpathValue("/invocation/result/text()", dom));
			data.put("shortDescription", "");
			data.put("description", "");
			data.put("postalcode", "");
			data.put("town", "");
			data.put("addr", "");
			data.put("tags", "");
			getAddress();
			data.put("title", "");
			template.process(data, stringWriter);
			SimpleMailMessage mail = new SimpleMailMessage();
			mail.setTo("");
			mail.setFrom(from);
			mail.setText(stringWriter.toString());
			mail.setSubject("");
			mailSender.send(mail);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (TemplateException e) {
			throw new RuntimeException(e);
		}
	}

	private void getAddress() {
		
		getJdbcTemplate().execute(new ConnectionCallback() {
			
			public Object doInConnection(Connection connection) throws SQLException,
					DataAccessException {
				return null;
			}
		});
	}

	public String getTemplateDir() {
		return templateDir;
	}

	public void setTemplateDir(String templateDir) {
		this.templateDir = templateDir;
	}

}
