package de.unibox.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * Use this Class to configure more then just the logging level. You can
 * activate logging by depending on your functional level. Furthermore you are
 * able to set the database credentials and host directly here.
 */
public class InternalConfig {

	/** The Constant DRIVER. */
    private static String DB_DRIVER = "com.mysql.jdbc.Driver";

    /** The Constant DB_NAME. */
    private static String DB_NAME = "unibox";

    /** The Constant DB_PASSWORD. */
    private static String DB_PASSWORD = "root";

    /** The Constant PROTOCOL. */
    private static String DB_PROTOCOL = "jdbc:mysql:";

    /** The Constant DB_SERVER. */
    private static String DB_SERVER = "localhost";

    /** The Constant DB_USER. */
    private static String DB_USER = "root";

    /** The log. */
    private static Logger log = Logger.getLogger("UniBoxLogger");

    /** The log async sessions. */
    private static boolean LOG_ASYNC_SESSIONS = true;

    /** The log authentification. */
    private static boolean LOG_AUTHENTIFICATION = true;

    /** The log communication. */
    private static boolean LOG_COMMUNICATION = true;

    /** The log database. */
    private static boolean LOG_DATABASE = true;

    /** The log gamepool. */
    private static boolean LOG_GAMEPOOL = true;

    /** The log request header. */
    private static boolean LOG_REQUEST_HEADER = true;

    /** The log requested uri. */
    private static boolean LOG_REQUESTED_URI = false;

    /** The log threads. */
    private static boolean LOG_THREADS = true;

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

	/**
	 * Load: This method is loading the configuration values out of
	 * "config/config.prop". If a value is not found, the default value will be
	 * used (2nd argument).
	 *
	 * @param file
	 *            the file
	 */
	public static void load(InputStream inputStream) {
		final Properties properties = new Properties();
		try {
			
			// load properties file
			properties.load(inputStream);
			
			// parse strings
			InternalConfig.setDbDriver(properties.getProperty("",
					"com.mysql.jdbc.Driver"));
			InternalConfig.setDbName(properties.getProperty("", "unibox"));
			InternalConfig.setDbPassword(properties.getProperty("", "root"));
			InternalConfig.setDbProtocol(properties.getProperty("",
					"jdbc:mysql:"));
			InternalConfig.setDbServer(properties.getProperty("", "localhost"));
			InternalConfig.setDbUser(properties.getProperty("", "root"));
			
			// parse booleans
			InternalConfig.setLogAsyncSessions(Boolean.parseBoolean(properties
					.getProperty("LOG_ASYNC_SESSIONS", "false")));
			InternalConfig.setLogAuthentification(Boolean
					.parseBoolean(properties.getProperty(
							"LOG_AUTHENTIFICATION", "false")));
			InternalConfig.setLogCommunication(Boolean.parseBoolean(properties
					.getProperty("LOG_COMMUNICATION", "false")));
			InternalConfig.setLogDatabase(Boolean.parseBoolean(properties
					.getProperty("LOG_DATABASE", "false")));
			InternalConfig.setLogGamepool(Boolean.parseBoolean(properties
					.getProperty("LOG_GAMEPOOL", "false")));
			InternalConfig.setLogRequestHeader(Boolean.parseBoolean(properties
					.getProperty("LOG_REQUEST_HEADER", "false")));
			InternalConfig.setLogRequestedUri(Boolean.parseBoolean(properties
					.getProperty("LOG_REQUESTED_URI", "false")));
			InternalConfig.setLogThreads(Boolean.parseBoolean(properties
					.getProperty("LOG_THREADS", "false")));
			
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	public static void setDbDriver(final String dbDriver) {
		InternalConfig.DB_DRIVER = dbDriver;
	}

	public static void setDbName(final String dbName) {
		InternalConfig.DB_NAME = dbName;
	}

	public static void setDbPassword(final String dbPassword) {
		InternalConfig.DB_PASSWORD = dbPassword;
	}

	public static void setDbProtocol(final String dbProtocol) {
		InternalConfig.DB_PROTOCOL = dbProtocol;
	}

	public static void setDbServer(final String dbServer) {
		InternalConfig.DB_SERVER = dbServer;
	}

	public static void setDbUser(final String dbUser) {
		InternalConfig.DB_USER = dbUser;
	}

	public static void setLog(final Logger log) {
		InternalConfig.log = log;
	}

	public static void setLogAsyncSessions(final boolean logAsyncSessions) {
		InternalConfig.LOG_ASYNC_SESSIONS = logAsyncSessions;
	}

	public static void setLogAuthentification(final boolean logAuthentification) {
		InternalConfig.LOG_AUTHENTIFICATION = logAuthentification;
	}

	public static void setLogCommunication(final boolean logCommunication) {
		InternalConfig.LOG_COMMUNICATION = logCommunication;
	}

	public static void setLogDatabase(final boolean logDatabase) {
		InternalConfig.LOG_DATABASE = logDatabase;
	}

	public static void setLogGamepool(final boolean logGamepool) {
		InternalConfig.LOG_GAMEPOOL = logGamepool;
	}

	public static void setLogRequestedUri(final boolean logRequestedUri) {
		InternalConfig.LOG_REQUESTED_URI = logRequestedUri;
	}

	public static void setLogRequestHeader(final boolean logRequestHeader) {
		InternalConfig.LOG_REQUEST_HEADER = logRequestHeader;
	}

	public static void setLogThreads(final boolean logThreads) {
		InternalConfig.LOG_THREADS = logThreads;
	}

}
