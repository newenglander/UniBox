package de.unibox.http.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.unibox.config.InternalConfig;
import de.unibox.core.network.object.CommunicatorMessage;
import de.unibox.core.network.object.CommunicatorMessage.MessageType;
import de.unibox.http.servlet.comet.Communicator;
import de.unibox.http.servlet.type.ProtectedHttpServlet;
import de.unibox.model.database.DatabaseAction;
import de.unibox.model.database.DatabaseQuery;
import de.unibox.model.database.objects.GameInsert;
import de.unibox.model.database.objects.ResultInsert;
import de.unibox.model.database.objects.SelectionQuery;
import de.unibox.model.game.Game;
import de.unibox.model.game.GamePool;
import de.unibox.model.game.GamePool.ScoringType;

/**
 * The Class DatabaseHandler.
 */
@WebServlet("/Database")
public class DatabaseHandler extends ProtectedHttpServlet {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -4540064364185482534L;

    /**
     * Instantiates a new database handler.
     */
    public DatabaseHandler() {
        super();
    }

    /**
     * Convert to json.
     *
     * @param rs
     *            the rs
     * @return the JSON array
     * @throws SQLException
     *             the SQL exception
     * @throws JSONException
     *             the JSON exception
     */
    private JSONArray convertToJson(final ResultSet rs) throws SQLException,
            JSONException {
        final JSONArray json = new JSONArray();
        final ResultSetMetaData rsmd = rs.getMetaData();
        while (rs.next()) {
            final int numColumns = rsmd.getColumnCount();
            final JSONObject obj = new JSONObject();

            for (int i = 1; i < (numColumns + 1); i++) {
                final String column_name = rsmd.getColumnName(i);

                switch (rsmd.getColumnType(i)) {
                case java.sql.Types.ARRAY:
                    obj.put(column_name, rs.getArray(column_name));
                    break;
                case java.sql.Types.BIGINT:
                    obj.put(column_name, rs.getInt(column_name));
                    break;
                case java.sql.Types.BOOLEAN:
                    obj.put(column_name, rs.getBoolean(column_name));
                    break;
                case java.sql.Types.BLOB:
                    obj.put(column_name, rs.getBlob(column_name));
                    break;
                case java.sql.Types.DOUBLE:
                    obj.put(column_name, rs.getDouble(column_name));
                    break;
                case java.sql.Types.FLOAT:
                    obj.put(column_name, rs.getFloat(column_name));
                    break;
                case java.sql.Types.INTEGER:
                    obj.put(column_name, rs.getInt(column_name));
                    break;
                case java.sql.Types.NVARCHAR:
                    obj.put(column_name, rs.getNString(column_name));
                    break;
                case java.sql.Types.VARCHAR:
                    obj.put(column_name, rs.getString(column_name));
                    break;
                case java.sql.Types.TINYINT:
                    obj.put(column_name, rs.getInt(column_name));
                    break;
                case java.sql.Types.SMALLINT:
                    obj.put(column_name, rs.getInt(column_name));
                    break;
                case java.sql.Types.DATE:
                    obj.put(column_name, rs.getDate(column_name));
                    break;
                case java.sql.Types.TIMESTAMP:
                    obj.put(column_name, rs.getTimestamp(column_name));
                    break;
                default:
                    obj.put(column_name, rs.getObject(column_name));
                    break;
                }
            }
            json.put(obj);
        }
        return json;
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
        boolean doQuery = false;

        SelectionQuery query = null;

        if (action != null) {

            if (action.equals("getRanking")) {

                if (InternalConfig.LOG_DATABASE) {
                    this.log.debug(this.getClass().getSimpleName()
                            + ": select ranking table..");
                }
                query = new SelectionQuery(
                        "SELECT @curRank := @curRank + 1 AS Rank, Name, Score, ID FROM (SELECT Name, player.PlayerID AS ID, SUM(Scoring) AS Score FROM player INNER JOIN result WHERE player.PlayerID=result.PlayerID GROUP BY Name ORDER BY Score DESC) AS ranking, (SELECT @curRank := 0) r;");
                doQuery = true;

            } else if (action.equals("getGames")) {

                if (InternalConfig.LOG_DATABASE) {
                    this.log.debug(this.getClass().getSimpleName()
                            + ": select game table..");
                }
                query = new SelectionQuery(
                        "SELECT GameID, GameName, GameTitle, NumberOfPlayers FROM game INNER JOIN category WHERE game.CatID=category.CatID;");
                doQuery = true;

            } else if (action.equals("getUsers")) {

                if (InternalConfig.LOG_DATABASE) {
                    this.log.debug(this.getClass().getSimpleName()
                            + ": select users table..");
                }
                query = new SelectionQuery("SELECT PlayerID, Name FROM player;");
                doQuery = true;

            } else if (action.equals("getCategories")) {

                if (InternalConfig.LOG_DATABASE) {
                    this.log.debug(this.getClass().getSimpleName()
                            + ": select game table..");
                }
                query = new SelectionQuery(
                        "SELECT CatID, Gametitle, NumberOfPlayers FROM category;");
                doQuery = true;

            }
        }

        if (doQuery && (query != null)) {

            JSONArray jsonArray = null;

            try {

                final DatabaseQuery transaction = new DatabaseQuery();
                transaction.connect();
                query.attach(transaction);
                final ResultSet resultSet = query.execute();
                jsonArray = this.convertToJson(resultSet);
                transaction.commit();

                // refine data output for Places = joinedPlayer/maxPlayer
                if (action.equals("getGames")) {
                    // update games from database before parsing model
                    GamePool.getInstance().update();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        final JSONObject obj = jsonArray.getJSONObject(i);
                        final Game game = GamePool.getInstance().getGame(
                                obj.getInt("GameID"));
                        // add joined/available player count
                        obj.put("NumberOfPlayers",
                                "" + game.getPlayerList().size() + "/"
                                        + game.getNumberOfPlayers());
                        // add joined player names
                        obj.put("Players", "" + game.playerToString());
                    }
                }

            } catch (final SQLException e) {

                if (InternalConfig.LOG_DATABASE) {
                    this.log.debug(this.getClass().getSimpleName()
                            + ": Could not query game table.");
                }
                e.printStackTrace();

            } catch (final JSONException e) {

                if (InternalConfig.LOG_DATABASE) {
                    this.log.debug(this.getClass().getSimpleName()
                            + ": Could not convert game table data to json.");
                }
                e.printStackTrace();
            }

            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            final PrintWriter out = response.getWriter();
            out.print(jsonArray);
            out.flush();

        } else {
            super.invalidRequest(request, response);
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
            if (action.equals("createGame")) {

                if (InternalConfig.LOG_DATABASE) {
                    this.log.debug(this.getClass().getSimpleName()
                            + ": update game table..");
                }

                final String thisGameName = request.getParameter("gameName");
                final Integer thisCatID = Integer.parseInt(request
                        .getParameter("catId"));

                if ((thisGameName != null) && (thisCatID != null)) {
                    query = new GameInsert(thisGameName, thisCatID);
                    doInsert = true;
                }

            } else if (action.equals("createResult")) {

                if (InternalConfig.LOG_DATABASE) {
                    this.log.debug(this.getClass().getSimpleName()
                            + ": update result table..");
                }

                final Integer thisGameID = GamePool.getInstance()
                        .getGameByPlayer(super.thisUser).getGameId();
                final Integer thisPlayerId = super.thisUser.getPlayerId();
                final String thisScoringString = request.getParameter("status");

                ScoringType thisScore = null;

                switch (thisScoringString) {
                case "win":
                    thisScore = GamePool.ScoringType.WIN;
                    break;
                case "draw":
                    thisScore = GamePool.ScoringType.DRAW;
                    break;
                case "lose":
                    thisScore = GamePool.ScoringType.LOSE;
                    break;
                default:
                    break;
                }

                if ((thisGameID != null) && (thisPlayerId != null)
                        && (thisScore != null)) {
                    query = new ResultInsert(thisGameID, thisPlayerId,
                            thisScore.getScore());
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

                    /** update model */
                    if (action.equals("createGame")) {
                        GamePool.getInstance().update();

                        // send game update broadcast
                        Communicator
                                .getMessagequeue()
                                .add(new CommunicatorMessage(
                                        MessageType.JS_COMMAND, "ALL",
                                        "window.parent.app.updateGameTable();"));
                    }
                    if (action.equals("createResult")) {
                        // send game update broadcast
                        Communicator
                                .getMessagequeue()
                                .add(new CommunicatorMessage(
                                        MessageType.JS_COMMAND, "ALL",
                                        "window.parent.app.updateRankingTable();"));
                    }

                } catch (final SQLException e) {

                    if (InternalConfig.LOG_DATABASE) {
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
                super.serviceDenied(request, response);
            }
        } else {
            super.invalidRequest(request, response);
        }

    }

}
