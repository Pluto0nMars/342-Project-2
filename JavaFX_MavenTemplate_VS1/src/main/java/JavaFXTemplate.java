import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Application;

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


public class JavaFXTemplate extends Application {

    private static final String background_purple = "#6151F5";
    private static final String secondary_orange = "orange";
    private static final String background_gold = "#D0BA6B";
    private static final String secondary_red = "#B30400";


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

    private static final String UNSELECTED_COLOR = "#1E90FF";
    private static final String SELECTED_COLOR = "#778899";


    private GridPane createGridPane(){
        GridPane grid = new GridPane();

        grid.setVgap(5);
        grid.setHgap(5);

        int num = 1;

        for (int row = 0; row < 8; row++){
            for (int col = 0; col < 10; col++){
                Button numButton = new Button(String.valueOf(num++));
                numButton.setPrefSize(50, 50);


                String unselectedStyle =
                        "-fx-background-color: " + UNSELECTED_COLOR + ";" +
                                "-fx-font-size: 18px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-text-fill: white;" +
                                "-fx-border-color: orange;" +
                                "-fx-border-width: 2px;" +
                                "-fx-background-radius: 50;" +
                                "-fx-border-radius: 50;";

                String selectedStyle =
                        "-fx-background-color: " +  SELECTED_COLOR + ";" +
                                "-fx-font-size: 18px;" +
                                "-fx-font-weight: bold;" +
                                "-fx-text-fill: white;" +
                                "-fx-border-color: orange;" +
                                "-fx-border-width: 2px;" +
                                "-fx-background-radius: 50;" +
                                "-fx-border-radius: 50;";
                numButton.setStyle(unselectedStyle);

                numButton.setOnAction(e-> {
                    // Check if the button currently contains the UNSELECTED_COLOR style
                    if (numButton.getStyle().contains(UNSELECTED_COLOR)) {
                        numButton.setStyle(selectedStyle);
                    } else {
                        numButton.setStyle(unselectedStyle);
                    }
                });

                grid.add(numButton, col, row);
            }
        }

        return grid;
    }

    private String calculatePrize(int spots, int matches) {
        if (spots == 10) {
            if (matches == 10) return "$100000";
            if (matches == 9) return "$4250";
            if (matches == 8) return "$450";
            if (matches == 7) return "$40";
            if (matches == 6) return "$15";
            if (matches == 5) return "$2";
            if (matches == 0) return "$5";
        } else if (spots == 4) {
            if (matches == 4) return "$150";
            if (matches == 3) return "$5";
            if (matches == 2) return "$1";
        } else if (spots == 2) {
            if (matches == 2) return "$10";
            if (matches == 1) return "$1";
        } else if (spots == 1) {
            if (matches == 1) return "$2";
        }
        return "$0";
    }

