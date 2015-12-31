package com.prhythm.core.generic.util;

import java.util.*;

/**
 * Join 與 split 功能
 * Created by nanashi07 on 15/10/28.
 */
public class Delimiters {

    /**
     * 建立以分割符號的處理器
     *
     * @param delimiter 分割符號
     * @return
     */
    public static Delimiters with(String delimiter) {
        if (delimiter == null || delimiter.length() == 0) throw new IllegalArgumentException();
        return new Delimiters(delimiter);
    }

    String delimiter;

    Delimiters(String delimiter) {
        this.delimiter = delimiter;
    }

    /**
     * 分割
     *
     * @param text
     * @return
     */
    public List<String> split(CharSequence text) {
        if (text == null) throw new IllegalArgumentException();

        StringTokenizer tokenizer = new StringTokenizer(text.toString(), delimiter);
        List<String> list = new ArrayList<String>();
        for (; tokenizer.hasMoreElements(); ) {
            list.add(tokenizer.nextToken());
        }
        return list;
    }

    /**
     * 合併
     *
     * @param values
     * @param <T>
     * @return
     */
    public <T> CharSequence join(T... values) {
        StringBuilder sb = new StringBuilder();
        for (T value : values) {
            sb.append(delimiter).append(value);
        }
        if (sb.length() >= delimiter.length()) sb.delete(0, delimiter.length());
        return sb;
    }

    /**
     * 合併
     *
     * @param values
     * @param <T>
     * @return
     */
    public <T> CharSequence join(Iterable<T> values) {
        if (values == null) throw new IllegalArgumentException();

        StringBuilder sb = new StringBuilder();
        for (Iterator<T> iterator = values.iterator(); iterator.hasNext(); ) {
            sb.append(delimiter).append(iterator.next());
        }
        if (sb.length() >= delimiter.length()) sb.delete(0, delimiter.length());
        return sb;
    }

    /**
     * 合併
     *
     * @param values
     * @param <T>
     * @return
     */
    public <T> CharSequence join(Enumeration<T> values) {
        if (values == null) throw new IllegalArgumentException();

        StringBuilder sb = new StringBuilder();
        for (; values.hasMoreElements(); ) {
            sb.append(delimiter).append(values.nextElement());
        }
        if (sb.length() >= delimiter.length()) sb.delete(0, delimiter.length());
        return sb;
    }

}
