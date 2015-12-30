package com.prhythm.core.generic.data;

import com.prhythm.core.generic.exception.RecessiveException;

/**
 * 實作單次存取資料元件，類似 Singleton 或 Optional，
 * 提供一組未實作方法 {@link #get()} 處理取得實際資料的行為，並暫存於實體中。
 * <p>範例</p>
 * <pre>{@code
 * public class Program {
 * <p>
 *     static Once<Properties> config = new Once<Properties>() {
 *         &#x40;Override
 *         protected Properties get() throws Exception {
 *             return AppConfig.read("config.properties");
 *         }
 *     };
 * <p>
 *     public static void main(String[] args) {
 * <p>
 *         if (config.present()) {
 *             String url = config.value().getProperty("url");
 * <p>
 *             // more code here
 *         }
 * <p>
 * <p>
 *     }
 * <p>
 * }
 * }</pre>
 * Created by nanashi07 on 15/12/5.
 */
public abstract class Once<T> {

    /**
     * 暫存資料
     */
    protected T value;

    /**
     * 取得值，若無值時藉由 {@link #get()} 的實作取得值
     *
     * @return
     */
    public T value() {
        try {
            return value != null ? value : (value = get());
        } catch (Exception e) {
            throw new RecessiveException(e.getMessage(), e);
        }
    }

    /**
     * 資料是否有值
     *
     * @return
     */
    public boolean present() {
        return value() != null;
    }

    /**
     * 實作取值的方式
     *
     * @return
     * @throws Exception
     */
    protected abstract T get() throws Exception;

    @Override
    public String toString() {
        return value == null ? null : value.toString();
    }

}
