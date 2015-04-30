package de.unibox.model.database.objects;

import java.sql.ResultSet;
import java.sql.SQLException;

import de.unibox.model.database.DatabaseAction;
import de.unibox.model.database.DatabaseQuery;

/**
 * The Class SelectionQuery.
 */
public class SelectionQuery extends DatabaseAction<ResultSet> {

	/**
	 * Instantiates a new selection query.
	 *
	 * @param thisStatement
	 *            the this statement
	 */
	public SelectionQuery(final String thisStatement) {
		super(thisStatement);
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
		super.attachQuery(transaction);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.unibox.model.database.DatabaseAction#execute()
	 */
	@Override
	public ResultSet execute() throws SQLException {
		return super.executeQuery();
	}

}
