package de.alextape.tictactoe;

import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import de.unibox.client.api.ClientProvider;
import de.unibox.client.api.IncomingMessageHandler;

/**
 * The Class Control.
 */
public class Control extends Application {

    /** The gui. */
    private static Gui gui;

    /**
     * Gets the gui.
     *
     * @return the gui
     */
    public static Gui getGui() {
        return Control.gui;
    }

    /**
     * The main method.
     *
     * @param args
     *            the arguments
     */
    public static void main(final String[] args) {
        // TODO remove this if done..
        ClientProvider.setUrl("http://" + args[0] + ":8080/UniBox");
        ClientProvider.setUsername(args[1]);
        ClientProvider.setPassword(args[2]);
        
        Application.launch(args);
    }

    /**
     * Sets the gui.
     *
     * @param newGui
     *            the new gui
     */
    public static void setGui(final Gui newGui) {
        Control.gui = newGui;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javafx.application.Application#start(javafx.stage.Stage)
     */
    @Override
    public final void start(final Stage primaryStage) {

        // to setup the ClientProvider Environment call
        // ClientProvider.setupScanner();
        // or define url and credentials inline
        // ClientProvider.setUrl("http://192.168.0.2:8080/UniBox");
        // ClientProvider.setUsername("user");
        // ClientProvider.setPassword("password");

        ClientProvider.login();
        ClientProvider.connect();

        ClientProvider.sendGameMessage("GameClient connected");

        // init
        Control.gui = Gui.getInstance();
        Control.gui.initModel();

        // relate
        Control.gui.setPrimaryStage(primaryStage);
        Control.gui.setTitle("TicTacToe");
        Control.gui.setPlayer1("John");
        Control.gui.setPlayer2("Frank");
        Control.gui.setPlayer1color(Control.gui.getPlayerColors()[1]); // blue
        Control.gui.setPlayer2color(Control.gui.getPlayerColors()[4]); // red

        // render
        Control.gui.renderPlayfield();

        // release
        Control.gui.release();

        /**
         * other player starts..
         */
        ClientProvider.sendGameMessage("you_start");

        /**
         * UniBoxClient: bind event handler for incoming messages.
         */
        ClientProvider.bind(primaryStage, new IncomingMessageHandler() {

            @Override
            public void handle(final String user, final String msg) {

                // offer the other game client to start playing
                if (msg.equals("you_start")
                        && !user.equals(ClientProvider.getUsername())) {
                    // movecount
                    Gui.getInstance().nextMoveCounter();
                    System.out.println("_start()");
                }

                if (msg.contains("clicked")) {

                    final String id = msg.replace("clicked:", "");

                    System.out.println("_messageMove():" + id);

                    for (final Button[] gameButton : Gui.gameButtons) {
                        for (final Button clickedButton : gameButton) {

                            if (clickedButton.getId().equals(id)) {

                                if (clickedButton.getText().equals("0")) {

                                    // set X
                                    clickedButton.setText("2");
                                    System.out.println(clickedButton.getStyle());
                                    clickedButton.setStyle("-fx-base: "
                                            + Gui.getInstance()
                                                    .getPlayer2color() + ";");

                                    // movecount
                                    Gui.getInstance().nextMoveCounter();

                                    // is win/loss/draw?
                                    Gui.getInstance().determineSituation();
                                } else {
                                    System.out.println("BUTTON ALREADY MARKED");
                                }
                            }
                        }
                    }
                } else {
                    System.out.println(user + ": " + msg);
                }
            }
        });
    }

}
