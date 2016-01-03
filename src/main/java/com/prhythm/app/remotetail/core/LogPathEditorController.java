package com.prhythm.app.remotetail.core;

import com.prhythm.app.remotetail.models.LogPath;
import com.prhythm.core.generic.util.Strings;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
 * Log 編輯介面
 * Created by nanashi07 on 16/1/2.
 */
public class LogPathEditorController {

    @FXML
    TextField path;

    /**
     * 顯示資料
     *
     * @param logPath
     */
    public void from(LogPath logPath) {
        path.setText(logPath.getPath());
    }

    /**
     * 更新資料
     *
     * @param logPath
     */
    public boolean update(LogPath logPath) {
        boolean result = true;

        path.getStyleClass().clear();

        if (Strings.isNullOrWhiteSpace(path.getText())) {
            result = false;
            path.getStyleClass().add("red");
        } else {
            logPath.setPath(path.getText().trim());
        }

        return result;
    }

}
