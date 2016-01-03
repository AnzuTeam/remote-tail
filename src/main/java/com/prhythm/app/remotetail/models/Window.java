package com.prhythm.app.remotetail.models;

/**
 * 視窗設定
 * Created by nanashi07 on 15/12/31.
 */
public class Window {

    /**
     * 視窗位置 x
     */
    double x;
    /**
     * 視窗位置 y
     */
    double y;
    /**
     * 視窗寬度
     */
    double width;
    /**
     * 視窗高度
     */
    double height;

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

}
