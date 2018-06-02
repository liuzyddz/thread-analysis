package com.haotian.analysis.ui.config;

import javax.swing.*;

public class ComposeHolder {
    public static JTree tree;
    public static JTable table;
    public static JTextPane textPane;
    public static JTextField queryArg;
    public static JComboBox queryType;
    public static JFileChooser fileChooser;

    public static int byQuery() {
        Item selectedItem = (Item) queryType.getSelectedItem();
        if (selectedItem == null) {
            return -1;
        }
        return selectedItem.getValue();
    }
}
