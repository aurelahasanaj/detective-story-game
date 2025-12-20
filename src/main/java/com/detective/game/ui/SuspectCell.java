package com.detective.game.ui;

import com.detective.game.model.Suspect;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.function.Function;

public class SuspectCell extends ListCell<Suspect> {
    private final Function<String, Integer> suspicionFn;

    public SuspectCell(Function<String, Integer> suspicionFn) {
        this.suspicionFn = suspicionFn;
    }

    @Override
    protected void updateItem(Suspect item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setGraphic(null);
            setText(null);
            return;
        }

        Label name = new Label(item.getName());
        name.setStyle("-fx-font-weight: 900; -fx-font-size: 14px;");
        Label role = new Label(item.getRole());
        role.setStyle("-fx-text-fill: rgba(232,238,252,0.75);");

        int s = suspicionFn.apply(item.getId());
        Label score = new Label("Suspicion: " + s);
        score.setStyle("-fx-text-fill: rgba(232,238,252,0.75);");

        VBox info = new VBox(2, name, role, score);
        HBox row = new HBox(info);
        row.setPadding(new Insets(8));
        HBox.setHgrow(info, Priority.ALWAYS);

        setGraphic(row);
    }
}
