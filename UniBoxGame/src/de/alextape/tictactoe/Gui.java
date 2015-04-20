package de.alextape.tictactoe;

import javafx.animation.Animation;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.SequentialTransitionBuilder;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;
import de.unibox.client.api.ClientProvider;

/**
 * The Class Gui.
 */
public class Gui {

    /** The game buttons. */
    public static Button[][] gameButtons;

    /** The instance. */
    private static Gui instance = null;

    /** The primary stage. */
    private static Stage primaryStage;

    /**
     * Gets the single instance of Gui.
     *
     * @return single instance of Gui
     */
    public static Gui getInstance() {
        if (Gui.instance == null) {
            Gui.instance = new Gui();
        }
        return Gui.instance;
    }

    /**
     * Renew.
     *
     * @return the gui
     */
    public static Gui renew() {
        Gui.instance = null;
        Gui.instance = new Gui();
        return Gui.instance;
    }

    /**
     * Sets the instance.
     *
     * @param newInstance
     *            the new instance
     */
    public static void setInstance(final Gui newInstance) {
        Gui.instance = newInstance;
    }

    /** The button field. */
    private GridPane buttonField;

    /** The grid. */
    private GridPane grid;

    /** The headline. */
    private Button headline;

    /** The model. */
    private Model model;

    /** The root. */
    private VBox root;

    /** The scene. */
    private Scene scene;

    /** The time. */
    private Button time;

    /** The time counter. */
    private int timeCounter;

    /** The time grid. */
    private GridPane timeGrid;

    /**
     * Instantiates a new gui.
     */
    public Gui() {

        // grid
        this.grid = new GridPane();

        this.time = new Button();
        this.timeCounter = 0;
        this.time.setText("Time: " + String.valueOf(this.timeCounter));

        final int i15 = 15;
        final int i0 = 0;
        final int i10 = 10;

        // generate Grid
        this.timeGrid = new GridPane();
        this.timeGrid.setPadding(new Insets(i15, i0, i0, i10));
        this.timeGrid.add(this.time, 0, 0);

        // timer
        final Animation countAnimation = this.buildCounter();
        final SequentialTransitionBuilder builder = SequentialTransitionBuilder
                .create();
        builder.children(countAnimation);
        builder.cycleCount(Integer.MAX_VALUE);
        final SequentialTransition sq = builder.build();

        // play transition
        sq.play();

    }

    /**
     * Builds the counter.
     *
     * @return the pause transition
     */
    private PauseTransition buildCounter() {
        final PauseTransition counter = new PauseTransition(Duration.seconds(1));
        counter.setOnFinished(new EventHandler<ActionEvent>() {

            @Override
            public void handle(final ActionEvent arg0) {
                Gui.this.timeCounter++;
                Gui.this.time.setText("Time: "
                        + String.valueOf(Gui.this.timeCounter));
            }
        });
        return counter;
    }

    /**
     * Determine situation.
     */
    public final void determineSituation() {

        System.out.println("Gui: _determineSituation()");

        // determine situation
        final boolean[] result = Gui.this.model.isWin(Gui.this
                .getMatrixString());

        if (result[0]) {
            // player1 win
            Gui.this.winPopup(Gui.this.getPlayer1() + " wins!");
            ClientProvider.sendGameMessage("Player 1 won!");
            ClientProvider.sendWinResult();
        }
        if (result[1]) {
            // player2 win
            Gui.this.winPopup(Gui.this.getPlayer2() + " wins!");
            ClientProvider.sendGameMessage("Player 2 won!");
            ClientProvider.sendLoseResult();
        }
        if (Gui.this.model.isDraw(Gui.this.getMatrixString())) {
            Gui.this.winPopup("Draw!");
            ClientProvider.sendGameMessage("Game is drawn!");
            ClientProvider.sendDrawResult();
        }
    }

    /**
     * Gets the button feld.
     *
     * @return the button feld
     */
    public final GridPane getButtonFeld() {
        return this.buttonField;
    }

    /**
     * Gets the button field.
     *
     * @return the button field
     */
    public final GridPane getButtonField() {
        return this.buttonField;
    }

