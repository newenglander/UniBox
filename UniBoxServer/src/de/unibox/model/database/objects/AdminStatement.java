package de.unibox.model.database.objects;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import de.unibox.config.InternalConfig;
import de.unibox.model.database.DatabaseAction;
import de.unibox.model.database.DatabaseQuery;
import de.unibox.model.user.AbstractUser;
import de.unibox.model.user.AdministratorUser;

/**
 * The Class AdminStatement.
 */
public class AdminStatement extends DatabaseAction<Integer> {

	/** The log. */
	protected Logger log = Logger.getLogger("UniBoxLogger");

	/** The user. */
	private AbstractUser user = null;

	/**
	 * Instantiates a new admin statement.
	 *
	 * @param thisUser
	 *            the this user
	 * @param thisStatement
	 *            the this statement
	 */
	public AdminStatement(final AbstractUser thisUser,
			final String thisStatement) {
		super(thisStatement);
		this.user = thisUser;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.unibox.model.database.DatabaseAction#attach(de.unibox.model.database
	 * .DatabaseQuery)
	 */
	@Override
	public void attach(final DatabaseQuery transaction) throws SQLException {
		super.attachUpdate(transaction);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.unibox.model.database.DatabaseAction#execute()
	 */
	@Override
	public Integer execute() throws SQLException, IllegalAccessError {
		if (this.user != null) {
			if (this.user instanceof AdministratorUser) {
				if (InternalConfig.isLogDatabase()) {
					this.log.info(AdminStatement.class.getSimpleName()
							+ ": execute " + this);
				}
				return super.executeUpdate();
			} else {
				if (InternalConfig.isLogDatabase()) {
					this.log.info(AdminStatement.class.getSimpleName()
							+ ": denied access for " + this);
				}
				this.noPrivilegs("no admin privilegs for: "
						+ super.getStatement());
			}
		} else {
			if (InternalConfig.isLogDatabase()) {
				this.log.info(AdminStatement.class.getSimpleName()
						+ ": denied access for " + this);
			}
			this.noPrivilegs("user is NULL for: " + super.getStatement());
		}
		return null;
	}

	/**
	 * No privilegs.
	 *
	 * @param errorMessage
	 *            the error message
	 */
	private void noPrivilegs(final String errorMessage) {
		throw new IllegalAccessError(errorMessage);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.unibox.model.database.DatabaseAction#toString()
	 */
	@Override
	public String toString() {
		return "AdminStatement [user=" + this.user + "]";
	}

}