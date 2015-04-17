package de.unibox.model.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import de.unibox.config.InternalConfig;
import de.unibox.http.servlet.filter.SecurityWrapper.SecurityLevel;

/**
 * The Class DatabaseQuery.
 */
public class DatabaseQuery {

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
                if (InternalConfig.LOG_DATABASE) {
                    InternalConfig.log
                            .debug("Query: initialize ConnectionPool with "
                                    + SecurityLevel.USER);
                }
                DatabasePools.initialize(SecurityLevel.USER);
                if (InternalConfig.LOG_DATABASE) {
                    InternalConfig.log
                            .debug("Query: retrieve ConnectionPool for "
                                    + SecurityLevel.USER);
                }
                DatabaseQuery.pool = DatabasePools.getPool(SecurityLevel.USER);
            } catch (final Exception e) {
                if (InternalConfig.LOG_DATABASE) {
                    InternalConfig.log
                            .warn("Query: could not initialize ConnectionPool");
                }
                e.printStackTrace();
            }
        }
    }

    /** The con. */
    private Connection con = null;

    /**
     * Commit.
     *
     * @throws SQLException
     *             the SQL exception
     */
    public void commit() throws SQLException {
        this.con.commit();
        DatabaseQuery.pool.returnConnection(this.con);
        if (InternalConfig.LOG_DATABASE) {
            InternalConfig.log.debug("DatabaseQuery: committed");
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
            this.con = DatabaseQuery.pool.getConnection();
        } catch (final NullPointerException e) {
            throw new SQLException("SQL Server offline?");
        }
        this.con.setAutoCommit(false);
        if (InternalConfig.LOG_DATABASE) {
            InternalConfig.log.debug("DatabaseQuery: connected");
        }
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
        if (InternalConfig.LOG_DATABASE) {
            InternalConfig.log.debug("DatabaseQuery: Preparing Query: "
                    + statement);
        }
        final PreparedStatement query = this.con.prepareStatement(statement);
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
        if (InternalConfig.LOG_DATABASE) {
            InternalConfig.log.debug("DatabaseQuery: Preparing Update: "
                    + statement);
        }
        final PreparedStatement update = this.con.prepareStatement(statement);
        return update;
    }

    /**
     * Rollback.
     *
     * @throws SQLException
     *             the SQL exception
     */
    public void rollback() throws SQLException {
        this.con.rollback();
        DatabaseQuery.pool.returnConnection(this.con);
        if (InternalConfig.LOG_DATABASE) {
            InternalConfig.log.debug("DatabaseQuery: rollback");
        }
    }

}
