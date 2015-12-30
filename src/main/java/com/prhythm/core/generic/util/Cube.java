package com.prhythm.core.generic.util;

import java.lang.reflect.Array;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.*;

/**
 * 以 List 為基礎的集合處理工具。
 * Created by nanashi07 on 15/9/21.
 */
public class Cube<E> implements Iterable<E> {

    private static class NotImplementedException extends RuntimeException {

    }

    private static class Content<T> {
        T value;

        public T value() {
            return value;
        }

        public void value(T value) {
            this.value = value;
        }

    }

    /**
     * 處理布林值邏輯介面
     *
     * @param <I>
     */
    public interface Predicate<I> {

        boolean predicate(I item, int index);

    }

    /**
     * 處理數值邏輯介面
     *
     * @param <I>
     */
    public interface Calculator<I> {

        double calculate(I item, int index);

    }

    /**
     * 處理相等邏輯介面
     *
     * @param <I>
     */
    public interface Equality<I> {

        boolean equals(I source, I target);

    }

    /**
     * 處理比對邏輯介面
     *
     * @param <I>
     */
    public interface Comparator<I> {

        int compareTo(I source, I target);

    }

    /**
     * 查詢條件
     *
     * @param <T>
     */
    public static class Selection<T> implements Predicate<T>, Calculator<T>, Equality<T>, Comparator<T> {

        public boolean predicate(T item, int index) {
            throw new NotImplementedException();
        }

        public double calculate(T item, int index) {
            throw new NotImplementedException();
        }

        public boolean equals(T source, T target) {
            throw new NotImplementedException();
        }

        public int compareTo(T source, T target) {
            throw new NotImplementedException();
        }

    }

    /**
     * 轉換處理介面
     *
     * @param <I>
     * @param <O>
     */
    public interface Convertible<I, O> {

        O transform(I item, int index);

    }

    /**
     * 分類處理介面
     *
     * @param <I>
     * @param <O>
     */
    public interface Classify<I, O> {

        O groupBy(I item, int index);

    }

    /**
     * 轉換處理
     *
     * @param <I>
     * @param <O>
     */
    public static class Conversion<I, O> implements Convertible<I, O>, Classify<I, O> {

        public O transform(I item, int index) {
            throw new NotImplementedException();
        }

        public O groupBy(I item, int index) {
            throw new NotImplementedException();
        }

    }

    /**
     * 產生指定數量的項目集合
     * <p>範例</p>
     * <pre>{@code
     * Cube.forCount(10)
     * }</pre>
     * <p>輸出結果</p>
     * <pre>{@code
     * [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
     * }</pre>
     *
     * @param count 項目數
     * @return 由 0 至輸入數值 -1 的集合
     */
    public static Cube<Integer> forCount(int count) {
        Integer[] values = new Integer[count];
        for (int i = 0; i < count; i++) {
            values[i] = i;
        }
        return from(values);
    }

    /**
     * 由陣列產生 {@link Cube}
     * 若與 {@link #from(Object[])} 重疊時，以 <code>new boolean[]</code> 輸入
     * <p>範例</p>
     * <pre>{@code
     * Cube.from(new boolean[]{true, false, true, false, false, true})
     * }</pre>
     * 或
     * <pre>{@code
     * Cube.from(true, false, true, false, false, true)
     * }</pre>
     * <p>輸出結果</p>
     * <pre>{@code
     * [true, false, true, false, false, true]
     * }</pre>
     *
     * @param data 布林值陣列
     * @return 布林值集合
     */
    public static Cube<Boolean> from(boolean... data) {
        Boolean[] values = new Boolean[data.length];
        for (int i = 0; i < data.length; i++) {
            values[i] = data[i];
        }
        return from(values);
    }

    /**
     * 由陣列產生 {@link Cube}
     * 若與 {@link #from(Object[])} 重疊時，以 <code>new byte[]</code> 輸入
     * <p>範例</p>
     * <pre>{@code
     * Cube.from(new byte[]{1, 2, 3, 4, 5, 6})
     * }</pre>
     * 或
     * <pre>{@code
     * Cube.from((byte)1, (byte)2, (byte)3, (byte)4, (byte)5, (byte)6)
     * }</pre>
     * <p>輸出結果</p>
     * <pre>{@code
     * [1, 2, 3, 4, 5, 6]
     * }</pre>
     *
     * @param data 位元數陣列
     * @return 位元集合
     */
    public static Cube<Byte> from(byte... data) {
        Byte[] values = new Byte[data.length];
        for (int i = 0; i < data.length; i++) {
            values[i] = data[i];
        }
        return from(values);
    }

    /**
     * 由陣列產生 {@link Cube}
     * 若與 {@link #from(Object[])} 重疊時，以 <code>new short[]</code> 輸入
     * <p>範例</p>
     * <pre>{@code
     * Cube.from(new short[]{1, 2, 3, 4, 5, 6})
     * }</pre>
     * 或
     * <pre>{@code
     * Cube.from((short)1, (short)2, (short)3, (short)4, (short)5, (short)6)
     * }</pre>
     * <p>輸出結果</p>
     * <pre>{@code
     * [1, 2, 3, 4, 5, 6]
     * }</pre>
     *
     * @param data 短整數陣列
     * @return 短整數集合
     */
    public static Cube<Short> from(short... data) {
        Short[] values = new Short[data.length];
        for (int i = 0; i < data.length; i++) {
            values[i] = data[i];
        }
        return from(values);
    }

    /**
     * 由陣列產生 {@link Cube}
     * 若與 {@link #from(Object[])} 重疊時，以 <code>new int[]</code> 輸入
     * <p>範例</p>
     * <pre>{@code
     * Cube.from(new int[]{1, 2, 3, 4, 5, 6})
     * }</pre>
     * 或
     * <pre>{@code
     * Cube.from(1, 2, 3, 4, 5, 6)
     * }</pre>
     * <p>輸出結果</p>
     * <pre>{@code
     * [1, 2, 3, 4, 5, 6]
     * }</pre>
     *
     * @param data 整數陣列
     * @return 整數集合
     */
    public static Cube<Integer> from(int... data) {
        Integer[] values = new Integer[data.length];
        for (int i = 0; i < data.length; i++) {
            values[i] = data[i];
        }
        return from(values);
    }

    /**
     * 由陣列產生 {@link Cube}
     * 若與 {@link #from(Object[])} 重疊時，以 <code>new long[]</code> 輸入
     * <p>範例</p>
     * <pre>{@code
     * Cube.from(new long[]{1L, 2L, 3L, 4L, 5L, 6L})
     * }</pre>
     * 或
     * <pre>{@code
     * Cube.from(1L, 2L, 3L, 4L, 5L, 6L)
     * }</pre>
     * <p>輸出結果</p>
     * <pre>{@code
     * [1, 2, 3, 4, 5, 6]
     * }</pre>
     *
     * @param data 長整數陣列
     * @return 長整數集合
     */
    public static Cube<Long> from(long... data) {
        Long[] values = new Long[data.length];
        for (int i = 0; i < data.length; i++) {
            values[i] = data[i];
        }
        return from(values);
    }

    /**
     * 由陣列產生 {@link Cube}
     * 若與 {@link #from(Object[])} 重疊時，以 <code>new float[]</code> 輸入
     * <p>範例</p>
     * <pre>{@code
     * Cube.from(new float[]{1f, 2f, 3f, 4f, 5f, 6f})
     * }</pre>
     * 或
     * <pre>{@code
     * Cube.from(1f, 2f, 3f, 4f, 5f, 6f)
     * }</pre>
     * <p>輸出結果</p>
     * <pre>{@code
     * [1.0, 2.0, 3.0, 4.0, 5.0, 6.0]
     * }</pre>
     *
     * @param data 浮點數陣列
     * @return 浮點數集合
     */
    public static Cube<Float> from(float... data) {
        Float[] values = new Float[data.length];
        for (int i = 0; i < data.length; i++) {
            values[i] = data[i];
        }
        return from(values);
    }

    /**
     * 由陣列產生 {@link Cube}
     * 若與 {@link #from(Object[])} 重疊時，以 <code>new double[]</code> 輸入
     * <p>範例</p>
     * <pre>{@code
     * Cube.from(new double[]{1.0, 2.0, 3.0, 4.0, 5.0, 6.0})
     * }</pre>
     * 或
     * <pre>{@code
     * Cube.from(1.0, 2.0, 3.0, 4.0, 5.0, 6.0)
     * }</pre>
     * <p>輸出結果</p>
     * <pre>{@code
     * [1.0, 2.0, 3.0, 4.0, 5.0, 6.0]
     * }</pre>
     *
     * @param data 雙精度浮點數陣列
     * @return 雙精度浮點數集合
     */
    public static Cube<Double> from(double... data) {
        Double[] values = new Double[data.length];
        for (int i = 0; i < data.length; i++) {
            values[i] = data[i];
        }
        return from(values);
    }

