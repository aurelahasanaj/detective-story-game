package com.detective.game.ui;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.function.Function;

public class CardCell<T> extends ListCell<T> {
    private final Function<T, String> titleFn;
    private final Function<T, String> descFn;
    private final Function<T, String> imgFn;

    public CardCell(Function<T, String> titleFn,
                    Function<T, String> descFn,
                    Function<T, String> imgFn) {
        this.titleFn = titleFn;
        this.descFn = descFn;
        this.imgFn = imgFn;
    }

    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
            return;
        }

        ImageView iv = new ImageView();
        iv.setFitWidth(86);
        iv.setFitHeight(54);
        iv.setPreserveRatio(true);
        try {
            String p = imgFn.apply(item);
            if (p != null) iv.setImage(new Image(getClass().getResourceAsStream(p)));
        } catch (Exception ignored) {}

        Label t = new Label(titleFn.apply(item));
        t.setStyle("-fx-font-weight: 800; -fx-font-size: 14px;");
        Label d = new Label(descFn.apply(item));
        d.setStyle("-fx-text-fill: rgba(232,238,252,0.75);");
        d.setWrapText(true);

        VBox vv = new VBox(3, t, d);
        HBox row = new HBox(10, iv, vv);
        row.setPadding(new Insets(8));
        HBox.setHgrow(vv, Priority.ALWAYS);

        setGraphic(row);
    }
}
