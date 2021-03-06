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

package open_teradata_viewer.plugin.notepad.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import open_teradata_viewer.plugin.notepad.HtmlEditorPanel;
import open_teradata_viewer.plugin.notepad.NotepadPlugin;

/**
 *
 * 
 * @author  D. Campione
 * 
 */
public class CutAction extends AbstractAction {

    private static final long serialVersionUID = -405831146090810974L;

    /** Ctor. */
    protected CutAction() {
        super("cut");
        setEnabled(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        HtmlEditorPanel htmlEditorPanel = NotepadPlugin.getInstance()
                .getEditor().getPaneEditor();
        htmlEditorPanel.getTextComponent().cut();
    }
}