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

import java.awt.Cursor;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public abstract interface TreeViewDndHandler {

    public static final int ACCEPT_NONE = 0;
    public static final int ACCEPT_INSIDE = 1;
    public static final int ACCEPT_BEFORE = 2;
    public static final int ACCEPT_AFTER = 3;

    public abstract int acceptDrop(TreeViewNode paramTreeViewNode1,
            TreeViewNode paramTreeViewNode2);

    public abstract void handleDrop(TreeViewNode paramTreeViewNode1,
            TreeViewNode paramTreeViewNode2);

    public abstract Cursor getNoDropCursor();
}