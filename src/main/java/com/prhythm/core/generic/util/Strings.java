package com.prhythm.core.generic.util;

import com.prhythm.core.generic.exception.RecessiveException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.Arrays;

/**
 * 文字類工具
 * Created by nanashi07 on 15/7/7.
 */
public final class Strings {

    /**
     * 檢查字串是否為 null 或空字串
     *
     * @param text
     * @return
     */
    public static boolean isNullOrEmpty(String text) {
        return text == null || text.length() == 0;
    }

    /**
     * 檢查字串是否為 null 是空白字元字串
     *
     * @param text
     * @return
     */
    public static boolean isNullOrWhiteSpace(String text) {
        return text == null || text.trim().length() == 0;
    }

    /**
     * 替換文字內容（不使用{@link java.util.regex.Pattern}）
     *
     * @param content
     * @param pattern
     * @param replacement
     * @return
     */
    public static CharSequence replace(String content, String pattern, String replacement) {
        if (content == null) return null;

        while (content.contains(pattern)) {
            content = content.replace(pattern, replacement);
        }
        return content;
    }

    /**
     * 產生亂數字串
     *
     * @param pattern 用來產生字串的字元內容
     * @param length  字串長度
     * @return
     */
    public static CharSequence shuffle(String pattern, int length) {
        if (pattern == null || pattern.isEmpty()) return null;
        if (length < 1) throw new IllegalArgumentException();

        char[] chars = pattern.toCharArray();

        StringBuilder sb = new StringBuilder();

        while (length-- > 0) {
            sb.append(chars[new SecureRandom().nextInt(chars.length)]);
        }

        return sb;
    }

    /**
     * 以utf-8進行網址編碼
     *
     * @param url
     * @return
     */
    public static CharSequence encodeUrl(String url) {
        return encodeUrl(url, "utf-8");
    }

    /**
     * 網址編碼
     *
     * @param url
     * @param encoding
     * @return
     */
    public static CharSequence encodeUrl(String url, String encoding) {
        try {
            return URLEncoder.encode(url, encoding);
        } catch (UnsupportedEncodingException e) {
            throw new RecessiveException(e.getMessage(), e);
        }
    }

    /**
     * 以utf-8進行網址解碼
     *
     * @param url
     * @return
     */
    public static CharSequence decodeUrl(String url) {
        return decodeUrl(url, "utf-8");
    }

    /**
     * 網址解碼
     *
     * @param url
     * @param encoding
     * @return
     */
    public static CharSequence decodeUrl(String url, String encoding) {
        try {
            return URLDecoder.decode(url, encoding);
        } catch (UnsupportedEncodingException e) {
            throw new RecessiveException(e.getMessage(), e);
        }
    }

    /**
     * 字串組合
     * 改以 {@link Delimiters} 替代使用
     *
     * @param delimiter
     * @param array
     * @return
     */
    @Deprecated
    public static <T> CharSequence join(String delimiter, T... array) {
        return join(delimiter, Arrays.asList(array));
    }

    /**
     * 字串組合
     * 改以 {@link Delimiters} 替代使用
     *
     * @param delimiter
     * @param iterable
     * @return
     */
    @Deprecated
    public static CharSequence join(String delimiter, Iterable iterable) {
        StringBuilder sb = new StringBuilder();

        for (Object o : iterable) {
            sb.append(delimiter);
            sb.append(o);
        }

        return sb.length() < delimiter.length() ? sb : sb.substring(delimiter.length());
    }

    /**
     * 將{@link Throwable#printStackTrace()}轉為文字內容
     *
     * @param ex
     * @return
     */
    public static String stringify(Throwable ex) {
        if (ex == null) return null;

        try {
            StringWriter sw;
            PrintWriter pw;

            sw = new StringWriter();
            pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            pw.close();
            sw.close();

            return sw.toString();
        } catch (Throwable e) {
            throw new RecessiveException(e.getMessage(), e);
        }
    }

}
