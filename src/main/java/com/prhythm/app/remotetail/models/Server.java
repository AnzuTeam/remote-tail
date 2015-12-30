package com.prhythm.app.remotetail.models;

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

    String host;

    int port;

    String account;

    String password;

    boolean expanded;

    @XmlElementWrapper(name = "logs")
    @XmlElement(name = "logPath")
    List<LogPath> logPaths;

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
    public String toString() {
        return host;
    }

}
