package com.prhythm.app.remotetail.data;

import com.prhythm.app.remotetail.models.LogPath;

import java.util.Observable;
import java.util.Observer;

/**
 * 資料行
 * Created by nanashi07 on 15/12/30.
 */
public class Line implements Observer {

    /**
     * 行號
     */
    int index;
    /**
     * 行的內容
     */
    String content;

    public Line() {
    }

    public Line(int index, String content) {
        this.index = index;
        this.content = content;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Line line = (Line) o;

        return index == line.index;
    }

    @Override
    public int hashCode() {
        return index;
    }

    @Override
    public String toString() {
        return content == null ? "loading..." : content;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg == null || !(arg instanceof LogPath)) return;

        LogPath path = (LogPath) arg;
        if (path.hasLine(index)) {
            content = path.atLine(index);
        }
    }
}
