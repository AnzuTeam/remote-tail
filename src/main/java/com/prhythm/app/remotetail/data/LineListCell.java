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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        // 載入 UI 設計
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
            // 取得設定值
            DataWrapper wrapper = Singleton.of(DataWrapper.class);
            Preference preference = wrapper.getPreference();
            HighLights highLights = wrapper.getHighLights();
            String searchText = ((MainController) Singleton.of(FXMLLoader.class).getController()).getSearchText();

            // 計算行號欄的寬
            String num = String.valueOf(item.getIndex());
            MAX_INDEX_WIDTH = Math.max(MAX_INDEX_WIDTH, num.length() * (rowId.getFont().getSize() * 0.8));

            // 設定顯示的行號
            rowId.setText(num);
            rowId.setFont(Font.font(preference.getFontFamily(), preference.getFontSize()));
            rowId.setPrefWidth(MAX_INDEX_WIDTH);

            /** 顯示 log 內容 **/
            // 先清除資料
            children.clear();

            if (item.getContent() == null) {
                // 未載入時，直接以預設格式顯示
                children.add(createStandardText(preference, item.toString()));
            } else {
                // 取得符合的顯著標示設定
                HighLight match = highLights.match(item.toString());
                if (highLights.isMarkSearchPattern() && !Strings.isNullOrWhiteSpace(searchText)) {
                    // 處理搜尋文字的顯著標示
                    String[] values = item.toString().split(searchText);
                    // 取得符合的項目
                    Matcher matcher = Pattern.compile(searchText).matcher(item.toString());
                    // 將分割的字串與符合的字串重新組合
                    if (match == null) {
                        for (int i = 0; i < values.length; i++) {
                            if (i > 0 && matcher.find()) {
                                //noinspection ConstantConditions
                                children.add(createHighLightedSearchPattern(preference, match, matcher.group()));
                            }
                            children.add(createStandardText(preference, values[i]));
                        }
                    } else {
                        for (int i = 0; i < values.length; i++) {
                            if (i > 0 && matcher.find()) {
                                children.add(createHighLightedSearchPattern(preference, match, matcher.group()));
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
            }

            setGraphic(hBox);
        }
    }

    /**
     * 建立標準（無顯著標示）的 log 內容
     *
     * @param preference 偏好設定
     * @param value      log 文字內容
     * @return
     */
    Label createStandardText(Preference preference, String value) {
        Label text = new Label(value);
        text.setFont(Font.font(preference.getFontFamily(), preference.getFontSize()));
        return text;
    }

    /**
     * 建立有搜尋文字的顯著標示的 log 內容
     *
     * @param preference 偏好設定
     * @param match      符合的 {@link HighLight} 設定
     * @param value      log 文字內容
     * @return
     */
    Label createHighLightedSearchPattern(Preference preference, HighLight match, String value) {
        Label text = new Label(value);
        if (match == null) {
            text.setFont(Font.font(preference.getFontFamily(), preference.getFontSize()));
            // 顏色
            switch (preference.getTheme()) {
                default:
                case White:
                    text.setStyle("-fx-background-color: #222222; -fx-text-fill: #dddddd;");
                    break;
                case Dark:
                    text.setStyle("-fx-background-color: #dddddd; -fx-text-fill: #222222;");
                    break;
            }
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
                    oppositeColor(match.getBackground().substring(2, 8)),
                    oppositeColor(match.getForeground().substring(2, 8))
            ));
        }
        return text;
    }

    /**
     * 計算對比色
     *
     * @param color 顏色(RRGGBB)
     * @return
     */
    String oppositeColor(String color) {
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

    /**
     * 建立顯著標示的 log 內容
     *
     * @param preference 偏好設定
     * @param match      符合的 {@link HighLight} 設定
     * @param value      log 文字內容
     * @return
     */
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
