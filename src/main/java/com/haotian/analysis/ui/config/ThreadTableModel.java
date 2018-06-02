package com.haotian.analysis.ui.config;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.List;

public class ThreadTableModel implements TableModel {
    private String[] columns = new String[] {"num", "shortName", "threadName", "threadState", "threadDecId", "threadHexId"};
    private Field[] columnFields = new Field[columns.length];
    private List<ThreadItem> threadItemList;
    private final String threadEncId;

    public static final int ASC = 0;
    public static final int DESC = 1;

    static {

    }

    public ThreadTableModel(String threadEncId, int orderFieldIdx, final int order, String queryArg, int queryType) {
        this.threadEncId = threadEncId;
        if (queryType == 1 && queryArg != null && queryArg.length() != 0) {
            threadItemList = H2Helper.searchThreadItemByName(threadEncId, queryArg);
        } else if (queryType == 2 && queryArg != null && queryArg.length() != 0) {
            threadItemList = H2Helper.searchThreadItemByStack(threadEncId, queryArg);
        }  else if (queryType == 3 && queryArg != null && queryArg.length() != 0) {
            threadItemList = H2Helper.searchThreadItemByState(threadEncId, queryArg);
        }  else if (queryType == 4 && queryArg != null && queryArg.length() != 0) {
            threadItemList = H2Helper.searchThreadItemByDecId(threadEncId, queryArg);
        }  else if (queryType == 5 && queryArg != null && queryArg.length() != 0) {
            threadItemList = H2Helper.searchThreadItemByHexId(threadEncId, queryArg);
        } else {
            threadItemList = H2Helper.searchThreadItem(threadEncId);
        }
        for (int i = 0; i < columns.length; i++) {
            try {
                columnFields[i] = ThreadItem.class.getDeclaredField(columns[i]);
                columnFields[i].setAccessible(true);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException("parse field error", e);
            }
        }

        String orderField = orderFieldIdx == -1 ? null : columns[orderFieldIdx];
        for (int i = 0; i < columns.length && orderField != null; i++) {
            if (columns[i].equals(orderField)) {
                Field field = columnFields[i];
                threadItemList.sort(new Comparator<ThreadItem>() {
                    @Override
                    public int compare(ThreadItem item1, ThreadItem item2) {
                        int result = 0;
                        try {
                            Object fieldValue1 = field.get(item1);
                            Object fieldValue2 = field.get(item2);
                            if (ThreadTableModel.ASC == order) {
                                result = fieldValue1.toString().compareTo(fieldValue2.toString());
                            } else if (ThreadTableModel.DESC == order) {
                                result = fieldValue2.toString().compareTo(fieldValue1.toString());
                            } else {
                                throw new RuntimeException("unsupport order[" + order + "]");
                            }
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException("order thread table value error", e);
                        }
                        return result;
                    }
                });
                break;
            }
        }
        for (int i = 0; i < threadItemList.size(); i++) {
            threadItemList.get(i).setNum(Integer.toString(i + 1));
        }
    }

    public String getThreadEncId() {
        return threadEncId;
    }

    @Override
    public int getRowCount() {
        return threadItemList.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columns[columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columnFields[columnIndex].getType();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (threadItemList.size() <= rowIndex || rowIndex < 0) {
            return null;
        }
        ThreadItem rowItem = threadItemList.get(rowIndex);
        try {
            return columnFields[columnIndex].get(rowItem);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Invoke Field[" + columnFields[columnIndex].getName() + "] error", e);
        }
    }

    public ThreadItem getValueAt(int rowIndex) {
        if (threadItemList.size() <= rowIndex || rowIndex < 0) {
            return null;
        }
        return threadItemList.get(rowIndex);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

    }

    @Override
    public void addTableModelListener(TableModelListener l) {

    }

    @Override
    public void removeTableModelListener(TableModelListener l) {

    }
}
