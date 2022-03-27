package gurdle.gui;

import gurdle.Model;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import util.Observer;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * The graphical user interface to the Wordle game model in
 * {@link Model}.
 *
 * @author YOUR NAME HERE
 */
public class Gurdle extends Application
        implements Observer< Model, String > {

    private Model model;

    @Override public void init() {
        System.out.println( "TODO - init code here" );
    }

    @Override
    public void start( Stage mainStage ) {
        BorderPane bigBP = new BorderPane();
        Label topText = new Label("#guesses: 0       Make a guess!");
        topText.setFont(Font.font(18));
        bigBP.setTop(topText);
        topText.setAlignment(Pos.TOP_RIGHT);

        GridPane mainGrid = makeMainGrid();
        mainGrid.setHgap(5);
        mainGrid.setVgap(5);
        bigBP.setCenter(mainGrid);
        mainGrid.setAlignment(Pos.CENTER);

        BorderPane bottomBP = new BorderPane();

        GridPane keyboard = makeKeyboard();

        bottomBP.setCenter(keyboard);
        keyboard.setAlignment(Pos.TOP_LEFT);

        Button enter = new Button("ENTER");
        enter.setMinWidth(100);
        enter.setMinHeight(30);
        Button cheat = new Button("CHEAT");
        cheat.setMinWidth(100);
        cheat.setMinHeight(30);
        Button newGame = new Button("NEW GAME");
        newGame.setMinWidth(100);
        newGame.setMinHeight(30);

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
    }

    @Override
    public void update( Model model, String message ) {
        System.out.println( """
                TODO
                Here is where the model is queried
                and the view is updated."""
        );
    }

    public GridPane makeKeyboard(){
        GridPane keyboard = new GridPane();

        String letters = "QWERTYUIOPASDFGHJKLZXCVBNM";
        int curRow = 0;
        for (int i = 0; i < 26; ++i) {
            String currentLetter = String.valueOf(letters.charAt(i));
            Button button = new Button(currentLetter);
            button.setMinHeight(40);
            button.setMinWidth(40);
            button.setBackground(new Background(new BackgroundFill(new Color(1, 1, 1, 1), CornerRadii.EMPTY, Insets.EMPTY)));
            if (currentLetter.equals("A")) {
                curRow = 1;
            }
            if (currentLetter.equals("Z")) {
                curRow = 2;
            }
            if (curRow == 0) {
                keyboard.add(button, i, curRow);
            }
            else if (curRow == 1) {
                keyboard.add(button, (i - 10), curRow);
            }
            else {
                keyboard.add(button, (i - 19), curRow);
            }
        }
        return keyboard;
    }

    public GridPane makeMainGrid(){
        GridPane maingrid = new GridPane();
        for (int i = 0; i < 5; ++i){
            for (int j = 0; j < 6; ++j){
                Label label = new Label();
                label.setMinHeight(75);
                label.setMinWidth(50);
                label.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
                maingrid.add(label, i, j);
            }
        }
        maingrid.setBackground(new Background(new BackgroundFill(new Color(1, 1, 1, 1), CornerRadii.EMPTY, new Insets(-5, 150, -15, 150))));
        return maingrid;
    }

    public static void main( String[] args ) {
        if ( args.length > 1 ) {
            System.err.println( "Usage: java Gurdle [1st-secret-word]" );
        }
        Application.launch( args );
    }
}
