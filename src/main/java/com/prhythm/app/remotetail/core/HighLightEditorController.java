package com.prhythm.app.remotetail.core;

import com.prhythm.app.remotetail.App;
import com.prhythm.app.remotetail.data.HighLightListCell;
import com.prhythm.app.remotetail.models.DataWrapper;
import com.prhythm.app.remotetail.models.HighLight;
import com.prhythm.app.remotetail.models.HighLights;
import com.prhythm.core.generic.data.Singleton;
import com.prhythm.core.generic.util.Cube;
import com.prhythm.core.generic.util.Strings;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.ResourceBundle;

/**
 * 顯著標示編輯
 * Created by nanashi07 on 16/1/4.
 */
public class HighLightEditorController implements IFocusable {

    @FXML
    CheckBox searchPattern;
    @FXML
    ListView rules;
    @FXML
    ColorPicker foreground;
    @FXML
    ColorPicker background;
    @FXML
    TextField patternText;
    @FXML
    CheckBox ignoreCase;
    @FXML
    CheckBox bold;
    @FXML
    CheckBox italic;

    @FXML
    Button removeItem;
    @FXML
    Button moveUp;
    @FXML
    Button moveDown;

    /**
     * 載入目前設定值
     */
    public void load() {
        HighLights highLights = Singleton.of(DataWrapper.class).getHighLights();

        // High light search pattern
        searchPattern.setSelected(highLights.isMarkSearchPattern());

        // 變更 patter 設定值
        patternText.textProperty().addListener((observable, oldValue, newValue) -> {
            HighLight selected = getSelected();
            if (selected != null && !Strings.isNullOrWhiteSpace(newValue)) {
                selected.setPattern(newValue.trim());
                rules.refresh();
            }
        });

        // 變更呈現
        //noinspection unchecked
        rules.setCellFactory(param -> new HighLightListCell());

        // 選擇事件
        //noinspection unchecked
        rules.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<HighLight>() {
            @Override
            public void changed(ObservableValue observable, HighLight oldValue, HighLight newValue) {
                if (newValue == null) {
                    foreground.setValue(Color.WHITE);
                    background.setValue(Color.WHITE);
                    patternText.setText("");
                    ignoreCase.setSelected(false);
                    bold.setSelected(false);
                    italic.setSelected(false);
                } else {
                    foreground.setValue(Color.valueOf("#" + newValue.getForeground().substring(2)));
                    background.setValue(Color.valueOf("#" + newValue.getBackground().substring(2)));
                    patternText.setText(newValue.getPattern());
                    ignoreCase.setSelected(newValue.isIgnoreCase());
                    bold.setSelected(newValue.isBold());
                    italic.setSelected(newValue.isItalic());
                }
            }
        });

