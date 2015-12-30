package com.prhythm.app.remotetail;

import com.prhythm.app.remotetail.core.MainController;
import com.prhythm.app.remotetail.models.DataWrapper;
import com.prhythm.app.remotetail.models.LogPath;
import com.prhythm.app.remotetail.models.Server;
import com.prhythm.core.generic.util.Strings;
import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TreeItem;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;

/**
 * 啟動程式
 * Created by nanashi07 on 15/12/30.
 */
public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    final String main1 = "/com/prhythm/app/remotetail/core/main.fxml";

    DataWrapper wrapper;

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(main1));
        primaryStage.setScene(new Scene(loader.load(), 800, 450));
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                // 儲存資料
                if (wrapper == null) return;
                try {
                    wrapper.save(new File("app.xml"));
                } catch (JAXBException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText(Strings.stringify(e));
                    alert.show();
                }
            }
        });

        // 初始化資料
        try {
            initialize(loader);
        } catch (JAXBException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(Strings.stringify(e));
            alert.show();
        }

        // 顯示
        primaryStage.show();
    }

    void initialize(FXMLLoader loader) throws JAXBException {
        // 讀取資料
        wrapper = DataWrapper.read(new File("app.xml"));

        // 取得 controller
        MainController controller = loader.getController();

        // 載入設定
        TreeItem<Object> root = new TreeItem<>();
        //noinspection unchecked
        controller.areas.setRoot(root);
        if (wrapper != null) {
            ObservableList<TreeItem<Object>> rootChildren = root.getChildren();
            for (Server server : wrapper.getServers()) {
                // 顯示 server 設定
                TreeItem<Object> treeItem = new TreeItem<>(server);
                treeItem.setExpanded(server.isExpanded());
                rootChildren.add(treeItem);

                // 記錄展開／折疊的變更
                treeItem.expandedProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        BooleanProperty property = (BooleanProperty) observable;
                        TreeItem item = (TreeItem) property.getBean();
                        Server server = (Server) item.getValue();
                        server.setExpanded(newValue);
                    }
                });

                final ObservableList<TreeItem<Object>> serverChildren = treeItem.getChildren();

                // 處理 log 路徑設定
                for (LogPath logPath : server.getLogPaths()) {
                    TreeItem<Object> logPathTreeItem = new TreeItem<>(logPath);
                    serverChildren.add(logPathTreeItem);
                }

            }
        }


        ObservableList<String> items = FXCollections.observableArrayList(
                "Single", "Double", "Suite", "Hibernate: /* criteria query */ select this_.ID as ID10_0_, this_.CATE as CATE10_0_, this_.CATE_NAME as CATE3_10_0_, this_.code as code10_0_, this_.CONTENT as CONTENT10_0_, this_.CREATE_BY as CREATE6_10_0_, this_.CREATE_DT as CREATE7_10_0_, this_.FUNCTION as FUNCTION10_0_, this_.FUNCTION_NAME as FUNCTION9_10_0_, this_.MEMO as MEMO10_0_, this_.PAGE as PAGE10_0_, this_.PAGE_NAME as PAGE12_10_0_, this_.SEQ as SEQ10_0_, this_.SYSTEM_TYPE as SYSTEM14_10_0_, this_.TITLE as TITLE10_0_, this_.TITLE_NAME as TITLE16_10_0_, this_.UPDATE_BY as UPDATE17_10_0_, this_.UPDATE_DT as UPDATE18_10_0_, this_.USR_IP as USR19_10_0_, this_.VER as VER10_0_ from CFG_MSG this_ where this_.PAGE=?");
        controller.plContent.setItems(items);


    }
}
