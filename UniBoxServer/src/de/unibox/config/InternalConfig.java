package de.unibox.config;

import org.apache.log4j.Logger;

/**
 * Use this Class to configure more then just the logging level. You can
 * activate logging by depending on your functional level. Furthermore you are
 * able to set the database credentials and host directly here.
 */
public class InternalConfig {

    /** The Constant DRIVER. */
    private static final String DB_DRIVER = "com.mysql.jdbc.Driver";

    /** The Constant DB_NAME. */
    private static final String DB_NAME = "unibox";

    /** The Constant DB_PASSWORD. */
    private static final String DB_PASSWORD = "root";

    /** The Constant PROTOCOL. */
    private static final String DB_PROTOCOL = "jdbc:mysql:";

    /** The Constant DB_SERVER. */
    private static final String DB_SERVER = "localhost";

    /** The Constant DB_USER. */
    private static final String DB_USER = "root";

    /** The log. */
    private static Logger log = Logger.getLogger("UniBoxLogger");

    /** The log async sessions. */
    private static final boolean LOG_ASYNC_SESSIONS = true;

    /** The log authentification. */
    private static final boolean LOG_AUTHENTIFICATION = true;

    /** The log communication. */
    private static final boolean LOG_COMMUNICATION = true;

    /** The log database. */
    private static final boolean LOG_DATABASE = true;

    /** The log gamepool. */
    private static final boolean LOG_GAMEPOOL = true;

    /** The log request header. */
    private static final boolean LOG_REQUEST_HEADER = true;

    /** The log requested uri. */
    private static final boolean LOG_REQUESTED_URI = false;

    /** The log threads. */
    private static final boolean LOG_THREADS = true;

    /**
     * Gets the db driver.
     *
     * @return the db driver
     */
    public static final String getDbDriver() {
        return InternalConfig.DB_DRIVER;
    }

    /**
     * Gets the db name.
     *
     * @return the db name
     */
    public static final String getDbName() {
        return InternalConfig.DB_NAME;
    }

    /**
     * Gets the db password.
     *
     * @return the db password
     */
    public static final String getDbPassword() {
        return InternalConfig.DB_PASSWORD;
    }

    /**
     * Gets the db protocol.
     *
     * @return the db protocol
     */
    public static final String getDbProtocol() {
        return InternalConfig.DB_PROTOCOL;
    }

    /**
     * Gets the db server.
     *
     * @return the db server
     */
    public static final String getDbServer() {
        return InternalConfig.DB_SERVER;
    }

    /**
     * Gets the db user.
     *
     * @return the db user
     */
    public static final String getDbUser() {
        return InternalConfig.DB_USER;
    }

    /**
     * Gets the log.
     *
     * @return the log
     */
    public static final Logger getLog() {
        return InternalConfig.log;
    }

    /**
     * Checks if is log async sessions.
     *
     * @return true, if is log async sessions
     */
    public static final boolean isLogAsyncSessions() {
        return InternalConfig.LOG_ASYNC_SESSIONS;
    }

    /**
     * Checks if is log authentification.
     *
     * @return true, if is log authentification
     */
    public static final boolean isLogAuthentification() {
        return InternalConfig.LOG_AUTHENTIFICATION;
    }

    /**
     * Checks if is log communication.
     *
     * @return true, if is log communication
     */
    public static final boolean isLogCommunication() {
        return InternalConfig.LOG_COMMUNICATION;
    }

    /**
     * Checks if is log database.
     *
     * @return true, if is log database
     */
    public static final boolean isLogDatabase() {
        return InternalConfig.LOG_DATABASE;
    }

    /**
     * Checks if is log gamepool.
     *
     * @return true, if is log gamepool
     */
    public static final boolean isLogGamepool() {
        return InternalConfig.LOG_GAMEPOOL;
    }

    /**
     * Checks if is log requested uri.
     *
     * @return true, if is log requested uri
     */
    public static final boolean isLogRequestedUri() {
        return InternalConfig.LOG_REQUESTED_URI;
    }

    /**
     * Checks if is log request header.
     *
     * @return true, if is log request header
     */
    public static final boolean isLogRequestHeader() {
        return InternalConfig.LOG_REQUEST_HEADER;
    }

    /**
     * Checks if is log threads.
     *
     * @return true, if is log threads
     */
    public static final boolean isLogThreads() {
        return InternalConfig.LOG_THREADS;
    }

}
