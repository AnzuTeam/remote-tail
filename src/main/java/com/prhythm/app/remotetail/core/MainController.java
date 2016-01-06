package com.prhythm.app.remotetail.core;

import com.prhythm.app.remotetail.App;
import com.prhythm.app.remotetail.data.*;
import com.prhythm.app.remotetail.models.DataWrapper;
import com.prhythm.app.remotetail.models.LogPath;
import com.prhythm.app.remotetail.models.Preference;
import com.prhythm.app.remotetail.models.Server;
import com.prhythm.core.generic.data.Once;
import com.prhythm.core.generic.data.OutContent;
import com.prhythm.core.generic.data.Singleton;
import com.prhythm.core.generic.exception.RecessiveException;
import com.prhythm.core.generic.logging.Logs;
import com.prhythm.core.generic.util.Boxings;
import com.prhythm.core.generic.util.Cube;
import com.prhythm.core.generic.util.Delimiters;
import com.sun.javafx.scene.control.skin.VirtualFlow;
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
    final public static String LAYOUT_HIGH_LIGHT_EDITOR = "/com/prhythm/app/remotetail/core/edit.high.light.fxml";

    final public static String ICON_SERVER = "/com/prhythm/app/remotetail/icons/icon_server.png";
    final public static String ICON_LOG = "/com/prhythm/app/remotetail/icons/icon_log.png";
    final public static String ICON_ERROR = "/com/prhythm/app/remotetail/icons/icon_error.png";

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
    @FXML
    Label status;

    /**
     * 點選 {@link Server} 的右鍵選單
     */
    Once<ContextMenu> serverMenu = new Once<ContextMenu>() {
        @Override
        protected ContextMenu get() throws Exception {
            ContextMenu menu = new ContextMenu();
            ObservableList<MenuItem> items = menu.getItems();
            MenuItem menuItem;

            // 新增 log
            menuItem = new MenuItem(Singleton.of(ResourceBundle.class).getString("rmt.tree.server.context.menu.add.log"));
            menuItem.setOnAction(MainController.this::addLogClick);
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
            return menu;
        }
    };

    /**
     * 點選 {@link LogPath} 的右鍵選單
     */
    Once<ContextMenu> logMenu = new Once<ContextMenu>() {
        @Override
        protected ContextMenu get() throws Exception {
            ContextMenu menu = new ContextMenu();
            ObservableList<MenuItem> items = menu.getItems();
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
            return menu;
        }
    };

    boolean tailing = true;

    Integer fullLogIndex;

    /**
     * 初始化
     */
    public void load() {
        DataWrapper wrapper = Singleton.of(DataWrapper.class);

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

                ObservableList<TreeItem<Object>> serverChildren = treeItem.getChildren();

                // 處理 log 路徑設定
                for (LogPath logPath : server.getLogPaths()) {
                    TreeItem<Object> logPathTreeItem = new TreeItem<>(logPath, getIcon(ICON_LOG));
                    serverChildren.add(logPathTreeItem);
                }
            }
        }

        // 點選 log 事件
        areas.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                // 顯示 log
                connectLogFile(event);
            } else if (event.getButton() == MouseButton.SECONDARY) {
                // 顯示選單
                showTreeContextMenu(event);
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
     * 取得目前的搜尋文字
     *
     * @return
     */
    public String getSearchText() {
        return searchBar.isVisible() ? searchText.getText().trim() : null;
    }

    /**
     * 更新功能 disconnect 狀態
     *
     * @param item 目前選擇的節點
     */
    void flushDisconnectStatus(TreeItem item) {
        OutContent<Server> server = new OutContent<>();
        OutContent<LogPath> log = new OutContent<>();
        findValue(item, server, log);
        disconnect.setDisable(!(server.present() && server.value().isConnected()));
    }

    /**
     * 顯示 {@link TreeItem} 的右鍵選單
     *
     * @param event 點選事件
     */
    void showTreeContextMenu(MouseEvent event) {
        TreeItem selectedItem = (TreeItem) areas.getSelectionModel().getSelectedItem();
        if (selectedItem == null) return;
        Object value = selectedItem.getValue();
        if (value instanceof Server) {
            showServerContextMenu(event);
        } else if (value instanceof LogPath) {
            showLogPathContextMenu(event);
        }
    }

    /**
     * 顯示 {@link Server} 的右鍵選單
     *
     * @param event 點選事件
     */
    void showServerContextMenu(MouseEvent event) {
        // 隱藏所有選單
        if (serverMenu.present()) serverMenu.value().hide();
        if (logMenu.present()) logMenu.value().hide();

        serverMenu.value().show(areas, event.getScreenX(), event.getScreenY());
    }

    /**
     * 顯示 {@link LogPath} 的右鍵選單
     *
     * @param event 點選事件
     */
    void showLogPathContextMenu(MouseEvent event) {
        // 隱藏所有選單
        if (serverMenu.present()) serverMenu.value().hide();
        if (logMenu.present()) logMenu.value().hide();

        logMenu.value().show(areas, event.getScreenX(), event.getScreenY());
    }

    /**
     * 顯示 {@link Server} 編輯視窗
     *
     * @param server {@link Server} 資料內容
     * @return
     */
    Dialog<Server> createEditDialog(Server server) {
        Dialog<Server> dialog = new Dialog<>();
        dialog.setTitle(Singleton.of(ResourceBundle.class).getString("rmt.dialog.server.edit.title"));

        ServerEditorController controller;
        VBox content;

        try {
            // 載入 UI 設計
            FXMLLoader loader = new FXMLLoader(getClass().getResource(LAYOUT_SERVER_EDITOR), Singleton.of(ResourceBundle.class));
            content = loader.load();
            controller = loader.getController();
        } catch (IOException e) {
            throw new RecessiveException(e.getMessage(), e);
        }

        // 更新外觀
        App.changeTheme(dialog.getDialogPane().getStylesheets(), Singleton.of(DataWrapper.class).getPreference().getTheme());

        // 設定顯示介面
        dialog.getDialogPane().setContent(content);
        // 載入設定
        controller.from(server);

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(param -> {
            if (param == ButtonType.OK && controller.update(server)) {
                App.info(Singleton.of(ResourceBundle.class).getString("rmt.status.info.server.updated"), server);
                Logs.info("Server 設定 %s 已更新", server);
                return server;
            }
            return null;
        });

        controller.focus();
        Optional<Server> result = dialog.showAndWait();

        // 更新畫面與資料
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
     * @param server {@link Server} 資料內容
     * @param path   {@link LogPath} 資料內容
     * @return
     */
    Dialog<LogPath> createEditDialog(Server server, LogPath path) {
        Dialog<LogPath> dialog = new Dialog<>();
        dialog.setTitle(Singleton.of(ResourceBundle.class).getString("rmt.dialog.log.edit.title"));

        LogPathEditorController controller;
        HBox content;

        try {
            // 載入 UI 設計
            FXMLLoader loader = new FXMLLoader(getClass().getResource(LAYOUT_LOG_EDITOR), Singleton.of(ResourceBundle.class));
            content = loader.load();
            controller = loader.getController();
        } catch (IOException e) {
            throw new RecessiveException(e.getMessage(), e);
        }

        // 更新外觀
        App.changeTheme(dialog.getDialogPane().getStylesheets(), Singleton.of(DataWrapper.class).getPreference().getTheme());

        // 設定顯示介面
        dialog.getDialogPane().setContent(content);
        // 載入設定
        controller.from(path);

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(param -> {
            if (param == ButtonType.OK && controller.update(path)) {
                App.info(Singleton.of(ResourceBundle.class).getString("rmt.status.info.log.path.updated"));
                Logs.debug("Log 路徑 %s 已更新", path);
                return path;
            }
            return null;
        });

        controller.focus();
        Optional<LogPath> result = dialog.showAndWait();

        // 更新畫面及資料
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
     * @param event 點選事件
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
            // 取得目前的選擇項目
            TreeItem item = (TreeItem) areas.getSelectionModel().getSelectedItem();
            OutContent<Server> server = new OutContent<>();
            OutContent<LogPath> log = new OutContent<>();
            findValue(item, server, log);

            RemoteSourceReaderList list = (RemoteSourceReaderList) contents.getItems();
            if (list.getServer().equals(server.value()) && list.getPath().equals(log.value())) {
                // 同一檔不再次讀取
                return;
            } else {
                // 不同檔時清除畫面
                fullLogIndex = null;
                //noinspection unchecked
                contents.setItems(null);
            }
        }

        new Thread(() -> {
            // 取得目前的選擇項目
            TreeItem item = (TreeItem) areas.getSelectionModel().getSelectedItem();
            OutContent<Server> server = new OutContent<>();
            OutContent<LogPath> log = new OutContent<>();
            findValue(item, server, log);
            if (!log.present()) return;

            // 連線
            synchronized (server.value()) {
                if (!server.value().isConnected()) server.value().connect();
            }

            RemoteLogReaderList list = new RemoteLogReaderList(server.value(), log.value());

            // 利用 InvalidationListener 由 RemoteLogReaderList 更新畫面
            list.addListener((Observable observable) -> {
                Platform.runLater(() -> {
                    // 更新畫面
                    //noinspection Convert2MethodRef
                    contents.refresh();
                });
            });

            // 更新資料與畫面
            Platform.runLater(() -> {
                try {
                    //noinspection unchecked
                    contents.setItems(list);
                    // 連接時移至底部
                    contents.scrollTo(fullLogIndex == null ? list.size() - 1 : fullLogIndex);
                    fullLogIndex = null;
                } catch (Exception e) {
                    Logs.warn(RecessiveException.unwrapp(e));
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
     * @param item   節點
     * @param server 要取得的 {@link Server} 資料
     * @param log    要取得的 {@link LogPath} 資料
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
     * 取得指定位置的圖片並轉為 {@link ImageView}
     *
     * @param path 圖片位置
     * @return
     */
    Node getIcon(String path) {
        return new ImageView(new Image(getClass().getResourceAsStream(path)));
    }

    /**
     * 新增 {@link Server} node
     *
     * @param event 事件
     */
    @FXML
    void addServerClick(Event event) {
        createEditDialog(new Server());
    }

    /**
     * 新增 {@link LogPath} node
     *
     * @param event
     */
    @FXML
    void addLogClick(Event event) {
        // 取得目前的選擇項目
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

        setTailing(true);

        new Thread(() -> {
            App.info(Singleton.of(ResourceBundle.class).getString("rmt.status.info.tailing.start"));
            Logs.debug("開始追蹤檔尾");

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

            App.info(Singleton.of(ResourceBundle.class).getString("rmt.status.info.tailing.stop"));
        }).start();
    }

    /**
     * 個人偏好
     *
     * @param event 事件
     */
    @FXML
    void preferenceClick(Event event) {
        Dialog<Preference> dialog = new Dialog<>();
        dialog.setTitle(Singleton.of(ResourceBundle.class).getString("rmt.dialog.preference.edit.title"));
        Preference preference = Singleton.of(DataWrapper.class).getPreference();

        PreferenceEditorController controller;
        VBox content;

        try {
            // 載入 UI 設計
            FXMLLoader loader = new FXMLLoader(getClass().getResource(LAYOUT_PREFERENCE_EDITOR), Singleton.of(ResourceBundle.class));
            content = loader.load();
            controller = loader.getController();
        } catch (IOException e) {
            throw new RecessiveException(e.getMessage(), e);
        }

        // 更新外觀
        App.changeTheme(dialog.getDialogPane().getStylesheets(), Singleton.of(DataWrapper.class).getPreference().getTheme());

        // 設定顯示介面
        dialog.getDialogPane().setContent(content);
        // 載入設定
        controller.from(preference);

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(param -> {
            if (param == ButtonType.OK && controller.update(preference)) {
                App.info(Singleton.of(ResourceBundle.class).getString("rmt.status.info.preference.updated"));
                Logs.debug("偏好設定已更新");
                return preference;
            }
            return null;
        });

        controller.focus();
        dialog.show();
    }

    /**
     * 中斷連線
     *
     * @param event 事件
     */
    @FXML
    void disconnectClick(Event event) {
        // 取得目前的選擇項目
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
     * @param event 事件
     */
    @FXML
    void hotKeyTyped(KeyEvent event) {
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
                App.info(Singleton.of(ResourceBundle.class).getString("rmt.status.info.selected.content.copied"));
            }
        }
        // 搜尋
        if ("f".equalsIgnoreCase(event.getCharacter()) && (event.isMetaDown() || event.isControlDown())) {
            // 取得目前的選擇項目
            TreeItem item = (TreeItem) areas.getSelectionModel().getSelectedItem();
            if (item == null) return;
            OutContent<Server> server = new OutContent<>();
            OutContent<LogPath> log = new OutContent<>();
            findValue(item, server, log);
            if (!server.present() || !log.present()) return;
            if (!server.value().isConnected()) return;

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
            // 取得目前顯示檔案
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
            // 載入 UI 設計
            FXMLLoader loader = new FXMLLoader(getClass().getResource(LAYOUT_GO_TO_EDITOR), Singleton.of(ResourceBundle.class));
            content = loader.load();
            controller = loader.getController();
        } catch (IOException e) {
            throw new RecessiveException(e.getMessage(), e);
        }

        // 更新外觀
        App.changeTheme(dialog.getDialogPane().getStylesheets(), Singleton.of(DataWrapper.class).getPreference().getTheme());

        // 設定顯示介面
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(param -> {
            Integer lineNumber = controller.selectedLineNumber();
            if (param == ButtonType.OK && lineNumber != null && lineNumber > 0) {
                Platform.runLater(() -> {
                    // 取得目前顯示檔案
                    TreeItem item = (TreeItem) areas.getSelectionModel().getSelectedItem();
                    OutContent<Server> server = new OutContent<>();
                    OutContent<LogPath> log = new OutContent<>();
                    findValue(item, server, log);
                    if (!server.present() || !log.present()) return;
                    if (!server.value().isConnected()) return;

                    String line = log.value().atLine(lineNumber);

                    //noinspection unchecked
                    contents.scrollTo(new Line(lineNumber, line));
                    Logs.debug("跳至第 %s 行", lineNumber);
                });
            }
            return null;
        });

        controller.focus();
        dialog.show();
    }

    /**
     * 設定追蹤檔尾
     *
     * @param tailing 開啟或關閉
     */
    void setTailing(boolean tailing) {
        // 搜尋時不開啟 tailing
        if (contents.getItems() instanceof FilteredLogReaderList) tailing = false;

        this.tailing = tailing;
        // 更新 tail 功能狀態
        tail.setDisable(!tailing);
    }

    /**
     * 隱藏 search bar
     *
     * @param event 事件
     */
    @FXML
    void searchTrigger(KeyEvent event) {
        if (event.getCode() == KeyCode.ESCAPE) {
            // 關閉 search
            searchText.setText("");
            searchBar.setManaged(false);
            searchBar.setVisible(false);

            // 顯示完整內容
            connectLogFile(null);
        } else if (event.getCode() == KeyCode.ENTER) {
            // 開始搜尋
            searchClick(null);
        }
    }

    /**
     * 執行搜尋
     *
     * @param actionEvent 事件
     */
    @FXML
    void searchClick(ActionEvent actionEvent) {
        if (searchText.getText().trim().isEmpty())
            return;

        // 搜尋時停用追尾
        setTailing(false);

        new Thread(() -> {
            // 取得目前顯示檔案
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
                    // 取得目前資料的位置
                    VirtualFlow flow = (VirtualFlow) contents.lookup(".virtual-flow");
                    fullLogIndex = flow.getFirstVisibleCell().getIndex();

                    //noinspection unchecked
                    contents.setItems(list);
                    // 連接時移至底部
                    contents.scrollTo(list.size() - 1);
                } catch (Exception e) {
                    Logs.warn(RecessiveException.unwrapp(e));
                    //noinspection unchecked
                    contents.setItems(FXCollections.observableArrayList());
                }
                flushDisconnectStatus(item);
            });
        }).start();
    }

    /**
     * 編輯顯著標示設定
     *
     * @param event 事件
     */
    @FXML
    void highLightClick(Event event) {
        // 開啟 high light 設定
        Dialog<Object> dialog = new Dialog<>();
        dialog.setTitle(Singleton.of(ResourceBundle.class).getString("rmt.dialog.high.light.title"));

        HighLightEditorController controller;
        VBox content;

        try {
            // 載入 UI 設計
            FXMLLoader loader = new FXMLLoader(getClass().getResource(LAYOUT_HIGH_LIGHT_EDITOR), Singleton.of(ResourceBundle.class));
            content = loader.load();
            controller = loader.getController();
            controller.load();
        } catch (IOException e) {
            throw new RecessiveException(e.getMessage(), e);
        }

        // 更新外觀
        App.changeTheme(dialog.getDialogPane().getStylesheets(), Singleton.of(DataWrapper.class).getPreference().getTheme());

        // 設定顯示介面
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE);

        controller.focus();
        dialog.show();
    }

    /**
     * 顯示狀態文字
     *
     * @param message 內容
     */
    public void error(String message) {
        status.setText(message);
        status.setGraphic(getIcon(ICON_ERROR));
    }

    /**
     * 顯示狀態文字
     *
     * @param message 內容
     */
    public void info(String message) {
        status.setText(message);
        status.setGraphic(null);
    }

}
