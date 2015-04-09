package de.unibox.http.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.unibox.config.InternalConfig;
import de.unibox.http.servlet.type.ProtectedHttpServlet;
import de.unibox.model.game.Game;
import de.unibox.model.game.GamePool;
import de.unibox.model.user.AbstractUser;

/**
 * The Class DashboardHandler.
 */
@WebServlet("/Game")
public class GameHandler extends ProtectedHttpServlet {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -3278754155030900080L;

    /**
     * Instantiates a new dashboard handler.
     */
    public GameHandler() {
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

        final AbstractUser user = (AbstractUser) request.getSession()
                .getAttribute("login.object");

        String action = null;
        int gameId = 0;
        Game game = null;

        try {
            action = request.getParameter("action");

            if (!action.equals("whichgame")) {
                gameId = Integer.parseInt(request.getParameter("gameid"));
                game = GamePool.getInstance().getGame(gameId);

                if (null == game) {
                    throw new Exception("Could not find game with id: "
                            + gameId);
                }
            }

        } catch (final Exception e) {
            this.log.debug("GameHandler: could not retrieve relevant game");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            out.print("invalid request\n");
            e.printStackTrace();
        } finally {

            if (InternalConfig.LOG_GAMEPOOL) {
                if (game == null) {
                    this.log.debug("GameHandler: switching " + user.getName()
                            + " with " + action);
                } else {
                    this.log.debug("GameHandler: switching " + user.getName()
                            + " with " + action + " for " + game);
                }
            }

            String errorMessage = "unknown_error";
            Integer gameid = null;

            boolean done = false;
            switch (action) {
            case "join":
                Game prevGame = GamePool.getInstance().getGameByPlayer(user);
                if (prevGame == null) {
                    done = game.addPlayer(user);
                } else if (prevGame != game) {
                    prevGame.removePlayer(user);
                    done = game.addPlayer(user);
                } else {
                    errorMessage = "skipped:already_joined";
                }
                break;
            case "leave":
                done = game.removePlayer(user);
                break;
            case "whichgame":
                game = GamePool.getInstance().getGameByPlayer(user);
                if (game != null) {
                    gameid = game.getGameId();
                } else {
                    gameid = null;
                }
                done = true;
                break;
            default:
                break;
            }

            if (done) {
                response.setStatus(HttpServletResponse.SC_OK);
                if (gameid == null) {
                    out.print("success");
                } else {
                    out.print("gameid:" + gameid);
                }
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print(errorMessage);
            }

            if (InternalConfig.LOG_GAMEPOOL) {
                this.log.debug("GameHandler: " + game);
            }
        }

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
