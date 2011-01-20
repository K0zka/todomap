package org.todomap.alertbox;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.derby.jdbc.EmbeddedDataSource;
import org.todomap.alertbox.Monitorable.StatusDescription;

public final class History {

	final EmbeddedDataSource dataSource;

	public History() {
		dataSource = new EmbeddedDataSource();
		dataSource.setCreateDatabase("create");
		dataSource.setDatabaseName(System.getProperty("user.home")
				+ "/.alertbox/history");
		try {
			setupDb();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	void setupDb() throws SQLException {
		Connection connection = null;
		Statement statement = null;

		try {
			connection = dataSource.getConnection();
			final ArrayList<String> tables = getTableNames(connection
					.getMetaData());
			final ArrayList<String> indices = getIndexNames(
					connection.getMetaData(), tables);
			statement = connection.createStatement();
			if (!tables.contains("ab_events")) {
				statement.execute("create table ab_events("
						+ "ev_id int generated always as identity, "
						+ "ev_res varchar(128) not null, "
						+ "ev_created date not null default CURRENT_DATE, "
						+ "ev_type int, " + "ev_status int, " + "ev_desc clob "
						+ ")");
			}
			if (!indices.contains("ab_events_idx_types")) {
				statement
						.execute("create index ab_events_idx_types on ab_events(ev_type)");
			}
			statement
					.execute("create index ab_events_idx_res on ab_events(ev_res)");
			statement
					.execute("create index ab_events_idx_created on ab_events(ev_created)");

		} finally {
			if (connection != null) {
				connection.close();
			}
		}
	}

	private ArrayList<String> getIndexNames(DatabaseMetaData metaData,
			List<String> tables) throws SQLException {
		ArrayList<String> indexList = new ArrayList<String>();
		for (String table : tables) {
			final ResultSet indices = metaData.getIndexInfo(null, null, table,
					false, true);

			while (indices.next()) {
				indexList.add(indices.getString("INDEX_NAME"));
			}
			indices.close();
		}
		return indexList;
	}

	private ArrayList<String> getTableNames(final DatabaseMetaData metaData)
			throws SQLException {
		final ResultSet tables = metaData.getTables(null, null, null,
				new String[] { "TABLE" });
		ArrayList<String> tablesList = new ArrayList<String>();
		while (tables.next()) {
			tablesList.add(tables.getString("TABLE_NAME").toLowerCase());
		}
		tables.close();
		return tablesList;
	}

	void record(String bla) throws SQLException {
		Connection connection = dataSource.getConnection();
		connection.prepareStatement("insert into events()");
	}

	final File historyDirectory = new File(System.getProperty("user.home"),
			".alertbox/history");

	File getResourceFile(Monitorable monitorable) throws IOException {
		final File recordFile = new File(historyDirectory, monitorable.getId());
		if (!recordFile.exists()) {
			recordFile.createNewFile();
		}
		return recordFile;
	}

	public void recordResourceMonitorStarted(final Monitorable monitorable) {
		//TODO
	}

	public void recordOutageStart(final Monitorable monitorable,
			final StatusDescription description) {
		//TODO
	}

	public void recordOutageEnd(final Monitorable monitorable) {
		//TODO
	}

}
