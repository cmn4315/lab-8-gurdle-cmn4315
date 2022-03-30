package gurdle.gui;

import gurdle.CharChoice;
import gurdle.Model;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import util.Observer;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.List;

/**
 * The graphical user interface to the Wordle game model in
 * {@link Model}.
 *
 * @author YOUR NAME HERE
 */
public class Gurdle extends Application
        implements Observer< Model, String > {

    private Model model;
    private GridPane mainGrid;
    private Label[][] gridPanels;
    private GridPane keyboard;
    private Button[][] keys;
    private Label topText;
    private Label secretText;
    private HashMap<Character, CharChoice.Status> keyMap;

    @Override public void init() {
        this.model = new Model();
        model.addObserver(this);
    }

    @Override
    public void start( Stage mainStage ) {
        this.keyMap = new HashMap<>();
        BorderPane bigBP = new BorderPane();
        this.topText = new Label("#guesses:" + model.numAttempts() + "     Make a guess!");
        topText.setFont(Font.font(18));
        this.secretText = new Label("");
        secretText.setFont(Font.font(18));
        HBox topBox = new HBox(topText, secretText);
        bigBP.setTop(topBox);
        topText.setAlignment(Pos.TOP_CENTER);

        this.gridPanels = new Label[5][6];
        mainGrid = makeMainGrid();
        mainGrid.setHgap(5);
        mainGrid.setVgap(5);
        bigBP.setCenter(mainGrid);
        mainGrid.setAlignment(Pos.CENTER);

        BorderPane bottomBP = new BorderPane();

        this.keys = new Button[10][3];
        keyboard = makeKeyboard();

        bottomBP.setCenter(keyboard);
        keyboard.setAlignment(Pos.TOP_LEFT);

        Button enter = new Button("ENTER");
        enter.setMinWidth(100);
        enter.setMinHeight(30);
        enter.setOnAction((event) -> {
            model.confirmGuess();
            updateKeyColors();
        });
        Button cheat = new Button("CHEAT");
        cheat.setMinWidth(100);
        cheat.setMinHeight(30);
        cheat.setOnAction((event) -> this.secretText.setText("    secret: " + model.secret()));
        Button newGame = new Button("NEW GAME");
        newGame.setMinWidth(100);
        newGame.setMinHeight(30);
        newGame.setOnAction((event) -> model.newGame());
        GridPane bottomButtons = new GridPane();
        bottomButtons.add(enter, 1, 0);
        bottomButtons.add(newGame, 0, 1);
        bottomButtons.add(cheat, 1, 1);

        bottomBP.setRight(bottomButtons);
        bottomButtons.setAlignment(Pos.BOTTOM_RIGHT);

        HBox spacer = new HBox();
        spacer.setMinHeight(30);
        bottomBP.setTop(spacer);

        bigBP.setBottom(bottomBP);
        bigBP.setBackground(new Background(new BackgroundFill(new Color(0.9, 0.9, 0.9, 1), CornerRadii.EMPTY, Insets.EMPTY)));

        Scene scene = new Scene( bigBP );
        mainStage.setScene( scene );
        mainStage.show();
        model.newGame();
    }

    @Override
    public void update( Model model, String message ) {
        ObservableList<Node> list = mainGrid.getChildren();
        for (int i = 0; i < list.size(); ++i){
            Label l = (Label) list.get(i);
            int row = GridPane.getRowIndex(l);
            int col = GridPane.getColumnIndex(l);
            l.setText(String.valueOf(model.get(row,col).getChar()));
        }
        updateColors();
        updateMessage(message);

    }

    /**
     * updates the top message above the window
     * @param newMessage the model message to put on the thing
     */
    public void updateMessage(String newMessage){
        this.topText.setText("#guesses: " + model.numAttempts() + "    " + newMessage);
    }

    /**
     * updates the colors of the main grid. also keeps track of letters and their status for keyboard color updates in keyMap
     */
    public void updateColors(){
        ObservableList<Node> list = mainGrid.getChildren();
        for (int i = 0; i < 5; ++i){
            if (model.numAttempts() == 0){
                for (int j = 0; j < 6; ++j){
                    Label label = (Label) list.get((6*i)+j);
                    label.setBackground(new Background(new BackgroundFill(new Color(1, 1, 1, 1), CornerRadii.EMPTY, Insets.EMPTY)));
                }
            }else{
                for (int j = 0; j < model.numAttempts(); ++j){
                    Label label = (Label) list.get((6*i)+j);
                    CharChoice.Status status = model.get(j,i).getStatus();
                    if (status.equals(CharChoice.Status.RIGHT_POS)){
                        label.setBackground(new Background(new BackgroundFill(new Color(0.525, 1, 0.2705, 1), CornerRadii.EMPTY, Insets.EMPTY)));
                        keyMap.put(model.get(j,i).getChar(), status);
                    } else if (status.equals(CharChoice.Status.WRONG_POS)){
                        label.setBackground(new Background(new BackgroundFill(new Color(0.976, 1, 0.2705, 1), CornerRadii.EMPTY, Insets.EMPTY)));
                        if (!keyMap.get(model.get(j,i).getChar()).equals(CharChoice.Status.RIGHT_POS)){
                            keyMap.put(model.get(j,i).getChar(), status);
                        }
                    } else {
                        label.setBackground(new Background(new BackgroundFill(new Color(0.2901, 0.2901, 0.2901, 1), CornerRadii.EMPTY, Insets.EMPTY)));
                        if (!keyMap.get(model.get(j,i).getChar()).equals(CharChoice.Status.RIGHT_POS) && !keyMap.get(model.get(j,i).getChar()).equals(CharChoice.Status.WRONG_POS)){
                            keyMap.put(model.get(j,i).getChar(), status);
                        }
                    }
                }
            }
        }
    }

    /**
     * updates the keyboard colors
     */
    public void updateKeyColors(){
        ObservableList keys = keyboard.getChildren();
        for (int i = 0; i < keys.size(); ++i){
            Button key = (Button) keys.get(i);
            String name = key.getText();
            if (keyMap.get(name.charAt(0)) == null){
                key.setBackground(new Background(new BackgroundFill(new Color(1, 1, 1, 1), CornerRadii.EMPTY, Insets.EMPTY)));
            }else if (keyMap.get(name.charAt(0)).equals(CharChoice.Status.RIGHT_POS)) {
                key.setBackground(new Background(new BackgroundFill(new Color(0.525, 1, 0.2705, 1), CornerRadii.EMPTY, Insets.EMPTY)));
            } else if (keyMap.get(name.charAt(0)).equals(CharChoice.Status.WRONG_POS)) {
                key.setBackground(new Background(new BackgroundFill(new Color(0.976, 1, 0.2705, 1), CornerRadii.EMPTY, Insets.EMPTY)));
            } else {
                key.setBackground(new Background(new BackgroundFill(new Color(0.2901, 0.2901, 0.2901, 1), CornerRadii.EMPTY, Insets.EMPTY)));
            }
        }
    }

    /**
     * makes the main keyboard gridpane
     * @return the gridpane
     */
    public GridPane makeKeyboard(){
        GridPane keyboard = new GridPane();

        String letters = "QWERTYUIOPASDFGHJKLZXCVBNM";
        int curRow = 0;
        for (int i = 0; i < 26; ++i) {
            char currentLetter = letters.charAt(i);
            Button button = new Button(String.valueOf(currentLetter));
            button.setFont(Font.font(16));
            button.setMinHeight(40);
            button.setMinWidth(40);
            button.setBackground(new Background(new BackgroundFill(new Color(1, 1, 1, 1), CornerRadii.EMPTY, Insets.EMPTY)));
            button.setOnAction((event) -> {
                this.model.enterNewGuessChar(currentLetter);
                this.keyMap.put(currentLetter, CharChoice.Status.EMPTY);
            });
            if (currentLetter == 'A') {
                curRow = 1;
            }
            if (currentLetter == 'Z') {
                curRow = 2;
            }
            if (curRow == 0) {
                this.keys[i][curRow] = button;
                keyboard.add(this.keys[i][curRow], i, curRow);
            }
            else if (curRow == 1) {
                this.keys[i-10][curRow] = button;
                keyboard.add(this.keys[i-10][curRow], (i - 10), curRow);
            }
            else {
                this.keys[i-19][curRow] = button;
                keyboard.add(this.keys[i-19][curRow], (i - 19), curRow);
            }
        }
        return keyboard;
    }

    /**
     * makes the main gridpane of labels
     * @return the gridpane
     */
    public GridPane makeMainGrid(){
        GridPane maingrid = new GridPane();
        for (int i = 0; i < 5; ++i){
            for (int j = 0; j < 6; ++j){
                Label label = new Label();
                label.setMinHeight(75);
                label.setMinWidth(50);
                label.setStyle( """
                            -fx-padding: 2;
                            -fx-border-style: solid inside;
                            -fx-border-width: 2;
                            -fx-border-insets: 5;
                            -fx-border-radius: 2;
                            -fx-border-color: black;
                """);
                label.setBackground(new Background(new BackgroundFill(new Color(1, 1, 1, 1), CornerRadii.EMPTY, new Insets(-5, -5, -5, -5))));
                gridPanels[i][j] = label;
                maingrid.add(gridPanels[i][j], i, j);
            }
        }
        return maingrid;
    }

    public static void main( String[] args ) {
        if ( args.length > 1 ) {
            System.err.println( "Usage: java Gurdle [1st-secret-word]" );
        }
        Application.launch( args );
    }
}
