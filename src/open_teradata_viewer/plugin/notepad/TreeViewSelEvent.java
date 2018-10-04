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

import java.util.EventObject;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class TreeViewSelEvent extends EventObject {

    private static final long serialVersionUID = -2759577586441800755L;

    private TreeViewNode selNode;
    private boolean doubleClick;

    public TreeViewSelEvent(Object source, TreeViewNode node) {
        this(source, node, false);
    }

    public TreeViewSelEvent(Object source, TreeViewNode node, boolean dblClick) {
        super(source);

        this.selNode = node;
        this.doubleClick = dblClick;
    }

    public TreeViewNode getSelectedNode() {
        return this.selNode;
    }
    public boolean isDoubleClicked() {
        return this.doubleClick;
    }
}