    /**
     * Gets the game board.
     *
     * @return the game board
     */
    private GridPane getGameBoard() {

        final EventHandler<MouseEvent> myMouseHandler = new EventHandler<MouseEvent>() {

            /*
             * (non-Javadoc)
             * 
             * @see javafx.event.EventHandler#handle(javafx.event.Event)
             */
            @Override
            public void handle(final MouseEvent event) {

                // get active element
                final Object source = event.getSource();

                if (source instanceof Button) {

                    final Button clickedButton = (Button) source;

                    if (clickedButton.getText().equals("0")) {

                        System.out.println("Gui: _mouseMove(): "
                                + Gui.this.getMoveCounter());

                        // set color
                        if ((Gui.this.getMoveCounter() % 2) != 0) {
                            // set X
                            clickedButton.setText("1");
                            System.out.println("Gui: _send():"
                                    + clickedButton.getId());
                            ClientProvider.sendGameMessage("clicked:"
                                    + clickedButton.getId());
                            clickedButton.setStyle("-fx-base: "
                                    + Gui.this.getPlayer1color() + ";");
                            // movecount
                            Gui.this.nextMoveCounter();
                        } else {
                            System.out.println("Gui: REMOTE PLAYER TURN");
                        }
                    } else {
                        System.out.println("Gui: BUTTON ALREADY MARKED");
                    }

                    Gui.this.determineSituation();

                }
            }
        };

        final int i10 = 10;

        // generate grid
        this.buttonField = new GridPane();
        this.buttonField.setHgap(i10);
        this.buttonField.setVgap(i10);
        this.buttonField.setPadding(new Insets(i10, i10, i10, i10));

        final int i3 = 3;
        final int i50 = 50;

        int idCounter = 0;

        // instance buttons
        Gui.gameButtons = new Button[i3][i3];
        for (int i = 0; i < Gui.gameButtons.length; i++) {

            for (int z = 0; z < Gui.gameButtons[i].length; z++) {

                Gui.gameButtons[i][z] = new Button();
                Gui.gameButtons[i][z].setId("" + idCounter++);
                Gui.gameButtons[i][z].setText("0");
                Gui.gameButtons[i][z].setMinSize(i50, i50);
                Gui.gameButtons[i][z].setMaxSize(i50, i50);

                // relate listener
                Gui.gameButtons[i][z].setOnMouseClicked(myMouseHandler);

                // add to pane
                this.buttonField.add(Gui.gameButtons[i][z], i, z);

            }
        }

        // return field
        return this.buttonField;

    }

    /**
     * Gets the grid.
     *
     * @return the grid
     */
    public final GridPane getGrid() {
        return this.grid;
    }

    /**
     * Gets the headline.
     *
     * @return the headline
     */
    public final Button getHeadline() {
        return this.headline;
    }

    /**
     * Gets the matrix string.
     *
     * @return the matrix string
     */
    public final String getMatrixString() {

        final StringBuilder result = new StringBuilder();

        int elementCounter = 0;

        for (final Button[] button : Gui.gameButtons) {
            elementCounter = elementCounter + button.length;
        }

        final int[] matrix = new int[elementCounter];
        int index = 0;

        for (final Button[] button2 : Gui.gameButtons) {
            for (final Button button : button2) {
                if (button.getText().equals("0")) {
                    matrix[index] = 0;
                } else {
                    if (button.getText().equals("1")) {
                        matrix[index] = 1;
                    }
                    if (button.getText().equals("2")) {
                        matrix[index] = 2;
                    }
                }
                index++;
            }
        }

        for (final int value : matrix) {
            result.append(value);
        }

        return result.toString();
    }

    /**
     * Gets the model.
     *
     * @return the model
     */
    public final Model getModel() {
        return this.model;
    }

    /**
     * Gets the move counter.
     *
     * @return the move counter
     */
    public final int getMoveCounter() {
        return this.getModel().getMoveCounter();
    }

    /**
     * Gets the player1.
     *
     * @return the player1
     */
    public final String getPlayer1() {
        return this.getModel().getPlayer1();
    }

