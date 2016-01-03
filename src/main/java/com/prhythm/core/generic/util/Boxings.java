package com.prhythm.core.generic.util;

import java.util.Enumeration;
import java.util.Iterator;

/**
 * 轉換物件陣列為基礎型別陣列
 * Created by nanashi07 on 15/10/22.
 */
public class Boxings {

    public static boolean[] packBoolean(Boolean... values) {
        if (values == null) return null;
        boolean[] result = new boolean[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = values[i];
        }
        return result;
    }

    public static boolean[] packBoolean(Iterable<Boolean> values) {
        if (values == null) return null;

        int len = 0;
        for (Iterator<Boolean> iterator = values.iterator(); iterator.hasNext() && iterator.next() != null; len++) ;

        boolean[] result = new boolean[len];
        Iterator<Boolean> iterator = values.iterator();
        for (int i = 0; iterator.hasNext(); i++) {
            result[i] = iterator.next();
        }

        return result;
    }

    public static boolean[] packBoolean(Enumeration<Boolean> values) {
        if (values == null) return null;

        int len = 0;
        for (; values.hasMoreElements(); len++) values.nextElement();

        boolean[] result = new boolean[len];
        for (int i = 0; values.hasMoreElements(); i++) {
            result[i] = values.nextElement();
        }

        return result;
    }

    public static byte[] packByte(Byte... values) {
        if (values == null) return null;
        byte[] result = new byte[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = values[i];
        }
        return result;
    }

    public static byte[] packByte(Iterable<Byte> values) {
        if (values == null) return null;

        int len = 0;
        for (Iterator<Byte> iterator = values.iterator(); iterator.hasNext() && iterator.next() != null; len++) ;

        byte[] result = new byte[len];
        Iterator<Byte> iterator = values.iterator();
        for (int i = 0; iterator.hasNext(); i++) {
            result[i] = iterator.next();
        }

        return result;
    }

    public static byte[] packByte(Enumeration<Byte> values) {
        if (values == null) return null;

        int len = 0;
        for (; values.hasMoreElements(); len++) values.nextElement();

        byte[] result = new byte[len];
        for (int i = 0; values.hasMoreElements(); i++) {
            result[i] = values.nextElement();
        }

        return result;
    }

    public static short[] packShort(Short... values) {
        if (values == null) return null;
        short[] result = new short[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = values[i];
        }
        return result;
    }

    public static short[] packShort(Iterable<Short> values) {
        if (values == null) return null;

        int len = 0;
        for (Iterator<Short> iterator = values.iterator(); iterator.hasNext() && iterator.next() != null; len++) ;

        short[] result = new short[len];
        Iterator<Short> iterator = values.iterator();
        for (int i = 0; iterator.hasNext(); i++) {
            result[i] = iterator.next();
        }

        return result;
    }

    public static short[] packShort(Enumeration<Short> values) {
        if (values == null) return null;

        int len = 0;
        for (; values.hasMoreElements(); len++) values.nextElement();

        short[] result = new short[len];
        for (int i = 0; values.hasMoreElements(); i++) {
            result[i] = values.nextElement();
        }

        return result;
    }

    public static char[] packCharacter(Character... values) {
        if (values == null) return null;
        char[] result = new char[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = values[i];
        }
        return result;
    }

    public static char[] packCharacter(Iterable<Character> values) {
        if (values == null) return null;

        int len = 0;
        for (Iterator<Character> iterator = values.iterator(); iterator.hasNext() && iterator.next() != null; len++) ;

        char[] result = new char[len];
        Iterator<Character> iterator = values.iterator();
        for (int i = 0; iterator.hasNext(); i++) {
            result[i] = iterator.next();
        }

        return result;
    }

    public static char[] packCharacter(Enumeration<Character> values) {
        if (values == null) return null;

        int len = 0;
        for (; values.hasMoreElements(); len++) values.nextElement();

        char[] result = new char[len];
        for (int i = 0; values.hasMoreElements(); i++) {
            result[i] = values.nextElement();
        }

        return result;
    }

    public static int[] packInteger(Integer... values) {
        if (values == null) return null;
        int[] result = new int[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = values[i];
        }
        return result;
    }

    public static int[] packInteger(Iterable<Integer> values) {
        if (values == null) return null;

        int len = 0;
        for (Iterator<Integer> iterator = values.iterator(); iterator.hasNext() && iterator.next() != null; len++) ;

        int[] result = new int[len];
        Iterator<Integer> iterator = values.iterator();
        for (int i = 0; iterator.hasNext(); i++) {
            result[i] = iterator.next();
        }

        return result;
    }

    public static int[] packInteger(Enumeration<Integer> values) {
        if (values == null) return null;

        int len = 0;
        for (; values.hasMoreElements(); len++) values.nextElement();

        int[] result = new int[len];
        for (int i = 0; values.hasMoreElements(); i++) {
            result[i] = values.nextElement();
        }

        return result;
    }

    public static long[] packLong(Long... values) {
        if (values == null) return null;
        long[] result = new long[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = values[i];
        }
        return result;
    }

    public static long[] packLong(Iterable<Long> values) {
        if (values == null) return null;

        int len = 0;
        for (Iterator<Long> iterator = values.iterator(); iterator.hasNext() && iterator.next() != null; len++) ;

        long[] result = new long[len];
        Iterator<Long> iterator = values.iterator();
        for (int i = 0; iterator.hasNext(); i++) {
            result[i] = iterator.next();
        }

        return result;
    }

    public static long[] packLong(Enumeration<Long> values) {
        if (values == null) return null;

        int len = 0;
        for (; values.hasMoreElements(); len++) values.nextElement();

        long[] result = new long[len];
        for (int i = 0; values.hasMoreElements(); i++) {
            result[i] = values.nextElement();
        }

        return result;
    }

    public static float[] packFloat(Float... values) {
        if (values == null) return null;
        float[] result = new float[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = values[i];
        }
        return result;
    }

    public static float[] packFloat(Iterable<Float> values) {
        if (values == null) return null;

        int len = 0;
        for (Iterator<Float> iterator = values.iterator(); iterator.hasNext() && iterator.next() != null; len++) ;

        float[] result = new float[len];
        Iterator<Float> iterator = values.iterator();
        for (int i = 0; iterator.hasNext(); i++) {
            result[i] = iterator.next();
        }

        return result;
    }

    public static float[] packFloat(Enumeration<Float> values) {
        if (values == null) return null;

        int len = 0;
        for (; values.hasMoreElements(); len++) values.nextElement();

        float[] result = new float[len];
        for (int i = 0; values.hasMoreElements(); i++) {
            result[i] = values.nextElement();
        }

        return result;
    }

    public static double[] packDouble(Double... values) {
        if (values == null) return null;
        double[] result = new double[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = values[i];
        }
        return result;
    }

    public static double[] packDouble(Iterable<Double> values) {
        if (values == null) return null;

        int len = 0;
        for (Iterator<Double> iterator = values.iterator(); iterator.hasNext() && iterator.next() != null; len++) ;

        double[] result = new double[len];
        Iterator<Double> iterator = values.iterator();
        for (int i = 0; iterator.hasNext(); i++) {
            result[i] = iterator.next();
        }

        return result;
    }

    public static double[] packDouble(Enumeration<Double> values) {
        if (values == null) return null;

        int len = 0;
        for (; values.hasMoreElements(); len++) values.nextElement();

        double[] result = new double[len];
        for (int i = 0; values.hasMoreElements(); i++) {
            result[i] = values.nextElement();
        }

        return result;
    }

}