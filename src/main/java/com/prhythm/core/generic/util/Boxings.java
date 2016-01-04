package com.prhythm.core.generic.util;

import java.util.*;

/**
 * 轉換物件陣列為基礎型別陣列
 * Created by nanashi07 on 15/10/22.
 */
public class Boxings {

    public static boolean[] packBoolean(Boolean... values) {
        return packBoolean(false, values);
    }

    public static boolean[] packBoolean(boolean null2false, Boolean... values) {
        return packBoolean(null2false, Arrays.asList(values));
    }

    public static boolean[] packBoolean(Enumeration<Boolean> values) {
        return packBoolean(false, values);
    }

    public static boolean[] packBoolean(boolean null2false, Enumeration<Boolean> values) {
        if (values == null) return null;

        List<Boolean> result = new ArrayList<Boolean>();
        for (; values.hasMoreElements(); ) {
            result.add(values.nextElement());
        }

        return packBoolean(null2false, result);
    }

    public static boolean[] packBoolean(Iterable<Boolean> values) {
        return packBoolean(true, values);
    }

    public static boolean[] packBoolean(boolean null2false, Iterable<Boolean> values) {
        if (values == null) return null;

        int len = 0;
        for (Boolean value : values) {
            if (value == null && !null2false) continue;
            len++;
        }

        boolean[] result = new boolean[len];
        Iterator<Boolean> iterator = values.iterator();
        for (int i = 0; iterator.hasNext(); ) {
            Boolean value = iterator.next();
            if (value == null) {
                if (null2false) {
                    result[i++] = false;
                }
            } else {
                result[i++] = value;
            }
        }

        return result;
    }

    public static byte[] packByte(Byte... values) {
        return packByte(false, values);
    }

    public static byte[] packByte(boolean null2zero, Byte... values) {
        return packByte(null2zero, Arrays.asList(values));
    }

    public static byte[] packByte(Enumeration<Byte> values) {
        return packByte(false, values);
    }

    public static byte[] packByte(boolean null2zero, Enumeration<Byte> values) {
        if (values == null) return null;

        List<Byte> result = new ArrayList<Byte>();
        for (; values.hasMoreElements(); ) {
            result.add(values.nextElement());
        }

        return packByte(null2zero, result);
    }

    public static byte[] packByte(Iterable<Byte> values) {
        return packByte(true, values);
    }

    public static byte[] packByte(boolean null2zero, Iterable<Byte> values) {
        if (values == null) return null;

        int len = 0;
        for (Byte value : values) {
            if (value == null && !null2zero) continue;
            len++;
        }

        byte[] result = new byte[len];
        Iterator<Byte> iterator = values.iterator();
        for (int i = 0; iterator.hasNext(); ) {
            Byte value = iterator.next();
            if (value == null) {
                if (null2zero) {
                    result[i++] = 0;
                }
            } else {
                result[i++] = value;
            }
        }

        return result;
    }

    public static char[] packCharacter(Character... values) {
        return packCharacter(false, values);
    }

    public static char[] packCharacter(boolean null2zero, Character... values) {
        return packCharacter(null2zero, Arrays.asList(values));
    }

    public static char[] packCharacter(Enumeration<Character> values) {
        return packCharacter(false, values);
    }

    public static char[] packCharacter(boolean null2zero, Enumeration<Character> values) {
        if (values == null) return null;

        List<Character> result = new ArrayList<Character>();
        for (; values.hasMoreElements(); ) {
            result.add(values.nextElement());
        }

        return packCharacter(null2zero, result);
    }

    public static char[] packCharacter(Iterable<Character> values) {
        return packCharacter(true, values);
    }

    public static char[] packCharacter(boolean null2zero, Iterable<Character> values) {
        if (values == null) return null;

        int len = 0;
        for (Character value : values) {
            if (value == null && !null2zero) continue;
            len++;
        }

        char[] result = new char[len];
        Iterator<Character> iterator = values.iterator();
        for (int i = 0; iterator.hasNext(); ) {
            Character value = iterator.next();
            if (value == null) {
                if (null2zero) {
                    result[i++] = 0;
                }
            } else {
                result[i++] = value;
            }
        }

        return result;
    }

    public static short[] packShort(Short... values) {
        return packShort(false, values);
    }

    public static short[] packShort(boolean null2zero, Short... values) {
        return packShort(null2zero, Arrays.asList(values));
    }

    public static short[] packShort(Enumeration<Short> values) {
        return packShort(false, values);
    }

    public static short[] packShort(boolean null2zero, Enumeration<Short> values) {
        if (values == null) return null;

        List<Short> result = new ArrayList<Short>();
        for (; values.hasMoreElements(); ) {
            result.add(values.nextElement());
        }

        return packShort(null2zero, result);
    }

    public static short[] packShort(Iterable<Short> values) {
        return packShort(true, values);
    }

    public static short[] packShort(boolean null2zero, Iterable<Short> values) {
        if (values == null) return null;

        int len = 0;
        for (Short value : values) {
            if (value == null && !null2zero) continue;
            len++;
        }

        short[] result = new short[len];
        Iterator<Short> iterator = values.iterator();
        for (int i = 0; iterator.hasNext(); ) {
            Short value = iterator.next();
            if (value == null) {
                if (null2zero) {
                    result[i++] = 0;
                }
            } else {
                result[i++] = value;
            }
        }

        return result;
    }

