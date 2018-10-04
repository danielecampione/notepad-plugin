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

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class NotepadToolBar extends JPanel {

    private static final long serialVersionUID = -3393521704363570763L;

    public NotepadToolBar() {
        setLayout(new FlowLayout(0, 2, 2));
        setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
    }

    public AbstractButton add(ImageIcon img, ActionListener al, String command,
            String toolTip) {
        return add(img, al, command, toolTip, false);
    }

    public AbstractButton add(ImageIcon img, ActionListener al, String command,
            String toolTip, boolean makeToggle) {
        AbstractButton ab;
        if (!makeToggle)
            ab = new NotepadButton(img, command, al, toolTip);
        else {
            ab = new NotepadToggleButton(img, command, al, toolTip);
        }
        add(ab);

        return ab;
    }

    public void addSeparator() {
        add(new NotepadSeparator());
    }

    public JLabel add(String label) {
        NotepadLabel lb = new NotepadLabel(label);

        add(lb);
        return lb;
    }

    public JTextField addText(ActionListener al, int columns) {
        NotepadTextField txt = new NotepadTextField(columns);

        if (al != null) {
            txt.addActionListener(al);
        }
        add(txt);
        return txt;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Color c1 = Color.white;
        Color c2 = Color.gray;

        int w = getWidth() - 2;
        int h = getHeight() - 2;

        g.setColor(c2);
        g.drawRect(0, 0, w, h);

        g.setColor(c1);
        g.drawRect(1, 1, w, h);
    }
}