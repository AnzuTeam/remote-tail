package com.prhythm.core.generic.logging;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

/**
 * 基本的console log實作
 * Created by nanashi07 on 15/7/7.
 */
public class GenericLogger implements ILogger {

    final static protected String NULL = "null";

    protected Level level = Level.Info;

    public GenericLogger() {
    }

    public GenericLogger(Level level) {
        this.level = level;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    @Override
    public void trace(Object... message) {
        log(Level.Trace, format(message));
    }

    @Override
    public void trace(String message, Object... params) {
        log(Level.Trace, format(message, params));
    }

    @Override
    public void debug(Object... message) {
        log(Level.Debug, format(message));
    }

    @Override
    public void debug(String message, Object... params) {
        log(Level.Debug, format(message, params));
    }

    @Override
    public void info(Object... message) {
        log(Level.Info, format(message));
    }

    @Override
    public void info(String message, Object... params) {
        log(Level.Info, format(message, params));
    }

    @Override
    public void warn(Object... message) {
        log(Level.Warn, format(message));
    }

    @Override
    public void warn(String message, Object... params) {
        log(Level.Warn, format(message, params));
    }

    @Override
    public void error(Object... message) {
        log(Level.Error, format(message));
    }

    @Override
    public void error(String message, Object... params) {
        log(Level.Error, format(message, params));
    }

    @Override
    public void fatal(Object... message) {
        log(Level.Fatal, format(message));
    }

    @Override
    public void fatal(String message, Object... params) {
        log(Level.Fatal, format(message, params));
    }

    /**
     * 實際記錄log的方法
     *
     * @param level
     * @param message
     */
    protected void log(Level level, CharSequence message) {
        switch (level.hash()) {
            case 0:  // trace
            case 1:  // debug
            case 2:  // info
            default:
                if (level.hash() < this.level.hash()) break;
                System.out.printf("%s %s%n", createPrefix(level), message);
                break;
            case 3:  // warn
            case 4:  // error
            case 5:  // fatal
                if (level.hash() < this.level.hash()) break;
                System.err.printf("%s %s%n", createPrefix(level), message);
                break;
        }
    }

    /**
     * 訊息轉為文字
     *
     * @param messages
     * @return
     */
    protected CharSequence format(Object... messages) {
        if (messages == null || messages.length == 0) {
            return NULL;
        } else {
            StringBuilder sb = new StringBuilder();
            for (Object message : build(messages)) {
                sb.append("\n").append(message);
            }
            return sb.substring(1);
        }
    }

    /**
     * 訊息轉為文字
     *
     * @param message
     * @param params
     * @return
     */
    protected CharSequence format(String message, Object... params) {
        if (message == null) {
            return NULL;
        } else {
            if (params == null || params.length == 0) {
                return message;
            } else {
                return String.format(message, build(params));
            }
        }
    }

    /**
     * 建立log時間, {@link Level}等資訊
     *
     * @param level
     * @return
     */
    protected CharSequence createPrefix(Level level) {
        return String.format("%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS [%2$s]", new Date(), level);
    }

    /**
     * 轉換特定型別的物件為文字(ex: {@link Exception})
     *
     * @param values
     * @return
     */
    protected Object[] build(Object... values) {
        if (values == null || values.length == 0) return new Object[0];

        for (int i = 0, count = values.length; i < count; i++) {
            Object value = values[i];
            // 轉換exception的訊息
            if (value instanceof Throwable) {
                values[i] = stringify((Throwable) value);
            }
        }

        return values;
    }

    /**
     * 將{@link Exception#printStackTrace()}轉為文字內容
     *
     * @param ex
     * @return
     */
    protected CharSequence stringify(Throwable ex) {
        if (ex == null) return null;

        StringWriter sw;
        PrintWriter pw;

        sw = new StringWriter();
        pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        pw.close();
        try {
            sw.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return sw.toString();
    }

}
