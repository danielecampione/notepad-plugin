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

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class FlexTableRenderer extends DefaultTableCellRenderer {

    private static final long serialVersionUID = -8697903968594641915L;

    private NotepadComboBox tCombo;

    public FlexTableRenderer(NotepadComboBox combo) {
        this.tCombo = combo;
    }

    public Component getTableCellRendererComponent(JTable table, Object data,
            boolean isSelected, boolean hasFocus, int row, int column) {
        Object newData = this.tCombo.getItemFromKey(data.toString());

        return super.getTableCellRendererComponent(table, newData, isSelected,
                hasFocus, row, column);
    }
}