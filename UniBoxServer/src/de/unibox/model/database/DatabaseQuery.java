package de.unibox.model.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import de.unibox.config.InternalConfig;
import de.unibox.http.servlet.filter.SecurityLevel;

/**
 * The Class DatabaseQuery implements the basic functions of a database query.
 */
public class DatabaseQuery {

	/** The log. */
	protected static Logger log = Logger.getLogger("UniBoxLogger");

	/** The pool. */
	private static DatabaseConnection pool = null;

	/**
	 * Inits the database with user credentials.
	 *
	 * @throws SQLException
	 *             the SQL exception
	 */
	public static void init() throws SQLException {
		if (DatabaseQuery.pool == null) {
			try {
				if (InternalConfig.isLogDatabase()) {
					DatabaseQuery.log.debug(DatabaseQuery.class.getSimpleName()
							+ ": initialize ConnectionPool with "
							+ SecurityLevel.USER);
				}
				DatabasePools.initialize(SecurityLevel.USER);
				if (InternalConfig.isLogDatabase()) {
					DatabaseQuery.log.debug(DatabaseQuery.class.getSimpleName()
							+ ": retrieve ConnectionPool for "
							+ SecurityLevel.USER);
				}
				DatabaseQuery.pool = DatabasePools.getPool(SecurityLevel.USER);
			} catch (final Exception e) {
				if (InternalConfig.isLogDatabase()) {
					DatabaseQuery.log.warn(DatabaseQuery.class.getSimpleName()
							+ ": could not initialize ConnectionPool");
				}
				e.printStackTrace();
			}
		}
	}

	/** The con. */
	private Connection connnection = null;

	/**
	 * Commit.
	 *
	 * @throws SQLException
	 *             the SQL exception
	 */
	public void commit() throws SQLException {
		this.connnection.commit();
		DatabaseQuery.pool.returnConnection(this.connnection);
		if (InternalConfig.isLogDatabase()) {
			DatabaseQuery.log.debug(DatabaseQuery.class.getSimpleName()
					+ ": committed");
		}
	}

	/**
	 * Connect.
	 *
	 * @throws SQLException
	 *             the SQL exception
	 */
	public void connect() throws SQLException {
		try {
			this.connnection = DatabaseQuery.pool.getConnection();
		} catch (final NullPointerException e) {
			throw new SQLException("SQL Server offline?");
		}
		this.connnection.setAutoCommit(false);
		if (InternalConfig.isLogDatabase()) {
			DatabaseQuery.log.debug(DatabaseQuery.class.getSimpleName()
					+ ": connected");
		}
	}

	/**
	 * Gets the connection.
	 *
	 * @return the connection
	 */
	public Connection getConnection() {
		return this.connnection;
	}

	/**
	 * Drop query.
	 *
	 * @param statement
	 *            the statement
	 * @return the query
	 * @throws SQLException
	 *             the SQL exception
	 */
	public PreparedStatement getQuery(final String statement)
			throws SQLException {
		if (InternalConfig.isLogDatabase()) {
			DatabaseQuery.log.debug(DatabaseQuery.class.getSimpleName()
					+ ": Preparing Query: " + statement);
		}
		final PreparedStatement query = this.connnection
				.prepareStatement(statement);
		return query;
	}

	/**
	 * Drop update.
	 *
	 * @param statement
	 *            the statement
	 * @return the update
	 * @throws SQLException
	 *             the SQL exception
	 */
	public PreparedStatement getUpdate(final String statement)
			throws SQLException {
		if (InternalConfig.isLogDatabase()) {
			DatabaseQuery.log.debug(DatabaseQuery.class.getSimpleName()
					+ ": Preparing Update: " + statement);
		}
		final PreparedStatement update = this.connnection
				.prepareStatement(statement);
		return update;
	}

	/**
	 * Rollback.
	 *
	 * @throws SQLException
	 *             the SQL exception
	 */
	public void rollback() throws SQLException {
		this.connnection.rollback();
		DatabaseQuery.pool.returnConnection(this.connnection);
		if (InternalConfig.isLogDatabase()) {
			DatabaseQuery.log.debug(DatabaseQuery.class.getSimpleName()
					+ ": rollback");
		}
	}

}
