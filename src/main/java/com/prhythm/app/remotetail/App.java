package com.prhythm.app.remotetail;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Logger;
import com.prhythm.app.remotetail.core.MainController;
import com.prhythm.app.remotetail.models.DataWrapper;
import com.prhythm.app.remotetail.models.Preference;
import com.prhythm.app.remotetail.models.Server;
import com.prhythm.core.generic.data.Singleton;
import com.prhythm.core.generic.exception.RecessiveException;
import com.prhythm.core.generic.logging.GenericLogger;
import com.prhythm.core.generic.logging.Level;
import com.prhythm.core.generic.logging.LogFactory;
import com.prhythm.core.generic.logging.Logs;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
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

    final public static String APP_ICON = "/com/prhythm/app/remotetail/icons/app_icon.png";

    final public static String STYLE_DARK_APP = "/com/prhythm/app/remotetail/style/dark-theme.css";
    final public static String STYLE_DEFAULT = "/com/prhythm/app/remotetail/style/default.css";

    final static String CONFIG_FILE = "app.xml";
    final static double MIN_WIDTH = 400;
    final static double MIN_HEIGHT = 300;

    /**
     * 變更外觀
     *
     * @param styleSheets 樣式表
     * @param theme       外觀設定
     */
    public static void changeTheme(ObservableList<String> styleSheets, Preference.Theme theme) {
        switch (theme) {
            case White:
                styleSheets.remove(App.STYLE_DARK_APP);
                break;
            case Dark:
                styleSheets.add(App.STYLE_DARK_APP);
                break;
        }
    }

    /**
     * 顯示狀態文字
     *
     * @param message 訊息格式
     * @param args    參數
     */
    public static void error(String message, Object... args) {
        Platform.runLater(() -> {
            MainController controller = Singleton.of(FXMLLoader.class).getController();
            controller.error(args.length == 0 ? message : String.format(message, args));
        });
    }

    /**
     * 顯示狀態文字
     *
     * @param message 訊息格式
     * @param args    參數
     */
    public static void info(String message, Object... args) {
        Platform.runLater(() -> {
            MainController controller = Singleton.of(FXMLLoader.class).getController();
            controller.info(args.length == 0 ? message : String.format(message, args));
        });
    }

    public static void main(String[] args) {
        // 不處理 host key
        JSch.setConfig("StrictHostKeyChecking", "no");

        // 設定 JSCH 的 logs
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

        // 設定 log
        new Logs().setLogFactory(new LogFactory(new GenericLogger(Level.Trace)));

        // i18n
        Singleton.of(ResourceBundle.getBundle("com.prhythm.app.remotetail.bundles.ui"));

        // 未處理錯誤
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            Logs.error(RecessiveException.unwrapp(e));
            App.error(Singleton.of(ResourceBundle.class).getString("rmt.status.error.unknown.exception"), e.getMessage());
        });

        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        // 暫存資料
        Singleton.of(stage);
        // 載入 UI 設計
        FXMLLoader loader = Singleton.of(new FXMLLoader(
                getClass().getResource(MainController.LAYOUT_MAIN),
                Singleton.of(ResourceBundle.class)
        ));

        // 設定預設屬性
        Scene scene = Singleton.of(new Scene(loader.load(), MIN_WIDTH * 2, MIN_HEIGHT * 2));
        scene.getStylesheets().add(STYLE_DEFAULT);
        stage.setTitle(Singleton.of(ResourceBundle.class).getString("rmt.title"));
        stage.setScene(scene);
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

            // 儲存目前視窗屬性
            wrapper.getWindow().setWidth(stage.getWidth());
            wrapper.getWindow().setHeight(stage.getHeight());

            wrapper.getWindow().setX(stage.getX());
            wrapper.getWindow().setY(stage.getY());

            try {
                // 儲存資料
                wrapper.save(new File(CONFIG_FILE));
            } catch (JAXBException e) {
                Logs.error("儲存資料失敗: %s", e);
            }
        });

        // 初始化資料
        try {
            initialize(stage);
        } catch (JAXBException e) {
            Logs.error("初始化資料失敗: %s", e);
            App.error(Singleton.of(ResourceBundle.class).getString("rmt.status.error.load.config.failed"), e);
        }

        // 顯示
        stage.show();
    }

    /**
     * 初始化
     *
     * @param stage
     * @throws JAXBException
     */
    void initialize(Stage stage) throws JAXBException {
        // 讀取資料
        DataWrapper wrapper = Singleton.of(DataWrapper.read(new File(CONFIG_FILE)));

        // app icon
        stage.getIcons().add(new Image(APP_ICON));

        // 重設屬性
        if (wrapper != null) {
            if (wrapper.getPreference().getTheme() == Preference.Theme.Dark)
                Singleton.of(Scene.class).getStylesheets().add(STYLE_DARK_APP);

            stage.setX(wrapper.getWindow().getX());
            stage.setY(wrapper.getWindow().getY());

            stage.setWidth(wrapper.getWindow().getWidth());
            stage.setHeight(wrapper.getWindow().getHeight());
        }

        // 取得 controller
        MainController controller = Singleton.of(FXMLLoader.class).getController();
        controller.load();
    }

}
