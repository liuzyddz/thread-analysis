package com.haotian.analysis.ui.config;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.util.List;

public class ThreadTreeModel implements TreeModel {
    private List<ThreadSummary> threadSummaryList;
    private ThreadSummary root;

    public ThreadTreeModel() {
        root = new ThreadSummary(Long.toString(System.nanoTime()),ConfigUtil.getDefaultConfig().getMenu().get("tree.label.root"));
        threadSummaryList = H2Helper.searchThreadSummary();
    }

    @Override
    public Object getRoot() {
        return root;
    }

    @Override
    public Object getChild(Object parent, int index) {
        if (getRoot().equals(parent)) {
            return threadSummaryList.get(index);
        }
        return null;
    }

    @Override
    public int getChildCount(Object parent) {
        if (getRoot().equals(parent)) {
            return threadSummaryList.size();
        }
        return 0;
    }

    @Override
    public boolean isLeaf(Object node) {
        return !getRoot().equals(node);
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {

    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        return 0;
    }

    @Override
    public void addTreeModelListener(TreeModelListener l) {

    }

    @Override
    public void removeTreeModelListener(TreeModelListener l) {

    }
}
