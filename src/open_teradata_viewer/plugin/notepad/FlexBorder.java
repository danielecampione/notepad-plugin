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

import javax.swing.border.Border;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class FlexBorder implements Border {

    public static final int NONE = 0;
    public static final int TOP_LINE = 1;
    public static final int BOTTOM_LINE = 2;
    public static final int RECT = 3;
    private Insets insets = new Insets(0, 0, 0, 0);
    private Color color = Color.blue;
    private int type = 3;

    public FlexBorder() {
    }

    public FlexBorder(Color c, int t) {
        color = c;
        type = t;
    }

    public void setColor(Color c) {
        color = c;
    }

    public void setType(int t) {
        type = t;
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int width,
            int height) {
        g.setColor(color);

        switch (type) {
            case 1 :
                g.drawLine(x, y, x + width - 1, y);
                break;
            case 2 :
                g.drawLine(x, y + height - 1, x + width - 1, y + height - 1);
                break;
            case 3 :
                g.drawRect(x, y, width - 1, height - 1);
        }
    }

    public Insets getBorderInsets(Component c) {
        return insets;
    }

    public boolean isBorderOpaque() {
        return false;
    }
}