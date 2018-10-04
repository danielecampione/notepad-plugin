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

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class NotepadDialog extends JDialog {

    private static final long serialVersionUID = 6652675038194142140L;

    private boolean bCancel;

    public NotepadDialog(Frame f, String title, boolean modal) {
        super(f, title, modal);

        this.bCancel = false;

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
            }

        });
    }

    protected JRootPane createRootPane() {
        ActionListener al = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                NotepadDialog.this.setVisible(false);
            }

        };
        KeyStroke stroke = KeyStroke.getKeyStroke(27, 0);
        JRootPane rootPane = super.createRootPane();

        rootPane.registerKeyboardAction(al, stroke, 2);

        return rootPane;
    }

    public void showDialog() {
        this.bCancel = false;
        pack();
        setLocationRelativeTo(getParent());
        setVisible(true);
    }

    public void setCancelled() {
        this.bCancel = true;
    }

    public void clearCancelled() {
        this.bCancel = false;
    }

    public boolean isCancelled() {
        return this.bCancel;
    }
}