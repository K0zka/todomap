package org.todomap.alertbox.resources;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="jdbc")
public class JdbcResource extends BaseMonitorable {

	String jdbcUrl;
	String username;
	String password;
	String query;
	String driverClass;

	@Override
	public String getName() {
		return "JDBC Resource:" + jdbcUrl;
	}

	@Override
	public StatusDescription check() throws Exception {
		if(driverClass != null) {
			Class.forName(driverClass);
		}
		final Connection connection = DriverManager.getConnection(jdbcUrl,
				username, password);
		return new StatusDescription(Status.Ok, getResult(connection));
	}

	private String getResult(final Connection connection) throws SQLException {
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			if (query != null) {
				statement = connection.createStatement();
				resultSet = statement.executeQuery(query);
				resultSet.next();
				return resultSet.getString(1);
			} else {
				return "OK";
			}
		} finally {
			if(resultSet != null) {
				resultSet.close();
			}
			if(statement != null) {
				statement.close();
			}
		}
	}

	@Override
	public String getTypeId() {
		return "jdbc";
	}

	@XmlAttribute(name="url")
	public String getJdbcUrl() {
		return jdbcUrl;
	}

	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	@XmlAttribute(name="user")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@XmlAttribute(name="pwd")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@XmlAttribute(name="query")
	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	@XmlAttribute(name="driver-class")
	public String getDriverClass() {
		return driverClass;
	}

	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}

}
