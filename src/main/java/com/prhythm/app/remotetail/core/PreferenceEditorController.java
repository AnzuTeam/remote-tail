package com.prhythm.app.remotetail.core;

import com.prhythm.app.remotetail.App;
import com.prhythm.app.remotetail.data.ListLineItem;
import com.prhythm.app.remotetail.models.Preference;
import com.prhythm.core.generic.data.Singleton;
import com.prhythm.core.generic.util.Strings;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

/**
 * 偏好設定
 * Created by nanashi07 on 16/1/3.
 */
public class PreferenceEditorController {

    @FXML
    TextField fontFamily;
    @FXML
    TextField fontSize;
    @FXML
    TextField charset;
    @FXML
    ChoiceBox theme;

    /**
     * 顯示資料
     *
     * @param preference
     */
    public void from(Preference preference) {
        fontFamily.setText(preference.getFontFamily());
        fontSize.setText(String.valueOf(preference.getFontSize()));
        charset.setText(preference.getCharset());
        //noinspection unchecked
        theme.setItems(FXCollections.observableArrayList(Preference.Theme.values()));
        //noinspection unchecked
        theme.getSelectionModel().select(preference.getTheme());
    }

    /**
     * 更新資料
     *
     * @param preference
     */
    public boolean update(Preference preference) {
        boolean result = true;

        if (Strings.isNullOrWhiteSpace(fontFamily.getText())) {
            result = false;
        } else {
            preference.setFontFamily(fontFamily.getText().trim());
        }

        if (Strings.isNullOrWhiteSpace(fontSize.getText())) {
            result = false;
        } else {
            try {
                preference.setFontSize(Double.parseDouble(fontSize.getText().trim()));
                ListLineItem.MAX_INDEX_WIDTH = 1;
            } catch (Exception e) {
                result = false;
            }
        }

        if (Strings.isNullOrWhiteSpace(charset.getText())) {
            result = false;
        } else {
            preference.setCharset(charset.getText().trim());
        }

        preference.setTheme((Preference.Theme) theme.getSelectionModel().getSelectedItem());

        switch (preference.getTheme()) {
            case White:
                Singleton.of(Scene.class).getStylesheets().remove(App.STYLE_DARK_APP);
                break;
            case Dark:
                Singleton.of(Scene.class).getStylesheets().add(App.STYLE_DARK_APP);
                break;
        }

        return result;
    }

}
