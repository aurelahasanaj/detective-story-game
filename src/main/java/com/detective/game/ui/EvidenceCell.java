package com.detective.game.ui;

import com.detective.game.model.Evidence;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;

public class EvidenceCell extends ListCell<Evidence> {
    @Override
    protected void updateItem(Evidence item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setGraphic(null);
            setText(null);
            return;
        }

        Label title = new Label(item.getTitle());
        title.setStyle("-fx-font-weight: 800;");
        Label desc = new Label(item.getDescription());
        desc.setStyle("-fx-text-fill: rgba(232,238,252,0.75);");
        desc.setWrapText(true);

        VBox box = new VBox(3, title, desc);
        box.setPadding(new Insets(8));
        setGraphic(box);
    }
}