    private Button createPlayButton(){
        Button playBtn = new Button("PLAY");
        playBtn.setStyle("-fx-background-color: orange;" +
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
    private Label createKenoLabel(String lab){
        Label label = new Label(lab);
        label.setStyle("-fx-background-color: orange;" +
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
    private void showRulesDialog(){
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Keno Rules");
        alert.setHeaderText("Keno Rules of Play");
        alert.setContentText(
            "1. Choose 1–10 numbers from 1–80.\n" +
            "2. Then 20 numbers are drawn randomly.\n" +
            "3. Multiply your winnings\n" +
            "4. The more you match, the more money you win!\n"+
            "5. Maximum of 4 draws."
        );
        DialogPane dp = alert.getDialogPane();
//        dp.setStyle("-fx-background-color: orange;");
//        dp.lookup()
        alert.showAndWait();
    }

    private void showOddsDialog(){
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

    private MenuBar createMenuBar(Stage stage, BorderPane root){
        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Menu Options (Click ▼)");

        MenuItem hintItem = new MenuItem("Select an option below:");
        hintItem.setDisable(true);

        MenuItem rulesItem = new MenuItem("Rules");
        MenuItem oddsItem = new MenuItem("Odds");
        MenuItem exitItem = new MenuItem("Exit");
        MenuItem newLook = new MenuItem("New Look!");

        menu.getItems().addAll(hintItem,rulesItem, oddsItem, newLook, exitItem);
        menuBar.getMenus().add(menu);
        menuBar.setStyle("-fx-text-fill: orange;" + "-fx-background-color: " + background_purple + ";"
                +"-fx-font-size: 18 px;" +
                "-fx-font-weight: bold;");

        // Attach event handlers
        rulesItem.setOnAction(e -> showRulesDialog());
        oddsItem.setOnAction(e -> showOddsDialog());
        newLook.setOnAction(e -> root.setStyle("-fx-background-color: " + background_gold + ";"));      //method call to change the scene theme and the menu theme color as well
        exitItem.setOnAction(e -> stage.close());

        return menuBar;
    }

    private Scene buildIntroSceen (Stage stage){
        BorderPane root = new BorderPane();
        MenuBar menubar = createMenuBar(stage, root);
        Label welcomeLabel = createKenoLabel("WELCOME TO KENO!");
        Button playButton = createPlayButton();

        VBox center = new VBox(30, welcomeLabel, playButton);

        center.setAlignment(Pos.CENTER);
        root.setTop(menubar);
        root.setCenter(center);
        root.setStyle("-fx-text-fill: orange;" + "-fx-background-color: " + background_purple + ";"
                +"-fx-font-size: 18 px;" +
                "-fx-font-weight: bold;");

//        playButton.setOnAction(e-> stage.setScene(buildGameScreen(stage)));
        return new Scene(root, 700,700);
    }

//    private Scene buildGameScreen (Stage stage){

//        return new Scene(stage);
//    }

	//feel free to remove the starter code from this method
	@Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Keno");
        primaryStage.setScene(buildIntroSceen(primaryStage));
        primaryStage.show();
    }





//    existing code is below. may delete if approved by Lord Sola





//	public void start(Stage primaryStage) throws Exception {
//        BorderPane root = new BorderPane();
//        MenuBar menuBar = new MenuBar();
//        Label kenoLabel = new Label("KENO");
//        Label welcomeLabel = new Label("WELCOME TO KENO!");
//        Button playBtn = createPlayButton();
//
////        Button playBtn = new Button("PLAY");
//
//        BorderPane gamePlay =new BorderPane();
//
//        Label winningsLabel = new Label("Winnings: $0");
//        winningsLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");
//
//        Menu menu = new Menu("Menu Options (Click ▼)");
//
//        menu.setStyle("-fx-text-fill: orange;");
//
//        menuBar.lookupAll(".label").forEach(node -> node.setStyle("-fx-text-fill: orange;"));
//
//        MenuItem hintItem = new MenuItem("Select an option below:");
//        hintItem.setDisable(true);
//
//        MenuItem rulesItem = new MenuItem("Rules");
//        MenuItem oddsItem = new MenuItem("Odds");
//        MenuItem exitItem = new MenuItem("Exit");
//        MenuItem newLook = new MenuItem("New Look");
//
//        rulesItem.setStyle("-fx-text-fill: orange;");
//        oddsItem.setStyle("-fx-text-fill: orange;");
//        newLook.setStyle("-fx-text-fill: orange;");
//        exitItem.setStyle("-fx-text-fill: orange;");
//
//
//        menu.getItems().addAll(hintItem,rulesItem, oddsItem, exitItem);
//        menuBar.getMenus().add(menu);
//        menuBar.setStyle("-fx-text-fill: orange;" + "-fx-background-color: #1E90FF;"
//                +"-fx-font-size: 18 px;" +
//                "-fx-font-weight: bold;");
//
//
//        kenoLabel.setStyle( "-fx-text-fill: orange;" +
//                "-fx-font-size: 24px;" +
//                "-fx-font-weight: bold;");
//
//        welcomeLabel.setStyle("-fx-text-fill: orange;" +
//                "-fx-font-size: 48 px;" +
//                "-fx-font-weight: bold;");
//
////        playBtn.setStyle("-fx-background-color: #32CD32;" +
////        "-fx-text-fill: white;" +
////                "-fx-font-size: 20px;" +
////                "-fx-font-weight: bold;" +
////                "-fx-border-color: white;" +
////                "-fx-border-width: 2px;" +
////                "-fx-background-radius: 10;" +
////                "-fx-border-radius: 10;");
////
////        playBtn.prefWidth(300);
////        playBtn.prefHeight(50);
//
//        //Style border pane
//        root.setStyle("-fx-background-color: linear-gradient(to bottom, #1E90FF, #4682B4);");
//
//        HBox top = new HBox(menuBar, kenoLabel);
//        VBox center = new VBox(30,welcomeLabel, playBtn);
//        center.setStyle("-fx-alignment: center;");
//
//
//        top.setStyle("-fx-border-color: #FFA500;" +
//                "-fx-border-width: 5px;" );
//
//        top.setSpacing(400);
//
//
//        root.setTop(top);
//        root.setCenter(center);
//
//
//        gamePlay.setStyle("-fx-background-color: linear-gradient(to bottom, #1E90FF, #4682B4);");
//        gamePlay.setTop(top);
//
//        Scene gameSCene =new Scene(gamePlay, 700, 700);
//
//        playBtn.setOnAction(e->{
//
//            double currentWidth = primaryStage.getWidth();
//            double currentHeight = primaryStage.getHeight();
//            boolean wasMaximized = primaryStage.isMaximized();
//
//
//            if(!menu.getItems().contains(newLook)){
//                menu.getItems().add(3, newLook);
//            }
//            playBtn.setDisable(true);
//
//            GridPane betCard = createGridPane();
//
//
//
//
//            VBox wrapperGrid = new VBox(30, betCard, winningsLabel);
//            gamePlay.setCenter(wrapperGrid);
//            wrapperGrid.setAlignment(Pos.CENTER);
//            wrapperGrid.setPadding(new javafx.geometry.Insets(0, 0, 0, 50));
//            primaryStage.setWidth(currentWidth);
//            primaryStage.setHeight(currentHeight);
//
//
//            if (wasMaximized) {
//                primaryStage.setMaximized(true);
//            }
//
//            primaryStage.setScene(gameSCene);
//        });
//
//
//
//        oddsItem.setOnAction(e -> {
//            Alert alert = new Alert(AlertType.INFORMATION);
//            alert.setTitle("Keno Odds");
//            alert.setHeaderText("Odds of Winning");
//            alert.setContentText(
//                    "Example odds (NC Lottery):\n" +
//                            "Spot 1: 1 in 4.0\n" +
//                            "Spot 4: 1 in 3.9\n" +
//                            "Spot 8: 1 in 9.7\n" +
//                            "Spot 10: 1 in 9.1"
//            );
//            // Dark background for dialog
//            alert.getDialogPane().setStyle("-fx-background-color: #222831;");
//
//            // Style header
//            Label headerLabel = (Label) alert.getDialogPane().lookup(".header-label");
//            if (headerLabel != null) {
//                headerLabel.setStyle("-fx-text-fill: white; " +
//                                    "-fx-font-size: 16px;" +
//                                    "-fx-font-family: Comic Sans MS;" +
//                                    "-fx-font-weight: bold;");
//            }
//
//            // Style content text
//            Label contentLabel = (Label) alert.getDialogPane().lookup(".content.label");
//            if (contentLabel != null) {
//                contentLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-font-family: Comic Sans MS; -fx-font-weight: bold;");
//            }
//
//            // Style OK button
//            alert.getDialogPane().lookupButton(ButtonType.OK).setStyle(
//                    "-fx-background-color: #00ADB5; -fx-text-fill: white; -fx-font-weight: bold;"
//            );
//
//            alert.showAndWait();
//        });
//
//        rulesItem.setOnAction(e -> {
//            Alert alert = new Alert(AlertType.INFORMATION);
//            alert.setTitle("Keno Rules");
//            alert.setHeaderText("Keno Rules of Play");
//            alert.setContentText(
//                                    "1. Choose 1–10 numbers from 1–80.\n" +
//                                    "2. Then 20 numbers are drawn randomly.\n" +
//                                    "3. Multiply your winnings\n" +
//                                    "4. The more you match, the more money you win!\n"+
//                                    "5. Maximum of 4 draws."
//            );
//            // Dark background for dialog
//            alert.getDialogPane().setStyle("-fx-background-color: #222831;");
//
//            // Style header
//            Label headerLabel = (Label) alert.getDialogPane().lookup(".header-label");
//            if (headerLabel != null) {
//                headerLabel.setStyle("-fx-text-fill: white; " +
//                        "-fx-font-size: 16px;" +
//                        "-fx-font-family: Comic Sans MS;" +
//                        "-fx-font-weight: bold;");
//            }
//
//            // Style content text
//            Label contentLabel = (Label) alert.getDialogPane().lookup(".content.label");
//            if (contentLabel != null) {
//                contentLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px; -fx-font-family: Comic Sans MS; -fx-font-weight: bold;");
//            }
//
//            // Style OK button
//            alert.getDialogPane().lookupButton(ButtonType.OK).setStyle(
//                    "-fx-background-color: #00ADB5; -fx-text-fill: white; -fx-font-weight: bold;"
//            );
//
//            alert.showAndWait();
//        });
//
//        exitItem.setOnAction(e -> primaryStage.close());
//
//
//
//	     Scene scene = new Scene(root, 700,700);
//			primaryStage.setScene(scene);
//			primaryStage.show();
//
//
//
//	}

}
