package com.prhythm.app.remotetail.core;

import com.prhythm.app.remotetail.models.LogPath;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
 * Log 編輯介面
 * Created by nanashi07 on 16/1/2.
 */
public class LogPathEditorController {

    @FXML
    TextField path;

    public void from(LogPath logPath) {
        path.setText(logPath.getPath());
    }

    public void update(LogPath logPath) {
        logPath.setPath(path.getText().trim());
    }

}
