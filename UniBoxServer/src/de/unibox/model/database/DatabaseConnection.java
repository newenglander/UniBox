package de.unibox.model.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;

import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;

import de.unibox.config.InternalConfig;

/**
 * The Class DatabaseConnection.
 */
public class DatabaseConnection {

    /** The connections. */
    private final Hashtable connections = new Hashtable();

    /** The props. */
    private final Properties props;

    /**
     * Instantiates a new database connection.
     *
     * @param props
     *            the props
     * @param initialConnections
     *            the initial connections
     * @throws SQLException
     *             the SQL exception
     * @throws ClassNotFoundException
     *             the class not found exception
     */
    public DatabaseConnection(final Properties props,
            final int initialConnections) throws SQLException,
            ClassNotFoundException {
        this.props = props;
        if (InternalConfig.LOG_DATABASE) {
            InternalConfig.log
                    .debug("DatabaseConnection: new instance [connectionCount= "
                            + initialConnections + "] " + props.toString());
        }
        this.initConnections(props, initialConnections);
    }

    /**
     * Gets the connection.
     *
     * @return the connection
     * @throws SQLException
     *             the SQL exception
     */
    public Connection getConnection() throws SQLException {
        Connection con = null;
        @SuppressWarnings("rawtypes")
        final Enumeration cons = this.connections.keys();
        synchronized (this.connections) {
            while (cons.hasMoreElements()) {
                con = (Connection) cons.nextElement();
                final Boolean b = (Boolean) this.connections.get(con);
                if (b == Boolean.FALSE) {
                    // got unused connection, check integrity
                    try {
                        con.setAutoCommit(true);
                    } catch (final SQLException e) {
                        // connection error, replace instance
                        this.connections.remove(con);
                        con = this.getNewConnection();
                    }
                    // refresh hashtable to mark this con as used
                    this.connections.put(con, Boolean.TRUE);
                    return con;
                }
            }
            con = this.getNewConnection();
            this.connections.put(con, Boolean.FALSE);
            return con;
        }
    }

    /**
     * Gets the new connection.
     *
     * @return the new connection
     * @throws SQLException
     *             the SQL exception
     */
    private Connection getNewConnection() throws SQLException {
        return DriverManager.getConnection(
                this.props.getProperty("connection.url"), this.props);
    }

    /**
     * Inits the connections.
     *
     * @param props
     *            the props
     * @param initialConnections
     *            the initial connections
     * @throws SQLException
     *             the SQL exception
     * @throws ClassNotFoundException
     *             the class not found exception
     */
    private void initConnections(final Properties props,
            final int initialConnections) throws SQLException,
            ClassNotFoundException {
        Class.forName(props.getProperty("connection.driver"));
        for (int i = 0; i < initialConnections; i++) {
            final Connection con = this.getNewConnection();
            this.connections.put(con, Boolean.FALSE);
        }
        if (InternalConfig.LOG_DATABASE) {
            InternalConfig.log.debug("DatabaseConnection: "
                    + initialConnections + " Connections etablished");
        }
    }

    /**
     * Return connection.
     *
     * @param returned
     *            the returned
     */
    public void returnConnection(final Connection returned) {
        if (this.connections.containsKey(returned)) {
            this.connections.put(returned, Boolean.FALSE);
            if (InternalConfig.LOG_DATABASE) {
                InternalConfig.log
                        .debug("DatabaseConnection connection returned");
            }
        }
    }
}