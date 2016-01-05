package com.prhythm.app.remotetail.data;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.prhythm.app.remotetail.App;
import com.prhythm.app.remotetail.models.DataWrapper;
import com.prhythm.app.remotetail.models.LogPath;
import com.prhythm.app.remotetail.models.Server;
import com.prhythm.core.generic.data.Singleton;
import com.prhythm.core.generic.exception.RecessiveException;
import com.prhythm.core.generic.logging.Logs;
import com.prhythm.core.generic.util.Cube;
import com.prhythm.core.generic.util.Streams;
import javafx.beans.InvalidationListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 來源讀取
 * Created by nanashi07 on 16/1/4.
 */
public abstract class RemoteSourceReaderList extends Observable implements ObservableList<Line>, Runnable {

    final protected static long INTERVAL = TimeUnit.SECONDS.toMillis(2);

    transient final Server server;
    transient final LogPath path;

    /**
     * 待讀取的行
     */
    final Set<Integer> linesToRead = Cube.newConcurrentHashSet();
    boolean stopReadTask = false;

    transient InvalidationListener invalidationListener;

    public RemoteSourceReaderList(Server server, LogPath logPath) {
        this.server = server;
        this.path = logPath;
    }

    @Override
    public void addListener(InvalidationListener listener) {
        this.invalidationListener = listener;
    }

    @Override
    public void removeListener(InvalidationListener listener) {

    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public ListIterator<Line> listIterator() {
        return new ListIterator<Line>() {

            int index;

            @Override
            public boolean hasNext() {
                return index + 1 < size();
            }

            @Override
            public Line next() {
                return get(index++);
            }

            @Override
            public boolean hasPrevious() {
                return index - 1 > -1;
            }

            @Override
            public Line previous() {
                return get(index--);
            }

            @Override
            public int nextIndex() {
                return index + 1;
            }

            @Override
            public int previousIndex() {
                return index - 1;
            }

            @Deprecated
            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

            @Deprecated
            @Override
            public void set(Line line) {
                throw new UnsupportedOperationException();
            }

            @Deprecated
            @Override
            public void add(Line line) {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public void run() {
        while (!App.STOP_ALL_TASK && !stopReadTask) {
            if (!linesToRead.isEmpty()) {
                try {
                    readLines();
                } catch (Exception e) {
                    App.error(e.toString());
                    Logs.error("讀取 log 發生錯誤: %s", e);
                }
            }

            try {
                if (linesToRead.isEmpty()) Thread.sleep(INTERVAL);
            } catch (InterruptedException e) {
                Logs.error(RecessiveException.unwrapp(e));
            }
        }
    }

    /**
     * 讀取暫存表中的行數
     *
     * @throws IOException
     * @throws JSchException
     */
    void readLines() throws IOException, JSchException {
        Cube<Integer> values;
        // 有資料才處理
        if (linesToRead.isEmpty()) return;

        // 取得連續的行號
        values = Cube.from(linesToRead).orderBy().takeUntil(new Cube.Predicate<Integer>() {
            int previous;

            @Override
            public boolean predicate(Integer item, int index) {
                if (index == 0) {
                    previous = item;
                    return false;
                } else {
                    boolean b = previous + 1 != item;
                    previous = item;
                    return b;
                }
            }
        });

        // 無資料時不執行
        if (!values.any()) return;

        // 取得最小行號及最大行號
        int min = (int) Math.max(1, values.min());
        int max = (int) Math.max(1, values.max());

        // 連線
        synchronized (server) {
            if (!server.isConnected()) server.connect();
        }

        // 取得檔案內容
        ChannelExec channel = server.openChannel("exec");
        // 指令 sed : 顯示指定行的內容
        String cmd = String.format("sed -n %d,%dp %s", min, max, path);
        Logs.trace("讀取指定行(%s)", cmd);
        App.info("讀取 %s 由 %d 至 %d", path, min, max);
        channel.setCommand(cmd);
        InputStream in = channel.getInputStream();
        channel.connect();

        // 暫存資料
        List<String> lines = Streams.toLines(in, Singleton.of(DataWrapper.class).getPreference().getCharset());
        channel.disconnect();

        for (int i = 0; i < lines.size(); i++) {
            synchronized (path) {
                path.addLine(i + min, lines.get(i));
            }
        }

        // 移除已讀取內容
        linesToRead.removeAll(values.toSet());

        // 通知顯示變更
        notifyObservers(path);

        // 更新畫面
        if (invalidationListener != null) invalidationListener.invalidated(this);
    }

    @Override
    protected void finalize() throws Throwable {
        stopReadTask = true;
        super.finalize();
    }

    // getter & setter

    public Server getServer() {
        return server;
    }

    public LogPath getPath() {
        return path;
    }

    // UnsupportedOperation

    @Deprecated
    @Override
    public void addListener(ListChangeListener<? super Line> listener) {
    }

    @Deprecated
    @Override
    public void removeListener(ListChangeListener<? super Line> listener) {

    }

    @SuppressWarnings("NullableProblems")
    @Deprecated
    @Override
    public Iterator<Line> iterator() {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("NullableProblems")
    @Deprecated
    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("NullableProblems")
    @Deprecated
    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("NullableProblems")
    @Deprecated
    @Override
    public ListIterator<Line> listIterator(int index) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    @Override
    public Line set(int index, Line element) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    @Override
    public void add(int index, Line element) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    @Override
    public Line remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    @Override
    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("NullableProblems")
    @Deprecated
    @Override
    public List<Line> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    @Override
    public boolean add(Line s) {
        // throw new UnsupportedOperationException();
        return true;
    }

    @Deprecated
    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    @Override
    public boolean addAll(Collection<? extends Line> c) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    @Override
    public boolean addAll(int index, Collection<? extends Line> c) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    @Override
    public boolean addAll(Line... elements) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    @Override
    public boolean setAll(Line... elements) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    @Override
    public boolean setAll(Collection<? extends Line> col) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    @Override
    public boolean removeAll(Line... elements) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    @Override
    public boolean retainAll(Line... elements) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    @Override
    public void remove(int from, int to) {
        throw new UnsupportedOperationException();
    }

}

