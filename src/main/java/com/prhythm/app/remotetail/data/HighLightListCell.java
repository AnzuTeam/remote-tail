package com.prhythm.app.remotetail.data;

import com.prhythm.app.remotetail.models.DataWrapper;
import com.prhythm.app.remotetail.models.HighLight;
import com.prhythm.app.remotetail.models.Preference;
import com.prhythm.core.generic.data.Singleton;
import com.prhythm.core.generic.exception.RecessiveException;
import javafx.fxml.FXMLLoader;
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
 * 顯著標示呈現
 * Created by nanashi07 on 16/1/5.
 */
public class HighLightListCell extends ListCell<HighLight> {

    final static String LAYOUT = "/com/prhythm/app/remotetail/core/list.cell.high.light.fxml";
    final static URL LAYOUT_URL = LineListCell.class.getResource(LAYOUT);

    HBox hBox;
    Label render;
    Label pattern;

    public HighLightListCell() {
        // 載入 UI 設計
        FXMLLoader loader = new FXMLLoader(LAYOUT_URL, Singleton.of(ResourceBundle.class));
        try {
            hBox = loader.load();
        } catch (IOException e) {
            throw new RecessiveException(e.getMessage(), e);
        }
        render = (Label) hBox.lookup("#render");
        pattern = (Label) hBox.lookup("#pattern");
    }

    @Override
    protected void updateItem(HighLight item, boolean empty) {
        super.updateItem(item, empty);  // 重要，不能刪
        if (empty) {
            setGraphic(null);
        } else {
            Preference preference = Singleton.of(DataWrapper.class).getPreference();
            // 字體
            render.setFont(Font.font(
                    preference.getFontFamily(),
                    item.isBold() ? FontWeight.BOLD : FontWeight.NORMAL,
                    item.isItalic() ? FontPosture.ITALIC : FontPosture.REGULAR,
                    render.getFont().getSize()
            ));
            // 文字顏色／背景色
            render.setStyle(String.format(
                    "-fx-background-color: #%s !important; -fx-text-fill: #%s !important;",
                    item.getBackground().substring(2),
                    item.getForeground().substring(2)
            ));
            // pattern
            pattern.setText(item.getPattern());

            setGraphic(hBox);
        }
    }

}
