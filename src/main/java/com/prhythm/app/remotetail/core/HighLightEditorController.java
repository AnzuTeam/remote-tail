package com.prhythm.app.remotetail.core;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

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
