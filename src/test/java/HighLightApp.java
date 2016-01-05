import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Logger;
import com.prhythm.app.remotetail.core.HighLightEditorController;
import com.prhythm.core.generic.data.Singleton;
import com.prhythm.core.generic.exception.RecessiveException;
import com.prhythm.core.generic.logging.GenericLogger;
import com.prhythm.core.generic.logging.Level;
import com.prhythm.core.generic.logging.LogFactory;
import com.prhythm.core.generic.logging.Logs;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ResourceBundle;

/**
 * Created by nanashi07 on 16/1/5.
 */
public class HighLightApp extends Application {

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
    public void start(Stage stage) throws Exception {
        Singleton.of(stage);
        FXMLLoader loader = Singleton.of(new FXMLLoader(
                getClass().getResource("/com/prhythm/app/remotetail/core/edit.high.light.fxml"),
                Singleton.of(ResourceBundle.class)
        ));

        // 設定預設屬性
        Scene scene = Singleton.of(new Scene(loader.load(), 400, 300));
        // divider style
//        scene.getStylesheets().add(STYLE_SPLIT_PANE_DIVIDER);
        stage.setTitle("High Light");
        stage.setScene(scene);
        stage.setMinWidth(400);
        stage.setMinHeight(500);

        HighLightEditorController controller = loader.getController();
        controller.load();

        // 顯示
        stage.show();
    }
}
