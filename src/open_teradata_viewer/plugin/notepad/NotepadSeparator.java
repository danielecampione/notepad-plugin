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
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JLabel;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class NotepadSeparator extends JLabel {

    private static final long serialVersionUID = -48954342588809933L;

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    private int iType;

    public NotepadSeparator() {
        this(1);
    }

    public NotepadSeparator(int type) {
        this.iType = type;
        Dimension d;
        if (type == 1)
            d = new Dimension(16, 26);
        else {
            d = new Dimension(26, 16);
        }
        setPreferredSize(d);
        setMinimumSize(d);
        setMaximumSize(d);
    }

    public void paintComponent(Graphics g) {
        Color c1 = Color.white;
        Color c2 = Color.gray;

        int w = getWidth();
        int h = getHeight();

        int cx = w / 2;
        int cy = h / 2;

        g.setColor(c2);

        if (this.iType == 1)
            g.drawLine(cx, 1, cx, h - 2);
        else {
            g.drawLine(1, cy, w - 2, cy);
        }
        g.setColor(c1);

        if (this.iType == 1)
            g.drawLine(cx + 1, 1, cx + 1, h - 2);
        else
            g.drawLine(1, cy + 1, w - 2, cy + 1);
    }
}