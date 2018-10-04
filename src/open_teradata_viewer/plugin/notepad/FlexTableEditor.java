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

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class FlexTableEditor extends AbstractCellEditor
        implements
            TableCellEditor,
            ActionListener,
            ItemListener {
    private static final long serialVersionUID = -6979849632041582969L;

    private JButton jButton;
    private NotepadComboBox tCombo;

    public FlexTableEditor(JButton button) {
        this.jButton = button;

        button.addActionListener(this);
    }

    public FlexTableEditor(NotepadComboBox combo) {
        this.tCombo = combo;

        combo.addItemListener(this);
    }

    public Object getCellEditorValue() {
        if (this.tCombo != null) {
            return this.tCombo.getSelectedKey();
        }
        return this.jButton.getText();
    }

    public boolean isCellEditable(EventObject anEvent) {
        if ((anEvent instanceof MouseEvent)) {
            return ((MouseEvent) anEvent).getClickCount() >= 1;
        }
        return true;
    }

    public boolean shouldSelectCell(EventObject anEvent) {
        if ((anEvent instanceof MouseEvent)) {
            MouseEvent e = (MouseEvent) anEvent;
            return e.getID() != 506;
        }
        return true;
    }

    public void actionPerformed(ActionEvent e) {
        stopCellEditing();
    }

    public void itemStateChanged(ItemEvent e) {
        stopCellEditing();
    }

    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        if (this.tCombo != null) {
            this.tCombo.setSelectedKey(value.toString());
            return this.tCombo;
        }

        this.jButton.setText((String) value);
        return this.jButton;
    }
}