    /**
     * 由陣列產生 {@link Cube}
     * 若與 {@link #from(Object[])} 重疊時，以 <code>new char[]</code> 輸入
     * <p>範例</p>
     * <pre>{@code
     * Cube.from(new char[]{'a', 'b', 'c', 'd', 'e'})
     * }</pre>
     * 或
     * <pre>{@code
     * Cube.from('a', 'b', 'c', 'd', 'e')
     * }</pre>
     * <p>輸出結果</p>
     * <pre>{@code
     * [a, b, c, d, e]
     * }</pre>
     *
     * @param data 字元陣列
     * @return 字元集合
     */
    public static Cube<Character> from(char... data) {
        Character[] values = new Character[data.length];
        for (int i = 0; i < data.length; i++) {
            values[i] = data[i];
        }
        return from(values);
    }

    /**
     * 由陣列產生 {@link Cube}
     * <p>範例</p>
     * <pre>{@code
     * Cube.from("US", "Japan", "Taiwan")
     * }</pre>
     * <p>輸出結果</p>
     * <pre>{@code
     * [US, Japan, Taiwan]
     * }</pre>
     *
     * @param data 物件陣列
     * @param <T>  項目型別
     * @return 物件集合
     */
    @SafeVarargs
    public static <T> Cube<T> from(T... data) {
        return new Cube<T>(data);
    }

    /**
     * 由 {@link Iterable} 集合產生 {@link Cube}
     *
     * @param data 集合物件
     * @param <T>  項目型別
     * @return 物件集合
     */
    public static <T> Cube<T> from(Iterable<T> data) {
        return new Cube<T>(data);
    }

    /**
     * 由 {@link Enumeration} 產生 {@link Cube}
     *
     * @param data 集合物件
     * @param <T>  項目型別
     * @return 物件集合
     */
    public static <T> Cube<T> from(Enumeration<T> data) {
        List<T> values = newArrayList();
        while (data.hasMoreElements()) {
            values.add(data.nextElement());
        }
        return from(values);
    }

    /**
     * 取得集合包含的項目數
     *
     * @param iterable 集合物件
     * @return 集合項目數
     */
    public static int size(Iterable iterable) {
        if (iterable == null) throw new IllegalArgumentException();
        if (iterable instanceof Collection) {
            return ((Collection) iterable).size();
        } else {
            Iterator iterator = iterable.iterator();
            int i;
            for (i = 0; iterator.hasNext(); i++) {
                iterator.next();
            }
            return i;
        }
    }

    /**
     * 取得集合包含的項目數
     *
     * @param enumeration 集合物件
     * @return 集合項目數
     */
    public static int size(Enumeration enumeration) {
        int i;
        for (i = 0; enumeration.hasMoreElements(); i++) {
            enumeration.nextElement();
        }
        return i;
    }

    /**
     * 建立一個空的 {@link Cube}
     *
     * @param <T> 項目型別
     * @return 空的集合
     */
    public static <T> Cube<T> emptyCube() {
        return new Cube<T>();
    }

    /**
     * 產生一個空的 {@link List}，取得的 {@link List} 為無法變更的集合
     *
     * @param <T> 項目型別
     * @return 項目數為 0 的 {@link List}
     * @see {@link Collections#emptyList()}
     */
    public static <T> List<T> emptyList() {
        return Collections.emptyList();
    }

    /**
     * 將陣列轉為 {@link List}
     *
     * @param values 物件陣列
     * @param <T>    項目型別
     * @return 物件集合
     * @see {@link Arrays#asList(Object[])}
     */
    @SafeVarargs
    public static <T> List<T> asList(T... values) {
        return new ArrayList<T>(Arrays.asList(values));
    }

    /**
     * 將 {@link Iterable} 集合轉為 {@link List}
     *
     * @param source 集合物件
     * @param <T>    項目型別
     * @return {@link List} 集合
     */
    public static <T> List<T> asList(Iterable<T> source) {
        if (source instanceof Cube) {
            return ((Cube<T>) source).toList();
        } else {
            List<T> list = newArrayList();
            if (source == null || !source.iterator().hasNext()) return list;
            for (T item : source) list.add(item);
            return list;
        }
    }

    /**
     * 將 {@link Enumeration} 集合轉為 {@link List}
     *
     * @param source 集合物件
     * @param <T>    項目型別
     * @return {@link List} 集合
     */
    public static <T> List<T> asList(Enumeration<T> source) {
        List<T> list = newArrayList();
        if (source == null) return list;
        for (; source.hasMoreElements(); ) list.add(source.nextElement());
        return list;
    }

    /**
     * 由輸入陣列產生一個新的 {@link ArrayList}
     *
     * @param values 物件陣列
     * @param <T>    項目型別
     * @return {@link ArrayList} 集合
     */
    @SafeVarargs
    public static <T> ArrayList<T> newArrayList(T... values) {
        /** {@link Arrays#asList(Object[])} 會產生一個不可變更的 {@link List} **/
        return new ArrayList<T>(Arrays.asList(values));
    }

    /**
     * 由輸入集合產生一個新的 {@link ArrayList}
     *
     * @param values 集合物件
     * @param <T>    項目型別
     * @return {@link ArrayList} 集合
     */
    public static <T> ArrayList<T> newArrayList(Iterable<T> values) {
        ArrayList<T> list = new ArrayList<T>();
        if (values == null) return list;
        for (T value : values) list.add(value);
        return list;
    }

    /**
     * 由輸入陣列產生一個新的 {@link LinkedList}
     *
     * @param values 物件陣列
     * @param <T>    項目型別
     * @return {@link LinkedList} 集合
     */
    @SafeVarargs
    public static <T> LinkedList<T> newLinkedList(T... values) {
        return new LinkedList<T>(Arrays.asList(values));
    }

    /**
     * 產生一個新的 {@link LinkedList}
     *
     * @param values 集合物件
     * @param <T>    項目型別
     * @return {@link LinkedList} 集合
     */
    public static <T> LinkedList<T> newLinkedList(Iterable<T> values) {
        LinkedList<T> list = new LinkedList<T>();
        if (values == null) return list;
        for (T value : values) list.add(value);
        return list;
    }

    /**
     * 產生一個新的 {@link CopyOnWriteArrayList}
     *
     * @param values 物件陣列
     * @param <T>    項目型別
     * @return {@link CopyOnWriteArrayList} 集合
     */
    @SafeVarargs
    public static <T> CopyOnWriteArrayList<T> newCopyOnWriteArrayList(T... values) {
        return new CopyOnWriteArrayList<T>(Arrays.asList(values));
    }

    /**
     * 產生一個新的 {@link CopyOnWriteArrayList}
     *
     * @param values 集合物件
     * @param <T>    項目型別
     * @return {@link CopyOnWriteArrayList} 集合
     */
    public static <T> CopyOnWriteArrayList<T> newCopyOnWriteArrayList(Iterable<T> values) {
        CopyOnWriteArrayList<T> list = new CopyOnWriteArrayList<T>();
        if (values == null) return list;
        for (T value : values) list.add(value);
        return list;
    }

    /**
     * 產生一個空的 {@link Set}，取得的 {@link Set} 為無法變更的集合
     *
     * @param <T> 項目型別
     * @return 項目數為 0 的 {@link Set}
     * @see {@link Collections#emptySet()}
     */
    public static <T> Set<T> emptySet() {
        return Collections.emptySet();
    }

    /**
     * 產生一個新的 {@link HashSet}
     *
     * @param values 物件陣列
     * @param <T>    項目型別
     * @return {@link HashSet} 集合
     */
    @SafeVarargs
    public static <T> HashSet<T> newHashSet(T... values) {
        return new HashSet<T>(Arrays.asList(values));
    }

    /**
     * 產生一個新的 {@link HashSet}
     *
     * @param values 集合物件
     * @param <T>    項目型別
     * @return {@link HashSet} 集合
     */
    public static <T> HashSet<T> newHashSet(Iterable<T> values) {
        HashSet<T> set = new HashSet<T>();
        if (values == null) return set;
        for (T value : values) set.add(value);
        return set;
    }

    /**
     * 以 {@link ConcurrentHashMap} 產生一個 concurrent set
     *
     * @param <T> 項目型別
     * @return {@link ConcurrentHashMap} 集合
     */
    public static <T> Set<T> newConcurrentHashSet() {
        return Collections.newSetFromMap(new ConcurrentHashMap<T, Boolean>());
    }

