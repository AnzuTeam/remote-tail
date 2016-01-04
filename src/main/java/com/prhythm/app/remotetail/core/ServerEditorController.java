package com.prhythm.app.remotetail.core;

import com.prhythm.app.remotetail.models.Server;
import com.prhythm.core.generic.util.Strings;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * Server 編輯介面
 * Created by nanashi07 on 16/1/2.
 */
public class ServerEditorController {

    @FXML
    TextField name;
    @FXML
    TextField host;
    @FXML
    TextField port;
    @FXML
    TextField account;
    @FXML
    PasswordField password;

    /**
     * 顯示資料
     *
     * @param server
     */
    public void from(Server server) {
        name.setText(server.getName());
        host.setText(server.getHost());
        port.setText(String.valueOf(server.getPort()));
        account.setText(server.getAccount());
    }

    /**
     * 更新資料
     *
     * @param server
     */
    public boolean update(Server server) {
        boolean result = true;

        name.getStyleClass().clear();
        host.getStyleClass().clear();
        port.getStyleClass().clear();
        account.getStyleClass().clear();
        password.getStyleClass().clear();

        if (Strings.isNullOrWhiteSpace(name.getText())) {
            result = false;
        } else {
            server.setName(name.getText().trim());
        }

        if (Strings.isNullOrWhiteSpace(host.getText())) {
            result = false;
        } else {
            server.setHost(host.getText().trim());
        }

        if (Strings.isNullOrWhiteSpace(port.getText()) || !port.getText().trim().replaceAll("\\d", "").isEmpty()) {
            result = false;
        } else {
            server.setPort(Integer.parseInt(port.getText().trim()));
        }

        if (Strings.isNullOrWhiteSpace(account.getText())) {
            result = false;
        } else {
            server.setAccount(account.getText().trim());
        }

        if (Strings.isNullOrWhiteSpace(password.getText())) {
            result = false;
        } else {
            server.setPassword(password.getText().trim());
        }

        return result;
    }

    public void focus() {
        Platform.runLater(() -> {
            name.requestFocus();
            name.deselect();
        });
    }
}
