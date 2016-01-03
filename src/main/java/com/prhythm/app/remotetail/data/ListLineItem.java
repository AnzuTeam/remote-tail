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

    static double INDEX_COUNT = 0;

    HBox hBox;
    Label rowId;
    Label text;

    public ListLineItem() {
        Preference preference = Singleton.of(DataWrapper.class).getPreference();

        hBox = new HBox();
        hBox.getChildren().addAll(rowId = new Label(), text = new Label());

        rowId.setPadding(new Insets(0, 5, 0, 5));
        rowId.setBackground(new Background(new BackgroundFill(Color.LIGHTGREY, null, null)));
        rowId.setAlignment(Pos.BASELINE_RIGHT);
        rowId.setFont(Font.font(preference.getFontFamily(), preference.getFontSize()));

        text.setPadding(new Insets(0, 5, 0, 5));
        text.setFont(Font.font(preference.getFontFamily(), preference.getFontSize()));

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
            INDEX_COUNT = Math.max(INDEX_COUNT, String.valueOf(item.getIndex()).length());

            rowId.setText(String.valueOf(item.getIndex() + 1));
            text.setText(item.toString());
            setGraphic(hBox);
        }

        rowId.setPrefWidth(rowId.getFont().getSize() * INDEX_COUNT * 0.8);
    }

}