    /**
     * 產生一個空的 {@link Map}，取得的 {@link Map} 為無法變更的集合
     *
     * @param <K> 鍵值型別
     * @param <V> 項目型別
     * @return 項目數為 0 的 {@link Map}
     */
    public static <K, V> Map<K, V> emptyMap() {
        return new Map<K, V>() {
            public int size() {
                return 0;
            }

            public boolean isEmpty() {
                return true;
            }

            public boolean containsKey(Object key) {
                return false;
            }

            public boolean containsValue(Object value) {
                return false;
            }

            public V get(Object key) {
                throw new UnsupportedOperationException();
            }

            public V put(K key, V value) {
                throw new UnsupportedOperationException();
            }

            public V remove(Object key) {
                throw new UnsupportedOperationException();
            }

            public void putAll(Map<? extends K, ? extends V> m) {
                throw new UnsupportedOperationException();
            }

            public void clear() {
                throw new UnsupportedOperationException();
            }

            public Set<K> keySet() {
                return Collections.emptySet();
            }

            public Collection<V> values() {
                return Collections.emptyList();
            }

            public Set<Entry<K, V>> entrySet() {
                return Collections.emptySet();
            }
        };
    }

    /**
     * 產生一個新的 {@link HashMap}
     *
     * @param <K> 鍵值型別
     * @param <V> 項目型別
     * @return {@link HashMap} 集合
     */
    public static <K, V> HashMap<K, V> newHashMap() {
        return new HashMap<K, V>();
    }

    /**
     * 產生一個新的 {@link ConcurrentHashMap}
     *
     * @param <K> 鍵值型別
     * @param <V> 項目型別
     * @return {@link ConcurrentHashMap} 集合
     */
    public static <K, V> ConcurrentHashMap<K, V> newConcurrentHashMap() {
        return new ConcurrentHashMap<K, V>();
    }

    /**
     * 產生一個新的 {@link LinkedBlockingQueue}
     *
     * @param <T> 項目型別
     * @return {@link LinkedBlockingQueue} 集合
     */
    public static <T> LinkedBlockingQueue<T> newLinkedBlockingQueue() {
        return new LinkedBlockingQueue<T>();
    }

    Iterable<E> data;

    @SafeVarargs
    private Cube(E... data) {
        if (data == null || data.length == 0) {
            this.data = newArrayList();
        } else {
            this.data = asList(data);
        }
    }

    private Cube(Iterable<E> data) {
        if (data == null) {
            this.data = newArrayList();
        } else {
            if (data instanceof Cube) {
                this.data = ((Cube<E>) data).data;
            } else {
                this.data = data;
            }
        }
    }

    final public Iterator<E> iterator() {
        return data.iterator();
    }

    /**
     * 將 {@link Cube} 轉為 {@link List}
     * <p>範例</p>
     * <pre>{@code
     * List<String> list = Cube.from("US", "Japan", "Taiwan").toList();
     * }</pre>
     *
     * @return {@link List} 集合
     */
    final public List<E> toList() {
        if (data instanceof List) return (List<E>) data;
        List<E> list = newArrayList();
        for (E current : this) list.add(current);
        return list;
    }

    /**
     * 將 {@link Cube} 轉為 {@link Set}
     * <p>範例</p>
     * <pre>{@code
     * Set<String> set = Cube.from("US", "Japan", "Taiwan").toSet();
     * }</pre>
     *
     * @return {@link Set} 集合
     */
    final public Set<E> toSet() {
        if (data instanceof Set) return (Set<E>) data;
        Set<E> set = newHashSet();
        for (E current : this) set.add(current);
        return set;
    }

    /**
     * 將 {@link Cube} 轉為指定型別的陣列，轉換失敗時會拋出 {@link ClassCastException}
     * <p>範例</p>
     * <pre>{@code
     * String[] strings = Cube.from("US", "Japan", "Taiwan").toArray(String.class);
     * CharSequence[] charSequences = Cube.from("US", "Japan", "Taiwan").toArray(CharSequence.class);
     * }</pre>
     *
     * @param type 目標陣列的型別
     * @return 陣列
     * @throws ClassCastException
     */
    final public <O> O[] toArray(Class<O> type) {
        List<E> list = toList();
        O[] array = (O[]) Array.newInstance(type, list.size());
        for (int i = 0; i < list.size(); i++) {
            array[i] = type.cast(list.get(i));
        }
        return array;
    }

    /**
     * 檢查是否為空集合
     *
     * @return 符合條件的項目數為 0 時為 false，其餘為 true
     */
    final public boolean any() {
        return iterator().hasNext();
    }

    /**
     * 檢查是否含有符合條件的項目
     * <p>範例</p>
     * <pre>{@code
     * boolean exits = Cube.from("US", "Japan", "Taiwan").any(new Cube.Predicate<String>() {
     *     &#x40;Override
     *     public boolean predicate(String item, int index) {
     *         return item != null && item.startsWith("T");
     *     }
     * });
     * }</pre>
     * <p>輸出結果</p>
     * <pre>{@code
     * true
     * }</pre>
     *
     * @param adapter，符合條件，{@link Predicate#predicate(Object, int)}
     * @return 符合條件的項目數為 0 時為 false，其餘為 true
     */
    final public boolean any(Predicate<E> adapter) {
        if (adapter == null) return iterator().hasNext();

        Iterator<E> iterator = iterator();
        for (int i = 0; iterator.hasNext(); i++) {
            if (adapter.predicate(iterator.next(), i)) return true;
        }

        return false;
    }

    /**
     * 檢查集合內是否含有指定項目
     *
     * @param value 指定項目
     * @return 項目中含有指定項目時為 true，否則為 false
     * @see {@link Collection#contains(Object)}
     */
    final public boolean has(E value) {
        if (value == null) return false;

        if (data instanceof Collection) {
            return ((Collection) data).contains(value);
        } else {
            Iterator<E> iterator = iterator();
            for (; iterator.hasNext(); ) {
                E current = iterator.next();
                if (current != null && current.equals(value)) return true;
            }
        }

        return false;
    }

    /**
     * 取得項目數
     *
     * @return 項目數
     */
    final public int count() {
        return size(this);
    }

    /**
     * 計算符合條件的項目數
     * <p>範例</p>
     * <pre>{@code
     * int count = Cube.from("US", "Japan", "Taiwan").count(new Cube.Predicate<String>() {
     *     &#x40;Override
     *     public boolean predicate(String item, int index) {
     *         return item != null && item.startsWith("T");
     *     }
     * });
     * }</pre>
     * <p>輸出結果</p>
     * <pre>{@code
     * 1
     * }</pre>
     *
     * @param adapter，符合條件，{@link Predicate#predicate(Object, int)}
     * @return 符合條件的項目數
     */
    final public int count(Predicate<E> adapter) {
        if (adapter == null) throw new IllegalArgumentException();

        int count = 0;
        Iterator<E> iterator = iterator();
        for (int i = 0; iterator.hasNext(); i++) {
            if (adapter.predicate(iterator.next(), i)) count++;
        }

        return count;
    }

    /**
     * 計算總合，集合內容需為數值 {@link Number} 型別
     *
     * @return 數值總合
     */
    final public double sum() {
        if (!any()) return 0;

        double sum = 0;
        Iterator<E> iterator = iterator();
        for (; iterator.hasNext(); ) {
            E current = iterator.next();
            if (current instanceof Number) {
                sum += ((Number) current).doubleValue();
            } else {
                throw new IllegalArgumentException(String.format("%s is not Number type", current));
            }
        }

        return sum;
    }

    /**
     * 計算項目內容總合
     * <p>範例</p>
     * <pre>{@code
     * double sum = Cube.forCount(10).sum(new Cube.Calculator<Integer>() {
     *     &#x40;Override
     *     public double calculate(Integer item, int index) {
     *         return item * item;
     *     }
     * });
     * }</pre>
     * <p>輸出結果</p>
     * <pre>{@code
     * 285.0 (=1*1 + 2*2 + 3*3 + 4*4 + 5*5 + 6*6 + 7*7 + 8*8 + 9*9)
     * }</pre>
     *
     * @param adapter 取值方式，{@link Calculator#calculate(Object, int)}
     * @return 數值總合
     */
    final public double sum(Calculator<E> adapter) {
        if (adapter == null) throw new IllegalArgumentException();

        double total = 0;
        Iterator<E> iterator = iterator();
        for (int i = 0; iterator.hasNext(); i++) {
            total += adapter.calculate(iterator.next(), i);
        }

        return total;
    }

    /**
     * 取得最大值，集合內容需為數值 {@link Number} 型別
     *
     * @return 最大值
     */
    final public double max() {
        if (!any()) return Double.NaN;

        double max = Double.MIN_VALUE;
        Iterator<E> iterator = iterator();
        for (; iterator.hasNext(); ) {
            E current = iterator.next();
            if (current instanceof Number) {
                max = Math.max(max, ((Number) current).doubleValue());
            } else {
                throw new IllegalArgumentException(String.format("%s is not Number type", current));
            }
        }

        return max;
    }

