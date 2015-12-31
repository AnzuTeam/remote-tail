package com.prhythm.core.generic.logging;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Log factory，集合所有{@link ILogger}的執行實體
 * Created by nanashi07 on 15/7/7.
 */
public class LogFactory implements ILogger {

    /**
     * log發生錯誤時，是否印出{@link Exception#printStackTrace()}
     */
    boolean printStackTrace;

    /**
     * 登錄要使用的logger
     */
    transient Set<ILogger> loggers;

    public LogFactory() {
        loggers = new HashSet<ILogger>();
    }

    public LogFactory(ILogger... loggers) {
        this();
        this.loggers.addAll(Arrays.asList(loggers));
    }

    public Set<ILogger> getLoggers() {
        return loggers;
    }

    public void setLoggers(Set<ILogger> loggers) {
        this.loggers = loggers;
    }

    public boolean isPrintStackTrace() {
        return printStackTrace;
    }

    public void setPrintStackTrace(boolean printStackTrace) {
        this.printStackTrace = printStackTrace;
    }

    @Override
    public void trace(Object... message) {
        for (ILogger logger : loggers) {
            try {
                logger.trace(message);
            } catch (Throwable ex) {
                if (printStackTrace) ex.printStackTrace();
            }
        }
    }

    @Override
    public void trace(String message, Object... params) {
        for (ILogger logger : loggers) {
            try {
                logger.trace(message, params);
            } catch (Throwable ex) {
                if (printStackTrace) ex.printStackTrace();
            }
        }
    }

    @Override
    public void debug(Object... message) {
        for (ILogger logger : loggers) {
            try {
                logger.debug(message);
            } catch (Throwable ex) {
                if (printStackTrace) ex.printStackTrace();
            }
        }
    }

    @Override
    public void debug(String message, Object... params) {
        for (ILogger logger : loggers) {
            try {
                logger.debug(message, params);
            } catch (Throwable ex) {
                if (printStackTrace) ex.printStackTrace();
            }
        }
    }

    @Override
    public void info(Object... message) {
        for (ILogger logger : loggers) {
            try {
                logger.info(message);
            } catch (Throwable ex) {
                if (printStackTrace) ex.printStackTrace();
            }
        }
    }

    @Override
    public void info(String message, Object... params) {
        for (ILogger logger : loggers) {
            try {
                logger.info(message, params);
            } catch (Throwable ex) {
                if (printStackTrace) ex.printStackTrace();
            }
        }
    }

    @Override
    public void warn(Object... message) {
        for (ILogger logger : loggers) {
            try {
                logger.warn(message);
            } catch (Throwable ex) {
                if (printStackTrace) ex.printStackTrace();
            }
        }
    }

    @Override
    public void warn(String message, Object... params) {
        for (ILogger logger : loggers) {
            try {
                logger.warn(message, params);
            } catch (Throwable ex) {
                if (printStackTrace) ex.printStackTrace();
            }
        }
    }

    @Override
    public void error(Object... message) {
        for (ILogger logger : loggers) {
            try {
                logger.error(message);
            } catch (Throwable ex) {
                if (printStackTrace) ex.printStackTrace();
            }
        }
    }

    @Override
    public void error(String message, Object... params) {
        for (ILogger logger : loggers) {
            try {
                logger.error(message, params);
            } catch (Throwable ex) {
                if (printStackTrace) ex.printStackTrace();
            }
        }
    }

    @Override
    public void fatal(Object... message) {
        for (ILogger logger : loggers) {
            try {
                logger.fatal(message);
            } catch (Throwable ex) {
                if (printStackTrace) ex.printStackTrace();
            }
        }
    }

    @Override
    public void fatal(String message, Object... params) {
        for (ILogger logger : loggers) {
            try {
                logger.fatal(message, params);
            } catch (Throwable ex) {
                if (printStackTrace) ex.printStackTrace();
            }
        }
    }

}
