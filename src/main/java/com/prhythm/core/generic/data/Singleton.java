package com.prhythm.core.generic.data;

import com.prhythm.core.generic.exception.RecessiveException;

import java.util.HashSet;
import java.util.Set;

/**
 * 單一實例
 * Created by nanashi07 on 16/1/2.
 */
public abstract class Singleton {

    static Set<Object> values = new HashSet<>();

    public static <T> T of(Class<T> clazz) {
        if (clazz == null) return null;

        for (Object value : values) {
            if (clazz.isAssignableFrom(value.getClass()))
                //noinspection unchecked
                return (T) value;
        }

        try {
            T value = clazz.newInstance();
            values.add(value);
            return value;
        } catch (Exception e) {
            throw new RecessiveException(e.getMessage(), e);
        }
    }

    public static <T> T of(T obj) {
        if (obj == null) return null;

        //noinspection UnnecessaryLocalVariable
        Object o = obj;
        Class<?> clazz = o.getClass();
        for (Object value : values) {
            if (value.getClass() == clazz)
                //noinspection unchecked
                return (T) value;
        }

        values.add(obj);
        return obj;
    }

}
