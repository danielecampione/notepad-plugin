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

import javax.swing.JTree;
import javax.swing.text.html.HTML;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class TreeViewRenderer extends DefaultTreeCellRenderer {

    private static final long serialVersionUID = 1436503007757562350L;

    public Component getTreeCellRendererComponent(JTree tree, Object value,
            boolean sel, boolean exp, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, exp, leaf, row,
                hasFocus);

        TreeViewNode node = (TreeViewNode) value;

        ElementInfo info = (ElementInfo) node.getUserData();

        if (info != null) {
            if (info.tag == HTML.Tag.P || info.tag == HTML.Tag.IMPLIED)
                setIcon(ImageFactory.PARAGRAPH);

            else if (info.tag == HTML.Tag.CONTENT)
                setIcon(ImageFactory.FONT_FAMIL);
            else if (info.tag == HTML.Tag.IMG)
                setIcon(ImageFactory.IMAGE);
            else if (info.tag == HTML.Tag.OL)
                setIcon(ImageFactory.LIST_ORDERED);
            else if (info.tag == HTML.Tag.UL)
                setIcon(ImageFactory.LIST_UNORDERED);
            else if (info.tag == HTML.Tag.LI)
                setIcon(ImageFactory.LIST_ITEM);
        }

        return this;
    }
}