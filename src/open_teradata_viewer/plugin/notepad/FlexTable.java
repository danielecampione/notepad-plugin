/*
 * Open Teradata Viewer ( notepad plugin )
 * Copyright (C) 2012, D. Campione
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package open_teradata_viewer.plugin.notepad;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class FlexTable extends JScrollPane
        implements
            TableModel,
            ListSelectionListener,
            MouseListener {

    private static final long serialVersionUID = 7032544849641499642L;

    private JTable jTable;

    private boolean bEditable;

    private EventListenerList listList = new EventListenerList();

    private FlexTableModel flexModel = new DefaultFlexTableModel();

    private DefaultTableColumnModel columnModel = new DefaultTableColumnModel();

    public FlexTable() {
        this(false);
    }

    public FlexTable(boolean editable) {
        bEditable = editable;

        jTable = new JTable(this, columnModel) {

            private static final long serialVersionUID = -8248391091904347532L;

            public void paintComponent(Graphics g) {
                CustomLook.setup(g);
                super.paintComponent(g);
            }

        };
        jTable.setSelectionMode(0);
        jTable.setSurrendersFocusOnKeystroke(true);
        jTable.addMouseListener(this);

        jTable.setTableHeader(new JTableHeader(columnModel) {

            private static final long serialVersionUID = 3478471839033755377L;

            public void paintComponent(Graphics g) {
                CustomLook.setup(g);
                super.paintComponent(g);
            }

        });
        setViewportView(jTable);

        ListSelectionModel lsm = jTable.getSelectionModel();

        lsm.addListSelectionListener(this);

        InputMap im = new InputMap();

        im.setParent(jTable.getInputMap());
        jTable.setInputMap(0, im);

        ActionMap am = new ActionMap();

        am.setParent(jTable.getActionMap());
        jTable.setActionMap(am);
    }

    public void addKeyBinding(KeyStroke ks, Action a) {
        jTable.getInputMap().put(ks, a);
        jTable.getActionMap().put(a, a);
    }

    public FlexTableModel getFlexModel() {
        return flexModel;
    }

    public void setFlexModel(FlexTableModel model) {
        flexModel = model;

        while (columnModel.getColumnCount() != 0) {
            columnModel.removeColumn(columnModel.getColumn(0));
        }
        for (int i = 0; i < flexModel.getColumnCount(); i++) {
            FlexTableColumn ftc = flexModel.getColumnAt(i);

            if (!ftc.isVisible())
                continue;
            ftc.setModelIndex(i);
            columnModel.addColumn(ftc);
        }

        clearSelection();
        updateTable();
    }

    public void updateFlexModel() {
        setFlexModel(flexModel);
    }

    public void setEditable(boolean yesno) {
        bEditable = yesno;
    }

    public void updateTable() {
        jTable.revalidate();
        jTable.repaint();
    }

    public void addSelectionListener(FlexTableSelListener l) {
        listList.add(FlexTableSelListener.class, l);
    }

    public void removeSelectionListener(FlexTableSelListener l) {
        listList.remove(FlexTableSelListener.class, l);
    }

    public void addClickListener(FlexTableClickListener l) {
        listList.add(FlexTableClickListener.class, l);
    }

    public void removeClickListener(FlexTableClickListener l) {
        listList.remove(FlexTableClickListener.class, l);
    }

    public void clearSelection() {
        jTable.clearSelection();
    }

    public void selectRow(int row) {
        if (getRowCount() == 0)
            return;

        jTable.setRowSelectionInterval(row, row);
        jTable.scrollRectToVisible(jTable.getCellRect(row, 0, true));
    }

    public void selectLastRow() {
        selectRow(getRowCount() - 1);
    }

    public int getSelectedRow() {
        return jTable.getSelectedRow();
    }

    public void setAutoResizeMode(int mode) {
        jTable.setAutoResizeMode(mode);
    }

    public void setShowGrid(boolean yesno) {
        jTable.setShowGrid(yesno);
    }

    public void addTableModelListener(TableModelListener l) {
    }

    public void removeTableModelListener(TableModelListener l) {
    }

    public Class getColumnClass(int index) {
        for (int row = 0; row < flexModel.getRowCount(); row++) {
            Object o = flexModel.getValueAt(row, index);

            if (o != null) {
                return o.getClass();
            }
        }
        return String.class;
    }

    public boolean isCellEditable(int row, int col) {
        if (!bEditable) {
            return false;
        }
        return ((FlexTableColumn) columnModel.getColumn(col)).isEditable();
    }

    public int getColumnCount() {
        return flexModel.getColumnCount();
    }
    public int getRowCount() {
        return flexModel.getRowCount();
    }

    public String getColumnName(int index) {
        return (String) columnModel.getColumn(index).getHeaderValue();
    }

    public Object getValueAt(int row, int col) {
        return flexModel.getValueAt(row, col);
    }

    public void setValueAt(Object v, int row, int col) {
        flexModel.setValueAt(v, row, col);
    }

    public void valueChanged(ListSelectionEvent e) {
        ListSelectionModel lsm = (ListSelectionModel) e.getSource();

        if (lsm.isSelectionEmpty())
            fireSelectionChanged(-1);
        else
            fireSelectionChanged(lsm.getMinSelectionIndex());
    }

    private void fireSelectionChanged(int row) {
        Object[] listeners = listList.getListenerList();

        FlexTableSelEvent e = new FlexTableSelEvent(this, row);
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == FlexTableSelListener.class)
                ((FlexTableSelListener) listeners[(i + 1)]).rowSelected(e);
        }
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            Point p = e.getPoint();

            int col = jTable.columnAtPoint(p);
            int row = jTable.rowAtPoint(p);

            fireClickChanged(col, row);
        }
    }

    private void fireClickChanged(int col, int row) {
        Object[] listeners = listList.getListenerList();

        FlexTableSelEvent e = new FlexTableSelEvent(this, row);

        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == FlexTableClickListener.class)
                ((FlexTableClickListener) listeners[(i + 1)]).rowClicked(e);
        }
    }
}