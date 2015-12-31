package com.prhythm.core.generic.util;

import com.prhythm.core.generic.exception.RecessiveException;
import com.prhythm.core.generic.logging.Logs;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 串流({@link InputStream})處理工具
 * Created by nanashi07 on 15/7/13.
 */
public final class Streams {

    /**
     * 讀取串流並轉為byte array
     *
     * @param is
     * @return
     */
    public static byte[] toByteArray(InputStream is) {
        if (is == null) throw new IllegalArgumentException("Input can't be null");

        ByteArrayOutputStream baos = null;

        try {
            baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[128];
            int len;
            while ((len = is.read(buffer)) > 0) {
                baos.write(buffer, 0, len);
            }
            return baos.toByteArray();
        } catch (Throwable e) {
            throw new RecessiveException(e.getMessage(), e);
        } finally {
            if (baos != null) try {
                baos.close();
            } catch (IOException e) {
                Logs.error(e);
            }
            try {
                is.close();
            } catch (IOException e) {
                Logs.error(e);
            }
        }

    }

    /**
     * 以文字內容讀取
     *
     * @param is
     * @param encoding
     * @return
     */
    public static CharSequence toText(InputStream is, String encoding) {
        if (is == null) throw new IllegalArgumentException("Input can't be null");
        if (encoding == null) throw new IllegalArgumentException("Invalid encoding");

        // 系統斷行符號
        final String separator = System.getProperty("line.separator");

        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            isr = new InputStreamReader(is, encoding);
            br = new BufferedReader(isr);

            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(separator);
                sb.append(line);
            }

            if (sb.length() > 0) sb.deleteCharAt(0);
            return sb;
        } catch (Throwable e) {
            throw new RecessiveException(e.getMessage(), e);
        } finally {
            if (br != null) try {
                br.close();
            } catch (IOException e) {
                Logs.error(e);
            }
            if (isr != null) try {
                isr.close();
            } catch (IOException e) {
                Logs.error(e);
            }
            try {
                is.close();
            } catch (IOException e) {
                Logs.error(e);
            }
        }
    }

    /**
     * 以文字內容讀取
     *
     * @param is
     * @param encoding
     * @return
     */
    public static List<String> toLines(InputStream is, String encoding) {
        if (is == null) throw new IllegalArgumentException("Input can't be null");
        if (encoding == null) throw new IllegalArgumentException("Invalid encoding");

        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            isr = new InputStreamReader(is, encoding);
            br = new BufferedReader(isr);

            List<String> lines = new ArrayList<String>();

            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }

            return lines;
        } catch (Throwable e) {
            throw new RecessiveException(e.getMessage(), e);
        } finally {
            if (br != null) try {
                br.close();
            } catch (IOException e) {
                Logs.error(e);
            }
            if (isr != null) try {
                isr.close();
            } catch (IOException e) {
                Logs.error(e);
            }
            try {
                is.close();
            } catch (IOException e) {
                Logs.error(e);
            }
        }
    }

}