    /**
     * 取得項目內容的最大值
     * <p>範例</p>
     * <pre>{@code
     * Cube.from("US", "Japan", "Taiwan").max(new Cube.Calculator<String>() {
     *     &#x40;Override
     *     public double calculate(String item, int index) {
     *         return item == null ? 0 : item.length();
     *     }
     * });
     * }</pre>
     * <p>輸出結果</p>
     * <pre>{@code
     * 6.0
     * }</pre>
     *
     * @param adapter 取值方式，{@link Calculator#calculate(Object, int)}
     * @return 最大值
     */
    final public double max(Calculator<E> adapter) {
        if (adapter == null) throw new IllegalArgumentException();

        double max = Double.MIN_VALUE;
        Iterator<E> iterator = iterator();
        for (int i = 0; iterator.hasNext(); i++) {
            max = Math.max(max, adapter.calculate(iterator.next(), i));
        }

        return max;
    }

    /**
     * 取得最小值，集合內容需為數值 {@link Number} 型別
     *
     * @return 最小值
     */
    final public double min() {
        if (!any()) return Double.NaN;

        double min = Double.MAX_VALUE;
        Iterator<E> iterator = iterator();
        for (; iterator.hasNext(); ) {
            E current = iterator.next();
            if (current instanceof Number) {
                min = Math.min(min, ((Number) current).doubleValue());
            } else {
                throw new IllegalArgumentException(String.format("%s is not Number type", current));
            }
        }

        return min;
    }

    /**
     * 取得項目內容的最小值
     * <p>範例</p>
     * <pre>{@code
     * Cube.from("US", "Japan", "Taiwan").min(new Cube.Calculator<String>() {
     *     &#x40;Override
     *     public double calculate(String item, int index) {
     *         return item == null ? 0 : item.length();
     *     }
     * });
     * }</pre>
     * <p>輸出結果</p>
     * <pre>{@code
     * 2.0
     * }</pre>
     *
     * @param adapter 取值方式，{@link Calculator#calculate(Object, int)}
     * @return 最小值
     */
    final public double min(Calculator<E> adapter) {
        if (adapter == null) throw new IllegalArgumentException();

        double min = Double.MAX_VALUE;
        Iterator<E> iterator = iterator();
        for (int i = 0; iterator.hasNext(); i++) {
            min = Math.min(min, adapter.calculate(iterator.next(), i));
        }

        return min;
    }

    /**
     * 取得有最大值的項目
     * <p>範例</p>
     * <pre>{@code
     * Cube.from("US", "Japan", "Taiwan").maxOne(new Cube.Calculator<String>() {
     *     &#x40;Override
     *     public double calculate(String item, int index) {
     *         return item == null ? 0 : item.length();
     *     }
     * });
     * }</pre>
     * <p>輸出結果</p>
     * <pre>{@code
     * Taiwan
     * }</pre>
     *
     * @param adapter 取值方式，{@link Calculator#calculate(Object, int)}
     * @return 有最大值的項目
     */
    final public E maxOne(Calculator<E> adapter) {
        if (adapter == null) throw new IllegalArgumentException();

        E value = null;
        double max = Double.MIN_VALUE;
        Iterator<E> iterator = iterator();
        for (int i = 0; iterator.hasNext(); i++) {
            E current = iterator.next();
            double calculated = adapter.calculate(current, i);
            // 值相同時，取第一筆即可
            if (max == calculated) continue;
            max = Math.max(max, calculated);
            if (max == calculated) {
                value = current;
            }
        }

        return value;
    }

    /**
     * 取得有最小值的項目
     * <p>範例</p>
     * <pre>{@code
     * Cube.from("US", "Japan", "Taiwan").minOne(new Cube.Calculator<String>() {
     *     &#x40;Override
     *     public double calculate(String item, int index) {
     *         return item == null ? 0 : item.length();
     *     }
     * });
     * }</pre>
     * <p>輸出結果</p>
     * <pre>{@code
     * US
     * }</pre>
     *
     * @param adapter 取值方式，{@link Calculator#calculate(Object, int)}
     * @return 有最小值的項目
     */
    final public E minOne(Calculator<E> adapter) {
        if (adapter == null) throw new IllegalArgumentException();

        E value = null;
        double min = Double.MAX_VALUE;
        Iterator<E> iterator = iterator();
        for (int i = 0; iterator.hasNext(); i++) {
            E current = iterator.next();
            double calculated = adapter.calculate(current, i);
            // 值相同時，取第一筆即可
            if (min == calculated) continue;
            min = Math.min(min, calculated);
            if (min == calculated) {
                value = current;
            }
        }

        return value;
    }

    /**
     * 取得第一筆項目，若無資料時回傳 null
     *
     * @return 集合的第一筆資料或 null
     */
    final public E first() {
        return any() ? iterator().next() : null;
    }

    /**
     * 取得第一筆符合的項目，若有多筆資料符合時僅巡覽至第一筆就結束
     * <p>範例</p>
     * <pre>{@code
     * Cube.from("US", "Japan", "UK", "Taiwan").first(new Cube.Predicate<String>() {
     *     &#x40;Override
     *     public boolean predicate(String item, int index) {
     *         return item != null && item.startsWith("U");
     *     }
     * });
     * }</pre>
     * <p>輸出結果</p>
     * <pre>{@code
     * US
     * }</pre>
     *
     * @param adapter 比對方式，{@link Predicate#predicate(Object, int)}
     * @return 集合中符合條件的第一筆資料或 null
     */
    final public E first(Predicate<E> adapter) {
        if (adapter == null) throw new IllegalArgumentException();

        Iterator<E> iterator = iterator();
        for (int i = 0; iterator.hasNext(); i++) {
            E current = iterator.next();
            if (adapter.predicate(current, i)) return current;
        }

        return null;
    }

    /**
     * 取得最後一筆，若無資料時回傳 null
     *
     * @return 集合的最後一筆資料或 null
     */
    final public E last() {
        List<E> values = toList();
        return values.get(values.size() - 1);
    }

    /**
     * 取得最後一筆符合的項目
     * <p>範例</p>
     * <pre>{@code
     * Cube.from("US", "Japan", "UK", "Taiwan").last(new Cube.Predicate<String>() {
     *     &#x40;Override
     *     public boolean predicate(String item, int index) {
     *         return item != null && item.startsWith("U");
     *     }
     * });
     * }</pre>
     * <p>輸出結果</p>
     * <pre>{@code
     * UK
     * }</pre>
     *
     * @param adapter 比對方式，{@link Predicate#predicate(Object, int)}
     * @return 集合中符合條件的最後一筆資料或 null
     */
    final public E last(Predicate<E> adapter) {
        if (adapter == null) throw new IllegalArgumentException();

        E value = null;
        Iterator<E> iterator = iterator();
        for (int i = 0; iterator.hasNext(); i++) {
            E current = iterator.next();
            if (adapter.predicate(current, i)) value = current;
        }

        return value;
    }

    /**
     * 亂數取得任意一筆項目，若無資料時回傳 null
     *
     * @return 集合中任意一筆資料或 null
     */
    final public E random() {
        List<E> values = toList();
        return any() ? values.get(new SecureRandom().nextInt(values.size())) : null;
    }

    /**
     * 串接另一個集合
     * <p>範例</p>
     * <pre>{@code
     * Cube.from("US", "Japan", "Taiwan").concat("UK", "Italy", "Denmark");
     * }</pre>
     * <p>輸出結果</p>
     * <pre>{@code
     * [US, Japan, Taiwan, UK, Italy, Denmark]
     * }</pre>
     *
     * @param values 物件陣列
     * @return 串接後的集合
     */
    @SafeVarargs
    final public Cube<E> concat(E... values) {
        List<E> list = toList();
        for (E value : values) list.add(value);
        return from(list);
    }

    /**
     * 串接另一個集合
     * <p>範例</p>
     * <pre>{@code
     * List<String> list = Arrays.asList("UK", "Italy", "Denmark");
     * Cube.from("US", "Japan", "Taiwan").concat(list);
     * }</pre>
     * <p>輸出結果</p>
     * <pre>{@code
     * [US, Japan, Taiwan, UK, Italy, Denmark]
     * }</pre>
     *
     * @param values 集合物件
     * @return 串接後的集合
     */
    final public Cube<E> concat(Iterable<E> values) {
        List<E> list = toList();
        for (E value : values) list.add(value);
        return from(list);
    }

