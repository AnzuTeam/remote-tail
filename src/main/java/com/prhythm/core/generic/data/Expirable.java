package com.prhythm.core.generic.data;

import com.prhythm.core.generic.exception.RecessiveException;

/**
 * 實作存取資料元件，類似 Singleton 或 Optional，
 * 提供一組未實作方法 {@link #get()} 取得實際資料，並暫存於實體中。
 * 提供一個資料逾期的設定，並在資料逾期後重新讀取。
 * <p>範例</p>
 * <pre>{@code
 * public class Program {
 * <p>
 *     static Expirable<Properties> config = new Expirable<Properties>(TimeUnit.MINUTES.toMillis(5)) {
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
 * <p>
 * }</pre>
 * Created by nanashi07 on 15/12/7.
 */
public abstract class Expirable<T> extends Once<T> {

    public Expirable() {
    }

    public Expirable(long timeout) {
        setTimeout(timeout);
    }

    /**
     * 逾期週期，單位為亳秒
     */
    long timeout = -1;

    /**
     * 前次讀取資料時間
     */
    long lastUpdated;

    @Override
    public T value() {
        // 更新時間戳記，並將資料設為 null 以重新讀取
        if (timeout > 0 && (System.currentTimeMillis() - lastUpdated) > timeout) {
            lastUpdated = System.currentTimeMillis();
            if (value != null) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Expirable.this.value = get();
                        } catch (Exception e) {
                            throw new RecessiveException(e.getMessage(), e);
                        }
                    }
                }).start();
            }
        }

        return super.value();
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout > 0 ? timeout : -1;
    }

}
