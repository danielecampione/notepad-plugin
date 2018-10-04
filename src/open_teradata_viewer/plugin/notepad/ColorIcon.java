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
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.Icon;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class ColorIcon implements Icon {

    private int iWidth;

    private int iHeight;

    private Color color;

    private Color border;

    private Insets insets;

    public ColorIcon() {
        this(32, 16);
    }

    public ColorIcon(int width, int height) {
        this(width, height, Color.black);
    }

    public ColorIcon(int width, int height, Color c) {
        iWidth = width;
        iHeight = height;

        color = c;
        border = Color.black;
        insets = new Insets(1, 1, 1, 1);
    }

    public void setColor(Color c) {
        color = c;
    }

    public Color getColor() {
        return color;
    }

    public void setBorderColor(Color c) {
        border = c;
    }

    public int getIconWidth() {
        return iWidth;
    }

    public int getIconHeight() {
        return iHeight;
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        g.setColor(border);
        g.drawRect(x, y, iWidth - 1, iHeight - 2);

        x += insets.left;
        y += insets.top;

        int w = iWidth - insets.left - insets.right;
        int h = iHeight - insets.top - insets.bottom - 1;

        g.setColor(color);
        g.fillRect(x, y, w, h);
    }
}