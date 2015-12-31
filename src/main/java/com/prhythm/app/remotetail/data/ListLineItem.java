package com.prhythm.app.remotetail.data;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Log 行
 * Created by nanashi07 on 15/12/31.
 */
public class ListLineItem extends ListCell<Line> {

    static double ROW_ID_WIDTH = 0;

    HBox hBox;
    Label rowId;
    Label text;

    public ListLineItem() {
        hBox = new HBox();
        hBox.getChildren().addAll(rowId = new Label(), text = new Label());

        rowId.setPadding(new Insets(0, 5, 0, 5));
        rowId.setBackground(new Background(new BackgroundFill(Color.LIGHTGREY, null, null)));
        rowId.setFont(Font.font("Courier New")); // fixme 字型可設定

        text.setPadding(new Insets(0, 5, 0, 5));
        text.setFont(Font.font("Courier New"));  // fixme 字型可設定

        setPadding(new Insets(0, 0, 0, 0));
    }

    @Override
    protected void updateItem(Line item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            rowId.setText(null);
            text.setText(null);
            setGraphic(null);
        } else {
            rowId.setText(String.valueOf(item.getIndex()));
            text.setText(item.toString());
            setGraphic(hBox);
        }

        ROW_ID_WIDTH = Math.max(rowId.getPrefWidth(), ROW_ID_WIDTH);
        rowId.setPrefWidth(ROW_ID_WIDTH);
    }

}
