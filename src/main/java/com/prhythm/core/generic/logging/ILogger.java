package com.prhythm.core.generic.logging;

/**
 * log界面
 * Created by nanashi07 on 15/7/7.
 */
public interface ILogger {

    void trace(Object... message);

    void trace(String message, Object... params);

    void debug(Object... message);

    void debug(String message, Object... params);

    void info(Object... message);

    void info(String message, Object... params);

    void warn(Object... message);

    void warn(String message, Object... params);

    void error(Object... message);

    void error(String message, Object... params);

    void fatal(Object... message);

    void fatal(String message, Object... params);

}
