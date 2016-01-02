package com.prhythm.app.remotetail.core;

import com.prhythm.app.remotetail.models.Server;
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

    public void from(Server server) {
        name.setText(server.getName());
        host.setText(server.getHost());
        port.setText(String.valueOf(server.getPort()));
        account.setText(server.getAccount());
    }

    public void update(Server server) {
        server.setName(name.getText().trim());
        server.setHost(host.getText().trim());
        server.setPort(Integer.parseInt(port.getText()));
        server.setAccount(account.getText().trim());
        if (!password.getText().trim().isEmpty())
            server.setPassword(password.getText().trim());
    }

}
