package com.prhythm.core.generic.logging;

/**
 * log的層級
 * Created by nanashi07 on 15/7/7.
 */
public enum Level {

    Trace(0, "TRACE"), Debug(1, "DEBUG"), Info(2, "INFO"), Warn(3, "WARN"), Error(4, "ERROR"), Fatal(5, "FATAL");

    int value;
    String type;

    Level(int value, String type) {
        this.value = value;
        this.type = type;
    }

    public int hash() {
        return value;
    }

    @Override
    public String toString() {
        return type;
    }

}