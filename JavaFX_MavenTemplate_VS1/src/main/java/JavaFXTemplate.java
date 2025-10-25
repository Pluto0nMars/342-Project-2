import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.Alert.AlertType;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap; //use for keeping a localsorting of the map
import java.util.Random;


public class JavaFXTemplate extends Application {

    // accessor variables to keep track of user dependent options
    private ComboBox<Integer> numSpots;   // accessible anywhere
    private ComboBox<Integer> multiplier;
    private GridPane grid;


    // specific round variables that *should* change each time START is called
    private boolean roundStarted = false;
    private boolean fastForward = false;
    private HashSet<Integer> selectedNums = new HashSet<>();
    private HashSet<Integer> drawnNums = new HashSet<>();
    private HashSet<Button> selectedButtons = new HashSet<>();
    private int numWinnings = 0;
    private int turnCount = 0;
    Label winningsLabel = new Label("Winnings: $0 Turns: " + turnCount + " / 4");

    // testing this out. dont know how to implement random so that it just appears on the screen.
    private ArrayList<Button> gridButtonsTrack = new ArrayList<>();

    // visual variables
    private static final String background_purple = "#6151F5";
    private static final String secondary_orange = "orange";
    private static final String background_gold = "#D0BA6B";
    private static final String secondary_red = "#B30400";
    private static final String winningColor = "#85bb65";
    private static final String losingColor = "#e34234";
    private static final String UNSELECTED_COLOR = "#1E90FF";
    private static final String SELECTED_COLOR = "#778899";
    private boolean isNewLook = false;

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        launch(args);
    }

    Button gamePlayButton = new Button("Play");
    Button randomSelection = new Button("Random Picks");

    EventHandler<ActionEvent> playGameEvent = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            randomSelection.setDisable(true);

            Alert noneSelected = new Alert(AlertType.INFORMATION);
            if(selectedNums.isEmpty()){
                noneSelected.setTitle("NO NUMBERS SELECTED");
                noneSelected.setHeaderText("SELECT AT LEAST ONE NUMBER");
                noneSelected.setContentText("You need at least one number selected in oreder to play\n");
                noneSelected.show();
                return;
            }
            // only ignore subsequent start game clicks while one is already running
            if (roundStarted) {
                return;
            }
            // bool flag keeping track to avoid concurrent games
            roundStarted = true;

            if (turnCount < 4) {
                resetAfterDraw();
                int winningAfterTurn = generateWinnings() * multiplier.getValue();
                numWinnings += winningAfterTurn;
                System.out.println(numWinnings);

                highlightDrawings();

                turnCount++;
                winningsLabel.setText("Winnings: $" + numWinnings + " Turns: " + turnCount + " / 4");

                PauseTransition roundFinishDelay = new PauseTransition(Duration.seconds(3.75));
                roundFinishDelay.setOnFinished(a -> {
//                  // after timer allow next round start, and enable random button again
                    roundStarted = false;
                    randomSelection.setDisable(false);

                    if (turnCount >= 4) {
                        addToTopGames(numWinnings);
                        gamePlayButton.setText("New Game");

                        gamePlayButton.setOnAction( resetHandler-> {
                            resetGame();
                            gamePlayButton.setText("Play");
                            gamePlayButton.setOnAction(playGameEvent);
                        });

                        Alert gameOver = new Alert(AlertType.INFORMATION);
                        gameOver.setTitle("GAME OVER");
                        gameOver.setHeaderText("OUT OF TURNS");
                        gameOver.setContentText("You have run out of turns.\n Select start new game to play again");
                        gameOver.show();

                    }
                });
                roundFinishDelay.play();
//                int winningAfterTurn = generateWinnings() * multiplier.getValue();
//                numWinnings += winningAfterTurn;
//                turnCount++;
//                System.out.println(numWinnings);
//                winningsLabel.setText("Winnings: $" + numWinnings + " Turns: " + turnCount + " / 4");
            }
        }
    };


    private GridPane createGridPane() {
        GridPane grid = new GridPane();
        grid.setVgap(5);
        grid.setHgap(5);

        int num = 1;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 10; col++) {
                Button numButton = new Button(String.valueOf(num++));
                gridButtonsTrack.add(numButton);
                numButton.setPrefSize(50, 50);

                String unselectedStyle =
                        "-fx-background-color: " + background_purple + ";" +
                                "-fx-font-size: 14px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-text-fill: white;" +
                                "-fx-border-color: " + background_gold + ";" +
                                "-fx-border-width: 3px;" +
                                "-fx-background-radius: 50;" +
                                "-fx-border-radius: 50;";

                String selectedStyle =
                        "-fx-background-color: " + SELECTED_COLOR + ";" +
                                "-fx-font-size: 14px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-text-fill: white;" +
                                "-fx-border-color: " + background_gold + ";" +
                                "-fx-border-width: 3px;" +
                                "-fx-background-radius: 50;" +
                                "-fx-border-radius: 50;";
                numButton.setStyle(unselectedStyle);

                numButton.setOnAction(e -> {
                    int value = Integer.parseInt(numButton.getText());
                    //System.out.println(value); // debug purpose

                    // also need to check if the game is not in progress, cant change once the round starts
                    // Check if the button currently contains the UNSELECTED_COLOR style
                    if (selectedNums.contains(value)) {
                        if (selectedNums.contains(value)) {
                            selectedNums.remove(value);
                            numButton.setStyle(unselectedStyle);
                        }
                    } else {
                        if (selectedNums.size() < numSpots.getValue()) {
                            selectedNums.add(value);
                            numButton.setStyle(selectedStyle);
                        } else {
                            Alert alert = new Alert(AlertType.INFORMATION);
                            alert.setTitle("Invalid Number of Selections");
                            alert.setHeaderText("Invalid Number of Selections");
                            alert.setContentText("Already Satisfied Max Number of Selections");
                            alert.showAndWait();
                        }
                    }
                });
                grid.add(numButton, col, row);
            }
        }
        return grid;
    }

    private void randomPick(int n) {
        selectedButtons.clear();
        selectedNums.clear();
        for (Button b : gridButtonsTrack) {
            b.setStyle(
                    "-fx-background-color: " + background_purple + ";" +
                            "-fx-font-size: 14px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-text-fill: white;" +
                            "-fx-border-color: " + background_gold + ";" +
                            "-fx-border-width: 3px;" +
                            "-fx-background-radius: 50;" +
                            "-fx-border-radius: 50;");
        }

        String selectedStyle =
                "-fx-background-color: " + SELECTED_COLOR + ";" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: white;" +
                        "-fx-border-color: " + background_gold + ";" +
                        "-fx-border-width: 3px;" +
                        "-fx-background-radius: 50;" +
                        "-fx-border-radius: 50;";


        // generate random numbers and change appearance to selected_color
        int i = 0;
        while (i < n) {
            Random r = new Random();
            int randomNum = r.nextInt(80) + 1;
            Button numButton = gridButtonsTrack.get(randomNum - 1);
            if (!selectedButtons.contains(numButton)) {
                selectedButtons.add(numButton);
                numButton.setStyle(selectedStyle);

                String label = numButton.getText();
                int num = Integer.parseInt(label);
                selectedNums.add(num);
                i++;
            }
        }
    }

    // generate 20 random numbers that the engine compares against users chosen numbers
    private void generateDrawings() {
        Random r = new Random();
        int a;
        for (int i = 0; i < 20; i++) {
            a = r.nextInt((80) + 1);
            while (drawnNums.contains(a)) {
                a = r.nextInt((80) + 1);
            }
            drawnNums.add(a);
        }
    }

    private int generateWinnings() {
        generateDrawings();
        int matches = 0;
        for (Button b : selectedButtons) {
            String label = b.getText();
            int num = Integer.parseInt(label);
            if (drawnNums.contains(num)) {
                matches++;
            }
        }

        return calculatePrize(numSpots.getValue(), matches);
    }
    private void resetGame(){
        numWinnings = 0;
        turnCount = 0;
        drawnNums.clear();
        selectedButtons.clear();
        selectedNums.clear();

        winningsLabel.setText("Winnings: $" + numWinnings + "Turns: " + turnCount + " / 4");


        for (Button b : gridButtonsTrack) {
            String unselectedStyle =
                    "-fx-background-color: " + background_purple + ";" +
                            "-fx-font-size: 14px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-text-fill: white;" +
                            "-fx-border-color: " + background_gold + ";" +
                            "-fx-border-width: 3px;" +
                            "-fx-background-radius: 50;" +
                            "-fx-border-radius: 50;";
            b.setStyle(unselectedStyle);
        }

    }

    private void resetAfterDraw() {
        drawnNums.clear();

        String unselectedStyle =
                "-fx-background-color: " + background_purple + ";" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: white;" +
                        "-fx-border-color: " + background_gold + ";" +
                        "-fx-border-width: 3px;" +
                        "-fx-background-radius: 50;" +
                        "-fx-border-radius: 50;";

        String selectedStyle =
                "-fx-background-color: " + SELECTED_COLOR + ";" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: white;" +
                        "-fx-border-color: " + background_gold + ";" +
                        "-fx-border-width: 3px;" +
                        "-fx-background-radius: 50;" +
                        "-fx-border-radius: 50;";

        for (Button b : gridButtonsTrack) {
            int num = Integer.parseInt(b.getText());
            if (selectedNums.contains(num)) {
                b.setStyle(selectedStyle);
            } else {
                b.setStyle(unselectedStyle);
            }
        }
    }

    // highlight drawn numbers with a purposeful delay using JavaFX Actions
    private void highlightDrawings() {
        List<Integer> drawnList = new ArrayList<>(drawnNums);

        for (int i = 0; i < drawnList.size(); i++) {
            int num = drawnList.get(i);
            if (num < 1 || num > 80) continue;
            PauseTransition pause = new PauseTransition(Duration.millis(200 * i));
            pause.setOnFinished(e -> {
                // if the user chose number and program chose it. MATCH
                Button B = gridButtonsTrack.get(num - 1);

                String color = selectedNums.contains(num) ? winningColor : losingColor;
                B.setStyle("-fx-background-color: " + color + ";" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: white;" +
                        "-fx-border-color: " + background_gold + ";" +
                        "-fx-border-width: 3px;" +
                        "-fx-background-radius: 50;" +
                        "-fx-border-radius: 50;");
            });
            pause.play();
        }
    }


    // create data structes for left and right side of the game. left contians a treemap we
    // access in decreasingKeyOrder. The right side contains a makeshift PriorityQueue 351 Type sh
    private static final TreeMap<Integer, Integer> prizesMap = new TreeMap<>();
    static {
        prizesMap.put(10, 10000);
        prizesMap.put(9, 4250);
        prizesMap.put(8, 450);
        prizesMap.put(7, 40);
        prizesMap.put(6, 15);
        prizesMap.put(5, 8);
        prizesMap.put(4, 6);
        prizesMap.put(3, 4);
        prizesMap.put(2, 2);
        prizesMap.put(1, 0);
        prizesMap.put(0, 0);
    }


    private final List<Integer> topGames = new ArrayList<>();
    private void addToTopGames(Integer game) {
        topGames.add(game);
        topGames.sort((a, b) -> Integer.compare(b, a)); // descending
        if (topGames.size() > 11) {
            topGames.remove(topGames.size() - 1); // remove lowest if >10
        }
    }




    private int calculatePrize(int spots, int matches) {
        if (spots == 10) {
            if (matches == 10) return 100000;
            if (matches == 9) return 4250;
            if (matches == 8) return 450;
            if (matches == 7) return 40;
            if (matches == 6) return 15;
            if (matches == 5) return 8;
            // added these
            if (matches == 4) return 6;
            if (matches == 3) return 4;
            if (matches == 2) return 2;
            ////
            if (matches == 0) return 0;
        } else if (spots == 4) {
            if (matches == 4) return 150;
            if (matches == 3) return 5;
            if (matches == 2) return 1;
        } else if (spots == 2) {
            if (matches == 2) return 10;
            if (matches == 1) return 1;
        } else if (spots == 1) {
            if (matches == 1) return 2;
        }
        return 0;
    }

    private Button createPlayButton() {
        Button playBtn = new Button("PLAY");
        playBtn.setStyle("-fx-background-color: " + background_gold + ";" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 20px;" +
                "-fx-font-weight: bold;" +
                "-fx-border-color: white;" +
                "-fx-border-width: 2px;" +
                "-fx-background-radius: 10;" +
                "-fx-border-radius: 10;");

        playBtn.prefWidth(300);
        playBtn.prefHeight(50);
        return playBtn;
    }

    private Label createKenoLabel(String lab) {
        Label label = new Label(lab);
        label.setStyle("-fx-background-color: " + background_gold + ";" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 40px;" +
                "-fx-font-weight: bold;" +
                "-fx-border-color: white;" +
                "-fx-border-width: 2px;" +
                "-fx-background-radius: 10;" +
                "-fx-border-radius: 10;");

        label.prefWidth(300);
        label.prefHeight(100);
        return label;
    }

    private void showRulesDialog() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Keno Rules");
        alert.setHeaderText("Keno Rules of Play");
        alert.setContentText(
                "1. Choose 1–10 numbers from 1–80.\n" +
                        "2. Then 20 numbers are drawn randomly.\n" +
                        "3. Multiply your winnings\n" +
                        "4. The more you match, the more money you win!\n" +
                        "5. Maximum of 4 draws."
        );