    /**
     * Gets the player1color.
     *
     * @return the player1color
     */
    public final String getPlayer1color() {
        return this.getModel().getPlayer1color();
    }

    /**
     * Gets the player2.
     *
     * @return the player2
     */
    public final String getPlayer2() {
        return this.getModel().getPlayer2();
    }

    /**
     * Gets the player2color.
     *
     * @return the player2color
     */
    public final String getPlayer2color() {
        return this.getModel().getPlayer2color();
    }

    /**
     * Gets the player colors.
     *
     * @return the player colors
     */
    public final String[] getPlayerColors() {
        return this.getModel().getPlayerColors();
    }

    /**
     * Gets the primary stage.
     *
     * @return the primary stage
     */
    public final Stage getPrimaryStage() {
        return Gui.primaryStage;
    }

    /**
     * Gets the root.
     *
     * @return the root
     */
    public final VBox getRoot() {
        return this.root;
    }

    /**
     * Gets the scene.
     *
     * @return the scene
     */
    public final Scene getScene() {
        return this.scene;
    }

    /**
     * Gets the time.
     *
     * @return the time
     */
    public final Button getTime() {
        return this.time;
    }

    /**
     * Gets the time counter.
     *
     * @return the time counter
     */
    public final int getTimeCounter() {
        return this.timeCounter;
    }

    /**
     * Gets the time grid.
     *
     * @return the time grid
     */
    public final GridPane getTimeGrid() {
        return this.timeGrid;
    }

    /**
     * Gets the title.
     *
     * @return the title
     */
    public final String getTitle() {
        return this.model.getTitle();
    }

    /**
     * Inits the model.
     */
    public final void initModel() {
        // init model
        this.model = new Model();
        // move counter
        this.model.setMoveCounter(0);

    }

    /**
     * Next move counter.
     */
    public final void nextMoveCounter() {
        System.out.println("Gui: _nextMoveCounter()");
        this.getModel().setMoveCounter(this.getModel().getMoveCounter() + 1);
    }

    /**
     * Release.
     */
    public final void release() {
        // release
        Gui.primaryStage.setScene(this.getScene());
        Gui.primaryStage.setResizable(false);
        Gui.primaryStage.show();
    }

    /**
     * Render playfield.
     */
    public final void renderPlayfield() {

        // e.g.
        // this.refreshMoveCounter();

        // introduce appname
        Gui.primaryStage.setTitle(this.model.getTitle());
        this.headline = this.setText(this.model.getTitle());

        final int i1 = 1;
        final int i2 = 2;
        final int i3 = 3;

        // generate view
        this.grid.add(this.timeGrid, i3, i1);
        this.grid.add(this.headline, i2, i1);
        this.grid.add(
                this.setText("Player1:" + this.getPlayer1() + "\n" + "Color:"
                        + this.getPlayer1color()), 1, 2);

        this.grid.add(this.getGameBoard(), 2, 2);
        this.grid.add(
                this.setText("Player2:" + this.getPlayer2() + "\n" + "Color:"
                        + this.getPlayer2color()), i3, i2);
        this.root = new VBox();
        this.root.getChildren().addAll(this.grid);

        // generate scene
        this.scene = new Scene(this.root);
    }

    /**
     * Sets the button field.
     *
     * @param newButtonField
     *            the new button field
     */
    public final void setButtonField(final GridPane newButtonField) {
        this.buttonField = newButtonField;
    }

    /**
     * Sets the grid.
     *
     * @param newGrid
     *            the new grid
     */
    public final void setGrid(final GridPane newGrid) {
        this.grid = newGrid;
    }

    /**
     * Sets the headline.
     *
     * @param newHeadline
     *            the new headline
     */
    public final void setHeadline(final Button newHeadline) {
        this.headline = newHeadline;
    }

    /**
     * Sets the model.
     *
     * @param newModel
     *            the new model
     */
    public final void setModel(final Model newModel) {
        this.model = newModel;
    }

    /**
     * Sets the player1.
     *
     * @param player1
     *            the new player1
     */
    public final void setPlayer1(final String player1) {
        this.getModel().setPlayer1(player1);
    }

