package de.unibox.http.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.unibox.config.InternalConfig;
import de.unibox.core.provider.Helper;
import de.unibox.http.servlet.type.AdminHttpServlet;
import de.unibox.model.database.DatabaseAction;
import de.unibox.model.database.DatabaseQuery;
import de.unibox.model.database.objects.CategoryInsert;
import de.unibox.model.database.objects.PlayerInsert;

/**
 * The Class AdminHandler.
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

        final PrintWriter out = response.getWriter();

        out.print("OK");

        out.flush();
        out.close();

        // TODO implement functions displayed on the admin dashboard.

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

        final String createData = request.getParameter("create");
        boolean doInsert = false;
        Integer result = null;

        DatabaseAction<Integer> query = null;

        if (createData != null) {
            if (createData.equals("player")) {

                if (InternalConfig.LOG_DATABASE) {
                    this.log.debug(this.getClass().getSimpleName()
                            + ": update player table..");
                }

                final String thisName = Helper.decodeBase64(request
                        .getParameter("name"));
                final int thisAdminRights = Integer.parseInt(request
                        .getParameter("adminrights"));

                // NOTE: avoid hardcoded default password
                final String thisPassword = "3022443b7e33a6a68756047e46b81bea";

                query = new PlayerInsert(thisAdminRights, thisName,
                        thisPassword);
                doInsert = true;

            } else if (createData.equals("category")) {

                if (InternalConfig.LOG_DATABASE) {
                    this.log.debug(this.getClass().getSimpleName()
                            + ": update category table..");
                }

                final String thisGametitle = request.getParameter("gametitle");
                final int thisNumberOfPlayers = Integer.parseInt(request
                        .getParameter("numberofplayers"));

                query = new CategoryInsert(thisGametitle, thisNumberOfPlayers);
                doInsert = true;

                // } else if (createData.equals("queue")) {
                //
                // if (InternalConfig.LOG_DATABASE) {
                // this.log.debug("DatabaseHandler: update game table..");
                // }
                //
                // final int thisPlayerID = Integer.parseInt(request
                // .getParameter("playerid"));
                // final int thisGameID = Integer.parseInt(request
                // .getParameter("gameid"));
                //
                // query = new QueueInsert(thisPlayerID, thisGameID);
                // doInsert = true;

            }

            if (doInsert && (query != null)) {

                try {

                    final DatabaseQuery transaction = new DatabaseQuery();

                    transaction.connect();
                    query.attach(transaction);
                    result = query.execute();

                    transaction.commit();

                    // /** update model */
                    // if (createData.equals("game")) {
                    // GamePool.getInstance().update();
                    // }

                } catch (final SQLException e) {

                    if (InternalConfig.LOG_DATABASE) {
                        this.log.debug(this.getClass().getSimpleName()
                                + ": Could not update database: "
                                + query.getSqlString());
                    }
                    e.printStackTrace();
                }

                response.setContentType("text/html");
                response.setStatus(HttpServletResponse.SC_CREATED);
                final PrintWriter out = response.getWriter();
                out.print("database updated with state: " + result);
                out.flush();

            } else {
                super.serviceDenied(request, response);
            }
        } else {
            super.invalidRequest(request, response);
        }

    }

}
