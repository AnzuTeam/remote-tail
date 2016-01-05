package com.prhythm.app.remotetail.core;

import com.prhythm.app.remotetail.App;
import com.prhythm.app.remotetail.data.*;
import com.prhythm.app.remotetail.models.DataWrapper;
import com.prhythm.app.remotetail.models.LogPath;
import com.prhythm.app.remotetail.models.Preference;
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
import javafx.event.ActionEvent;
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
    final public static String LAYOUT_PREFERENCE_EDITOR = "/com/prhythm/app/remotetail/core/edit.preference.fxml";
    final public static String LAYOUT_GO_TO_EDITOR = "/com/prhythm/app/remotetail/core/edit.goto.fxml";

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
    @FXML
    HBox searchBar;
    @FXML
    TextField searchText;

    ContextMenu serverMenu;
    ContextMenu logMenu;

    boolean tailing = true;

    /**
     * 初始化
     *
     * @param wrapper
     */
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
        contents.setCellFactory(param -> new LineListCell());

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
                                // 捲至底部時不停止追尾
                                if (newValue.doubleValue() < 1) setTailing(false);
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

                    // 更新外觀
                    App.changeTheme(alert.getDialogPane().getStylesheets(), Singleton.of(DataWrapper.class).getPreference().getTheme());

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

                    // 更新外觀
                    App.changeTheme(alert.getDialogPane().getStylesheets(), Singleton.of(DataWrapper.class).getPreference().getTheme());

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
        dialog.setTitle(Singleton.of(ResourceBundle.class).getString("rmt.dialog.server.edit.title"));

        ServerEditorController controller;
        VBox content;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(LAYOUT_SERVER_EDITOR), Singleton.of(ResourceBundle.class));
            content = loader.load();
            controller = loader.getController();
        } catch (IOException e) {
            throw new RecessiveException(e.getMessage(), e);
        }

        // 更新外觀
        App.changeTheme(dialog.getDialogPane().getStylesheets(), Singleton.of(DataWrapper.class).getPreference().getTheme());

        dialog.getDialogPane().setContent(content);
        controller.from(server);

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(param -> {
            if (param == ButtonType.OK && controller.update(server)) {
                Logs.debug("Server 設定 %s 已更新", server);
                return server;
            }
            // todo 狀態錯誤
            return null;
        });

        controller.focus();
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
        dialog.setTitle(Singleton.of(ResourceBundle.class).getString("rmt.dialog.log.edit.title"));

        LogPathEditorController controller;
        HBox content;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(LAYOUT_LOG_EDITOR), Singleton.of(ResourceBundle.class));
            content = loader.load();
            controller = loader.getController();
        } catch (IOException e) {
            throw new RecessiveException(e.getMessage(), e);
        }

        // 更新外觀
        App.changeTheme(dialog.getDialogPane().getStylesheets(), Singleton.of(DataWrapper.class).getPreference().getTheme());

        dialog.getDialogPane().setContent(content);
        controller.from(path);

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(param -> {
            if (param == ButtonType.OK && controller.update(path)) {
                Logs.debug("Log 路徑 %s 已更新", path);
                return path;
            }
            // todo 顯示狀態錯誤
            return null;
        });

        controller.focus();
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
        // 關閉搜尋
        searchText.setText("");
        searchBar.setManaged(false);
        searchBar.setVisible(false);

        // 關閉 tail
        setTailing(false);

        if (contents.getItems() instanceof RemoteLogReaderList) {
            /** 若不是同一個檔案時，先清空畫面 **/
            // 取得現在檔案
            TreeItem item = (TreeItem) areas.getSelectionModel().getSelectedItem();
            OutContent<Server> server = new OutContent<>();
            OutContent<LogPath> log = new OutContent<>();
            findValue(item, server, log);

            RemoteSourceReaderList list = (RemoteSourceReaderList) contents.getItems();
            if (list.getServer().equals(server.value()) && list.getPath().equals(log.value())) {
                // 同一檔不再讀取
                return;
            } else {
                // 不同檔時清除畫面
                //noinspection unchecked
                contents.setItems(null);
            }
        }

        new Thread(() -> {
            TreeItem item = (TreeItem) areas.getSelectionModel().getSelectedItem();
            OutContent<Server> server = new OutContent<>();
            OutContent<LogPath> log = new OutContent<>();
            findValue(item, server, log);
            if (!log.present()) return;

            synchronized (server.value()) {
                if (!server.value().isConnected()) server.value().connect();
            }

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

                // 更新中斷連線按鈕
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
    @FXML
    void addServerClick(Event event) {
        createEditDialog(new Server());
    }

    /**
     * 新增 Log node
     *
     * @param event
     */
    @FXML
    void addLogClick(Event event) {
        TreeItem item = (TreeItem) areas.getSelectionModel().getSelectedItem();
        OutContent<Server> server = new OutContent<>();
        OutContent<LogPath> log = new OutContent<>();
        findValue(item, server, log);

        if (server.present()) {
            createEditDialog(server.value(), new LogPath());
        }
    }

    /**
     * 切換 tail
     *
     * @param event
     */
    @FXML
    void switchTailClick(Event event) {
        // 執行中或查詢時時停用
        if (tailing || contents.getItems() instanceof FilteredLogReaderList) {
            setTailing(false);
            return;
        }

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
    @FXML
    void preferenceClick(Event event) {
        Dialog<Preference> dialog = new Dialog<>();
        dialog.setTitle(Singleton.of(ResourceBundle.class).getString("rmt.dialog.preference.edit.title"));
        Preference preference = Singleton.of(DataWrapper.class).getPreference();

        PreferenceEditorController controller;
        VBox content;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(LAYOUT_PREFERENCE_EDITOR), Singleton.of(ResourceBundle.class));
            content = loader.load();
            controller = loader.getController();
        } catch (IOException e) {
            throw new RecessiveException(e.getMessage(), e);
        }

        // 更新外觀
        App.changeTheme(dialog.getDialogPane().getStylesheets(), Singleton.of(DataWrapper.class).getPreference().getTheme());

        dialog.getDialogPane().setContent(content);
        controller.from(preference);

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(param -> {
            if (param == ButtonType.OK && controller.update(preference)) {
                Logs.debug("Server 設定 %s 已更新", preference);
                return preference;
            }
            // todo 狀態錯誤
            return null;
        });

        controller.focus();
        dialog.show();
    }

    /**
     * 中斷連線
     *
     * @param event
     */
    @FXML
    void disconnectClick(Event event) {
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
    @FXML
    void hotKeyTrigger(KeyEvent event) {
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
            // 檢查顯示狀態
            {
                TreeItem item = (TreeItem) areas.getSelectionModel().getSelectedItem();
                if (item == null) return;
                OutContent<Server> server = new OutContent<>();
                OutContent<LogPath> log = new OutContent<>();
                findValue(item, server, log);
                if (!server.present() || !log.present()) return;
                if (!server.value().isConnected()) return;
            }

            searchText.setText("");
            searchBar.setManaged(true);
            searchBar.setVisible(true);
            Platform.runLater(searchText::requestFocus);
        }
        // 到指定行
        if ("g".equalsIgnoreCase(event.getCharacter()) && (event.isMetaDown() || event.isControlDown())) {
            jumpToLine();
        }
    }

    /**
     * 跳至指定行
     */
    void jumpToLine() {
        // 檢查顯示狀態
        {
            TreeItem item = (TreeItem) areas.getSelectionModel().getSelectedItem();
            if (item == null) return;
            OutContent<Server> server = new OutContent<>();
            OutContent<LogPath> log = new OutContent<>();
            findValue(item, server, log);
            if (!server.present() || !log.present()) return;
            if (!server.value().isConnected()) return;
        }

        // 停止 tail
        setTailing(false);

        // 開啟行號輸入
        Dialog<Object> dialog = new Dialog<>();
        dialog.setTitle(Singleton.of(ResourceBundle.class).getString("rmt.dialog.go.to.title"));

        GotoLineEditorController controller;
        HBox content;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(LAYOUT_GO_TO_EDITOR), Singleton.of(ResourceBundle.class));
            content = loader.load();
            controller = loader.getController();
        } catch (IOException e) {
            throw new RecessiveException(e.getMessage(), e);
        }

        // 更新外觀
        App.changeTheme(dialog.getDialogPane().getStylesheets(), Singleton.of(DataWrapper.class).getPreference().getTheme());

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(param -> {
            if (param == ButtonType.OK && controller.selectedLineNumber() != null) {
                Platform.runLater(() -> {
                    // 取得目前顯示檔案
                    TreeItem item = (TreeItem) areas.getSelectionModel().getSelectedItem();
                    OutContent<Server> server = new OutContent<>();
                    OutContent<LogPath> log = new OutContent<>();
                    findValue(item, server, log);
                    if (!server.present() || !log.present()) return;
                    if (!server.value().isConnected()) return;

                    Integer index = controller.selectedLineNumber();
                    String line = log.value().atLine(index);

                    //noinspection unchecked
                    contents.scrollTo(new Line(index, line, log.value().hasLine(index)));
                    Logs.debug("跳至第 %s 行", index);
                });
                return null;
            }
            return null;
        });

        controller.focus();
        dialog.show();
    }

    /**
     * 設定追蹤檔尾
     *
     * @param tailing
     */
    void setTailing(boolean tailing) {
        if (contents.getItems() instanceof FilteredLogReaderList) tailing = false;

        this.tailing = tailing;
        tail.setDisable(!tailing);
    }

    /**
     * 隱藏 search bar
     *
     * @param event
     */
    @FXML
    void searchTrigger(KeyEvent event) {
        if (event.getCode() == KeyCode.ESCAPE) {
            searchText.setText("");
            searchBar.setManaged(false);
            searchBar.setVisible(false);

            // 顯示完整內容
            connectLogFile(null);
        } else if (event.getCode() == KeyCode.ENTER) {
            searchClick(null);
        }
    }

    /**
     * 執行搜尋
     *
     * @param actionEvent
     */
    @FXML
    void searchClick(ActionEvent actionEvent) {
        if (searchText.getText().trim().isEmpty())
            return;

        // 搜尋時停用追尾
        setTailing(false);

        new Thread(() -> {
            TreeItem item = (TreeItem) areas.getSelectionModel().getSelectedItem();
            OutContent<Server> server = new OutContent<>();
            OutContent<LogPath> log = new OutContent<>();
            findValue(item, server, log);
            if (!log.present()) return;

            FilteredLogReaderList list = new FilteredLogReaderList(server.value(), log.value(), searchText.getText().trim());
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

    @FXML
    void highLightClick(Event event) {
        // todo
    }

}
