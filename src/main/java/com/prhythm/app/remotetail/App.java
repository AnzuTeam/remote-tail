package com.prhythm.app.remotetail;

import com.jcraft.jsch.JSch;
import com.prhythm.app.remotetail.core.MainController;
import com.prhythm.app.remotetail.models.DataWrapper;
import com.prhythm.app.remotetail.models.Server;
import com.prhythm.core.generic.logging.GenericLogger;
import com.prhythm.core.generic.logging.Level;
import com.prhythm.core.generic.logging.LogFactory;
import com.prhythm.core.generic.logging.Logs;
import com.prhythm.core.generic.util.Strings;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;

/**
 * 啟動程式
 * Created by nanashi07 on 15/12/30.
 */
public class App extends Application {

    final static String CONFIG_FILE = "app.xml";

    public static void main(String[] args) {
        // 不處理 host key
        JSch.setConfig("StrictHostKeyChecking", "no");

        // log
        new Logs().setLogFactory(new LogFactory(new GenericLogger(Level.Trace)));

        launch(args);
    }

    DataWrapper wrapper;

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(MainController.LAYOUT_MAIN));
        primaryStage.setScene(new Scene(loader.load(), 800, 450));
        primaryStage.setOnCloseRequest(event -> {
            // 儲存資料
            if (wrapper == null) return;
            for (Server server : wrapper.getServers()) {
                server.disconnect();
            }

            try {
                wrapper.save(new File(CONFIG_FILE));
            } catch (JAXBException e) {
                alert("", e);
            }
        });

        // 初始化資料
        try {
            initialize(loader);
        } catch (JAXBException e) {
            alert("", e);
        }

        // 未處理錯誤
//        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
//            @Override
//            public void uncaughtException(Thread t, Throwable e) {
//                alert("未處理錯誤", e);
//            }
//        });

        // 顯示
        primaryStage.show();
    }

    void alert(String title, Throwable ex) {
        ex.printStackTrace();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(Strings.stringify(ex));
        alert.show();
    }

    void initialize(FXMLLoader loader) throws JAXBException {
        // 讀取資料
        wrapper = DataWrapper.read(new File(CONFIG_FILE));

        // 取得 controller
        MainController controller = loader.getController();
        controller.load(wrapper);
    }

}
