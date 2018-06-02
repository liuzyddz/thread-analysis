package com.haotian.analysis.ui.config;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static com.haotian.analysis.ui.config.ComposeHolder.table;

public class ThreadHeaderMouseListener extends MouseAdapter {
    private int order = ThreadTableModel.DESC;
    private int orderFieldIdx = -1;
    @Override
    public void mouseReleased(MouseEvent e) {
        orderFieldIdx = table.getTableHeader().columnAtPoint(e.getPoint());
        doThreadTableReload();
        if (orderFieldIdx == 0) {
            orderFieldIdx = -1;
        }
    }

    public void doThreadTableReload() {
        if (orderFieldIdx == 0) {
            return;
        }
        String threadEncId = ((ThreadTableModel) table.getModel()).getThreadEncId();
        ThreadTableModel tableModel = new ThreadTableModel(threadEncId, orderFieldIdx, order, ComposeHolder.queryArg.getText(), ComposeHolder.byQuery());
        if (order == ThreadTableModel.DESC) {
            order = ThreadTableModel.ASC;
        } else {
            order = ThreadTableModel.DESC;
        }
        table.setModel(tableModel);
        table.setEnabled(true);
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
