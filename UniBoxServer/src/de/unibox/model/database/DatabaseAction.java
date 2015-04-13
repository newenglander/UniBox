package de.unibox.model.database;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

/**
 * The Class DatabaseAction.
 *
 * @param <T>
 *            the generic type
 */
public abstract class DatabaseAction<T> {
    
    /** The log. */
    protected Logger log = Logger.getLogger("UniBoxLogger");


    /** The sql string. */
    private String sqlString = null;

    /** The statement. */
    private PreparedStatement statement = null;

    /**
     * Instantiates a new database action.
     *
     * @param thisSqlString
     *            the this sql string
     */
    protected DatabaseAction(final String thisSqlString) {
        this.sqlString = thisSqlString;
    }

    /**
     * Attach.
     *
     * @param transaction
     *            the transaction
     * @throws SQLException
     *             the SQL exception
     */
    public abstract void attach(DatabaseQuery transaction) throws SQLException;

    /**
     * Attach query.
     *
     * @param transaction
     *            the transaction
     * @throws SQLException
     *             the SQL exception
     */
    protected void attachQuery(final DatabaseQuery transaction)
            throws SQLException {
        if (this.sqlString != null) {
            this.statement = transaction.getQuery(this.sqlString);
        } else {
            throw new SQLException(this.getClass().getSimpleName()
                    + ".sqlString is null!");
        }
    }

    /**
     * Attach update.
     *
     * @param transaction
     *            the transaction
     * @throws SQLException
     *             the SQL exception
     */
    protected void attachUpdate(final DatabaseQuery transaction)
            throws SQLException {
        if (this.sqlString != null) {
            this.statement = transaction.getUpdate(this.sqlString);
        } else {
            throw new SQLException(this.getClass().getSimpleName()
                    + ".sqlString is null!");
        }
    }

    /**
     * Execute.
     *
     * @return the t
     * @throws SQLException
     *             the SQL exception
     */
    public abstract T execute() throws SQLException;

    /**
     * Execute query.
     *
     * @return the result set
     * @throws SQLException
     *             the SQL exception
     */
    protected ResultSet executeQuery() throws SQLException {
        ResultSet returnThis = null;
        if (!this.gotNullMembers()) {
            returnThis = this.getStatement().executeQuery();
        }
        return returnThis;
    }

    /**
     * Execute update.
     *
     * @return the int
     * @throws SQLException
     *             the SQL exception
     */
    protected int executeUpdate() throws SQLException {
        int returnThis = 0;
        if (!this.gotNullMembers()) {
            returnThis = this.getStatement().executeUpdate();
        }
        return returnThis;
    }

    public final String getSqlString() {
        return this.sqlString;
    }

    public final PreparedStatement getStatement() {
        return this.statement;
    }

    /**
     * Got null members.
     *
     * @return true, if successful
     * @throws SQLException
     *             the SQL exception
     */
    protected boolean gotNullMembers() throws SQLException {
        boolean gotNullValues = false;
        final Field[] allFields = this.getClass().getDeclaredFields();
        for (final Field field : allFields) {

            // allow accessing private members
            field.setAccessible(true);

            if (Modifier.isPrivate(field.getModifiers())) {
                final Class<?> t = field.getType();
                Object v;
                try {
                    v = field.get(this);
                } catch (final IllegalArgumentException e) {
                    e.printStackTrace();
                    throw new SQLException("Not able to validate "
                            + this.getClass().getSimpleName() + "."
                            + field.getName());
                } catch (final IllegalAccessException e) {
                    e.printStackTrace();
                    throw new SQLException("Not able to validate "
                            + this.getClass().getSimpleName() + "."
                            + field.getName());
                }
                if (t.isPrimitive()) {
                    if ((t == boolean.class) && (v != null)) {
                        continue;
                    } else if (((Number) v).doubleValue() == 0) {
                        gotNullValues = true;
                        throw new SQLException(this.getClass().getSimpleName()
                                + "." + field.getName() + " is null!");
                    }
                } else if (!t.isPrimitive() && (v == null)) {
                    gotNullValues = true;
                    throw new SQLException(this.getClass().getSimpleName()
                            + "." + field.getName() + " is null!");
                }
            }
        }
        return gotNullValues;
    }

    public final void setSqlString(final String sqlString) {
        this.sqlString = sqlString;
    }

    public final void setStatement(final PreparedStatement statement) {
        this.statement = statement;
    }

}
