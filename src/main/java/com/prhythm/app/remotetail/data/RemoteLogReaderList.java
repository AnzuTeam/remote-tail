package com.prhythm.app.remotetail.data;

import com.jcraft.jsch.ChannelExec;
import com.prhythm.app.remotetail.models.LogPath;
import com.prhythm.app.remotetail.models.Server;
import com.prhythm.core.generic.data.Expirable;
import com.prhythm.core.generic.logging.Logs;
import javafx.beans.InvalidationListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.io.InputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Log 內容
 * Created by nanashi07 on 15/12/30.
 */
public class RemoteLogReaderList implements ObservableList<Line> {

    final Server server;
    final LogPath logPath;

    Expirable<Integer> lineCount = new Expirable<Integer>(TimeUnit.SECONDS.toMillis(1)) {
        @Override
        protected Integer get() throws Exception {
            synchronized (server) {
                if (!server.isConnected()) server.connect();
            }
            ChannelExec exec = (ChannelExec) server.openChannel("exec");
            // 指令 wc : 計算檔案行數
            String cmd = String.format("wc -l %s", logPath);
            Logs.trace(cmd);
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

    public RemoteLogReaderList(Server server, LogPath logPath) {
        this.server = server;
        this.logPath = logPath;
    }

    @Override
    public void addListener(ListChangeListener<? super Line> listener) {

    }

    @Override
    public void removeListener(ListChangeListener<? super Line> listener) {

    }

    @Override
    public void addListener(InvalidationListener listener) {

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
        return new Line(index, String.valueOf(index));
    }

    @Override
    protected void finalize() throws Throwable {
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