        // 更新畫面
        flushRules();
    }

    /**
     * 取得目前選擇的 {@link HighLight} 規則
     *
     * @return
     */
    HighLight getSelected() {
        return (HighLight) rules.getSelectionModel().getSelectedItem();
    }

    /**
     * 增加 {@link HighLight} 規則
     *
     * @param actionEvent 動件事件
     */
    @FXML
    void addRule(ActionEvent actionEvent) {
        if (Strings.isNullOrWhiteSpace(patternText.getText())) {
            App.error(Singleton.of(ResourceBundle.class).getString("rmt.status.error.high.light.pattern.empty"));
            return;
        }

        // 建立資料
        HighLight value = new HighLight();
        value.setForeground(foreground.getValue().toString());
        value.setBackground(background.getValue().toString());
        value.setPattern(patternText.getText().trim());
        value.setIgnoreCase(ignoreCase.isSelected());
        value.setBold(bold.isSelected());
        value.setItalic(italic.isSelected());
        value.setOrder(10 * (rules.getItems().size() + 1));
        Singleton.of(DataWrapper.class).getHighLights().add(value);

        // 重整畫面
        //noinspection unchecked
        rules.getItems().add(value);

        // 重置欄位
        foreground.setValue(Color.WHITE);
        background.setValue(Color.WHITE);
        patternText.setText("");
        ignoreCase.setSelected(false);
        bold.setSelected(false);
        italic.setSelected(false);
    }

    /**
     * 移除 {@link HighLight} 規則
     *
     * @param actionEvent 動件事件
     */
    @FXML
    void removeRule(ActionEvent actionEvent) {
        HighLight selected = getSelected();
        if (selected != null) {
            Singleton.of(DataWrapper.class).getHighLights().remove(selected);
            rules.getItems().remove(selected);
            rules.refresh();
        }
    }

    /**
     * 調整 {@link HighLight} 規則順序
     *
     * @param actionEvent 動件事件
     */
    @FXML
    void moveRuleUp(ActionEvent actionEvent) {
        HighLight selected = getSelected();
        if (selected != null) {
            // 往上移
            selected.setOrder(selected.getOrder() - 15);
            // 更新畫面
            flushRules();
        }
    }

    /**
     * 調整 {@link HighLight} 規則順序
     *
     * @param actionEvent 動件事件
     */
    @FXML
    void moveRuleDown(ActionEvent actionEvent) {
        HighLight selected = getSelected();
        if (selected != null) {
            // 往下移
            selected.setOrder(selected.getOrder() + 15);
            // 更新畫面
            flushRules();
        }
    }

    /**
     * 重設排序，並更新 {@link HighLight} 顯示畫面
     */
    void flushRules() {
        List<HighLight> values = Cube.from(Singleton.of(DataWrapper.class).getHighLights())
                .orderBy((source, target) -> source.getOrder() - target.getOrder()) // 重新排序
                .each((item, index) -> {  // 重整排序號碼
                    item.setOrder((index + 1) * 10);
                    return true;
                }).toList();
        //noinspection unchecked
        rules.setItems(FXCollections.observableArrayList(values));
    }

    /**
     * 變更顯著標示搜尋內容
     *
     * @param actionEvent 動件事件
     */
    @FXML
    void searchPatternAction(ActionEvent actionEvent) {
        Singleton.of(DataWrapper.class).getHighLights().setMarkSearchPattern(searchPattern.isSelected());
    }

    /**
     * 變更 {@link HighLight} 區分大小寫規則
     *
     * @param actionEvent 動件事件
     */
    @FXML
    void ignoreCaseAction(ActionEvent actionEvent) {
        HighLight selected = getSelected();
        if (selected != null) {
            selected.setIgnoreCase(ignoreCase.isSelected());
            rules.refresh();
        }
    }

    /**
     * 變更 {@link HighLight} 顯示粗體規則
     *
     * @param actionEvent 動件事件
     */
    @FXML
    void boldAction(ActionEvent actionEvent) {
        HighLight selected = getSelected();
        if (selected != null) {
            selected.setBold(bold.isSelected());
            rules.refresh();
        }
    }

    /**
     * 變更 {@link HighLight} 顯示斜體規則
     *
     * @param actionEvent 動件事件
     */
    @FXML
    void italicAction(ActionEvent actionEvent) {
        HighLight selected = getSelected();
        if (selected != null) {
            selected.setItalic(italic.isSelected());
            rules.refresh();
        }
    }

    /**
     * 變更 {@link HighLight} 顯示文字顏色
     *
     * @param actionEvent 動件事件
     */
    @FXML
    void foregroundAction(ActionEvent actionEvent) {
        HighLight selected = getSelected();
        if (selected != null) {
            selected.setForeground(foreground.getValue().toString());
            rules.refresh();
        }
    }

    /**
     * 變更 {@link HighLight} 顯示背景顏色
     *
     * @param actionEvent 動件事件
     */
    @FXML
    void backgroundAction(ActionEvent actionEvent) {
        HighLight selected = getSelected();
        if (selected != null) {
            selected.setBackground(background.getValue().toString());
            rules.refresh();
        }
    }

    @Override
    public void focus() {
        Platform.runLater(() -> {
            patternText.requestFocus();
            patternText.deselect();
        });
    }
}
