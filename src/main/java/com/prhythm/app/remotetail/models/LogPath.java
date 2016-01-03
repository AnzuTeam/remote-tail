package com.prhythm.app.remotetail.models;

import java.util.HashMap;
import java.util.Map;

/**
 * Log 檔案
 * Created by nanashi07 on 15/12/30.
 */
public class LogPath {

    /**
     * Log 位置
     */
    String path;

    /**
     * 已讀取的 log 內容
     */
    transient Map<Integer, String> lines;

    public LogPath() {
        clearCachedLines();
    }

    public LogPath(String path) {
        this();
        this.path = path;
    }

    public boolean hasLine(int index) {
        return lines.containsKey(index);
    }

    public String atLine(int index) {
        return lines.get(index);
    }

    public void addLine(int index, String content) {
        lines.put(index, content);
    }

    public void clearCachedLines() {
        lines = new HashMap<>();
    }

    // setter & getter

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LogPath path1 = (LogPath) o;

        return path.equals(path1.path);
    }

    @Override
    public int hashCode() {
        return path.hashCode();
    }

    @Override
    public String toString() {
        return path;
    }

}
