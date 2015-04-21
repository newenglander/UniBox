package de.unibox.model.database;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

/**
 * Tool to run database scripts. This version of the script can be found at
 * https://gist.github.com/gists/831762/
 *
 * EDITED: Replaced logWriter* with UniBox Logger.
 */
public class CustomScriptRunner {

    /** The Constant DEFAULT_DELIMITER. */
    private static final String DEFAULT_DELIMITER = ";";

    /** The Constant DELIMITER_LINE_REGEX. */
    private static final String DELIMITER_LINE_REGEX = "(?i)DELIMITER.+";

    /** The Constant DELIMITER_LINE_SPLIT_REGEX. */
    private static final String DELIMITER_LINE_SPLIT_REGEX = "(?i)DELIMITER";

    /** The auto commit. */
    private final boolean autoCommit;

    /** The connection. */
    private final Connection connection;

    /** The delimiter. */
    private String delimiter = CustomScriptRunner.DEFAULT_DELIMITER;

    /** The full line delimiter. */
    private boolean fullLineDelimiter = false;

    /** The log. */
    protected Logger log = Logger.getLogger("UniBoxLogger");

    /** The stop on error. */
    private final boolean stopOnError;

    /**
     * Default constructor.
     *
     * @param connection
     *            the connection
     * @param autoCommit
     *            the auto commit
     * @param stopOnError
     *            the stop on error
     */
    public CustomScriptRunner(final Connection connection,
            final boolean autoCommit, final boolean stopOnError) {
        this.connection = connection;
        this.autoCommit = autoCommit;
        this.stopOnError = stopOnError;
    }

    private String getDelimiter() {
        return this.delimiter;
    }

    /**
     * Runs an SQL script (read in using the Reader parameter) using the
     * connection passed in.
     *
     * @param conn
     *            - the connection to use for the script
     * @param reader
     *            - the source of the script
     * @throws IOException
     *             if there is an error reading from the Reader
     * @throws SQLException
     *             if any SQL errors occur
     */
    private void runScript(final Connection conn, final Reader reader)
            throws IOException, SQLException {
        StringBuffer command = null;
        try {
            final LineNumberReader lineReader = new LineNumberReader(reader);
            String line = null;
            while ((line = lineReader.readLine()) != null) {
                if (command == null) {
                    command = new StringBuffer();
                }
                String trimmedLine = line.trim();
                if (trimmedLine.startsWith("--")) {
                    this.log.debug(trimmedLine);
                } else if ((trimmedLine.length() < 1)
                        || trimmedLine.startsWith("//")) {
                    // Do nothing
                } else if ((trimmedLine.length() < 1)
                        || trimmedLine.startsWith("--")) {
                    // Do nothing
                } else if ((!this.fullLineDelimiter && trimmedLine
                        .endsWith(this.getDelimiter()))
                        || (this.fullLineDelimiter && trimmedLine.equals(this
                                .getDelimiter()))) {

                    final Pattern pattern = Pattern
                            .compile(CustomScriptRunner.DELIMITER_LINE_REGEX);
                    final Matcher matcher = pattern.matcher(trimmedLine);
                    if (matcher.matches()) {
                        this.setDelimiter(
                                trimmedLine
                                        .split(CustomScriptRunner.DELIMITER_LINE_SPLIT_REGEX)[1]
                                        .trim(), this.fullLineDelimiter);
                        line = lineReader.readLine();
                        if (line == null) {
                            break;
                        }
                        trimmedLine = line.trim();
                    }

                    command.append(line.substring(0,
                            line.lastIndexOf(this.getDelimiter())));
                    command.append(" ");
                    final Statement statement = conn.createStatement();

                    this.log.debug(command);

                    boolean hasResults = false;
                    if (this.stopOnError) {
                        hasResults = statement.execute(command.toString());
                    } else {
                        try {
                            statement.execute(command.toString());
                        } catch (final SQLException e) {
                            e.fillInStackTrace();
                            this.log.debug("Error executing: " + command);
                            this.log.debug(e);
                        }
                    }

                    if (this.autoCommit && !conn.getAutoCommit()) {
                        conn.commit();
                    }

                    final ResultSet rs = statement.getResultSet();
                    if (hasResults && (rs != null)) {
                        final ResultSetMetaData md = rs.getMetaData();
                        final int cols = md.getColumnCount();
                        for (int i = 0; i < cols; i++) {
                            final String name = md.getColumnLabel(i);
                            this.log.debug(name + "\t");
                        }
                        this.log.debug("");
                        while (rs.next()) {
                            for (int i = 1; i <= cols; i++) {
                                final String value = rs.getString(i);
                                this.log.debug(value + "\t");
                            }
                            this.log.debug("");
                        }
                    }

                    command = null;
                    try {
                        if (rs != null) {
                            rs.close();
                        }
                    } catch (final Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        if (statement != null) {
                            statement.close();
                        }
                    } catch (final Exception e) {
                        e.printStackTrace();
                        // Ignore to workaround a bug in Jakarta DBCP
                    }
                } else {
                    final Pattern pattern = Pattern
                            .compile(CustomScriptRunner.DELIMITER_LINE_REGEX);
                    final Matcher matcher = pattern.matcher(trimmedLine);
                    if (matcher.matches()) {
                        this.setDelimiter(
                                trimmedLine
                                        .split(CustomScriptRunner.DELIMITER_LINE_SPLIT_REGEX)[1]
                                        .trim(), this.fullLineDelimiter);
                        line = lineReader.readLine();
                        if (line == null) {
                            break;
                        }
                        trimmedLine = line.trim();
                    }
                    command.append(line);
                    command.append(" ");
                }
            }
            if (!this.autoCommit) {
                conn.commit();
            }
        } catch (final SQLException e) {
            e.fillInStackTrace();
            this.log.debug("Error executing: " + command);
            this.log.debug(e);
            throw e;
        } catch (final IOException e) {
            e.fillInStackTrace();
            this.log.debug("Error executing: " + command);
            this.log.debug(e);
            throw e;
        } finally {
            conn.rollback();
        }
    }

    /**
     * Runs an SQL script (read in using the Reader parameter).
     *
     * @param reader
     *            - the source of the script
     * @throws IOException
     *             if there is an error reading from the Reader
     * @throws SQLException
     *             if any SQL errors occur
     */
    public void runScript(final Reader reader) throws IOException, SQLException {
        try {
            final boolean originalAutoCommit = this.connection.getAutoCommit();
            try {
                if (originalAutoCommit != this.autoCommit) {
                    this.connection.setAutoCommit(this.autoCommit);
                }
                this.runScript(this.connection, reader);
            } finally {
                this.connection.setAutoCommit(originalAutoCommit);
            }
        } catch (final IOException e) {
            throw e;
        } catch (final SQLException e) {
            throw e;
        } catch (final Exception e) {
            throw new RuntimeException("Error running script.  Cause: " + e, e);
        }
    }

    /**
     * Sets the delimiter.
     *
     * @param delimiter
     *            the delimiter
     * @param fullLineDelimiter
     *            the full line delimiter
     */
    public void setDelimiter(final String delimiter,
            final boolean fullLineDelimiter) {
        this.delimiter = delimiter;
        this.fullLineDelimiter = fullLineDelimiter;
    }

}