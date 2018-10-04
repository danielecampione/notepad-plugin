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
import java.awt.Font;
import java.awt.Graphics;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class CustomLook {

    public static boolean textAntialias = true;

    public static Font defaultFont = new Font("default", 0, 12);
    public static Font titleFont = new Font("default", 1, 12);
    public static Font statusBarFont = new Font("default", 0, 11);
    public static Font labelFont = defaultFont;
    public static Font menuFont = defaultFont;
    public static Font monospacedFont = new Font("Monospaced", 0, 12);

    public static Font buttonFont = titleFont;
    public static Color buttonColor = new Color(160, 180, 210);
    public static Dimension buttonSize = new Dimension(100, 26);
    public static Dimension buttonSmallSize = new Dimension(28, 26);

    public static Font comboFont = titleFont;
    public static Color comboColor = new Color(150, 190, 190);
    public static Dimension comboSize = new Dimension(100, 24);

    public static final void setup(Graphics g) {
        GuiUtil.setTextAntiAliasing(g, textAntialias);
    }

    public static void setTextAntiAliasing(boolean yesno) {
        textAntialias = yesno;
    }
}