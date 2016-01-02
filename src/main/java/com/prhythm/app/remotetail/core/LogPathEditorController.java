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

    public void from(LogPath logPath) {
        path.setText(logPath.getPath());
    }

    public void update(LogPath logPath) {
        if (!Strings.isNullOrWhiteSpace(path.getText())) logPath.setPath(path.getText().trim());
    }

}
