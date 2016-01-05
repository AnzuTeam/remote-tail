package com.prhythm.app.remotetail.data;

import com.prhythm.app.remotetail.models.DataWrapper;
import com.prhythm.app.remotetail.models.HighLight;
import com.prhythm.app.remotetail.models.HighLights;
import com.prhythm.app.remotetail.models.Preference;
import com.prhythm.core.generic.data.Singleton;
import com.prhythm.core.generic.exception.RecessiveException;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

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
    HBox content;

    public LineListCell() {
        setPadding(new Insets(0, 0, 0, 0));

        FXMLLoader loader = new FXMLLoader(LAYOUT_URL, Singleton.of(ResourceBundle.class));
        try {
            hBox = loader.load();
        } catch (IOException e) {
            throw new RecessiveException(e.getMessage(), e);
        }
        rowId = (Label) hBox.lookup("#rowId");
        content = (HBox) hBox.lookup("#content");
    }

    @Override
    protected void updateItem(Line item, boolean empty) {
        super.updateItem(item, empty);  // 重要，不能刪
        if (empty) {
            rowId.setText(null);
            content.getChildren().clear();
            setGraphic(null);
        } else {
            DataWrapper wrapper = Singleton.of(DataWrapper.class);
            Preference preference = wrapper.getPreference();
            HighLights highLights = wrapper.getHighLights();

            String num = String.valueOf(item.getIndex());
            MAX_INDEX_WIDTH = Math.max(MAX_INDEX_WIDTH, num.length() * (rowId.getFont().getSize() * 0.8));

            rowId.setText(num);
            rowId.setFont(Font.font(preference.getFontFamily(), preference.getFontSize()));
            rowId.setPrefWidth(MAX_INDEX_WIDTH);

            HighLight match = highLights.match(item.toString());
            if (highLights.isMarkSearchPattern()) {
                Label text = new Label(item.toString());
                text.setFont(Font.font(preference.getFontFamily(), preference.getFontSize()));
                content.getChildren().clear();
                content.getChildren().add(text);
            } else {
                Label text = new Label(item.toString());
                if (match == null) {
                    text.setFont(Font.font(preference.getFontFamily(), preference.getFontSize()));
                } else {
                    // 字體
                    text.setFont(Font.font(
                            preference.getFontFamily(),
                            match.isBold() ? FontWeight.BOLD : FontWeight.NORMAL,
                            match.isItalic() ? FontPosture.ITALIC : FontPosture.REGULAR,
                            preference.getFontSize()
                    ));
                    // 顏色
                    text.setStyle(String.format(
                            "-fx-background-color: #%s; -fx-text-fill: #%s;",
                            match.getBackground().substring(2),
                            match.getForeground().substring(2)
                    ));
                }
                content.getChildren().clear();
                content.getChildren().add(text);
            }

            setGraphic(hBox);
        }
    }


}
