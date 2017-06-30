/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.map.path;

import model.map.Edge;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Abstract iterable path that can hold any types of objects along the path
 *
 * @author Feng
 */
public abstract class AbstractPath<T> implements Iterable<T> {

    protected List<T> mPath;

    public int length() {
        return mPath.size();
    }

    
    public Collection<T> getPath() {
        return Collections.unmodifiableCollection(mPath);
    }

    public Collection<T> getPartialPath(int start, int end) {
        if (end >= mPath.size() || start > end) {
            return Collections.unmodifiableCollection(mPath);
        }
        return Collections.unmodifiableCollection(mPath.subList(start, end));
    }

    public void replace(int x, T node) {
        if (x > mPath.size()) {
            return;
        }
        mPath.set(x, node);
    }

    public T getLast() {
        return mPath.get(mPath.size() - 1);
    }

    @Override
    public Iterator<T> iterator() {
        return mPath.iterator();
    }

}
