package com.prhythm.app.remotetail.data;

import com.prhythm.app.remotetail.models.DataWrapper;
import com.prhythm.app.remotetail.models.Preference;
import com.prhythm.core.generic.data.Singleton;
import com.prhythm.core.generic.exception.RecessiveException;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Log 行
 * Created by nanashi07 on 15/12/31.
 */
public class LineListCell extends ListCell<Line> {

    final static String LAYOUT = "/com/prhythm/app/remotetail/core/list.cell.line.fxml";
    final static URL LAYOUT_URL = LineListCell.class.getResource(LAYOUT);

    public static double MAX_INDEX_WIDTH = 0;

    HBox hBox;
    Label rowId;
    Label text;

    public LineListCell() {
        setPadding(new Insets(0, 0, 0, 0));

        FXMLLoader loader = new FXMLLoader(LAYOUT_URL, Singleton.of(ResourceBundle.class));
        try {
            hBox = loader.load();
        } catch (IOException e) {
            throw new RecessiveException(e.getMessage(), e);
        }
        rowId = (Label) hBox.lookup("#rowId");
        text = (Label) hBox.lookup("#text");
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
