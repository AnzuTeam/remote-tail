package com.prhythm.app.remotetail.data;

import com.jcraft.jsch.ChannelExec;
import com.prhythm.app.remotetail.models.LogPath;
import com.prhythm.app.remotetail.models.Server;
import com.prhythm.core.generic.exception.RecessiveException;
import javafx.beans.InvalidationListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.io.InputStream;
import java.util.*;

/**
 * Created by nanashi07 on 15/12/30.
 */
public class RemoteLogReaderList implements ObservableList<String> {

    Server server;
    LogPath logPath;

    public RemoteLogReaderList(Server server, LogPath logPath) {
        this.server = server;
        this.logPath = logPath;
    }

    @Override
    public void addListener(ListChangeListener<? super String> listener) {

    }

    @Override
    public void removeListener(ListChangeListener<? super String> listener) {

    }

    @Override
    public void addListener(InvalidationListener listener) {

    }

    @Override
    public void removeListener(InvalidationListener listener) {

    }

    @Override
    public int size() {
        try {
            if (!server.isConnected()) server.connect();
            ChannelExec exec = (ChannelExec) server.openChannel("exec");
            exec.setCommand(String.format("wc -l %s", logPath));
            InputStream in = exec.getInputStream();
            exec.connect();
            Scanner scanner = new Scanner(in);
            int size = scanner.nextInt();
            exec.disconnect();
            return size;
        } catch (Throwable e) {
            throw new RecessiveException(e.getMessage(), e);
        }
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public String get(int index) {
        return String.valueOf(index);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        server = null;
        logPath = null;
    }

    // UnsupportedOperation

    @SuppressWarnings("NullableProblems")
    @Deprecated
    @Override
    public Iterator<String> iterator() {
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
    public ListIterator<String> listIterator() {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("NullableProblems")
    @Deprecated
    @Override
    public ListIterator<String> listIterator(int index) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    @Override
    public String set(int index, String element) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    @Override
    public void add(int index, String element) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    @Override
    public String remove(int index) {
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
    public List<String> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    @Override
    public boolean add(String s) {
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
    public boolean addAll(Collection<? extends String> c) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    @Override
    public boolean addAll(int index, Collection<? extends String> c) {
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
    public boolean addAll(String... elements) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    @Override
    public boolean setAll(String... elements) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    @Override
    public boolean setAll(Collection<? extends String> col) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    @Override
    public boolean removeAll(String... elements) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    @Override
    public boolean retainAll(String... elements) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    @Override
    public void remove(int from, int to) {
        throw new UnsupportedOperationException();
    }

}