    /**
     * Sets the player1color.
     *
     * @param player1color
     *            the new player1color
     */
    public final void setPlayer1color(final String player1color) {
        System.out.println("Gui: PLAYER1_color: " + player1color);
        this.getModel().setPlayer1color(player1color);
    }

    /**
     * Sets the player2.
     *
     * @param player2
     *            the new player2
     */
    public final void setPlayer2(final String player2) {
        this.getModel().setPlayer2(player2);
    }

    /**
     * Sets the player2color.
     *
     * @param player2color
     *            the new player2color
     */
    public final void setPlayer2color(final String player2color) {
        System.out.println("Gui: PLAYER2_color: " + player2color);
        this.getModel().setPlayer2color(player2color);
    }

    /**
     * Sets the primary stage.
     *
     * @param newPrimaryStage
     *            the new primary stage
     */
    public final void setPrimaryStage(final Stage newPrimaryStage) {
        Gui.primaryStage = newPrimaryStage;
    }

    /**
     * Sets the root.
     *
     * @param newRoot
     *            the new root
     */
    public final void setRoot(final VBox newRoot) {
        this.root = newRoot;
    }

    /**
     * Sets the scene.
     *
     * @param newScene
     *            the new scene
     */
    public final void setScene(final Scene newScene) {
        this.scene = newScene;
    }

    /**
     * Sets the text.
     *
     * @param text
     *            the text
     * @return the button
     */
    private Button setText(final String text) {

        final int i50 = 50;

        // generate textnode
        final Button button = new Button();
        button.setMinSize(i50, i50);
        button.setDisable(true);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setMaxHeight(Double.MAX_VALUE);

        final Text textNode = new Text(text);
        textNode.setFill(Color.BLACK);

        // relate
        button.setGraphic(textNode);

        // fix display of disabled button
        button.setOpacity(1);

        // return node
        return button;

    }

    /**
     * Sets the time.
     *
     * @param newTime
     *            the new time
     */
    public final void setTime(final Button newTime) {
        this.time = newTime;
    }

    /**
     * Sets the time counter.
     *
     * @param newTimeCounter
     *            the new time counter
     */
    public final void setTimeCounter(final int newTimeCounter) {
        this.timeCounter = newTimeCounter;
    }

    /**
     * Sets the time grid.
     *
     * @param newTimeGrid
     *            the new time grid
     */
    public final void setTimeGrid(final GridPane newTimeGrid) {
        this.timeGrid = newTimeGrid;
    }

    /**
     * Sets the title.
     *
     * @param title
     *            the new title
     */
    public final void setTitle(final String title) {
        this.model.setTitle(title);
    }

    /**
     * Win popup.
     *
     * @param popupText
     *            the popup text
     */
    public final void winPopup(final String popupText) {

        final Popup popup = new Popup();

        final Button restart = new Button("Restart");
        restart.setOnAction(new EventHandler<ActionEvent>() {

            /*
             * (non-Javadoc)
             * 
             * @see javafx.event.EventHandler#handle(javafx.event.Event)
             */
            @Override
            public void handle(final ActionEvent event) {

                // renew
                final Gui newGui = Gui.renew();
                newGui.setModel(Control.getGui().getModel());
                newGui.setTitle(Control.getGui().getTitle());
                newGui.setPlayer1(Control.getGui().getPlayer1());
                newGui.setPlayer2(Control.getGui().getPlayer2());
                newGui.setPlayer1color(Control.getGui().getPlayer1color());
                newGui.setPlayer2color(Control.getGui().getPlayer2color());

                // render
                newGui.renderPlayfield();

                // release
                Gui.primaryStage.setScene(newGui.getScene());

                // send game state
                ClientProvider.sendGameMessage("restart game..");

                // hide popup
                popup.hide();
            }
        });

        final int i10 = 10;

        final HBox popupContent = new HBox(i10);
        popupContent
                .setStyle("-fx-background-color: white; -fx-padding: 10 10;");
        popupContent.getChildren().addAll(new Text(popupText), restart);
        popup.getContent().addAll(popupContent);

        popup.show(Control.getGui().getPrimaryStage());

    }

}
