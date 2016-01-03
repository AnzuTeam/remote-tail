package com.prhythm.app.remotetail.core;

import com.prhythm.app.remotetail.App;
import com.prhythm.app.remotetail.data.Line;
import com.prhythm.app.remotetail.data.ListLineItem;
import com.prhythm.app.remotetail.data.RemoteLogReaderList;
import com.prhythm.app.remotetail.models.DataWrapper;
import com.prhythm.app.remotetail.models.LogPath;
import com.prhythm.app.remotetail.models.Server;
import com.prhythm.core.generic.data.OutContent;
import com.prhythm.core.generic.data.Singleton;
import com.prhythm.core.generic.exception.RecessiveException;
import com.prhythm.core.generic.logging.Logs;
import com.prhythm.core.generic.util.Boxings;
import com.prhythm.core.generic.util.Cube;
import com.prhythm.core.generic.util.Delimiters;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * 主介面
 * Created by nanashi07 on 15/12/30.
 */
public class MainController {

    final public static String LAYOUT_MAIN = "/com/prhythm/app/remotetail/core/main.fxml";
    final public static String LAYOUT_SERVER_EDITOR = "/com/prhythm/app/remotetail/core/edit.server.fxml";
    final public static String LAYOUT_LOG_EDITOR = "/com/prhythm/app/remotetail/core/edit.log.fxml";

    final public static String ICON_SERVER = "/com/prhythm/app/remotetail/icons/icon_server.png";
    final public static String ICON_LOG = "/com/prhythm/app/remotetail/icons/icon_log.png";

    @FXML
    ListView contents;
    @FXML
    TreeView areas;
    @FXML
    BorderPane addLog;
    @FXML
    BorderPane disconnect;
    @FXML
    Label tail;

    ContextMenu serverMenu;
    ContextMenu logMenu;

    boolean tailing = true;

