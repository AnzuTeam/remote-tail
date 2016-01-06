package com.prhythm.app.remotetail.core;

/**
 * 資料編輯介面
 * Created by nanashi07 on 16/1/6.
 */
public interface IEditable<T> {

    /**
     * 載入資火卜木十
     *
     * @param value 資料物件
     */
    void from(T value);

    /**
     * 更新資料
     *
     * @param value 資料物件
     * @return
     */
    boolean update(T value);

}
