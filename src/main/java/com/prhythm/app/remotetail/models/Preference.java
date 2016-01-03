package com.prhythm.app.remotetail.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * 偏號設定
 * Created by nanashi07 on 16/1/3.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Preference {

    /**
     * 字型
     */
    String fontFamily;
    /**
     * 文字大小
     */
    double fontSize;
    /**
     * 編碼
     */
    String charset;

    public Preference() {
        this.fontFamily = "Courier New";
        this.fontSize = 13;
        this.charset = "utf-8";
    }

    public double getFontSize() {
        return fontSize;
    }

    public void setFontSize(double fontSize) {
        this.fontSize = fontSize;
    }

    public String getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }
}
