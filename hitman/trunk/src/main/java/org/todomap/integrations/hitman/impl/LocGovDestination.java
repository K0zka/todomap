package org.todomap.integrations.hitman.impl;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;
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
			config.setTemplateLoader(new ClassTemplateLoader(
					LocGovDestination.class, ""));
			Template template = config.getTemplate("firstnotification.fm");
			StringWriter stringWriter = new StringWriter();
			HashMap<String, String> data = new HashMap<String, String>();
			data.put("lat", Utils.getXpathValue(
					"/invocation/result/location/latitude/text()", dom));
			data.put("long", Utils.getXpathValue(
					"/invocation/result/location/longitude/text()", dom));
			data.put("id", Utils.getXpathValue("/invocation/result/id/text()",
					dom));
			data.put("created", Utils.getXpathValue(
					"/invocation/result/created/text()", dom));
			data.put("created", Utils.getXpathValue(
					"/invocation/result/text()", dom));
			data.put("shortDescription", Utils.getXpathValue("/invocation/result/shortDescr/text()", dom));
			data.put("description", Utils.getXpathValue("/invocation/result/description/text()", dom));
			data.put("postalcode", Utils.getXpathValue("/invocation/result/addr/postalCode/text()", dom));
			data.put("town", Utils.getXpathValue("/invocation/result/addr/town/text()", dom));
			data.put("addr", "");
			data.put("tags", "");
			Address address = getAddress(Integer.parseInt(Utils.getXpathValue(
					"/invocation/result/addr/postalCode/text()", dom)), Utils
					.getXpathValue("/invocation/result/addr/town/text()", dom));
			data.put("title", address.name);
			template.process(data, stringWriter);
			SimpleMailMessage mail = new SimpleMailMessage();
			mail.setTo(address.email);
			mail.setFrom(from);
			mail.setText(stringWriter.toString());
			mail.setSubject(Utils.getXpathValue("/invocation/result/shortDescr/text()", dom) + " - todomap");
			mailSender.send(mail);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (TemplateException e) {
			throw new RuntimeException(e);
		}
	}

	private Address getAddress(final int postCode, final String town) {

		/*
		 * Budapest
		 */
		if (postCode > 1000 && postCode < 2000) {
			return getJdbcTemplate()
					.execute(
							"SELECT majoremail, majorname from addr where pcode / 10 = ?",
							new PreparedStatementCallback<Address>() {

								@Override
								public Address doInPreparedStatement(
										PreparedStatement statement)
										throws SQLException,
										DataAccessException {
									statement.setInt(1, postCode / 10);
									ResultSet resultSet = statement
											.executeQuery();
									if (!resultSet.next()) {
										return null;
									}
									Address address = new Address();
									address.email = resultSet
											.getString("majoremail");
									address.name = resultSet
											.getString("majorname");
									return address;
								}
							});
		}

		return getJdbcTemplate().execute(
				"SELECT majoremail, majorname from addr where town = ?",
				new PreparedStatementCallback<Address>() {

					@Override
					public Address doInPreparedStatement(
							PreparedStatement statement) throws SQLException,
							DataAccessException {
						statement.setString(1, town);
						ResultSet resultSet = statement.executeQuery();
						if (resultSet.next()) {
							final Address addr = new Address();
							addr.email = resultSet.getString("majoremail");
							addr.name = resultSet.getString("majorname");
							return addr;
						} else {
							return null;
						}
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
