package com.prhythm.core.generic.logging;

/**
 * log工具，需設定{@link LogFactory}
 * Created by nanashi07 on 15/6/26.
 */
public class Logs {

    public static void trace(Object... message) {
        if (logFactory == null) return;
        logFactory.trace(message);
    }

    public static void trace(String message, Object... params) {
        if (logFactory == null) return;
        logFactory.trace(message, params);
    }

    public static void debug(Object... message) {
        if (logFactory == null) return;
        logFactory.debug(message);
    }

    public static void debug(String message, Object... params) {
        if (logFactory == null) return;
        logFactory.debug(message, params);
    }

    public static void info(Object... message) {
        if (logFactory == null) return;
        logFactory.info(message);
    }

    public static void info(String message, Object... params) {
        if (logFactory == null) return;
        logFactory.info(message, params);
    }

    public static void warn(Object... message) {
        if (logFactory == null) return;
        logFactory.warn(message);
    }

    public static void warn(String message, Object... params) {
        if (logFactory == null) return;
        logFactory.warn(message, params);
    }

    public static void error(Object... message) {
        if (logFactory == null) return;
        logFactory.error(message);
    }

    public static void error(String message, Object... params) {
        if (logFactory == null) return;
        logFactory.error(message, params);
    }

    public static void fatal(Object... message) {
        if (logFactory == null) return;
        logFactory.fatal(message);
    }

    public static void fatal(String message, Object... params) {
        if (logFactory == null) return;
        logFactory.fatal(message, params);
    }

    public static void log(Level level, Object... message) {
        if (logFactory == null) return;
        switch (level) {
            case Trace:
                logFactory.trace(message);
                break;
            case Debug:
                logFactory.debug(message);
                break;
            case Info:
                logFactory.info(message);
                break;
            case Warn:
                logFactory.warn(message);
                break;
            case Error:
                logFactory.error(message);
                break;
            case Fatal:
                logFactory.fatal(message);
                break;
        }
    }

    public static void log(Level level, String message, Object... params) {
        if (logFactory == null) return;
        switch (level) {
            case Trace:
                logFactory.trace(message, params);
                break;
            case Debug:
                logFactory.debug(message, params);
                break;
            case Info:
                logFactory.info(message, params);
                break;
            case Warn:
                logFactory.warn(message, params);
                break;
            case Error:
                logFactory.error(message, params);
                break;
            case Fatal:
                logFactory.fatal(message, params);
                break;
        }
    }

    transient static LogFactory logFactory;

    public LogFactory getLogFactory() {
        return logFactory;
    }

    public void setLogFactory(LogFactory logFactory) {
        Logs.logFactory = logFactory;
    }

}