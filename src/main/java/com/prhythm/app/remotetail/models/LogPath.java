package com.prhythm.app.remotetail.models;

/**
 * Log 檔案
 * Created by nanashi07 on 15/12/30.
 */
public class LogPath {

    String path;

    public LogPath() {
    }

    public LogPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return path;
    }
}
