package org.todomap.alertbox;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.apache.derby.jdbc.EmbeddedDataSource;
import org.todomap.alertbox.Monitorable.StatusDescription;

public final class History {

	private static final int type_outage_end = 3;
	private static final int type_outage_start = 2;
	final EmbeddedDataSource dataSource;
	private final static Logger logger = Logger.getLogger(History.class
			.getName());

	interface ConnectionCallback {
		void doWithConnection(Connection connection) throws SQLException;
	}

	interface PreparedStatementCallback {
		void doWithPreparedStatement(PreparedStatement preparedStatement)
				throws SQLException;
	}

	interface PreparedStatementCallbackAndReturn<T> {
		T doWithPreparedStatement(PreparedStatement preparedStatement)
				throws SQLException;
	}

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

	private void doWithPreparedStatement(final String sql,
			final PreparedStatementCallback callback) throws SQLException {
		doWithConnection(new ConnectionCallback() {

			@Override
			public void doWithConnection(Connection connection)
					throws SQLException {
				PreparedStatement statement = null;
				try {
					statement = connection.prepareStatement(sql);
					callback.doWithPreparedStatement(statement);
				} finally {
					if (statement != null) {
						statement.close();
					}
				}
			}
		});
	}

	@SuppressWarnings("unchecked")
	private <T> T doWithPreparedStatementAndReturn(final String sql,
			final PreparedStatementCallbackAndReturn<T> callback)
			throws SQLException {
		final Object[] holder = new Object[1];
		doWithConnection(new ConnectionCallback() {

			@Override
			public void doWithConnection(Connection connection)
					throws SQLException {
				PreparedStatement statement = null;
				try {
					statement = connection.prepareStatement(sql);
					holder[0] = callback.doWithPreparedStatement(statement);
				} finally {
					if (statement != null) {
						statement.close();
					}
				}
			}
		});
		return (T) holder[0];
	}

	private void doWithConnection(final ConnectionCallback callback)
			throws SQLException {
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			callback.doWithConnection(connection);
		} finally {
			if (connection != null) {
				connection.close();
			}
		}

	}

	void setupDb() throws SQLException {
		doWithConnection(new ConnectionCallback() {

			@Override
			public void doWithConnection(Connection connection)
					throws SQLException {
				final ArrayList<String> tables = getTableNames(connection
						.getMetaData());
				Statement statement = connection.createStatement();
				if (!tables.contains("ab_events")) {
					statement.execute("create table ab_events("
							+ "ev_id int generated always as identity, "
							+ "ev_res varchar(128) not null, "
							+ "ev_created timestamp not null default CURRENT_TIMESTAMP, "
							+ "ev_type int, " + "ev_status int, "
							+ "ev_desc clob " + ")");
					statement
							.execute("create index ab_events_idx_types on ab_events(ev_type)");
					statement
							.execute("create index ab_events_idx_res on ab_events(ev_res)");
					statement
							.execute("create index ab_events_idx_created on ab_events(ev_created)");
				}
			}
		});

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
		try {
			doWithPreparedStatement(
					"insert into ab_events (ev_res, ev_type) values (?, ?)",
					new PreparedStatementCallback() {

						@Override
						public void doWithPreparedStatement(
								final PreparedStatement preparedStatement)
								throws SQLException {
							preparedStatement.setString(1, monitorable.getId());
							preparedStatement.setInt(2, 1);
							preparedStatement.execute();
						}
					});
		} catch (SQLException e) {
			logger.warning(e.getMessage());
		}
	}

	public void recordOutageStart(final Monitorable monitorable,
			final StatusDescription description) {
		try {
			doWithPreparedStatement(
					"insert into ab_events(ev_res, ev_type, ev_status, ev_desc) values (?, ?, ?, ?)",
					new PreparedStatementCallback() {

						@Override
						public void doWithPreparedStatement(
								PreparedStatement preparedStatement)
								throws SQLException {
							preparedStatement.setString(1, monitorable.getId());
							preparedStatement.setInt(2, type_outage_start);
							preparedStatement.setInt(3, description.getStatus()
									.ordinal());
							preparedStatement.setString(4,
									description.getDescription());
							preparedStatement.execute();
						}
					});
		} catch (SQLException e) {
			logger.warning(e.getMessage());
		}
	}

	public void recordOutageEnd(final Monitorable monitorable) {
		try {
			doWithPreparedStatement(
					"insert into ab_events(ev_res, ev_type) values (?, ?)",
					new PreparedStatementCallback() {

						@Override
						public void doWithPreparedStatement(
								PreparedStatement preparedStatement)
								throws SQLException {
							preparedStatement.setString(1, monitorable.getId());
							preparedStatement.setInt(2, type_outage_end);
						}
					});
		} catch (SQLException e) {
			logger.warning(e.getMessage());
		}
	}

	public Date getLastFail(final Monitorable monitorable) {
		try {
			return doWithPreparedStatementAndReturn(
					"select ev_created from ab_events where ev_res = ? and ev_type = 2 order by ev_created desc",
					new PreparedStatementCallbackAndReturn<Date>() {

						@Override
						public Date doWithPreparedStatement(
								PreparedStatement preparedStatement)
								throws SQLException {
							preparedStatement.setString(1, monitorable.getId());
							final ResultSet resultSet = preparedStatement
									.executeQuery();
							if (resultSet.next()) {
								return new Date(resultSet.getTimestamp(1).getTime());
							}
							return null;
						}

					});
		} catch (SQLException e) {
			logger.warning(e.getMessage());
			return null;
		}
	}

	public class Outage {
		public Date getStart() {
			return start;
		}
		public void setStart(Date start) {
			this.start = start;
		}
		public Date getEnd() {
			return end;
		}
		public void setEnd(Date end) {
			this.end = end;
		}
		public String getReason() {
			return reason;
		}
		public void setReason(String reason) {
			this.reason = reason;
		}
		Date start;
		Date end;
		String reason;
	}

	public List<Outage> listOutages(final Monitorable monitorable, final Date startFrom, final Date untilDate) {
		try {
			return doWithPreparedStatementAndReturn("select " +
					"ev_id, " +
					"ev_created, " +
					"ev_res, " +
					"ev_type, " +
					"ev_desc " +
					"from ab_events " +
					"where ev_res = ? and ev_created between ? and ? " +
					"order by ev_res desc", new PreparedStatementCallbackAndReturn<List<Outage>>() {

				@Override
				public List<Outage> doWithPreparedStatement(
						final PreparedStatement preparedStatement) throws SQLException {

					final ArrayList<History.Outage> outages = new ArrayList<History.Outage>();
					preparedStatement.setString(1, monitorable.getId());
					preparedStatement.setDate(2, new java.sql.Date(startFrom.getTime()));
					preparedStatement.setDate(3, new java.sql.Date(untilDate.getTime()));
					final ResultSet resultSet = preparedStatement.executeQuery();
					Outage outage = null;
					while(resultSet.next()) {
						final int type = resultSet.getInt("ev_type");
						if(type == type_outage_start && outage == null) {
							outage = new Outage();
							outage.start = resultSet.getDate("ev_created");
							outages.add(outage);
						} else if(type == type_outage_end) {
							outage.end = outage.end;
						}
					}
					return outages;
				}
			});
		} catch (SQLException e) {
			logger.warning(e.getMessage());
			return null;
		}
	}
	
}
