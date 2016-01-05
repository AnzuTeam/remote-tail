package com.prhythm.app.remotetail.core;

import com.prhythm.app.remotetail.data.HighLightListCell;
import com.prhythm.app.remotetail.models.DataWrapper;
import com.prhythm.app.remotetail.models.HighLight;
import com.prhythm.app.remotetail.models.HighLights;
import com.prhythm.core.generic.data.Singleton;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

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

        foreground.setDisable(true);
        background.setDisable(true);
        patternText.setDisable(true);
        ignoreCase.setDisable(true);
        bold.setDisable(true);
        italic.setDisable(true);
        removeItem.setDisable(true);
        moveUp.setDisable(true);
        moveDown.setDisable(true);

        // High light search pattern
        searchPattern.setSelected(highLights.isMarkSearchPattern());

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

                    foreground.setDisable(true);
                    background.setDisable(true);
                    patternText.setDisable(true);
                    ignoreCase.setDisable(true);
                    bold.setDisable(true);
                    italic.setDisable(true);
                    removeItem.setDisable(true);
                    moveUp.setDisable(true);
                    moveDown.setDisable(true);
                } else {
                    foreground.setValue(newValue.getForeground());
                    background.setValue(newValue.getBackground());
                    patternText.setText(newValue.getPattern());
                    ignoreCase.setSelected(newValue.isIgnoreCase());
                    bold.setSelected(newValue.isBold());
                    italic.setSelected(newValue.isIgnoreCase());

                    foreground.setDisable(false);
                    background.setDisable(false);
                    patternText.setDisable(false);
                    ignoreCase.setDisable(false);
                    bold.setDisable(false);
                    italic.setDisable(false);
                    removeItem.setDisable(false);
                    moveUp.setDisable(false);
                    moveDown.setDisable(false);
                }
            }
        });


        // rules
        //noinspection unchecked
//        rules.setItems(FXCollections.observableArrayList(highLights.toArray(new HighLight[highLights.size()])));
        //fixme
        ObservableList<HighLight> objects = FXCollections.observableArrayList();
        objects.add(new HighLight(false, "abc", Color.RED, Color.GREEN, false, false));
        objects.add(new HighLight(false, "de", Color.BEIGE, Color.GREEN, false, false));
        rules.setItems(objects);

//        foreground.setOnAction(new);

    }


    @FXML
    void addRule(ActionEvent actionEvent) {

    }

    @FXML
    void removeRule(ActionEvent actionEvent) {

    }

    @FXML
    void moveRuleUp(ActionEvent actionEvent) {

    }

    @FXML
    void moveRuleDown(ActionEvent actionEvent) {

    }

}
