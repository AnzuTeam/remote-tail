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

    /**
     * 停止執行的作業
     */
    public static boolean STOP_ALL_TASK = false;

    final static String CONFIG_FILE = "app.xml";
    final double MIN_WIDTH = 400;
    final double MIN_HEIGHT = 300;

    public static void main(String[] args) {
        // 不處理 host key
        JSch.setConfig("StrictHostKeyChecking", "no");

        // log
        new Logs().setLogFactory(new LogFactory(new GenericLogger(Level.Trace)));

        launch(args);
    }

    DataWrapper wrapper;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(MainController.LAYOUT_MAIN));
        stage.setTitle("Remote Tail - via SSH");
        stage.setScene(new Scene(loader.load(), MIN_WIDTH * 2, MIN_HEIGHT * 2));
        stage.setMinWidth(MIN_WIDTH);
        stage.setMinHeight(MIN_HEIGHT);

        // 設定結束程式前處理作業
        stage.setOnCloseRequest(event -> {
            // 停止所有作業
            STOP_ALL_TASK = true;

            if (wrapper == null) return;

            // 結束連線
            wrapper.getServers().forEach(Server::disconnect);

            wrapper.getWindow().setWidth(stage.getWidth());
            wrapper.getWindow().setHeight(stage.getHeight());

            wrapper.getWindow().setX(stage.getX());
            wrapper.getWindow().setY(stage.getY());

            try {
                // 儲存資料
                wrapper.save(new File(CONFIG_FILE));
            } catch (JAXBException e) {
                alert("儲存資料失敗", e);
            }
        });

        // 初始化資料
        try {
            initialize(stage, loader);
        } catch (JAXBException e) {
            alert("初始化資料失敗", e);
        }

        // 未處理錯誤
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> Logs.error(e));

        // 顯示
        stage.show();
    }

    void alert(String title, Throwable ex) {
        ex.printStackTrace();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(Strings.stringify(ex));
        alert.show();
    }

    void initialize(Stage stage, FXMLLoader loader) throws JAXBException {
        // 讀取資料
        wrapper = DataWrapper.read(new File(CONFIG_FILE));


        if (wrapper != null) {
            stage.setX(wrapper.getWindow().getX());
            stage.setY(wrapper.getWindow().getY());

            stage.setWidth(wrapper.getWindow().getWidth());
            stage.setHeight(wrapper.getWindow().getHeight());
        }

        // 取得 controller
        MainController controller = loader.getController();
        controller.load(wrapper);
    }

}
