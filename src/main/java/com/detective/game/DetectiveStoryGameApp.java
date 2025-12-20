package com.detective.game;

import com.detective.game.model.GameData;
import com.detective.game.model.GameState;
import com.detective.game.ui.GameView;
import com.detective.game.ui.IntroView;
import com.detective.game.ui.Sfx;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DetectiveStoryGameApp extends Application {

    @Override
    public void start(Stage stage) {
        Sfx sfx = new Sfx();

        IntroView intro = new IntroView((data, state) -> {
            GameView game = new GameView(data, state, sfx);
            Scene gameScene = new Scene(game, 1100, 720);
            gameScene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            stage.setScene(gameScene);
        }, sfx);

        Scene scene = new Scene(intro, 1100, 720);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        stage.setTitle("Detective Story Game — The Library Case");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
