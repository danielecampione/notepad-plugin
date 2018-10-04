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

import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * 
 * 
 * @author D. Campione
 *
 */
class NotepadComboRenderer extends NotepadLabel implements ListCellRenderer<Object> {

    private static final long serialVersionUID = 6851332165843318136L;

    private NotepadComboBox tcb;

    public NotepadComboRenderer(NotepadComboBox t) {
        this.tcb = t;
        setOpaque(true);
        setVerticalAlignment(0);
        setIconTextGap(8);
    }

    public Component getListCellRendererComponent(JList<?> list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        if (index == -1)
            index = this.tcb.getSelectedIndex();

        setFont(list.getFont());
        setIcon(this.tcb.getItemIcon(index));

        setText(value != null ? value.toString() : "");

        return this;
    }
}