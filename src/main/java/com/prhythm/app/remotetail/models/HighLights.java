package com.prhythm.app.remotetail.models;

import com.prhythm.core.generic.util.Cube;
import com.prhythm.core.generic.util.Strings;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 顯著標示
 * Created by nanashi07 on 16/1/5.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class HighLights implements Iterable<HighLight> {

    boolean markSearchPattern = true;

    @XmlElement(name = "highLight")
    Set<HighLight> highLights;

    public HighLights() {
        highLights = new HashSet<>();
    }

    /**
     * 找出符合的 {@link HighLight} 規則
     *
     * @param text
     * @return
     */
    public HighLight match(final String text) {
        if (Strings.isNullOrWhiteSpace(text)) return null;
        return Cube.from(highLights)
                .orderBy((source, target) -> target.getOrder() - source.getOrder())
                .first((item, index) -> {
                    Pattern expression = item.getExpression();
                    return expression != null && expression.matcher(text).find();
                });
    }

    // set interface

    public int size() {
        return highLights.size();
    }

    public boolean isEmpty() {
        return highLights.isEmpty();
    }

    public boolean contains(Object o) {
        return highLights.contains(o);
    }

    @Override
    public Iterator<HighLight> iterator() {
        return highLights.iterator();
    }

    public Object[] toArray() {
        return highLights.toArray();
    }

    public <T> T[] toArray(T[] a) {
        return highLights.toArray(a);
    }

    public boolean add(HighLight highLight) {
        return highLights.add(highLight);
    }

    public boolean remove(Object o) {
        return highLights.remove(o);
    }

    public boolean containsAll(Collection<?> c) {
        return highLights.containsAll(c);
    }

    public boolean addAll(Collection<? extends HighLight> c) {
        return highLights.addAll(c);
    }

    public boolean retainAll(Collection<?> c) {
        return highLights.retainAll(c);
    }

    public boolean removeAll(Collection<?> c) {
        return highLights.removeAll(c);
    }

    public void clear() {
        highLights.clear();
    }

    // getter & setter

    public boolean isMarkSearchPattern() {
        return markSearchPattern;
    }

    public void setMarkSearchPattern(boolean markSearchPattern) {
        this.markSearchPattern = markSearchPattern;
    }

}
