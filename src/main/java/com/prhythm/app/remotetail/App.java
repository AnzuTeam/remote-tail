package com.prhythm.app.remotetail;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Logger;
import com.prhythm.app.remotetail.core.MainController;
import com.prhythm.app.remotetail.models.DataWrapper;
import com.prhythm.app.remotetail.models.Server;
import com.prhythm.core.generic.data.Singleton;
import com.prhythm.core.generic.exception.RecessiveException;
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
import java.util.ResourceBundle;

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

        // logs
        JSch.setLogger(new Logger() {
            @Override
            public boolean isEnabled(int level) {
                return true;
            }

            @Override
            public void log(int level, String message) {
                switch (level) {
                    case Logger.DEBUG:
                        Logs.debug(message);
                        break;
                    case Logger.INFO:
                        Logs.info(message);
                        break;
                    case Logger.WARN:
                        Logs.warn(message);
                        break;
                    case Logger.ERROR:
                        Logs.error(message);
                        break;
                    case Logger.FATAL:
                        Logs.fatal(message);
                        break;
                }
            }
        });

        // log
        new Logs().setLogFactory(new LogFactory(new GenericLogger(Level.Trace)));

        // i18n
        Singleton.of(ResourceBundle.getBundle("com.prhythm.app.remotetail.bundles.ui"));

        // 未處理錯誤
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> Logs.error(RecessiveException.unwrapp(e)));

        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        Singleton.of(stage);
        FXMLLoader loader = Singleton.of(new FXMLLoader(
                getClass().getResource(MainController.LAYOUT_MAIN),
                Singleton.of(ResourceBundle.class)
        ));

        stage.setTitle("Remote Tail - via SSH");
        stage.setScene(new Scene(loader.load(), MIN_WIDTH * 2, MIN_HEIGHT * 2));
        stage.setMinWidth(MIN_WIDTH);
        stage.setMinHeight(MIN_HEIGHT);

        // 設定結束程式前處理作業
        stage.setOnCloseRequest(event -> {
            // 停止所有作業
            STOP_ALL_TASK = true;

            DataWrapper wrapper = Singleton.of(DataWrapper.class);
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
            initialize(stage);
        } catch (JAXBException e) {
            alert("初始化資料失敗", e);
        }

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

    void initialize(Stage stage) throws JAXBException {
        // 讀取資料
        DataWrapper wrapper = Singleton.of(DataWrapper.read(new File(CONFIG_FILE)));


        if (wrapper != null) {
            stage.setX(wrapper.getWindow().getX());
            stage.setY(wrapper.getWindow().getY());

            stage.setWidth(wrapper.getWindow().getWidth());
            stage.setHeight(wrapper.getWindow().getHeight());
        }

        // 取得 controller
        MainController controller = Singleton.of(FXMLLoader.class).getController();
        controller.load(wrapper);
    }

}
