package com.prhythm.app.remotetail.models;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.prhythm.core.generic.exception.RecessiveException;
import com.prhythm.core.generic.logging.Logs;
import com.prhythm.core.generic.util.Cube;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.ArrayList;
import java.util.List;

/**
 * 主機
 * Created by nanashi07 on 15/12/30.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Server {

    /**
     * 顯示名稱
     */
    String name;

    /**
     * 主機位址
     */
    String host;

    /**
     * 通訊埠
     */
    int port = 22;

    /**
     * 帳號
     */
    String account;

    /**
     * 密碼
     */
    String password;

    /**
     * 檔案是否展開
     */
    boolean expanded;

    /**
     * 已記錄的 log 檔
     */
    @XmlElementWrapper(name = "logs")
    @XmlElement(name = "logPath")
    List<LogPath> logPaths;

    /**
     * SSH 連線的 {@link Session}
     */
    transient Session session;

    public Server() {
        logPaths = new ArrayList<>();
    }

    public Server(String host, int port, String account, String password) {
        this();
        this.host = host;
        this.port = port;
        this.account = account;
        this.password = password;
    }

    /**
     * 檢查 SSH 是否已連線
     *
     * @return
     */
    public boolean isConnected() {
        return session != null && session.isConnected();
    }

    /**
     * 連線 SSH
     */
    public void connect() {
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(account, host, port);
            session.setPassword(password);
            session.connect();
            Logs.info("Connect %s", this);
        } catch (Exception e) {
            throw new RecessiveException(e.getMessage(), e);
        }
    }

    /**
     * 中斷 SSH 連線
     */
    public void disconnect() {
        if (session != null && session.isConnected()) {
            session.disconnect();

            // clear temp log
            Cube.from(logPaths).each((item, index) -> {
                item.lines.clear();
                return true;
            });

            Logs.info("Disconnect %n", this);
        }
    }

    /**
     * 開啟一個新的 {@link Channel}
     *
     * @param type {@link Channel} 的類型
     * @return
     */
    public <T extends Channel> T openChannel(String type) {
        try {
            //noinspection unchecked
            return (T) session.openChannel(type);
        } catch (JSchException e) {
            throw new RecessiveException(e.getMessage(), e);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        // 中斷連線
        disconnect();
        // 清除暫存的 log 內容
        logPaths.forEach(LogPath::clearCachedLines);
        super.finalize();
    }

    // setter & getter

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public List<LogPath> getLogPaths() {
        return logPaths;
    }

    public void setLogPaths(List<LogPath> logPaths) {
        this.logPaths = logPaths;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Server server = (Server) o;

        if (port != server.port) return false;
        if (!host.equals(server.host)) return false;
        return account.equals(server.account);
    }

    @Override
    public int hashCode() {
        int result = host.hashCode();
        result = 31 * result + port;
        result = 31 * result + account.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return name == null || name.trim().length() == 0 ? host : name;
    }

}
