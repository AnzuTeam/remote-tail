package com.prhythm.app.remotetail.data;

import com.prhythm.app.remotetail.models.LogPath;

import java.util.Observable;
import java.util.Observer;

/**
 * 資料行
 * Created by nanashi07 on 15/12/30.
 */
public class Line implements Observer {

    int index;
    String content;
    boolean loaded;

    public Line() {
    }

    public Line(int index, String content, boolean loaded) {
        this.index = index;
        this.content = content;
        this.loaded = loaded;
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
    public String toString() {
        return loaded ? content : String.format("#%s loading...", index);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg != null && arg instanceof LogPath) {
            LogPath path = (LogPath) arg;
            if (path.hasLine(index)) {
                content = path.atLine(index);
                loaded = true;
            }
        }
    }
}
