package com.prhythm.app.remotetail.core;

import com.prhythm.app.remotetail.App;
import com.prhythm.app.remotetail.models.LogPath;
import com.prhythm.core.generic.data.Singleton;
import com.prhythm.core.generic.util.Delimiters;
import com.prhythm.core.generic.util.Strings;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Log 編輯介面
 * Created by nanashi07 on 16/1/2.
 */
public class LogPathEditorController implements IFocusable, IEditable<LogPath> {

    @FXML
    TextField path;

    /**
     * 顯示資料
     *
     * @param logPath Log 路徑
     */
    @Override
    public void from(LogPath logPath) {
        path.setText(logPath.getPath());
    }

    /**
     * 更新資料
     *
     * @param logPath Log 路徑
     */
    @Override
    public boolean update(LogPath logPath) {
        boolean result = true;

        List<String> fields = new ArrayList<>();

        if (Strings.isNullOrWhiteSpace(path.getText())) {
            result = false;
            fields.add(Singleton.of(ResourceBundle.class).getString("rmt.dialog.log.edit.path"));
        } else {
            logPath.setPath(path.getText().trim());
        }

        // 顯示錯誤
        if (fields.size() > 0) {
            App.error(
                    Singleton.of(ResourceBundle.class).getString("rmt.status.error.input.error"),
                    Delimiters.with(", ").join(fields)
            );
        }

        return result;
    }

    @Override
    public void focus() {
        Platform.runLater(() -> {
            path.requestFocus();
            path.deselect();
        });
    }
}
