package com.detective.game.ui;

import com.detective.game.model.GameData;
import com.detective.game.model.GameState;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class IntroView extends StackPane {
    public interface StartHandler {
        void start(GameData data, GameState state);
    }

    private static final String RULES =
            "Welcome, Detective.\n\n" +
            "GOAL\n" +
            "- Find who stole the manuscript by collecting evidence and exposing lies.\n\n" +
            "ACTIONS (you start with 6)\n" +
            "- Search a location = costs 1 action.\n" +
            "- Interview a suspect = costs 1 action.\n" +
            "- Present evidence to a suspect = FREE.\n" +
            "- When actions reach 0, accuse.\n\n" +
            "COLLECT EVIDENCE\n" +
            "1) Locations tab\n" +
            "2) Select a location\n" +
            "3) SEARCH LOCATION (costs 1 action)\n\n" +
            "EXPOSE LIES\n" +
            "1) Suspects tab -> Interview\n" +
            "2) Select suspect + evidence\n" +
            "3) Present Selected Evidence (FREE)\n\n" +
            "ENDING\n" +
            "Accuse tab -> choose suspect -> Make Accusation\n";

    public IntroView(StartHandler startHandler, Sfx sfx) {
        setPadding(new Insets(24));

        Label title = new Label("🕵️ DETECTIVE STORY GAME");
        title.getStyleClass().add("title-label");
        title.setStyle("-fx-font-size: 36px;");

        Label caseName = new Label("Case: The Missing Manuscript");
        caseName.getStyleClass().add("subtle");
        caseName.setStyle("-fx-font-size: 18px; -fx-font-weight: 700;");

        ImageView art = new ImageView();
        try {
            art.setImage(new Image(getClass().getResourceAsStream("/assets/locations/rare_books.png")));
            art.setFitWidth(900);
            art.setPreserveRatio(true);
            art.setSmooth(true);
        } catch (Exception ignored) {}

        TextArea rules = new TextArea(RULES);
        rules.setEditable(false);
        rules.setWrapText(true);
        rules.setFocusTraversable(false);
        rules.setPrefRowCount(12);

        Button how = new Button("How to Play");
        how.getStyleClass().add("btn-ghost");
        how.setOnAction(e -> {
            sfx.click();
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("How to Play");
            a.setHeaderText("Full Instructions");
            a.setContentText(RULES);
            a.showAndWait();
        });

        Button start = new Button("Start Investigation");
        start.getStyleClass().add("btn-primary");
        start.setOnAction(e -> {
            sfx.click();
            GameData data = GameData.createDefaultCase();
            GameState state = new GameState(data);
            startHandler.start(data, state);
        });

        HBox btns = new HBox(12, how, start);
        btns.setAlignment(Pos.CENTER);

        VBox box = new VBox(14, title, caseName, art, rules, btns);
        box.setAlignment(Pos.CENTER);
        VBox.setVgrow(art, Priority.ALWAYS);

        getChildren().add(box);
    }
}