    /**
     * 串接另一個集合
     * <p>範例</p>
     * <pre>{@code
     * Vector<String> vector = new Vector<String>();
     * vector.add("UK");
     * vector.add("Italy");
     * vector.add("Denmark");
     * Enumeration<String> elements = vector.elements();
     * Cube.from("US", "Japan", "Taiwan").concat(elements);
     * }</pre>
     * <p>輸出結果</p>
     * <pre>{@code
     * [US, Japan, Taiwan, UK, Italy, Denmark]
     * }</pre>
     *
     * @param values 集合物件
     * @return 串接後的集合
     */
    final public Cube<E> concat(Enumeration<E> values) {
        List<E> list = toList();
        for (; values.hasMoreElements(); ) list.add(values.nextElement());
        return from(list);
    }

    /**
     * 濾除重覆且相同的項目，以 {@link Object#equals(Object)} 及 {@link Object#hashCode()} 比對
     * <p>範例</p>
     * <pre>{@code
     * Cube.from(1, 2, 3, 3, 3, 5, 2, 7, 3, 2, 1, 2, 5, 4, 1).distinct();
     * }</pre>
     * <p>輸出結果</p>
     * <pre>{@code
     * [1, 2, 3, 4, 5, 7]
     * }</pre>
     *
     * @return 篩選後的集合
     */
    final public Cube<E> distinct() {
        return from(toSet());
    }

    /**
     * 濾除重覆且相同的項目
     *
     * @param adapter 比對方式 {@link Equality#equals(Object, Object)}
     * @return 篩選後的集合
     */
    final public Cube<E> distinct(Equality<E> adapter) {
        if (adapter == null) throw new IllegalArgumentException();

        List<E> list = newArrayList();
        Iterator<E> iterator = iterator();

        source:
        for (; iterator.hasNext(); ) {
            E current = iterator.next();
            for (E target : list) {
                // 判定為相同的物件時，略過
                if (adapter.equals(current, target)) continue source;
            }
            list.add(current);
        }

        return from(list);
    }

    /**
     * 略過指定筆數的項目，即取得指定筆數後的所有項目。若輸入筆數大於項目數時，丟出 {@link IndexOutOfBoundsException}
     * <p>範例</p>
     * <pre>{@code
     * Cube.from(1, 2, 3, 3, 3, 5, 2, 7, 3, 2, 1, 2, 5, 4, 1).skip(4);
     * }</pre>
     * <p>輸出結果</p>
     * <pre>{@code
     * [3, 5, 2, 7, 3, 2, 1, 2, 5, 4, 1]
     * }</pre>
     *
     * @param count 筆數
     * @return 篩選後的集合
     * @throws IndexOutOfBoundsException
     */
    final public Cube<E> skip(int count) {
        if (count < 0) throw new IllegalArgumentException();

        List<E> list = newArrayList();
        Iterator<E> iterator = iterator();

        int i;
        for (i = 0; iterator.hasNext(); i++) {
            if (i < count) {
                iterator.next();
                continue;
            }
            list.add(iterator.next());
        }

        if (i < count) throw new IndexOutOfBoundsException(String.format("size: %d < %d", i, count));

        return from(list);
    }

    /**
     * 略過項目直到符合指定條件，包含符合的當筆項目。即取符合條件後的所有項目
     * <p>範例</p>
     * <pre>{@code
     * Cube.from(1, 2, 3, 3, 3, 5, 2, 7, 3, 2, 1, 2, 5, 4, 1).skipUntil(new Cube.Predicate<Integer>() {
     *     &#x40;Override
     *     public boolean predicate(Integer item, int index) {
     *         return item > 5;
     *     }
     * });
     * }</pre>
     * <p>輸出結果</p>
     * <pre>{@code
     * [7, 3, 2, 1, 2, 5, 4, 1]
     * }</pre>
     *
     * @param adapter 比對方式，{@link Predicate#predicate(Object, int)}
     * @return 篩選後的集合
     */
    final public Cube<E> skipUntil(Predicate<E> adapter) {
        if (adapter == null) throw new IllegalArgumentException();

        List<E> list = newArrayList();
        Iterator<E> iterator = iterator();

        boolean picked = false;
        for (int i = 0; iterator.hasNext(); i++) {
            E current = iterator.next();
            if (!picked && !(picked = adapter.predicate(current, i))) {
                continue;
            }
            list.add(current);
        }

        return from(list);
    }

    /**
     * 取出集合中，由前算起指定的筆數，不足數量時取全部
     * <p>範例</p>
     * <pre>{@code
     * Cube.from(1, 2, 3, 3, 3, 5, 2, 7, 3, 2, 1, 2, 5, 4, 1).take(5);
     * }</pre>
     * <p>輸出結果</p>
     * <pre>{@code
     * [1, 2, 3, 3, 3]
     * }</pre>
     *
     * @param count 筆數
     * @return 篩選後的集合
     */
    final public Cube<E> take(int count) {
        if (count < 0) throw new IllegalArgumentException();

        List<E> list = newArrayList();
        Iterator<E> iterator = iterator();
        int i;
        for (i = 0; iterator.hasNext() && i < count; i++) {
            list.add(iterator.next());
        }

        return from(list);
    }

    /**
     * 取出集合中，到符合條件為止的項目（不含符合項目本身）
     * <p>範例</p>
     * <pre>{@code
     * Cube.from(1, 2, 3, 3, 3, 5, 2, 7, 3, 2, 1, 2, 5, 4, 1).takeUntil(new Cube.Predicate<Integer>() {
     *     &#x40;Override
     *     public boolean predicate(Integer item, int index) {
     *         return item > 5;
     *     }
     * });
     * }</pre>
     * <p>輸出結果</p>
     * <pre>{@code
     * [1, 2, 3, 3, 3, 5, 2]
     * }</pre>
     *
     * @param adapter 比對方式，{@link Predicate#predicate(Object, int)}
     * @return 篩選後的集合
     */
    final public Cube<E> takeUntil(Predicate<E> adapter) {
        if (adapter == null) throw new IllegalArgumentException();

        List<E> list = newArrayList();
        Iterator<E> iterator = iterator();

        for (int i = 0; iterator.hasNext(); i++) {
            E current = iterator.next();
            if (adapter.predicate(current, i)) break;
            list.add(current);
        }

        return from(list);
    }

    /**
     * 項目順序反轉
     * <p>範例</p>
     * <pre>{@code
     * Cube.from("US", "Japan", "Taiwan").reverse();
     * }</pre>
     * <p>輸出結果</p>
     * <pre>{@code
     * [Taiwan, Japan, US]
     * }</pre>
     *
     * @return 反轉後的集合
     */
    final public Cube<E> reverse() {
        List<E> list = toList();
        Collections.reverse(list);
        return from(list);
    }

    /**
     * 移除集合中值為 null 的項目
     * <p>範例</p>
     * <pre>{@code
     * Cube.from("US", "Japan", null, "Taiwan").notNull();
     * }</pre>
     * <p>輸出結果</p>
     * <pre>{@code
     * [US, Japan, Taiwan]
     * }</pre>
     *
     * @return 篩選後的集合
     */
    final public Cube<E> notNull() {
        List<E> list = newArrayList();

        for (E current : this) {
            if (current != null) list.add(current);
        }

        return from(list);
    }

    /**
     * 篩選符合條件的項目
     * <p>範例</p>
     * <pre>{@code
     * Cube.from(1, 2, 3, 4, 5, 6, 7)
     *     .where(new Cube.Predicate<Integer>() {
     *         &#x40;Override
     *         public boolean predicate(Integer item, int index) {
     *             return item > 5;
     *         }
     *     });
     * }</pre>
     * <p>輸出結果</p>
     * <pre>{@code
     * [6, 7]
     * }</pre>
     *
     * @param adapter 篩選方式，{@link Predicate#predicate(Object, int)}
     * @return 篩選後的集合
     */
    final public Cube<E> where(Predicate<E> adapter) {
        if (adapter == null) throw new IllegalArgumentException();
        if (!iterator().hasNext()) return emptyCube();

        List<E> list = newArrayList();
        Iterator<E> iterator = iterator();
        for (int i = 0; iterator.hasNext(); i++) {
            E current = iterator.next();
            if (adapter.predicate(current, i)) list.add(current);
        }

        return from(list);
    }

    /**
     * 對項目進行排序
     * <p>範例</p>
     * <pre>{@code
     * Cube.from("US", "Japan", "Taiwan").orderBy();
     * }</pre>
     * <p>輸出結果</p>
     * <pre>{@code
     * [Japan, Taiwan, US]
     * }</pre>
     *
     * @return 排序後的集合
     * @see {@link Arrays#sort(Object[])}
     * @see {@link Comparator}
     */
    final public Cube<E> orderBy() {
        List<E> list = toList();
        Object[] array = list.toArray();
        Arrays.sort(array);
        ListIterator<E> iterator = list.listIterator();
        for (int i = 0; i < array.length; i++) {
            iterator.next();
            iterator.set((E) array[i]);
        }
        return from(list);
    }

