package de.unibox.model.database;

import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;

import de.unibox.config.InternalConfig;
import de.unibox.http.servlet.filter.SecurityLevel;

/**
 * The Class DatabasePools implements a functional tool of database connections.
 */
public class DatabasePools {

    /** The administrator pool. */
    public static DatabaseConnection administratorPool = null;

    /** The log. */
    protected static Logger log = Logger.getLogger("UniBoxLogger");

    /** The page pool. */
    public static DatabaseConnection pagePool = null;

    /** The Constant URL. */
    private static final String URL = InternalConfig.getDbProtocol() + "//"
            + InternalConfig.getDbServer() + "/" + InternalConfig.getDbName();

    /** The user pool. */
    public static DatabaseConnection userPool = null;

    /**
     * Gets the administrator pool.
     *
     * @return the administrator pool
     */
    @Deprecated
    synchronized private static DatabaseConnection getAdministratorPool() {
        return DatabasePools.administratorPool;
    }

    /**
     * Gets the pool.
     *
     * @param userRole
     *            the user role
     * @return the pool
     */
    synchronized public static DatabaseConnection getPool(
            final SecurityLevel userRole) {
        switch (userRole) {
        case USER:
            return DatabasePools.getUserPool();
        case ADMINISTRATOR:
            return DatabasePools.getAdministratorPool();
        default:
            return null;
        }
    }

    /**
     * Gets the user pool.
     *
     * @return the user pool
     */
    synchronized private static DatabaseConnection getUserPool() {
        return DatabasePools.userPool;
    }

    /**
     * Initialize.
     *
     * @param userRole
     *            the user role
     * @throws SQLException
     *             the SQL exception
     * @throws ClassNotFoundException
     *             the class not found exception
     */
    synchronized public static void initialize(final SecurityLevel userRole)
            throws SQLException, ClassNotFoundException {
        final Properties props = new Properties();
        props.put("connection.driver", InternalConfig.getDbDriver());
        props.put("connection.url", DatabasePools.URL);
        switch (userRole) {
        case USER:
            props.put("user", InternalConfig.getDbUser());
            props.put("password", InternalConfig.getDbPassword());
            if (DatabasePools.userPool == null) {
                DatabasePools.userPool = new DatabaseConnection(props,
                        userRole.getConnectionCount());
                if (InternalConfig.isLogDatabase()) {
                    DatabasePools.log.info(DatabasePools.class.getSimpleName()
                            + ": userPool is online");
                }
                break;
            } else {
                if (InternalConfig.isLogDatabase()) {
                    DatabasePools.log
                            .info("ClassPools userPool already online");
                }
                break;
            }
        case ADMINISTRATOR:
            // refine admin credentials via webview if needed (please do not
            // save admin credentials plain e.g. in code)
            //
            // props.put("user", DatabasePools.DB_USER);
            // props.put("password", DatabasePools.DB_PASSWORD);
            if (DatabasePools.administratorPool == null) {
                DatabasePools.pagePool = new DatabaseConnection(props,
                        userRole.getConnectionCount());
                if (InternalConfig.isLogDatabase()) {
                    DatabasePools.log.info(DatabasePools.class.getSimpleName()
                            + ": administratorPool is online");
                }
                break;
            } else {
                if (InternalConfig.isLogDatabase()) {
                    DatabasePools.log.info(DatabasePools.class.getSimpleName()
                            + ": administratorPool already online");
                }
                break;
            }
        default:
            if (InternalConfig.isLogDatabase()) {
                DatabasePools.log
                        .warn(DatabasePools.class.getSimpleName()
                                + ": non valid configuration trys to instance new pool");
            }
            break;
        }
    }

}