package com.prhythm.app.remotetail.data;

/**
 * 資料行
 * Created by nanashi07 on 15/12/30.
 */
public class Line {

    int index;
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
    public String toString() {
        return content;
    }

}