    public void load(DataWrapper wrapper) {
        // 載入設定
        TreeItem<Object> root = new TreeItem<>();
        //noinspection unchecked
        areas.setRoot(root);

        // 顯示節點
        if (wrapper != null) {
            ObservableList<TreeItem<Object>> rootChildren = root.getChildren();
            for (Server server : wrapper.getServers()) {
                // 顯示 server 設定
                TreeItem<Object> treeItem = new TreeItem<>(server, getIcon(ICON_SERVER));
                treeItem.setExpanded(server.isExpanded());
                rootChildren.add(treeItem);

                // 記錄展開／折疊的變更
                treeItem.expandedProperty().addListener((observable, oldValue, newValue) -> {
                    BooleanProperty property = (BooleanProperty) observable;
                    TreeItem item = (TreeItem) property.getBean();
                    Object value = item.getValue();
                    if (value == null || !(value instanceof Server)) return;
                    Server serverValue = (Server) value;
                    serverValue.setExpanded(newValue);
                });

                final ObservableList<TreeItem<Object>> serverChildren = treeItem.getChildren();

                // 處理 log 路徑設定
                for (LogPath logPath : server.getLogPaths()) {
                    TreeItem<Object> logPathTreeItem = new TreeItem<>(logPath, getIcon(ICON_LOG));
                    serverChildren.add(logPathTreeItem);
                }
            }
        }

        // 註冊點選 log 事件
        areas.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                // 顯示 log
                connectLogFile(event);
            } else if (event.getButton() == MouseButton.SECONDARY) {
                // 顯示選單
                showMenu(event);
            }
        });

        //noinspection unchecked
        areas.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            // 啟用／停用 log 新增功能
            addLog.setDisable(newValue == null);

            // disconnect
            if (newValue == null) {
                // 空值時直接停用
                disconnect.setDisable(true);
            } else {
                // 檢查 server 是否連接
                flushDisconnectStatus((TreeItem) newValue);
            }
        });

        // 變更檔案呈現（包含行號與文字）
        //noinspection unchecked
        contents.setCellFactory(param -> new ListLineItem());

        // 設定 log 可多選
        contents.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        // 捲動時停止 tail
        Singleton.of(Stage.class).setOnShown(new EventHandler<WindowEvent>() {

            boolean done = false;

            @Override
            public void handle(WindowEvent event) {
                if (done) return;
                Cube.from(contents.lookupAll(".scroll-bar"))
                        .where((item, index) -> item instanceof ScrollBar)
                        .ofType(ScrollBar.class)
                        .each((item, index) -> {
                            item.valueProperty().addListener((value, oldValue, newValue) -> {
                                // fixme 有時會影響按下 tail 功能
                                setTailing(false);
                            });
                            return true;
                        });
            }
        });
    }

    /**
     * 更新 disconnect 狀態
     *
     * @param item
     */
    void flushDisconnectStatus(TreeItem item) {
        OutContent<Server> server = new OutContent<>();
        OutContent<LogPath> log = new OutContent<>();
        findValue(item, server, log);
        disconnect.setDisable(!(server.present() && server.value().isConnected()));
    }

    /**
     * 顯示 node 的右鍵選單
     *
     * @param event
     */
    void showMenu(MouseEvent event) {
        TreeItem selectedItem = (TreeItem) areas.getSelectionModel().getSelectedItem();
        if (selectedItem == null) return;
        Object value = selectedItem.getValue();
        if (value instanceof Server) {
            if (serverMenu != null) serverMenu.hide();
            if (logMenu != null) logMenu.hide();
            if (serverMenu == null) {
                serverMenu = new ContextMenu();
                ObservableList<MenuItem> items = serverMenu.getItems();
                MenuItem menuItem;

                // 新增 log
                menuItem = new MenuItem(Singleton.of(ResourceBundle.class).getString("rmt.tree.server.context.menu.add.log"));
                menuItem.setOnAction(this::addLogClick);
                items.add(menuItem);

                // 修改
                menuItem = new MenuItem(Singleton.of(ResourceBundle.class).getString("rmt.tree.server.context.menu.edit"));
                menuItem.setOnAction(evt -> {
                    // 重新取值
                    TreeItem node = (TreeItem) areas.getSelectionModel().getSelectedItem();
                    Server server = (Server) node.getValue();

                    createEditDialog(server);
                });
                items.add(menuItem);

                // 刪除
                menuItem = new MenuItem(Singleton.of(ResourceBundle.class).getString("rmt.tree.server.context.menu.delete"));
                menuItem.setOnAction(evt -> {
                    // 重新取值
                    TreeItem node = (TreeItem) areas.getSelectionModel().getSelectedItem();
                    OutContent<Server> server = new OutContent<>();
                    OutContent<LogPath> log = new OutContent<>();
                    findValue(node, server, log);

                    Alert alert = new Alert(
                            Alert.AlertType.CONFIRMATION,
                            String.format(Singleton.of(ResourceBundle.class).getString("rmt.dialog.delete.server"), server),
                            ButtonType.YES, ButtonType.NO
                    );
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.YES) {
                        // 移除資料
                        Singleton.of(DataWrapper.class).getServers().remove(server.value());
                        // 移除顯示
                        node.getParent().getChildren().remove(node);
                    }
                });
                items.add(menuItem);
            }

            serverMenu.show(areas, event.getScreenX(), event.getScreenY());
        } else if (value instanceof LogPath) {
            if (serverMenu != null) serverMenu.hide();
            if (logMenu != null) logMenu.hide();
            if (logMenu == null) {
                logMenu = new ContextMenu();
                ObservableList<MenuItem> items = logMenu.getItems();
                MenuItem menuItem;

                // 修改
                menuItem = new MenuItem(Singleton.of(ResourceBundle.class).getString("rmt.tree.log.context.menu.edit"));
                menuItem.setOnAction(evt -> {
                    // 重新取值
                    TreeItem node = (TreeItem) areas.getSelectionModel().getSelectedItem();
                    OutContent<Server> server = new OutContent<>();
                    OutContent<LogPath> log = new OutContent<>();
                    findValue(node, server, log);

                    createEditDialog(server.value(), log.value());
                });
                items.add(menuItem);

                // 刪除
                menuItem = new MenuItem(Singleton.of(ResourceBundle.class).getString("rmt.tree.log.context.menu.delete"));
                menuItem.setOnAction(evt -> {
                    // 重新取值
                    TreeItem node = (TreeItem) areas.getSelectionModel().getSelectedItem();
                    OutContent<Server> server = new OutContent<>();
                    OutContent<LogPath> log = new OutContent<>();
                    findValue(node, server, log);

                    Alert alert = new Alert(
                            Alert.AlertType.CONFIRMATION,
                            String.format(Singleton.of(ResourceBundle.class).getString("rmt.dialog.delete.log"), log.value()),
                            ButtonType.YES, ButtonType.NO
                    );
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.YES) {
                        // 移除資料
                        server.value().getLogPaths().remove(log.value());
                        // 移除顯示
                        node.getParent().getChildren().remove(node);
                    }
                });
                items.add(menuItem);
            }

            logMenu.show(areas, event.getScreenX(), event.getScreenY());
        }
    }

    /**
     * 顯示 {@link Server} 編輯視窗
     *
     * @param server
     * @return
     */
    Dialog<Server> createEditDialog(Server server) {
        Dialog<Server> dialog = new Dialog<>();
        dialog.setTitle(Singleton.of(ResourceBundle.class).getString("rmt.dialog.server.edit.title.update"));

        ServerEditorController controller;
        VBox content;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(LAYOUT_SERVER_EDITOR), Singleton.of(ResourceBundle.class));
            content = loader.load();
            controller = loader.getController();
        } catch (IOException e) {
            throw new RecessiveException(e.getMessage(), e);
        }

        dialog.getDialogPane().setContent(content);
        controller.from(server);

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(param -> {
            if (param == ButtonType.OK) {
                controller.update(server);
                Logs.debug("Server 設定 %s 已更新", server);
                return server;
            }
            return null;
        });

        Optional<Server> result = dialog.showAndWait();
        if (result.isPresent() && !Cube.from(Singleton.of(DataWrapper.class).getServers()).has(result.get())) {
            Singleton.of(DataWrapper.class).getServers().add(server);

            // 增加節點
            TreeItem<Object> treeItem = new TreeItem<>(server, getIcon(ICON_SERVER));
            treeItem.setExpanded(server.isExpanded());
            //noinspection unchecked
            areas.getRoot().getChildren().add(treeItem);
        }

        areas.refresh();

        return dialog;
    }

    /**
     * 顯示 {@link LogPath} 編輯視窗
     *
     * @param server
     * @param path
     * @return
     */
    Dialog<LogPath> createEditDialog(Server server, LogPath path) {
        Dialog<LogPath> dialog = new Dialog<>();
        dialog.setTitle(Singleton.of(ResourceBundle.class).getString("rmt.dialog.log.edit.title.update"));

        LogPathEditorController controller;
        HBox content;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(LAYOUT_LOG_EDITOR), Singleton.of(ResourceBundle.class));
            content = loader.load();
            controller = loader.getController();
        } catch (IOException e) {
            throw new RecessiveException(e.getMessage(), e);
        }

        dialog.getDialogPane().setContent(content);
        controller.from(path);

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(param -> {
            if (param == ButtonType.OK) {
                controller.update(path);
                Logs.debug("Log 路徑 %s 已更新", path);
                return path;
            }
            return null;
        });

        Optional<LogPath> result = dialog.showAndWait();
        if (result.isPresent() && !server.getLogPaths().contains(path)) {
            server.getLogPaths().add(path);

            // 增加節點
            TreeItem<Object> treeItem = new TreeItem<>(path, getIcon(ICON_LOG));
            //noinspection unchecked
            TreeItem item = (TreeItem) Cube.from(areas.getRoot().getChildren()).first(new Cube.Predicate<TreeItem>() {
                @Override
                public boolean predicate(TreeItem item, int index) {
                    return server.equals(item.getValue());
                }
            });

            if (item != null) {
                //noinspection unchecked
                item.getChildren().add(treeItem);
            }
        }

        areas.refresh();

        return dialog;
    }

    /**
     * 顯示檔案內容
     *
     * @param event
     */
    void connectLogFile(MouseEvent event) {
        new Thread(() -> {
            TreeItem item = (TreeItem) areas.getSelectionModel().getSelectedItem();
            OutContent<Server> server = new OutContent<>();
            OutContent<LogPath> log = new OutContent<>();
            findValue(item, server, log);
            if (!log.present()) return;

            RemoteLogReaderList list = new RemoteLogReaderList(server.value(), log.value());
            list.addListener((Observable observable) -> {
                Platform.runLater(() -> {
                    // 更新畫面
                    //noinspection Convert2MethodRef
                    contents.refresh();
                });
            });
            Platform.runLater(() -> {
                try {
                    //noinspection unchecked
                    contents.setItems(list);
                    // 連接時移至底部
                    contents.scrollTo(list.size() - 1);
                } catch (Exception e) {
                    Logs.error(RecessiveException.unwrapp(e));
                    //noinspection unchecked
                    contents.setItems(FXCollections.observableArrayList());
                }
                flushDisconnectStatus(item);
            });
        }).start();
    }

    /**
     * 取得目前選擇的節點資料
     *
     * @param item
     * @param server
     * @param log
     */
    void findValue(TreeItem item, OutContent<Server> server, OutContent<LogPath> log) {
        if (item == null) return;
        if (item.getValue() instanceof Server && server != null) {
            server.value((Server) item.getValue());
        }
        if (item.getValue() instanceof LogPath && log != null) {
            log.value((LogPath) item.getValue());

            // server
            if (item.getParent().getValue() instanceof Server && server != null) {
                server.value((Server) item.getParent().getValue());
            }
        }

        Logs.debug("當前資料 server=%s, logPath=%s", server, log);
    }

    /**
     * 取得 icon
     *
     * @param path
     * @return
     */
    Node getIcon(String path) {
        return new ImageView(new Image(getClass().getResourceAsStream(path)));
    }

    /**
     * 新增 Server node
     *
     * @param event
     */
    public void addServerClick(Event event) {
        createEditDialog(new Server());
    }

    /**
     * 新增 Log node
     *
     * @param event
     */
    public void addLogClick(Event event) {
        TreeItem item = (TreeItem) areas.getSelectionModel().getSelectedItem();
        OutContent<Server> server = new OutContent<>();
        OutContent<LogPath> log = new OutContent<>();
        findValue(item, server, log);

        if (server.present()) {
            if (log.present()) {
                createEditDialog(server.value(), log.value());
            } else {
                createEditDialog(server.value());
            }
        }
    }

    /**
     * 切換 tail
     *
     * @param event
     */
    public void switchTailClick(Event event) {
        Logs.trace("開始追蹤檔尾");
        setTailing(true);
        new Thread(() -> {
            while (tailing && !App.STOP_ALL_TASK) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // nothing
                }
                ObservableList items = contents.getItems();
                //noinspection unchecked
                ObservableList<Integer> selection = contents.getSelectionModel().getSelectedIndices();
                int[] ints = Boxings.packInteger(selection);
                Platform.runLater(() -> {
                    //noinspection unchecked
                    contents.setItems(null);
                    //noinspection unchecked
                    contents.setItems(items);
                    contents.scrollTo(items.size() - 1);
                    if (ints.length > 0) contents.getSelectionModel().selectIndices(ints[0], ints);
                });
            }
        }).start();
    }

    /**
     * 個人偏好
     *
     * @param event
     */
    public void preferenceClick(Event event) {
        // todo
    }

    /**
     * 中斷連線
     *
     * @param event
     */
    public void disconnectClick(Event event) {
        TreeItem item = (TreeItem) areas.getSelectionModel().getSelectedItem();
        OutContent<Server> server = new OutContent<>();
        OutContent<LogPath> log = new OutContent<>();
        findValue(item, server, log);

        if (server.present() && server.value().isConnected()) {
            // 中斷連線
            server.value().disconnect();
            // 清除顯示資料
            //noinspection unchecked
            contents.setItems(FXCollections.observableArrayList());
            disconnect.setDisable(true);
        }
    }

    /**
     * 複製文字
     *
     * @param event
     */
    public void hotKeyTrigger(KeyEvent event) {
        // 複制文字
        if ("c".equalsIgnoreCase(event.getCharacter()) && (event.isMetaDown() || event.isControlDown())) {
            //noinspection unchecked
            ObservableList<Line> selectedItems = contents.getSelectionModel().getSelectedItems();
            if (selectedItems.size() > 0) {
                Cube<String> logs = Cube.from(selectedItems).select((item, index) -> item.getContent());

                // 複製內容
                Clipboard systemClipboard = Clipboard.getSystemClipboard();
                ClipboardContent content = new ClipboardContent();
                content.putString(Delimiters.with(String.format("%n")).join(logs).toString());

                systemClipboard.setContent(content);
            }
        }
        // 搜尋
        if ("f".equalsIgnoreCase(event.getCharacter()) && (event.isMetaDown() || event.isControlDown())) {
            // todo
            //grep -n project mylog.log | cut -d : -f1
            //grep -n 搜尋內容 檔案 | cut -d : -f1 ＝》輸出行號
        }
    }

    /**
     * 設定追蹤檔尾
     *
     * @param tailing
     */
    public void setTailing(boolean tailing) {
        this.tailing = tailing;
        tail.setDisable(!tailing);
    }
}
