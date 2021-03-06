package me.synnk.jbytecustom.utils.list;

import javax.swing.*;
import java.util.ArrayList;

public class LazyListModel<E> extends AbstractListModel<E> {

    private ArrayList<E> list;

    public LazyListModel() {
        this.list = new ArrayList<E>();
    }

    public void addElement(E e) {
        list.add(e);
    }

    @Override
    public int getSize() {
        return list.size();
    }

    @Override
    protected void fireIntervalAdded(Object source, int index0, int index1) {
    }

    @Override
    public E getElementAt(int index) {
        return list.get(index);
    }
}