    public static int[] packInteger(Integer... values) {
        return packInteger(false, values);
    }

    public static int[] packInteger(boolean null2zero, Integer... values) {
        return packInteger(null2zero, Arrays.asList(values));
    }

    public static int[] packInteger(Enumeration<Integer> values) {
        return packInteger(false, values);
    }

    public static int[] packInteger(boolean null2zero, Enumeration<Integer> values) {
        if (values == null) return null;

        List<Integer> result = new ArrayList<Integer>();
        for (; values.hasMoreElements(); ) {
            result.add(values.nextElement());
        }

        return packInteger(null2zero, result);
    }

    public static int[] packInteger(Iterable<Integer> values) {
        return packInteger(true, values);
    }

    public static int[] packInteger(boolean null2zero, Iterable<Integer> values) {
        if (values == null) return null;

        int len = 0;
        for (Integer value : values) {
            if (value == null && !null2zero) continue;
            len++;
        }

        int[] result = new int[len];
        Iterator<Integer> iterator = values.iterator();
        for (int i = 0; iterator.hasNext(); ) {
            Integer value = iterator.next();
            if (value == null) {
                if (null2zero) {
                    result[i++] = 0;
                }
            } else {
                result[i++] = value;
            }
        }

        return result;
    }

    public static long[] packLong(Long... values) {
        return packLong(false, values);
    }

    public static long[] packLong(boolean null2zero, Long... values) {
        return packLong(null2zero, Arrays.asList(values));
    }

    public static long[] packLong(Enumeration<Long> values) {
        return packLong(false, values);
    }

    public static long[] packLong(boolean null2zero, Enumeration<Long> values) {
        if (values == null) return null;

        List<Long> result = new ArrayList<Long>();
        for (; values.hasMoreElements(); ) {
            result.add(values.nextElement());
        }

        return packLong(null2zero, result);
    }

    public static long[] packLong(Iterable<Long> values) {
        return packLong(true, values);
    }

    public static long[] packLong(boolean null2zero, Iterable<Long> values) {
        if (values == null) return null;

        int len = 0;
        for (Long value : values) {
            if (value == null && !null2zero) continue;
            len++;
        }

        long[] result = new long[len];
        Iterator<Long> iterator = values.iterator();
        for (int i = 0; iterator.hasNext(); ) {
            Long value = iterator.next();
            if (value == null) {
                if (null2zero) {
                    result[i++] = 0;
                }
            } else {
                result[i++] = value;
            }
        }

        return result;
    }

    public static float[] packFloat(Float... values) {
        return packFloat(false, values);
    }

    public static float[] packFloat(boolean null2zero, Float... values) {
        return packFloat(null2zero, Arrays.asList(values));
    }

    public static float[] packFloat(Enumeration<Float> values) {
        return packFloat(false, values);
    }

    public static float[] packFloat(boolean null2zero, Enumeration<Float> values) {
        if (values == null) return null;

        List<Float> result = new ArrayList<Float>();
        for (; values.hasMoreElements(); ) {
            result.add(values.nextElement());
        }

        return packFloat(null2zero, result);
    }

    public static float[] packFloat(Iterable<Float> values) {
        return packFloat(true, values);
    }

    public static float[] packFloat(boolean null2zero, Iterable<Float> values) {
        if (values == null) return null;

        int len = 0;
        for (Float value : values) {
            if (value == null && !null2zero) continue;
            len++;
        }

        float[] result = new float[len];
        Iterator<Float> iterator = values.iterator();
        for (int i = 0; iterator.hasNext(); ) {
            Float value = iterator.next();
            if (value == null) {
                if (null2zero) {
                    result[i++] = 0;
                }
            } else {
                result[i++] = value;
            }
        }

        return result;
    }

    public static double[] packDouble(Double... values) {
        return packDouble(false, values);
    }

    public static double[] packDouble(boolean null2zero, Double... values) {
        return packDouble(null2zero, Arrays.asList(values));
    }

    public static double[] packDouble(Enumeration<Double> values) {
        return packDouble(false, values);
    }

    public static double[] packDouble(boolean null2zero, Enumeration<Double> values) {
        if (values == null) return null;

        List<Double> result = new ArrayList<Double>();
        for (; values.hasMoreElements(); ) {
            result.add(values.nextElement());
        }

        return packDouble(null2zero, result);
    }

    public static double[] packDouble(Iterable<Double> values) {
        return packDouble(true, values);
    }

    public static double[] packDouble(boolean null2zero, Iterable<Double> values) {
        if (values == null) return null;

        int len = 0;
        for (Double value : values) {
            if (value == null && !null2zero) continue;
            len++;
        }

        double[] result = new double[len];
        Iterator<Double> iterator = values.iterator();
        for (int i = 0; iterator.hasNext(); ) {
            Double value = iterator.next();
            if (value == null) {
                if (null2zero) {
                    result[i++] = 0;
                }
            } else {
                result[i++] = value;
            }
        }

        return result;
    }

}