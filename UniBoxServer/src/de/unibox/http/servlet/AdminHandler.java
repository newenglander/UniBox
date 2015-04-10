package de.unibox.http.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.unibox.http.servlet.type.ProtectedHttpServlet;
import de.unibox.model.database.DatabaseQuery;
import de.unibox.model.database.objects.SelectionQuery;

/**
 * The Class AdminHandler.
 */
@WebServlet("/Admin")
public class AdminHandler extends ProtectedHttpServlet {

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

        response.setContentType("text/html");
        final PrintWriter out = response.getWriter();

        // final AbstractUser user = (AbstractUser) request.getSession()
        // .getAttribute("login.object");

        SelectionQuery query = null;

        try {

            query = new SelectionQuery(
                    "SELECT GameID, GameName, Gametitle, NumberOfPlayers FROM game INNER JOIN category WHERE game.CatID=category.CatID;");

            final DatabaseQuery transaction = new DatabaseQuery();
            transaction.connect();
            query.attach(transaction);
            // final ResultSet resultSet = query.execute();

            transaction.commit();

        } catch (final SQLException e) {
            e.printStackTrace();
        }

        response.setStatus(HttpServletResponse.SC_OK);

        out.flush();
        out.close();

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
        this.doGet(request, response);
    }

}