    /**
     * 對項目進行排序
     * <p>範例</p>
     * <pre>{@code
     * Cube.from("US", "Japan", "Taiwan").orderBy();
     * }</pre>
     * <p>輸出結果</p>
     * <pre>{@code
     * [Japan, Taiwan, US]
     * }</pre>
     *
     * @param adapter 比對方式，{@link Comparator#compareTo(Object, Object)}
     * @return 排序後的集合
     */
    final public Cube<E> orderBy(final Comparator<E> adapter) {
        if (adapter == null) throw new IllegalArgumentException();

        List<E> list = toList();

        Collections.sort(list, new java.util.Comparator<E>() {
            public int compare(E o1, E o2) {
                return adapter.compareTo(o1, o2);
            }
        });

        return from(list);
    }

    /**
     * 執行每個項目，當回傳 {@code true} 時，等同迴圈中的 continue，回傳 {@code false} 時，等同迴圈中的 break
     * <p>範例</p>
     * <pre>{@code
     * Cube.forCount(10).each(new Cube.Predicate<Integer>() {
     *     &#x40;Override
     *     public boolean predicate(Integer item, int index) {
     *         if (item % 2 == 0) return true;  // continue
     *         System.out.printf("print %d%n", item);
     *         return true;
     *     }
     * });
     * }</pre>
     * <p>輸出結果</p>
     * <pre>{@code
     * print 1
     * print 3
     * print 5
     * print 7
     * print 9
     * }</pre>
     * <p>範例</p>
     * <pre>{@code
     * Cube.forCount(10).each(new Cube.Predicate<Integer>() {
     *     &#x40;Override
     *     public boolean predicate(Integer item, int index) {
     *         if (item == 4) return false;  // break
     *         System.out.printf("print %d%n", item);
     *         return true;
     *     }
     * });
     * }</pre>
     * <p>輸出結果</p>
     * <pre>{@code
     * print 0
     * print 1
     * print 2
     * print 3
     * }</pre>
     *
     * @param adapter 執行方式，{@link Predicate#predicate(Object, int)}
     * @return 原本的集合
     */
    final public Cube<E> each(Predicate<E> adapter) {
        if (adapter == null) throw new IllegalArgumentException();
        if (!iterator().hasNext()) return emptyCube();

        Iterator<E> iterator = iterator();
        for (int i = 0; iterator.hasNext(); i++) {
            if (!adapter.predicate(iterator.next(), i)) break;
        }

        return this;
    }

    /**
     * 多序執行/並行處理
     *
     * @param adapter 執行方式，{@link Predicate#predicate(Object, int)}
     * @return 原本的集合
     */
    final public Cube<E> parallel(final Predicate<E> adapter) {
        return parallel(adapter, 5);
    }

    /**
     * 多序執行/並行處理
     *
     * @param adapter 執行方式，{@link Predicate#predicate(Object, int)}
     * @param size    執行序數
     * @return 原本的集合
     */
    final public Cube<E> parallel(final Predicate<E> adapter, int size) {
        if (adapter == null) throw new IllegalArgumentException();
        if (!iterator().hasNext()) return emptyCube();

        ExecutorService service = Executors.newFixedThreadPool(Math.max(1, size));

        // 中斷點
        final Content<Boolean> breakFlag = new Content<Boolean>();

        final Iterator<E> iterator = iterator();
        for (int i = 0; iterator.hasNext(); i++) {
            final int index = i;
            // 加入作業
            service.submit(new Runnable() {
                @Override
                public void run() {
                    breakFlag.value(breakFlag.value() & adapter.predicate(iterator.next(), index));
                }
            });

            // 若偵測到中斷時，停止處理
            if (!breakFlag.value()) break;
        }
        service.shutdown();

        return this;
    }

    /**
     * 轉型，若有項目不為指定型別時，丟出 {@link ClassCastException}
     * <p>範例</p>
     * <pre>{@code
     * Cube<CharSequence> texts = Cube.from("US", "Japan", "Taiwan").cast(CharSequence.class);
     * }</pre>
     *
     * @param type 目標型別的類別
     * @param <O>  項目型別
     * @return 轉型後的集合
     * @throws ClassCastException
     */
    final public <O> Cube<O> cast(Class<O> type) {
        if (type == null) throw new IllegalArgumentException();

        List<O> list = newArrayList();
        Iterator<E> iterator = iterator();
        for (; iterator.hasNext(); ) {
            list.add(type.cast(iterator.next()));
        }
        return from(list);
    }

    /**
     * 找出符合型別的項目，並進行轉型
     * <p>範例</p>
     * <pre>{@code
     * Cube<Date> dates = Cube.from(new Date(), Calendar.getInstance()).ofType(Date.class);
     * }</pre>
     * <p>輸出結果</p>
     * <code>[Thu Dec 03 00:14:13 CST 2015]</code>
     *
     * @param type 目標型別的類別
     * @param <O>  項目型別
     * @return 篩選後的集合
     */
    final public <O> Cube<O> ofType(Class<O> type) {
        if (type == null) throw new IllegalArgumentException();

        List<O> list = newArrayList();
        Iterator<E> iterator = iterator();
        for (; iterator.hasNext(); ) {
            E current = iterator.next();
            if (current != null && type.isAssignableFrom(current.getClass()))
                list.add(type.cast(current));
        }
        return from(list);
    }

    /**
     * 轉換為指定型別物件
     * <p>範例</p>
     * <pre>{@code
     * Cube.from(1449042314372L, 1449072114372L, 1449072314372L)
     *     .select(new Cube.Convertible<Long, Date>() {
     *         &#x40;Override
     *         public Date transform(Long item, int index) {
     *             Date date = new Date();
     *             date.setTime(item);
     *             return date;
     *         }
     *     });
     * }</pre>
     * <p>輸出結果</p>
     * <pre>{@code
     * [Wed Dec 02 15:45:14 CST 2015, Thu Dec 03 00:01:54 CST 2015, Thu Dec 03 00:05:14 CST 2015]
     * }</pre>
     *
     * @param adapter 轉換方式 {@link Convertible#transform(Object, int)}
     * @param <O>     轉換目標的型別
     * @return 轉換後的集合
     */
    final public <O> Cube<O> select(Convertible<E, O> adapter) {
        if (adapter == null) throw new IllegalArgumentException();
        if (!iterator().hasNext()) return emptyCube();

        List<O> list = newArrayList();
        Iterator<E> iterator = iterator();
        for (int i = 0; iterator.hasNext(); i++) {
            O result = adapter.transform(iterator.next(), i);
            list.add(result);
        }

        return from(list);
    }

    /**
     * 對資料進行分類
     * <p>範例</p>
     * <pre>{@code
     * List<Human> list = Cube.newArrayList();
     * list.add(new Human("Bruce", "male"));
     * list.add(new Human("Linda", "female"));
     * list.add(new Human("John", "male"));
     * list.add(new Human("Kiki", "female"));
     * list.add(new Human("Robert", "male"));
     * list.add(new Human("Shany", "female"));
     * list.add(new Human("Ken", "male"));
     * list.add(new Human("Melen", "female"));
     * Map<String, Cube<Human>> group = Cube.from(list).group(new Cube.Classify<Human, String>() {
     *     &#x40;Override
     *     public String groupBy(Human item, int index) {
     *         return item.gender;
     *     }
     * });
     * }</pre>
     * <p>輸出結果</p>
     * <pre>{@code
     * {
     *   "female": [
     *     {
     *       "name": "Linda",
     *       "gender": "female"
     *     },
     *     {
     *       "name": "Kiki",
     *       "gender": "female"
     *     },
     *     {
     *       "name": "Shany",
     *       "gender": "female"
     *     },
     *     {
     *       "name": "Melen",
     *       "gender": "female"
     *     }
     *   ],
     *   "male": [
     *     {
     *       "name": "Bruce",
     *       "gender": "male"
     *     },
     *     {
     *       "name": "John",
     *       "gender": "male"
     *     },
     *     {
     *       "name": "Robert",
     *       "gender": "male"
     *     },
     *     {
     *       "name": "Ken",
     *       "gender": "male"
     *     }
     *   ]
     * }
     * }</pre>
     *
     * @param adapter 分類方式 {@link Classify#groupBy(Object, int)}
     * @param <O>     分類鍵值的型別
     * @return 分類後的 {@link Map}
     */
    final public <O> Map<O, Cube<E>> group(Classify<E, O> adapter) {
        if (adapter == null) throw new IllegalArgumentException();
        if (!iterator().hasNext()) return emptyMap();

        Map<O, Cube<E>> map = newHashMap();
        Iterator<E> iterator = iterator();
        for (int i = 0; iterator.hasNext(); i++) {
            E item = iterator.next();
            O key = adapter.groupBy(item, i);
            if (map.containsKey(key)) {
                List<E> values = map.get(key).toList();
                values.add(item);
            } else {
                map.put(key, Cube.from(item));
            }
        }

        return map;
    }

