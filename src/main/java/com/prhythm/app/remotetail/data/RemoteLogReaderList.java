package com.prhythm.app.remotetail.data;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.prhythm.app.remotetail.App;
import com.prhythm.app.remotetail.models.LogPath;
import com.prhythm.app.remotetail.models.Server;
import com.prhythm.core.generic.data.Expirable;
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
 * Log 內容
 * Created by nanashi07 on 15/12/30.
 */
public class RemoteLogReaderList extends Observable implements ObservableList<Line>, Runnable {

    final long INTERVAL = TimeUnit.SECONDS.toMillis(2);

    transient final Server server;
    transient final LogPath path;

    /**
     * 檔案行數
     */
    transient Expirable<Integer> lineCount = new Expirable<Integer>(INTERVAL) {
        @Override
        protected Integer get() throws Exception {
            synchronized (server) {
                if (!server.isConnected()) server.connect();
            }
            ChannelExec exec = server.openChannel("exec");
            // 指令 wc : 計算檔案行數
            String cmd = String.format("wc -l %s", path);
            Logs.trace("計算行數(%s)", cmd);
            exec.setCommand(cmd);
            InputStream in = exec.getInputStream();
            exec.connect();
            Scanner scanner = new Scanner(in);
            int size = scanner.nextInt();
            in.close();
            exec.disconnect();
            return size;
        }
    };

    /**
     * 待讀取的行
     */
    transient final Set<Integer> linesToRead = Cube.newConcurrentHashSet();
    boolean stopReadTask = false;

    transient InvalidationListener invalidationListener;

    public RemoteLogReaderList(Server server, LogPath logPath) {
        this.server = server;
        this.path = logPath;

        // 起始讀取作業
        new Thread(this).start();
    }

    @Override
    public void addListener(ListChangeListener<? super Line> listener) {
    }

    @Override
    public void removeListener(ListChangeListener<? super Line> listener) {

    }

    @Override
    public void addListener(InvalidationListener listener) {
        this.invalidationListener = listener;
    }

    @Override
    public void removeListener(InvalidationListener listener) {

    }

    @Override
    public int size() {
        return lineCount.value();
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public Line get(int index) {
        if (path.hasLine(index)) {
            // fixme cache line obj
            return new Line(index, path.atLine(index), true);
        } else {
            linesToRead.add(index);
            // fixme cache line obj
            Line line = new Line(index, null, false);
            addObserver(line);
            return line;
        }
    }

    @Override
    public void run() {
        while (!App.STOP_ALL_TASK && !stopReadTask) {
            if (!linesToRead.isEmpty()) {
                try {
                    readLines();
                } catch (Exception e) {
                    Logs.error("讀取 log 發生錯誤: %s", e);
                }
            }

            try {
                Thread.sleep(INTERVAL);
            } catch (InterruptedException e) {
                Logs.error(e);
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
        int min = (int) values.min();
        int max = (int) values.max();

        // 取得檔案內容
        synchronized (server) {
            if (!server.isConnected()) server.connect();
        }
        ChannelExec channel = server.openChannel("exec");
        // 指令 sed : 顯示指定行的內容
        String cmd = String.format("sed -n %d,%dp %s", min, max, path);
        Logs.trace("讀取指定行(%s)", cmd);
        channel.setCommand(cmd);
        InputStream in = channel.getInputStream();
        channel.connect();

        // 暫存資料
        List<String> lines = Streams.toLines(in, "utf-8");
        for (int i = 0; i < lines.size(); i++) {
            path.addLine(i + min, lines.get(i));
        }

        // 移除已讀取內容
        linesToRead.removeAll(values.toSet());

        channel.disconnect();

        // 通知顯示變更
        notifyObservers(path);

        if (invalidationListener != null) invalidationListener.invalidated(this);
    }

    @Override
    protected void finalize() throws Throwable {
        stopReadTask = true;
        super.finalize();
    }

    // UnsupportedOperation

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
    public ListIterator<Line> listIterator() {
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
    public int indexOf(Object o) {
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
        throw new UnsupportedOperationException();
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
