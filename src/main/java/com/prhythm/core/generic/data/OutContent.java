package com.prhythm.core.generic.data;

/**
 * 用於 {@link java.lang.reflect.Method} 中處理輸出的參數
 * <p>範例</p>
 * <pre>{@code
 * public int filter(String name, OutContent<List<Item>> items) {
 *     // code here
 * }
 * }</pre>
 * <p>使用</p>
 * <pre>{@code
 * String name = "PSD";
 * OutContent<List<Item>> itemContent = new OutContent<List<Item>>();
 * int effected = filter(name, itemContent);
 * List<Item> items = itemContent.value();
 * // more code here
 * }</pre>
 * Created by nanashi07 on 15/12/9.
 */
public class OutContent<E> {

    E value;

    /**
     * 取得輸出資料值
     *
     * @return
     */
    public E value() {
        return value;
    }

    public void value(E value) {
        this.value = value;
    }

    /**
     * 資料是否有值
     *
     * @return
     */
    public boolean present() {
        return value != null;
    }

}