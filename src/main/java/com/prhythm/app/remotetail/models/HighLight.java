package com.prhythm.app.remotetail.models;

import javafx.scene.paint.Color;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * 顯著標示項目
 * Created by nanashi07 on 16/1/5.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class HighLight {

    /**
     * 不分大小寫
     */
    boolean ignoreCase;
    /**
     * 符合的 {@link java.util.regex.Pattern} 字串
     */
    String pattern;

    /**
     * 文字顏色
     */
    Color foreground;
    /**
     * 背景顏色
     */
    Color background;
    /**
     * 粗體
     */
    boolean bold;
    /**
     * 斜體
     */
    boolean italic;

    public HighLight() {
    }

    public HighLight(boolean ignoreCase, String pattern, Color foreground, Color background, boolean bold, boolean italic) {
        this.ignoreCase = ignoreCase;
        this.pattern = pattern;
        this.foreground = foreground;
        this.background = background;
        this.bold = bold;
        this.italic = italic;
    }

    public boolean isIgnoreCase() {
        return ignoreCase;
    }

    public void setIgnoreCase(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public Color getForeground() {
        return foreground;
    }

    public void setForeground(Color foreground) {
        this.foreground = foreground;
    }

    public Color getBackground() {
        return background;
    }

    public void setBackground(Color background) {
        this.background = background;
    }

    public boolean isBold() {
        return bold;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }

    public boolean isItalic() {
        return italic;
    }

    public void setItalic(boolean italic) {
        this.italic = italic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HighLight highLight = (HighLight) o;

        return pattern.equals(highLight.pattern);
    }

    @Override
    public int hashCode() {
        return pattern.hashCode();
    }
}
