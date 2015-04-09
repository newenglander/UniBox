package de.unibox.config;

import org.apache.log4j.Logger;

/**
 * The Class InternalConfig.
 */
public class InternalConfig {

    /** The Constant DB_NAME. */
    protected static final String DB_NAME = "unibox";

    /** The Constant DB_PASSWORD. */
    protected static final String DB_PASSWORD = "root";

    /** The Constant DB_SERVER. */
    protected static final String DB_SERVER = "localhost";

    /** The Constant DB_USER. */
    protected static final String DB_USER = "root";

    /** The Constant DRIVER. */
    protected static final String DRIVER = "com.mysql.jdbc.Driver";

    /** The log. */
    protected static Logger log = Logger.getLogger("UniBoxLogger");

    /** The log async sessions. */
    public static boolean LOG_ASYNC_SESSIONS = false;

    /** The log authentification. */
    public static boolean LOG_AUTHENTIFICATION = false;

    /** The log communication. */
    public static boolean LOG_COMMUNICATION = true;

    /** The log database. */
    public static boolean LOG_DATABASE = false;

    /** The log gamepool. */
    public static boolean LOG_GAMEPOOL = true;

    /** The log request header. */
    public static boolean LOG_REQUEST_HEADER = false;

    /** The log requested uri. */
    public static boolean LOG_REQUESTED_URI = false;

    /** The log threads. */
    public static boolean LOG_THREADS = false;

    /** The Constant PROTOCOL. */
    protected static final String PROTOCOL = "jdbc:mysql:";
}