    /**
     * 合併物件中多個子項的集合
     * <p>範例</p>
     * <pre>{@code
     * Cube.from(Thread.class.getDeclaredMethods())
     *     .many(new Cube.Convertible<Method, Iterable<Annotation>>() {
     *         &#x40;Override
     *         public Iterable<Annotation> transform(Method item, int index) {
     *             return Arrays.asList(item.getDeclaredAnnotations());
     *         }
     *     });
     * }</pre>
     * <p>輸出結果</p>
     * <pre>{@code
     * [@java.lang.Deprecated(), @java.lang.Deprecated(), @java.lang.Deprecated(), @java.lang.Deprecated(), @java.lang.Deprecated(), @java.lang.Deprecated(), @sun.reflect.CallerSensitive()]
     * }</pre>
     *
     * @param adapter 轉換方式 {@link Convertible#transform(Object, int)}
     * @param <O>     轉換目標的型別
     * @return 轉換後的集合
     */
    final public <O> Cube<O> many(Convertible<E, Iterable<O>> adapter) {
        if (adapter == null) throw new IllegalArgumentException();

        Cube<O> list = Cube.emptyCube();

        Iterator<E> iterator = iterator();
        for (int i = 0; iterator.hasNext(); i++) {
            list = list.concat(adapter.transform(iterator.next(), i));
        }

        return list;
    }

    /**
     * 以指定個數切割集合，輸入數值即為預期的切割數。
     * <p>範例</p>
     * <pre>{@code
     * Cube.forCount(20).split(3)
     * }</pre>
     * <p>輸出結果</p>
     * <pre>{@code
     * [[0, 1, 2, 3, 4, 5, 6], [7, 8, 9, 10, 11, 12, 13], [14, 15, 16, 17, 18, 19]]
     * }</pre>
     *
     * @param section 切割數
     * @return 分割後的集合
     */
    final public Cube<Cube<E>> split(int section) {
        if (section < 1) throw new IllegalArgumentException();
        int size = size(this);
        // 計算個數
        int itemCount = (int) Math.ceil(1.0 * size / section);

        List<Cube<E>> list = newArrayList();
        Iterator<E> iterator = iterator();

        List<E> item;
        for (item = newArrayList(); iterator.hasNext(); ) {
            item.add(iterator.next());
            if (item.size() + 1 > itemCount) {
                list.add(from(item));
                item = newArrayList();
            }
        }
        if (item.size() > 0) list.add(from(item));

        return from(list);
    }

    /**
     * 以項目數切割集合，輸入數值為每個切割集合的最大上限。
     * <p>範例</p>
     * <pre>{@code
     * Cube.forCount(20).split(3.0)
     * }</pre>
     * <p>輸出結果</p>
     * <pre>{@code
     * [[0, 1, 2], [3, 4, 5], [6, 7, 8], [9, 10, 11], [12, 13, 14], [15, 16, 17], [18, 19]]
     * }</pre>
     *
     * @param itemCount 每個區塊的項目數
     * @return 分割後的集合
     */
    final public Cube<Cube<E>> split(double itemCount) {
        if (itemCount < 1) throw new IllegalArgumentException();

        List<Cube<E>> list = newArrayList();
        Iterator<E> iterator = iterator();

        List<E> item;
        for (item = newArrayList(); iterator.hasNext(); ) {
            item.add(iterator.next());
            if (item.size() + 1 > itemCount) {
                list.add(from(item));
                item = newArrayList();
            }
        }
        if (item.size() > 0) list.add(from(item));

        return from(list);
    }

    /**
     * 切割指定區間
     * <p>範例</p>
     * <pre>{@code
     * Cube.forCount(10).slice(3, 7)
     * }</pre>
     * <p>輸出結果</p>
     * <pre>{@code
     * [3, 4, 5, 6]
     * }</pre>
     * <p>範例</p>
     * <pre>{@code
     * Cube.forCount(10).slice(3, -2)
     * }</pre>
     * <p>輸出結果</p>
     * <pre>{@code
     * [3, 4, 5, 6, 7]
     * }</pre>
     * <p>範例</p>
     * <pre>{@code
     * Cube.forCount(10).slice(-6, -3)
     * }</pre>
     * <p>輸出結果</p>
     * <pre>{@code
     * [4, 5, 6]
     * }</pre>
     *
     * @param start 起始位置，可輸入負數，代表由集合後端開始往前計數
     * @param end   結束位置，可輸入負數，代表由集合後端開始往前計數
     * @return 分割後的集合
     * @throws IndexOutOfBoundsException
     */
    final public Cube<E> slice(int start, int end) {
        int size = size(this);
        // 負數的概念由 python 而來，代表由集合後端開始往前計數
        // 重新計算起始位置與結束位置
        if (start > size || (start < 0 && (start = size + start) < 0)) throw new IndexOutOfBoundsException();
        if (end > size || (end < 0 && (end = size + end) < 0)) throw new IndexOutOfBoundsException();

        List<E> list = newArrayList();
        Iterator<E> iterator = iterator();
        for (int i = 0; iterator.hasNext() && i < end; i++) {
            E current = iterator.next();
            if (i >= start) list.add(current);
        }
        return from(list);
    }

    /**
     * 取得與目標集合的交集
     * <p>範例</p>
     * <pre>{@code
     * Cube<Integer> source = Cube.from(1, 2, 3, 4, 5, 6, 7);
     * Cube<Integer> target = Cube.from(5, 6, 7, 9, 10);
     * Cube<Integer> intersect = source.intersect(target);
     * }</pre>
     * <p>輸出結果</p>
     * <pre>{@code
     * intersect = [5, 6, 7]
     * }</pre>
     *
     * @param targets 目標集合
     * @return 交集項目的集合
     */
    @SafeVarargs
    final public Cube<E> intersect(E... targets) {
        return intersect(asList(targets));
    }

    /**
     * 取得與目標集合的交集
     * <p>範例</p>
     * <pre>{@code
     * Cube<Integer> source = Cube.from(1, 2, 3, 4, 5, 6, 7);
     * Cube<Integer> target = Cube.from(5, 6, 7, 9, 10);
     * Cube<Integer> intersect = source.intersect(target);
     * }</pre>
     * <p>輸出結果</p>
     * <pre>{@code
     * intersect = [5, 6, 7]
     * }</pre>
     *
     * @param adapter 比對方式，{@link Equality#equals(Object, Object)}，若為 null 時以 {@link Object#equals(Object)} 比對
     * @param targets 目標集合
     * @return 交集項目的集合
     */
    @SafeVarargs
    final public Cube<E> intersect(Equality<E> adapter, E... targets) {
        return intersect(asList(targets), adapter);
    }

    /**
     * 取得與目標集合的交集
     * <p>範例</p>
     * <pre>{@code
     * Cube<Integer> source = Cube.from(1, 2, 3, 4, 5, 6, 7);
     * Cube<Integer> target = Cube.from(5, 6, 7, 9, 10);
     * Cube<Integer> intersect = source.intersect(target);
     * }</pre>
     * <p>輸出結果</p>
     * <pre>{@code
     * intersect = [5, 6, 7]
     * }</pre>
     *
     * @param targets 目標集合
     * @return 交集項目的集合
     */
    final public Cube<E> intersect(Iterable<E> targets) {
        return intersect(targets, null);
    }

    /**
     * 取得與目標集合的交集
     * <p>範例</p>
     * <pre>{@code
     * Cube<Integer> source = Cube.from(1, 2, 3, 4, 5, 6, 7);
     * Cube<Integer> target = Cube.from(5, 6, 7, 9, 10);
     * Cube<Integer> intersect = source.intersect(target);
     * }</pre>
     * <p>輸出結果</p>
     * <pre>{@code
     * intersect = [5, 6, 7]
     * }</pre>
     *
     * @param targets 目標集合
     * @param adapter 比對方式，{@link Equality#equals(Object, Object)}，若為 null 時以 {@link Object#equals(Object)} 比對
     * @return 交集項目的集合
     */
    final public Cube<E> intersect(Iterable<E> targets, Equality<E> adapter) {
        if (targets == null || !targets.iterator().hasNext()) return emptyCube();

        List<E> list = newArrayList();
        Iterator<E> iterator = iterator();

        if (adapter == null) {
            for (; iterator.hasNext(); ) {
                E current = iterator.next();
                boolean match = false;
                for (E target : targets) match = match || (current != null && target != null && current.equals(target));
                if (match) list.add(current);
            }
        } else {
            for (; iterator.hasNext(); ) {
                E current = iterator.next();
                boolean match = false;
                for (E target : targets) match = match || adapter.equals(current, target);
                if (match) list.add(current);
            }
        }

        return from(list);
    }

