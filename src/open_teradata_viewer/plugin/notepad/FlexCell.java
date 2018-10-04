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

import java.awt.Component;

/**
 * 
 * 
 * @author D. Campione
 *
 */
class FlexCell {

    public static final int LEFT = 0;
    public static final int CENTERX = 1;
    public static final int RIGHT = 2;
    public static final int EXPANDX = 3;
    public static final int TOP = 0;
    public static final int CENTERY = 1;
    public static final int BOTTOM = 2;
    public static final int EXPANDY = 3;
    public int xalign;
    public int xext;
    public int yalign;
    public int yext;
    public Component component;

    public FlexCell(int xalign, int yalign, int xext, int yext, Component c) {
        this.xalign = xalign;
        this.yalign = yalign;
        this.xext = xext;
        this.yext = yext;

        component = c;
    }
}