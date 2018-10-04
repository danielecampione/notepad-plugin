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

import java.util.Vector;

import javax.swing.JButton;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class DefaultFlexTableModel implements FlexTableModel {

    private Vector<FlexTableColumn> vCols = new Vector<FlexTableColumn>();

    private Vector<Object> vData = new Vector<Object>();

    private FlexTableConfirmator ftConf;

    public FlexTableColumn addColumn(String label, int width) {
        return addColumn(label, width, true, true);
    }

    public FlexTableColumn addColumn(String label, int width, boolean editable) {
        return addColumn(label, width, editable, true);
    }

    public FlexTableColumn addColumn(String label, int width, boolean editable,
            boolean visible) {
        FlexTableColumn ftc = new FlexTableColumn(label, width);
        ftc.setEditable(editable);
        ftc.setVisible(visible);

        vCols.addElement(ftc);

        return ftc;
    }

    public FlexTableColumn addColumn(String label, int width, JButton button) {
        FlexTableColumn ftc = addColumn(label, width, true, true);
        ftc.setCellEditor(new FlexTableEditor(button));

        return ftc;
    }

    public FlexTableColumn addColumn(String label, int width, NotepadComboBox tcb) {
        FlexTableColumn ftc = addColumn(label, width, true, true);
        ftc.setCellEditor(new FlexTableEditor(tcb));
        ftc.setCellRenderer(new FlexTableRenderer(tcb));

        return ftc;
    }

    public void addRow(Vector<?> v) {
        vData.addElement(v);
    }

    public void insertRow(int index, Vector<?> v) {
        vData.insertElementAt(v, index);
    }

    public void removeRow(int row) {
        vData.removeElementAt(row);
    }

    public void clearColumns() {
        vCols.removeAllElements();
    }
    public void clearData() {
        vData.removeAllElements();
    }

    public void setConfirmator(FlexTableConfirmator c) {
        ftConf = c;
    }

    public void swapRows(int i, int j) {
        Object io = vData.elementAt(i);
        Object jo = vData.elementAt(j);

        vData.setElementAt(io, j);
        vData.setElementAt(jo, i);
    }

    public Object getValueAt(int row, int col) {
        return ((Vector<?>) vData.elementAt(row)).elementAt(col);
    }

    @SuppressWarnings("unchecked")
    public void setValueAt(Object v, int row, int col) {
        if ((ftConf != null) && (!ftConf.confirmValueChanged(row, col, v))) {
            return;
        }
        Vector<Object> vRow = (Vector<Object>) vData.elementAt(row);
        vRow.setElementAt(v, col);
    }

    public int getRowCount() {
        return vData.size();
    }
    public int getColumnCount() {
        return vCols.size();
    }

    public FlexTableColumn getColumnAt(int i) {
        return (FlexTableColumn) vCols.elementAt(i);
    }
}