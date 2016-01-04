package com.prhythm.app.remotetail.data;

import com.prhythm.app.remotetail.models.DataWrapper;
import com.prhythm.app.remotetail.models.Preference;
import com.prhythm.core.generic.data.Singleton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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

    public static double MAX_INDEX_WIDTH = 0;

    HBox hBox;
    Label rowId;
    Label text;

    public ListLineItem() {
        hBox = new HBox();
        hBox.getChildren().addAll(rowId = new Label(), text = new Label());

        rowId.setPadding(new Insets(0, 5, 0, 5));
        rowId.setBackground(new Background(new BackgroundFill(Color.LIGHTGREY, null, null)));
        rowId.setAlignment(Pos.BASELINE_RIGHT);

        text.setPadding(new Insets(0, 5, 0, 5));

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
            Preference preference = Singleton.of(DataWrapper.class).getPreference();
            String num = String.valueOf(item.getIndex());
            MAX_INDEX_WIDTH = Math.max(MAX_INDEX_WIDTH, num.length() * (rowId.getFont().getSize() * 0.8));

            rowId.setText(num);
            rowId.setFont(Font.font(preference.getFontFamily(), preference.getFontSize()));
            rowId.setPrefWidth(MAX_INDEX_WIDTH);

            text.setText(item.toString());
            text.setFont(Font.font(preference.getFontFamily(), preference.getFontSize()));

            setGraphic(hBox);
        }

    }

}
