package com.prhythm.app.remotetail.core;

import com.prhythm.app.remotetail.App;
import com.prhythm.app.remotetail.models.Server;
import com.prhythm.core.generic.data.Singleton;
import com.prhythm.core.generic.util.Delimiters;
import com.prhythm.core.generic.util.Strings;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Server 編輯介面
 * Created by nanashi07 on 16/1/2.
 */
public class ServerEditorController implements IFocusable, IEditable<Server> {

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
     * @param server 資料內容
     */
    @Override
    public void from(Server server) {
        name.setText(server.getName());
        host.setText(server.getHost());
        port.setText(String.valueOf(server.getPort()));
        account.setText(server.getAccount());
    }

    /**
     * 更新資料
     *
     * @param server 資料內容
     */
    @Override
    public boolean update(Server server) {
        boolean result = true;

        name.getStyleClass().clear();
        host.getStyleClass().clear();
        port.getStyleClass().clear();
        account.getStyleClass().clear();
        password.getStyleClass().clear();

        List<String> fields = new ArrayList<>();

        if (Strings.isNullOrWhiteSpace(name.getText())) {
            fields.add(Singleton.of(ResourceBundle.class).getString("rmt.dialog.server.edit.name"));
            result = false;
        } else {
            server.setName(name.getText().trim());
        }

        if (Strings.isNullOrWhiteSpace(host.getText())) {
            fields.add(Singleton.of(ResourceBundle.class).getString("rmt.dialog.server.edit.host"));
            result = false;
        } else {
            server.setHost(host.getText().trim());
        }

        if (Strings.isNullOrWhiteSpace(port.getText())) {
            fields.add(Singleton.of(ResourceBundle.class).getString("rmt.dialog.server.edit.port"));
            result = false;
        } else {
            try {
                int portValue = Integer.parseInt(port.getText().trim());
                if (portValue > 0) {
                    server.setPort(portValue);
                } else {
                    fields.add(Singleton.of(ResourceBundle.class).getString("rmt.dialog.server.edit.port"));
                }
            } catch (Exception e) {
                fields.add(Singleton.of(ResourceBundle.class).getString("rmt.dialog.server.edit.port"));
            }
        }

        if (Strings.isNullOrWhiteSpace(account.getText())) {
            fields.add(Singleton.of(ResourceBundle.class).getString("rmt.dialog.server.edit.account"));
            result = false;
        } else {
            server.setAccount(account.getText().trim());
        }

        if (Strings.isNullOrWhiteSpace(password.getText())) {
            fields.add(Singleton.of(ResourceBundle.class).getString("rmt.dialog.server.edit.password"));
            result = false;
        } else {
            server.setPassword(password.getText().trim());
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
            name.requestFocus();
            name.deselect();
        });
    }

}
