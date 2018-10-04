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
import java.awt.Cursor;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class GuiUtil {

    private static Cursor defCursor = Cursor.getDefaultCursor();
    private static Cursor waitCursor = Cursor.getPredefinedCursor(3);

    public static void setWaitCursor(Component c, boolean yesno) {
        c.setCursor(yesno ? waitCursor : defCursor);
        getFrame(c).setCursor(yesno ? waitCursor : defCursor);
    }

    public static void setWaitCursor(boolean yesno) {
        Frame.getFrames()[0].setCursor(yesno ? waitCursor : defCursor);
    }

    public static Frame getFrame(Component c) {
        Object obj = c;

        while (!(obj instanceof Frame)) {
            obj = ((Component) obj).getParent();
        }
        return (Frame) obj;
    }

    public static Color cloneColor(Color c) {
        return new Color(c.getRed(), c.getGreen(), c.getBlue());
    }

    public static final void setTextAntiAliasing(Graphics g, boolean yesno) {
        Object obj = yesno
                ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON
                : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF;

        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                obj);
    }

    public static final void setAntiAliasing(Graphics g, boolean yesno) {
        Object obj = yesno
                ? RenderingHints.VALUE_ANTIALIAS_ON
                : RenderingHints.VALUE_ANTIALIAS_OFF;

        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, obj);
    }
}