//        mabe customize the alert pane color. but this seems a little tricky
        DialogPane dp = alert.getDialogPane();
//        dp.setStyle("-fx-background-color: orange;");
//        dp.lookup()
        alert.showAndWait();
    }

    private void showOddsDialog() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Keno Odds");
        alert.setHeaderText("Odds of Winning");
        alert.setContentText(
                "Example odds (NC Lottery):\n" +
                        "Spot 1: 1 in 4.0\n" +
                        "Spot 4: 1 in 3.9\n" +
                        "Spot 8: 1 in 9.7\n" +
                        "Spot 10: 1 in 9.1"
        );
//        alert.getDialogPane().setStyle("-fx-background-color: #222831;");
        alert.showAndWait();
    }

//    private void applyTheme(BorderPane root){
//        String bgColor = isNewLook ? background_gold : background_purple;
//        String textColor = isNewLook ? "black" : "white";
//
//        root.setStyle("-fx-background-color: " + bgColor + ";");
//
//    }

    private MenuBar createMenuBar(Stage stage, BorderPane root) {
        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Menu Options (Click ▼)");

        MenuItem hintItem = new MenuItem("Select an option below:");
        hintItem.setDisable(true);

        MenuItem rulesItem = new MenuItem("Rules");
        MenuItem oddsItem = new MenuItem("Odds");
        MenuItem exitItem = new MenuItem("Exit");
        MenuItem newLook = new MenuItem("New Look!");

        menu.getItems().addAll(hintItem, rulesItem, oddsItem, newLook, exitItem);
        menuBar.getMenus().add(menu);
        menuBar.setStyle("-fx-text-fill: orange;" + "-fx-background-color: " + background_purple + ";"
                + "-fx-font-size: 18 px;" +
                "-fx-font-weight: bold;");

        // Attach event handlers
        rulesItem.setOnAction(e -> showRulesDialog());
        oddsItem.setOnAction(e -> showOddsDialog());
//        newLook.setOnAction(e -> {
        newLook.setOnAction(e -> {
            if (!isNewLook) {
                // Apply new theme
                root.setStyle("-fx-background-color: " + background_gold + "; -fx-text-fill: red; -fx-font-weight: bold;");
                menuBar.setStyle("-fx-background-color: " + background_gold + "; -fx-text-fill: red; -fx-font-size: 18px; -fx-font-weight: bold;");
                newLook.setText("Old Look");
                isNewLook = true;
            } else {
                // Revert to original
                root.setStyle("-fx-background-color: " + background_purple + "; -fx-text-fill: orange; -fx-font-weight: bold;");
                menuBar.setStyle("-fx-background-color: " + background_purple + "; -fx-text-fill: orange; -fx-font-size: 18px; -fx-font-weight: bold;");
                newLook.setText("New Look!");
                isNewLook = false;
            }
        });
        exitItem.setOnAction(e -> stage.close());

        return menuBar;
    }

    private Scene buildIntroSceen(Stage stage) {
        BorderPane root = new BorderPane();
        MenuBar menubar = createMenuBar(stage, root);
        Label welcomeLabel = createKenoLabel("WELCOME TO KENO!");
        Button playButton = createPlayButton();

        playButton.setOnAction(e -> {
            Scene gameScene = buildGameScreen(stage, menubar);
            stage.setScene(gameScene);
        });

        VBox center = new VBox(30, welcomeLabel, playButton);

        center.setAlignment(Pos.CENTER);
        root.setTop(menubar);
        root.setCenter(center);
        root.setStyle("-fx-text-fill: orange;" + "-fx-background-color: " + background_purple + ";"
                + "-fx-font-size: 18 px;" +
                "-fx-font-weight: bold;");

//        playButton.setOnAction(e-> stage.setScene(buildGameScreen(stage)));
        return new Scene(root, 700, 700);
    }
    private HBox buildGameMenu(Stage stage) {
        gamePlayButton.setOnAction(playGameEvent);
        randomSelection.setOnAction(e -> {
            int n = numSpots.getValue();
            randomPick(n);
        });

        gamePlayButton.setPrefSize(180, 50);
        gamePlayButton.setAlignment(Pos.CENTER);
        randomSelection.setPrefSize(180, 50);
        randomSelection.setAlignment(Pos.CENTER);

        Label spotLabel = new Label("Select Spots:");
        spotLabel.setStyle("-fx-font-size: 23px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: white;" +
                "-fx-background-color: " + background_purple + ";" +
                "-fx-background-radius: 20;" +
                "-fx-padding: 3px 20px;" +
                "-fx-border-radius: 20;");
        spotLabel.setAlignment(Pos.CENTER);
        spotLabel.setMaxWidth(200);
        numSpots = new ComboBox<>();
        numSpots.getItems().addAll(1, 4, 8, 10);
        numSpots.setValue(1);
        numSpots.setPrefWidth(200);
        numSpots.setPrefHeight(55);

        Label multiplierLabel = new Label("Select Multiplier:");
        multiplierLabel.setStyle("-fx-font-size: 23px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: white;" +
                "-fx-background-color: " + background_purple + ";" +
                "-fx-background-radius: 20;" +
                "-fx-padding: 3px 20px;" +
                "-fx-border-radius: 20;");
        multiplierLabel.setAlignment(Pos.CENTER);
        multiplierLabel.setMaxWidth(200);

        multiplier = new ComboBox<>();
        multiplier.getItems().addAll(1, 2, 3, 4);
        multiplier.setValue(1);
        multiplier.setPrefWidth(200);
        multiplier.setPrefHeight(55);
        VBox multiplierBox = new VBox(10, multiplierLabel, multiplier);
        VBox playNRandom = new VBox(10, randomSelection, gamePlayButton);
        VBox spotsBox = new VBox(10, spotLabel, numSpots);

        HBox optionsBox = new HBox(40, multiplierBox, playNRandom, spotsBox);
        optionsBox.setAlignment(Pos.BOTTOM_CENTER);
        optionsBox.setPadding(new Insets(20, 0, 20, 0));

        return optionsBox;
    }

    private GridPane initPayoutBox(){
        GridPane payoutGrid = new GridPane();
        payoutGrid.setAlignment(Pos.CENTER);
        payoutGrid.setHgap(20);
        payoutGrid.setVgap(5);
        Label header1 = new Label("Matches");
        header1.setStyle("-fx-font-size: 28px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: white;" +
                "-fx-background-color: " + background_purple + ";" +
                "-fx-background-radius: 20;" +
                "-fx-padding: 3px 20px;" +
                "-fx-border-radius: 20;");
        header1.setAlignment(Pos.CENTER);
        header1.setMaxWidth(300);
        Label header2 = new Label("Payout");
        header2.setStyle("-fx-font-size: 28px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: white;" +
                "-fx-background-color: " + background_purple + ";" +
                "-fx-background-radius: 20;" +
                "-fx-padding: 3px 20px;" +
                "-fx-border-radius: 20;");
        header2.setAlignment(Pos.CENTER);
        header2.setMaxWidth(300);
        int row = 0;
        payoutGrid.addRow(row++, header1, header2);
        for (var key : prizesMap.descendingKeySet()) {
            Integer value = prizesMap.get(key);

            Label matchLabel = new Label(String.valueOf(key));
            Label payoutLabel = new Label("$" + value);
            matchLabel.setAlignment(Pos.CENTER);
            matchLabel.setMaxWidth(300);
            matchLabel.setStyle("-fx-font-size: 28px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: white;" +
                    "-fx-background-color: " + background_purple + ";" +
                    "-fx-background-radius: 20;" +
                    "-fx-padding: 3px 20px;" +
                    "-fx-border-radius: 20;");
            payoutLabel.setAlignment(Pos.CENTER);
            payoutLabel.setMaxWidth(300);
            payoutLabel.setStyle("-fx-font-size: 28px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: white;" +
                    "-fx-background-color: " + background_purple + ";" +
                    "-fx-background-radius: 20;" +
                    "-fx-padding: 3px 20px;" +
                    "-fx-border-radius: 20;");
            payoutGrid.addRow(row++, matchLabel, payoutLabel);
        }
        return payoutGrid;
    }

    private GridPane initTopGames(){
        GridPane topGamesGrid = new GridPane();
        topGamesGrid.setAlignment(Pos.CENTER);
        topGamesGrid.setHgap(20);
        topGamesGrid.setVgap(5);

        Label title = new Label("Top Games");
        title.setStyle("-fx-font-size: 28px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: white;" +
                "-fx-background-color: " + background_purple + ";" +
                "-fx-background-radius: 20;" +
                "-fx-padding: 3px 20px;" +
                "-fx-border-radius: 20;");
        title.setAlignment(Pos.CENTER);
        title.setMaxWidth(300);

        Label games = new Label("High Scores");
        games.setStyle("-fx-font-size: 28px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: white;" +
                "-fx-background-color: " + background_purple + ";" +
                "-fx-background-radius: 20;" +
                "-fx-padding: 3px 20px;" +
                "-fx-border-radius: 20;");
        games.setAlignment(Pos.CENTER);
        games.setMaxWidth(300);

        topGamesGrid.addRow(0, title, games);

        int count = 0;
        for (int i=0; i<11; i++){
            // want to show 10 values regardless of number of games played for aesthetic reasons
            int score = (i < topGames.size()) ? topGames.get(i) : 0;

            Label num  = new Label(String.valueOf(i+1));
            Label entry = new Label("$" + score);
            num.setStyle("-fx-font-size: 28px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: white;" +
                    "-fx-background-color: " + background_purple + ";" +
                    "-fx-background-radius: 20;" +
                    "-fx-padding: 3px 20px;" +
                    "-fx-border-radius: 20;");
            num.setAlignment(Pos.CENTER);
            num.setMaxWidth(300);
            entry.setStyle("-fx-font-size: 28px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: white;" +
                    "-fx-background-color: " + background_purple + ";" +
                    "-fx-background-radius: 20;" +
                    "-fx-padding: 3px 20px;" +
                    "-fx-border-radius: 20;");
            entry.setAlignment(Pos.CENTER);
            entry.setMaxWidth(300);

            topGamesGrid.addRow(i+1, num, entry);
        }
        return topGamesGrid;
    }

    private Scene buildGameScreen(Stage stage, MenuBar menu) {
        BorderPane root = new BorderPane();
        MenuBar menubar = createMenuBar(stage, root);
        root.setTop(menubar);

        Image leftSevensImg = new Image(getClass().getResourceAsStream("/images/die.png"));
        Image rightSevensImg = new Image(getClass().getResourceAsStream("/images/die.png"));
        ImageView leftSevensImgView = new ImageView(leftSevensImg);
        ImageView rightSevensImgView = new ImageView(rightSevensImg);

        leftSevensImgView.setFitWidth(120);
        rightSevensImgView.setFitWidth(120);
        leftSevensImgView.setPreserveRatio(true);
        rightSevensImgView.setPreserveRatio(true);

        winningsLabel.setStyle("-fx-font-size: 28px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: white;" +
                "-fx-background-color: " + background_purple + ";" +
                "-fx-background-radius: 20;" +
                "-fx-padding: 3px 20px;" +
                "-fx-border-radius: 20;");

        //Winning box looks bad will fix later
        HBox winningsBox = new HBox(leftSevensImgView, winningsLabel, rightSevensImgView);
        winningsBox.setAlignment(Pos.CENTER);
        winningsBox.setPadding(new Insets(5, 0, 5, 0));

        GridPane grid = createGridPane();
        grid.setAlignment(Pos.CENTER);
        HBox gameMenu = buildGameMenu(stage);

        GridPane leftPayoutBox = initPayoutBox();
        VBox leftSide = new VBox(-80, leftPayoutBox);
        GridPane rightTopScoresSide = initTopGames();         ///
        VBox rightSide = new VBox(80, rightTopScoresSide);        ///
        leftSide.setAlignment(Pos.CENTER);
        rightSide.setAlignment(Pos.CENTER);

        VBox mainGame = new VBox(30, winningsBox, grid, gameMenu);

        HBox mainScene = new HBox(150, leftSide, mainGame, rightSide);
        mainScene.setAlignment(Pos.CENTER);
        mainScene.setPadding(new Insets(80, 0, 0, 0));

//        mainGame.setAlignment(Pos.CENTER);
//        mainGame.setPadding(new Insets(80, 0, 0, 0));
//        root.setCenter(mainGame);
        root.setCenter(mainScene);

        root.setStyle("-fx-text-fill: orange;" + "-fx-background-color: " + background_purple + ";"
                + "-fx-font-size: 18 px;" +
                "-fx-font-weight: bold;");

        return new Scene(root, 700, 700);
    }

    //feel free to remove the starter code from this method
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Keno");
        primaryStage.setScene(buildIntroSceen(primaryStage));
//        primaryStage.setScene(buildGameScreen(primaryStage));
        primaryStage.show();
    }
}