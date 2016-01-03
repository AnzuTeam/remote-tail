package com.prhythm.app.remotetail.core;

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
import com.prhythm.core.generic.util.Cube;
import com.prhythm.core.generic.util.Delimiters;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
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

    ContextMenu serverMenu;
    ContextMenu logMenu;

    public void load(DataWrapper wrapper) {
        // 載入設定
        TreeItem<Object> root = new TreeItem<>();
        //noinspection unchecked
        areas.setRoot(root);
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
                disconnect.setDisable(true);
            } else {
                TreeItem item = (TreeItem) newValue;
                flushDisconnectStatus(item);
            }
        });

        // 變更檔案呈現（包含行號與文字）
        //noinspection unchecked
        contents.setCellFactory(param -> new ListLineItem());

        // copy
        contents.addEventFilter(KeyEvent.KEY_TYPED, event -> {

        });

        // 多選
        contents.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

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
        TreeItem treeNode = (TreeItem) areas.getSelectionModel().getSelectedItem();
        if (treeNode == null) return;
        Object value = treeNode.getValue();
        if (value instanceof Server) {
            if (serverMenu != null) serverMenu.hide();
            if (logMenu != null) logMenu.hide();
            if (serverMenu == null) {
                serverMenu = new ContextMenu();
                ObservableList<MenuItem> items = serverMenu.getItems();
                MenuItem menuItem;

                // 新增 log
                menuItem = new MenuItem("Add Log");
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
                    LogPath path = (LogPath) node.getValue();

                    Alert alert = new Alert(
                            Alert.AlertType.CONFIRMATION,
                            String.format(Singleton.of(ResourceBundle.class).getString("rmt.dialog.delete.log"), path),
                            ButtonType.YES, ButtonType.NO
                    );
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.YES) {
                        // 向上找出 server
                        TreeItem parent = node.getParent();
                        Server server = (Server) parent.getValue();

                        // 移除資料
                        server.getLogPaths().remove(path);
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
                    // 藉由變更資料更新畫面
                    //noinspection unchecked
//                    contents.setItems(FXCollections.observableArrayList());
                    //noinspection unchecked
//                    contents.setItems(list);
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
        // todo
    }

    public void preferenceClick(Event event) {

    }

    public void disconnectClick(Event event) {
        TreeItem item = (TreeItem) areas.getSelectionModel().getSelectedItem();
        OutContent<Server> server = new OutContent<>();
        OutContent<LogPath> log = new OutContent<>();
        findValue(item, server, log);

        if (server.present() && server.value().isConnected()) {
            server.value().disconnect();
            //noinspection unchecked
            contents.setItems(FXCollections.observableArrayList());
            disconnect.setDisable(true);
        }
    }

    public void copyText(KeyEvent event) {
        if ("c".equalsIgnoreCase(event.getCharacter()) && (event.isMetaDown() || event.isControlDown())) {
            //noinspection unchecked
            ObservableList<Line> selectedItems = contents.getSelectionModel().getSelectedItems();
            Cube<String> logs = Cube.from(selectedItems).select((item, index) -> item.getContent());

            // 複製內容
            Clipboard systemClipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(Delimiters.with(String.format("%n")).join(logs).toString());

            systemClipboard.setContent(content);
        }
    }
}
