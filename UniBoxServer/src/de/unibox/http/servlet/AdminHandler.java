package de.unibox.http.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.unibox.config.InternalConfig;
import de.unibox.core.network.object.CommunicatorMessage;
import de.unibox.core.network.object.CommunicatorMessageType;
import de.unibox.core.provider.Helper;
import de.unibox.http.servlet.comet.Communicator;
import de.unibox.http.servlet.type.AdminHttpServlet;
import de.unibox.model.database.CustomScriptRunner;
import de.unibox.model.database.DatabaseAction;
import de.unibox.model.database.DatabaseQuery;
import de.unibox.model.database.objects.AdminStatement;
import de.unibox.model.database.objects.CategoryInsert;
import de.unibox.model.database.objects.PlayerInsert;
import de.unibox.model.user.UserFactory;

/**
 * The Class AdminHandler defines and handles administrative tasks.
 */
@WebServlet("/Admin")
public class AdminHandler extends AdminHttpServlet {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -6489040877604784432L;

    /**
     * Instantiates a new admin handler.
     */
    public AdminHandler() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest
     * , javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(final HttpServletRequest request,
            final HttpServletResponse response) throws ServletException,
            IOException {

        final String action = request.getParameter("action");
        boolean isComplete = true;
        int result = 0;

        final String errorMessage = "illegal_Request";

        DatabaseAction<Integer> query = null;

        if (action != null) {

            if (action.equals("deleteGame")) {

                if (InternalConfig.isLogDatabase()) {
                    this.log.debug(this.getClass().getSimpleName()
                            + ": delete row of game table..");
                }

                final Integer thisGameId = Integer.parseInt(request
                        .getParameter("gameId"));

                if (thisGameId != null) {
                    query = new AdminStatement(super.thisUser,
                            "DELETE FROM game WHERE GameID = ? LIMIT 1;");
                    try {
                        final DatabaseQuery transaction = new DatabaseQuery();

                        transaction.connect();
                        query.attach(transaction);
                        query.getStatement().setInt(1, thisGameId);
                        result = query.execute();
                        transaction.commit();

                        // force clients to update game list
                        Communicator
                                .getMessagequeue()
                                .add(new CommunicatorMessage(
                                        CommunicatorMessageType.JS_COMMAND, "ALL",
                                        "window.parent.app.updateGameTable();"));

                    } catch (final SQLException e) {
                        if (InternalConfig.isLogDatabase()) {
                            this.log.debug(this.getClass().getSimpleName()
                                    + ": Could not update database: "
                                    + query.getSqlString());
                            e.printStackTrace();
                        }
                        isComplete = false;
                    }
                }
            } else if (action.equals("deleteUser")) {

                if (InternalConfig.isLogDatabase()) {
                    this.log.debug(this.getClass().getSimpleName()
                            + ": delete row of player table..");
                }

                final Integer thisPlayerId = Integer.parseInt(request
                        .getParameter("userId"));

                if (thisPlayerId != null) {
                    query = new AdminStatement(super.thisUser,
                            "DELETE FROM player WHERE PlayerID = ? LIMIT 1;");
                    try {
                        final DatabaseQuery transaction = new DatabaseQuery();

                        transaction.connect();
                        query.attach(transaction);
                        query.getStatement().setInt(1, thisPlayerId);
                        result = query.execute();
                        transaction.commit();

                        // force clients to update ranking table
                        Communicator
                                .getMessagequeue()
                                .add(new CommunicatorMessage(
                                        CommunicatorMessageType.JS_COMMAND, "ALL",
                                        "window.parent.app.updateRankingTable();"));

                    } catch (final SQLException e) {
                        if (InternalConfig.isLogDatabase()) {
                            this.log.debug(this.getClass().getSimpleName()
                                    + ": Could not update database: "
                                    + query.getSqlString());
                            e.printStackTrace();
                        }
                        isComplete = false;
                    }
                }
            } else if (action.equals("resetDatabase")) {

                if (InternalConfig.isLogDatabase()) {
                    this.log.debug(this.getClass().getSimpleName()
                            + ": Reset database. Drop all tables and insert default values.");
                }

                try {
                    final DatabaseQuery transaction = new DatabaseQuery();

                    final ServletContext context = this.getServletContext();

                    transaction.connect();
                    // execute sql scripts/files like this
                    final CustomScriptRunner runner = new CustomScriptRunner(
                            transaction.getConnection(), false, false);
                    runner.runScript(this.getReader(context
                            .getResourceAsStream("/WEB-INF/database/UniBoxCreate.sql")));
                    runner.runScript(this.getReader(context
                            .getResourceAsStream("/WEB-INF/database/UniBoxInserts.sql")));
                    transaction.commit();

                    // force all other clients to logout after Database drop
                    // (not yourself!)
                    Communicator.getMessagequeue().add(
                            new CommunicatorMessage(CommunicatorMessageType.JS_COMMAND,
                                    super.thisUser.getName(),
                                    "window.parent.app.logout();"));

                } catch (final SQLException e) {
                    if (InternalConfig.isLogDatabase()) {
                        this.log.debug(this.getClass().getSimpleName()
                                + ": Could not reset database: " + e.toString());
                        e.printStackTrace();
                    }
                    isComplete = false;
                }
            } else if (action.equals("resetScores")) {

                if (InternalConfig.isLogDatabase()) {
                    this.log.debug(this.getClass().getSimpleName()
                            + ": Reset score table.");
                }

                try {
                    final DatabaseQuery transaction = new DatabaseQuery();

                    query = new AdminStatement(super.thisUser,
                            "TRUNCATE result;");

                    transaction.connect();
                    query.attach(transaction);
                    result = query.execute();
                    transaction.commit();

                    // force clients to update rankings
                    Communicator.getMessagequeue().add(
                            new CommunicatorMessage(CommunicatorMessageType.JS_COMMAND,
                                    "ALL",
                                    "window.parent.app.updateRankingTable();"));

                } catch (final SQLException e) {
                    if (InternalConfig.isLogDatabase()) {
                        this.log.debug(this.getClass().getSimpleName()
                                + ": Could not reset scores: "
                                + query.getSqlString());
                        e.printStackTrace();
                    }
                    isComplete = false;
                }
            }

            if (isComplete) {

                response.setContentType("text/html");
                response.setStatus(HttpServletResponse.SC_CREATED);
                final PrintWriter out = response.getWriter();
                out.print("affected_rows:" + result);
                out.flush();

            } else {
                super.serviceErrorMessage(response, errorMessage);
            }

        } else {
            super.serviceDenied(request, response);
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest
     * , javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doPost(final HttpServletRequest request,
            final HttpServletResponse response) throws ServletException,
            IOException {

        final String action = request.getParameter("action");
        boolean doInsert = false;
        Integer result = null;

        String errorMessage = "illegal_Request";

        DatabaseAction<Integer> query = null;

        if (action != null) {
            if (action.equals("createUser")) {

                if (InternalConfig.isLogDatabase()) {
                    this.log.debug(this.getClass().getSimpleName()
                            + ": update player table..");
                }

                final String thisName = Helper.decodeBase64(request
                        .getParameter("name"));
                final Integer thisAdminRights = Integer.parseInt(request
                        .getParameter("adminRights"));

                // NOTE: avoid hardcoded default password
                final String thisPassword = "3022443b7e33a6a68756047e46b81bea";

                if ((thisName != null) && (thisAdminRights != null)) {
                    query = new PlayerInsert(thisAdminRights, thisName,
                            thisPassword);
                    // test if username exists
                    if (UserFactory.getUserByName(thisName) == null) {
                        doInsert = true;
                    } else {
                        errorMessage = "duplicate:user_exists";
                    }
                }

            } else if (action.equals("createCategory")) {

                if (InternalConfig.isLogDatabase()) {
                    this.log.debug(this.getClass().getSimpleName()
                            + ": update category table..");
                }

                final String thisGameTitle = request.getParameter("gameTitle");
                final Integer thisNumberOfPlayers = Integer.parseInt(request
                        .getParameter("numberOfPlayers"));
                
                if ((thisGameTitle != null) && (thisNumberOfPlayers != null)) {
                    query = new CategoryInsert(thisGameTitle,
                            thisNumberOfPlayers);
                    doInsert = true;
                }

            }

            if (doInsert && (query != null)) {

                boolean isComplete = true;

                try {

                    final DatabaseQuery transaction = new DatabaseQuery();

                    transaction.connect();
                    query.attach(transaction);
                    result = query.execute();
                    transaction.commit();

                    if (action.equals("createCategory")) {
                        // force clients to update create game formular
                        Communicator
                                .getMessagequeue()
                                .add(new CommunicatorMessage(
                                        CommunicatorMessageType.JS_COMMAND, "ALL",
                                        "window.parent.app.updateFormulars();"));
                    }
                    if (action.equals("createUser")) {
                        // force clients to update users list
                        Communicator
                                .getMessagequeue()
                                .add(new CommunicatorMessage(
                                        CommunicatorMessageType.JS_COMMAND, "ALL",
                                        "window.parent.app.updateUsersSelection();"));
                    }

                } catch (final SQLException e) {

                    if (InternalConfig.isLogDatabase()) {
                        this.log.debug(this.getClass().getSimpleName()
                                + ": Could not update database: "
                                + query.getSqlString());
                    }
                    e.printStackTrace();
                    errorMessage = "SQL_error";
                    isComplete = false;
                }

                if (isComplete) {

                    response.setContentType("text/html");
                    response.setStatus(HttpServletResponse.SC_CREATED);
                    final PrintWriter out = response.getWriter();
                    out.print("affected_rows:" + result);
                    out.flush();

                } else {
                    super.serviceErrorMessage(response, errorMessage);
                }
            } else {
                super.serviceErrorMessage(response, errorMessage);
            }
        } else {
            super.invalidRequest(request, response);
        }

    }

    /**
     * Gets the reader.
     *
     * @param thisStream
     *            the this stream
     * @return the reader
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private BufferedReader getReader(final InputStream thisStream)
            throws IOException {
        return new BufferedReader(new InputStreamReader(thisStream));
    }

}
