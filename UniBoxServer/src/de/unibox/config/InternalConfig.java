package de.unibox.config;

import org.apache.log4j.Logger;

/**
 * The Class InternalConfig.
 */
public class InternalConfig {

    /** The Constant DB_NAME. */
    public static final String DB_NAME = "unibox";

    /** The Constant DB_PASSWORD. */
    public static final String DB_PASSWORD = "root";

    /** The Constant DB_SERVER. */
    public static final String DB_SERVER = "localhost";

    /** The Constant DB_USER. */
    public static final String DB_USER = "root";

    /** The Constant DRIVER. */
    public static final String DRIVER = "com.mysql.jdbc.Driver";

    /** The log. */
    public static Logger log = Logger.getLogger("UniBoxLogger");

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

    /** The Constant MESSAGE_REFLECTION. */
    public static final boolean MESSAGE_REFLECTION = false;

    /** The Constant PROTOCOL. */
    public static final String PROTOCOL = "jdbc:mysql:";
}
