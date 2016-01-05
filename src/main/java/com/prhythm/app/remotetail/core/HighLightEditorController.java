package com.prhythm.app.remotetail.core;

import com.prhythm.app.remotetail.data.HighLightListCell;
import com.prhythm.app.remotetail.models.DataWrapper;
import com.prhythm.app.remotetail.models.HighLight;
import com.prhythm.app.remotetail.models.HighLights;
import com.prhythm.core.generic.data.Singleton;
import com.prhythm.core.generic.util.Cube;
import com.prhythm.core.generic.util.Strings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

import java.util.List;

/**
 * 顯著標示編輯
 * Created by nanashi07 on 16/1/4.
 */
public class HighLightEditorController {

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

    public void load() {
        HighLights highLights = Singleton.of(DataWrapper.class).getHighLights();

        // High light search pattern
        searchPattern.setSelected(highLights.isMarkSearchPattern());

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
                    foreground.setValue(newValue.getForeground());
                    background.setValue(newValue.getBackground());
                    patternText.setText(newValue.getPattern());
                    ignoreCase.setSelected(newValue.isIgnoreCase());
                    bold.setSelected(newValue.isBold());
                    italic.setSelected(newValue.isItalic());
                }
            }
        });

        // rules
        //noinspection unchecked
        flushRules();
    }

    HighLight getSelected() {
        return (HighLight) rules.getSelectionModel().getSelectedItem();
    }

    @FXML
    void addRule(ActionEvent actionEvent) {
        if (Strings.isNullOrWhiteSpace(patternText.getText())) {
            // todo show error
            return;
        }

        // 建立資料
        HighLight value = new HighLight();
        value.setForeground(foreground.getValue());
        value.setBackground(background.getValue());
        value.setPattern(patternText.getText().trim());
        value.setIgnoreCase(ignoreCase.isSelected());
        value.setBold(bold.isSelected());
        value.setItalic(italic.isSelected());
        value.setOrder(10 * (rules.getItems().size() + 1));
        Singleton.of(DataWrapper.class).getHighLights().add(value);

        // 重整畫面
        //noinspection unchecked
        rules.getItems().add(value);

        // 重置
        foreground.setValue(Color.WHITE);
        background.setValue(Color.WHITE);
        patternText.setText("");
        ignoreCase.setSelected(false);
        bold.setSelected(false);
        italic.setSelected(false);
    }

    @FXML
    void removeRule(ActionEvent actionEvent) {
        //noinspection unchecked
        HighLight selected = getSelected();
        if (selected != null) {
            Singleton.of(DataWrapper.class).getHighLights().remove(selected);
            rules.getItems().remove(selected);
            rules.refresh();
        }
    }

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
     * 重設排序
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

    @FXML
    void searchPatternAction(ActionEvent actionEvent) {
        Singleton.of(DataWrapper.class).getHighLights().setMarkSearchPattern(searchPattern.isSelected());
    }

    @FXML
    void ignoreCaseAction(ActionEvent actionEvent) {
        HighLight selected = getSelected();
        if (selected != null) {
            selected.setIgnoreCase(ignoreCase.isSelected());
            rules.refresh();
        }
    }

    @FXML
    void boldAction(ActionEvent actionEvent) {
        HighLight selected = getSelected();
        if (selected != null) {
            selected.setBold(bold.isSelected());
            rules.refresh();
        }
    }

    @FXML
    void italicAction(ActionEvent actionEvent) {
        HighLight selected = getSelected();
        if (selected != null) {
            selected.setItalic(italic.isSelected());
            rules.refresh();
        }
    }

    @FXML
    void foregroundAction(ActionEvent actionEvent) {
        HighLight selected = getSelected();
        if (selected != null) {
            selected.setForeground(foreground.getValue());
            rules.refresh();
        }
    }

    @FXML
    void backgroundAction(ActionEvent actionEvent) {
        HighLight selected = getSelected();
        if (selected != null) {
            selected.setBackground(background.getValue());
            rules.refresh();
        }
    }

}
