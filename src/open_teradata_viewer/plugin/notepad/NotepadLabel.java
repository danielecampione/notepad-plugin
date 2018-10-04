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

import javax.swing.JLabel;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class NotepadLabel extends JLabel {

    private static final long serialVersionUID = -1060582406448359099L;

    public NotepadLabel() {
        this("");
    }

    public NotepadLabel(String text) {
        this(text, 2);
    }

    public NotepadLabel(String text, int horizAlign) {
        super(text, horizAlign);

        setFont(CustomLook.labelFont);
    }

    public void paintComponent(Graphics g) {
        CustomLook.setup(g);
        super.paintComponent(g);
    }
}