package com.prhythm.app.remotetail.data;

import com.prhythm.app.remotetail.core.MainController;
import com.prhythm.app.remotetail.models.DataWrapper;
import com.prhythm.app.remotetail.models.HighLight;
import com.prhythm.app.remotetail.models.HighLights;
import com.prhythm.app.remotetail.models.Preference;
import com.prhythm.core.generic.data.Singleton;
import com.prhythm.core.generic.exception.RecessiveException;
import com.prhythm.core.generic.util.Strings;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
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
        ObservableList<Node> children = content.getChildren();
        if (empty) {
            rowId.setText(null);
            children.clear();
            setGraphic(null);
        } else {
            DataWrapper wrapper = Singleton.of(DataWrapper.class);
            Preference preference = wrapper.getPreference();
            HighLights highLights = wrapper.getHighLights();
            String searchText = ((MainController) Singleton.of(FXMLLoader.class).getController()).getSearchText();

            String num = String.valueOf(item.getIndex());
            MAX_INDEX_WIDTH = Math.max(MAX_INDEX_WIDTH, num.length() * (rowId.getFont().getSize() * 0.8));

            rowId.setText(num);
            rowId.setFont(Font.font(preference.getFontFamily(), preference.getFontSize()));
            rowId.setPrefWidth(MAX_INDEX_WIDTH);

            children.clear();
            HighLight match = highLights.match(item.toString());
            if (highLights.isMarkSearchPattern() && !Strings.isNullOrWhiteSpace(searchText)) {
                String[] values = item.toString().split(searchText);
                if (match == null) {
                    for (int i = 0; i < values.length; i++) {
                        if (i > 0) {
                            //noinspection ConstantConditions
                            children.add(createHighLightedSearchPattern(preference, match, searchText));
                        }
                        children.add(createStandardText(preference, values[i]));
                    }
                } else {
                    for (int i = 0; i < values.length; i++) {
                        if (i > 0) {
                            children.add(createHighLightedSearchPattern(preference, match, searchText));
                        }
                        children.add(createHighLightedText(preference, match, values[i]));
                    }
                }
            } else {
                Label text;
                if (match == null) {
                    text = createStandardText(preference, item.toString());
                } else {
                    text = createHighLightedText(preference, match, item.toString());
                }
                children.add(text);
            }

            setGraphic(hBox);
        }
    }

    Label createStandardText(Preference preference, String value) {
        Label text = new Label(value);
        text.setFont(Font.font(preference.getFontFamily(), preference.getFontSize()));
        return text;
    }

    Label createHighLightedSearchPattern(Preference preference, HighLight match, String value) {
        Label text = new Label(value);
        if (match == null) {
            text.setFont(Font.font(preference.getFontFamily(), preference.getFontSize()));
            // 顏色
            text.setStyle(
                    preference.getTheme() == Preference.Theme.White
                            ? "-fx-background-color: #222222; -fx-text-fill: #dddddd;" :
                            "-fx-background-color: #dddddd; -fx-text-fill: #222222;"
            );
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
                    opposite(match.getBackground().substring(2, 8)),
                    opposite(match.getForeground().substring(2, 8))
            ));
        }
        return text;
    }

    /**
     * 計算反色
     *
     * @param color
     * @return
     */
    String opposite(String color) {
        color = color.toUpperCase();
        String result = "";
        char ch;
        String list1 = "0123456789ABCDEF";
        String list2 = "FEDCBA9876543210";

        for (int i = 0; i < color.length(); i++) {
            ch = color.charAt(i);
            for (int n = 0; n < list1.length(); n++) {
                if (ch == list1.charAt(n)) result += list2.charAt(n);
            }
        }
        return result;
    }

    Label createHighLightedText(Preference preference, HighLight match, String value) {
        Label text = new Label(value);
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
        return text;
    }

}