    /**
     * 取得與目標集合的聯集
     * <p>範例</p>
     * <pre>{@code
     * Cube<Integer> source = Cube.from(1, 2, 3, 4, 5, 6, 7);
     * Cube<Integer> target = Cube.from(5, 6, 7, 9, 10);
     * Cube<Integer> union = source.union(target);
     * }</pre>
     * <p>輸出結果</p>
     * <pre>{@code
     * union = [5, 6, 7, 1, 2, 3, 4, 9, 10]
     * }</pre>
     *
     * @param targets 目標集合
     * @return 聯集項目的集合
     */
    @SafeVarargs
    final public Cube<E> union(E... targets) {
        return union(asList(targets));
    }

    /**
     * 取得與目標集合的聯集
     * <p>範例</p>
     * <pre>{@code
     * Cube<Integer> source = Cube.from(1, 2, 3, 4, 5, 6, 7);
     * Cube<Integer> target = Cube.from(5, 6, 7, 9, 10);
     * Cube<Integer> union = source.union(target);
     * }</pre>
     * <p>輸出結果</p>
     * <pre>{@code
     * union = [5, 6, 7, 1, 2, 3, 4, 9, 10]
     * }</pre>
     *
     * @param adapter 比對方式，{@link Equality#equals(Object, Object)}，若為 null 時以 {@link Object#equals(Object)} 比對
     * @param targets 目標集合
     * @return 聯集項目的集合
     */
    @SafeVarargs
    final public Cube<E> union(Equality<E> adapter, E... targets) {
        return union(asList(targets), adapter);
    }

    /**
     * 取得與目標集合的聯集
     * <p>範例</p>
     * <pre>{@code
     * Cube<Integer> source = Cube.from(1, 2, 3, 4, 5, 6, 7);
     * Cube<Integer> target = Cube.from(5, 6, 7, 9, 10);
     * Cube<Integer> union = source.union(target);
     * }</pre>
     * <p>輸出結果</p>
     * <pre>{@code
     * union = [5, 6, 7, 1, 2, 3, 4, 9, 10]
     * }</pre>
     *
     * @param target 目標集合
     * @return 聯集項目的集合
     */
    final public Cube<E> union(Iterable<E> target) {
        return union(target, null);
    }

    /**
     * 取得與目標集合的聯集
     * <p>範例</p>
     * <pre>{@code
     * Cube<Integer> source = Cube.from(1, 2, 3, 4, 5, 6, 7);
     * Cube<Integer> target = Cube.from(5, 6, 7, 9, 10);
     * Cube<Integer> union = source.union(target);
     * }</pre>
     * <p>輸出結果</p>
     * <pre>{@code
     * union = [5, 6, 7, 1, 2, 3, 4, 9, 10]
     * }</pre>
     *
     * @param targets 目標集合
     * @param adapter 比對方式，{@link Equality#equals(Object, Object)}，若為 null 時以 {@link Object#equals(Object)} 比對
     * @return 聯集項目的集合
     */
    final public Cube<E> union(Iterable<E> targets, Equality<E> adapter) {
        if (targets == null || !targets.iterator().hasNext()) return this;

        List<E> list = newArrayList();

        // Find intersect first
        List<E> temps = intersect(targets, adapter).toList();
        list.addAll(temps);
        if (adapter == null) {
            // Add difference of source
            for (E current : this) {
                boolean match = false;
                for (E temp : temps) match = match || temp.equals(current);
                if (!match) list.add(current);
            }
            // Add difference of target
            for (E target : targets) {
                boolean match = false;
                for (E temp : temps) match = match || temp.equals(target);
                if (!match) list.add(target);
            }
        } else {
            // Add difference of source
            for (E current : this) {
                boolean match = false;
                for (E temp : temps) match = match || adapter.equals(current, temp);
                if (!match) list.add(current);
            }
            // Add difference of target
            for (E target : targets) {
                boolean match = false;
                for (E temp : temps) match = match || adapter.equals(target, temp);
                if (!match) list.add(target);
            }
        }

        return from(list);
    }

    /**
     * 取得與目標集合的差集
     * <p>範例</p>
     * <pre>{@code
     * Cube<Integer> source = Cube.from(1, 2, 3, 4, 5, 6, 7);
     * Cube<Integer> target = Cube.from(5, 6, 7, 9, 10);
     * Cube<Integer> sourceDiff = source.difference(target);
     * Cube<Integer> targetDiff = target.difference(source);
     * }</pre>
     * <p>輸出結果</p>
     * <pre>{@code
     * sourceDiff = [1, 2, 3, 4]
     * targetDiff = [9, 10]
     * }</pre>
     *
     * @param targets 目標集合
     * @return 差集項目的集合
     */
    @SafeVarargs
    final public Cube<E> difference(E... targets) {
        return difference(asList(targets));
    }

    /**
     * 取得與目標集合的差集
     * <p>範例</p>
     * <pre>{@code
     * Cube<Integer> source = Cube.from(1, 2, 3, 4, 5, 6, 7);
     * Cube<Integer> target = Cube.from(5, 6, 7, 9, 10);
     * Cube<Integer> sourceDiff = source.difference(target);
     * Cube<Integer> targetDiff = target.difference(source);
     * }</pre>
     * <p>輸出結果</p>
     * <pre>{@code
     * sourceDiff = [1, 2, 3, 4]
     * targetDiff = [9, 10]
     * }</pre>
     *
     * @param adapter 比對方式，{@link Equality#equals(Object, Object)}，若為 null 時以 {@link Object#equals(Object)} 比對
     * @param targets 目標集合
     * @return 差集項目的集合
     */
    @SafeVarargs
    final public Cube<E> difference(Equality<E> adapter, E... targets) {
        return difference(asList(targets), adapter);
    }

    /**
     * 取得與目標集合的差集
     * <p>範例</p>
     * <pre>{@code
     * Cube<Integer> source = Cube.from(1, 2, 3, 4, 5, 6, 7);
     * Cube<Integer> target = Cube.from(5, 6, 7, 9, 10);
     * Cube<Integer> sourceDiff = source.difference(target);
     * Cube<Integer> targetDiff = target.difference(source);
     * }</pre>
     * <p>輸出結果</p>
     * <pre>{@code
     * sourceDiff = [1, 2, 3, 4]
     * targetDiff = [9, 10]
     * }</pre>
     *
     * @param target 目標集合
     * @return 差集項目的集合
     */
    final public Cube<E> difference(Iterable<E> target) {
        return difference(target, null);
    }

    /**
     * 取得與目標集合的差集
     * <p>範例</p>
     * <pre>{@code
     * Cube<Integer> source = Cube.from(1, 2, 3, 4, 5, 6, 7);
     * Cube<Integer> target = Cube.from(5, 6, 7, 9, 10);
     * Cube<Integer> sourceDiff = source.difference(target);
     * Cube<Integer> targetDiff = target.difference(source);
     * }</pre>
     * <p>輸出結果</p>
     * <pre>{@code
     * sourceDiff = [1, 2, 3, 4]
     * targetDiff = [9, 10]
     * }</pre>
     *
     * @param target  目標集合
     * @param adapter 比對方式，{@link Equality#equals(Object, Object)}，若為 null 時以 {@link Object#equals(Object)} 比對
     * @return 差集項目的集合
     */
    final public Cube<E> difference(Iterable<E> target, Equality<E> adapter) {
        if (target == null || !target.iterator().hasNext()) return this;

        List<E> list = newArrayList();

        // Find intersect first
        Cube<E> temps = intersect(target, adapter);
        if (adapter == null) {
            for (E current : this) {
                boolean match = false;
                for (E temp : temps) match = match || temp.equals(current);
                if (!match) list.add(current);
            }
        } else {
            for (E current : this) {
                boolean match = false;
                for (E temp : temps) match = match || adapter.equals(current, temp);
                if (!match) list.add(current);
            }
        }

        return from(list);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator<E> iterator = iterator();
        for (; iterator.hasNext(); ) {
            E current = iterator.next();
            sb.append(", ").append(current == null ? "null" : current.toString());
        }
        if (sb.length() > 1) sb.delete(0, 2);
        return sb.insert(0, "[").append("]").toString();
    }

}