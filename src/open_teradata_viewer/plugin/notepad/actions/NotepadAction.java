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

import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.actions.CustomAction;
import open_teradata_viewer.plugin.notepad.NotepadPlugin;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class NotepadAction extends CustomAction {

    private static final long serialVersionUID = -247597193455004912L;

    protected NotepadAction() {
        super("Notepad..", "notepad.png", null, "An embedded notepad.");
        setEnabled(true);
    }

    public void actionPerformed(final ActionEvent e) {
        // The "Notepad plug-in" can be used altough other processes are
        // running. No ThreadAction object must be instantiated because the
        // focus must still remains on the Notepad frame.
        try {
            performThreaded(e);
        } catch (Throwable t) {
            ExceptionDialog.notifyException(t);
        }
    }

    @Override
    protected void performThreaded(ActionEvent e) throws Exception {
        NotepadPlugin.getInstance().startPluginEntry();
    }
}