package de.unibox.http.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.unibox.config.InternalConfig;
import de.unibox.core.network.object.CommunicatorMessage;
import de.unibox.core.network.object.MessageType;
import de.unibox.http.servlet.comet.Communicator;
import de.unibox.http.servlet.type.ProtectedHttpServlet;
import de.unibox.model.game.Game;
import de.unibox.model.game.GamePool;
import de.unibox.model.user.AbstractUser;

/**
 * The Class DashboardHandler defines and handles game tasks.
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

        final PrintWriter out = response.getWriter();

        final String action = request.getParameter("action");
        Integer gameId = null;
        Game game = null;

        boolean done = false;

        String errorMessage = "unknown_error";

        if (action != null) {

            if (action.equals("whichGame")) {
                game = GamePool.getInstance().getGameByPlayer(super.thisUser);
                if (game != null) {
                    gameId = game.getGameId();
                } else {
                    gameId = null;
                }
                done = true;

            } else {

                gameId = Integer.parseInt(request.getParameter("gameId"));
                game = GamePool.getInstance().getGame(gameId);

                if (game != null) {

                    if (action.equals("joinGame")) {

                        this.log.debug(this.getClass().getSimpleName()
                                + ": switching " + super.thisUser.getName()
                                + " with " + action + " for " + game);

                        final Game prevGame = GamePool.getInstance()
                                .getGameByPlayer(super.thisUser);
                        if (prevGame == null) {
                            done = game.addPlayer(super.thisUser);
                            this.sendUpdateBroadcast(super.thisUser);
                        } else if (prevGame != game) {
                            prevGame.removePlayer(super.thisUser);
                            done = game.addPlayer(super.thisUser);
                            this.sendUpdateBroadcast(super.thisUser);
                        } else {
                            errorMessage = "already_joined";
                        }

                    } else if (action.equals("leaveGame")) {

                        if (InternalConfig.isLogGamepool()) {
                            this.log.debug(this.getClass().getSimpleName()
                                    + ": switching " + super.thisUser.getName()
                                    + " with " + action);
                        }

                        done = game.removePlayer(super.thisUser);
                        this.sendUpdateBroadcast(super.thisUser);

                    } else {
                        if (InternalConfig.isLogGamepool()) {
                            this.log.debug(this.getClass().getSimpleName()
                                    + ": could not retrieve relevant game");
                        }
                    }

                } else {
                    this.serviceErrorMessage(response, "invalid_GameID");
                }

            }

            if (done) {
                response.setStatus(HttpServletResponse.SC_OK);
                if (gameId == null) {
                    out.print("success");
                } else {
                    out.print("gameId:" + gameId);
                }
            } else {
                this.serviceErrorMessage(response, errorMessage);
            }

            if (InternalConfig.isLogGamepool()) {
                if (game != null) {
                    this.log.debug(this.getClass().getSimpleName() + ": "
                            + game);
                }
            }

            out.flush();
            out.close();

        } else {
            this.serviceDenied(request, response);
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
        this.doGet(request, response);
    }

    /**
     * Send update broadcast.
     *
     * @param user
     *            the user
     */
    private void sendUpdateBroadcast(final AbstractUser user) {
        // send update game table broadcast
        Communicator.getMessagequeue().add(
                new CommunicatorMessage(MessageType.JS_COMMAND, user.getName(),
                        "window.parent.app.updateGameTable();"));
    }

}
