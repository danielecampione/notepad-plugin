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

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

/**
 * 
 * 
 * @author D. Campione
 *
 */
class IntCellRenderer extends DefaultTreeCellRenderer {

    private static final long serialVersionUID = 2132620458705838122L;
    private TreeView treeView;
    private TreeCellRenderer treeCellRend;
    private FlexBorder flexBorder = new FlexBorder();

    public IntCellRenderer(TreeView tv) {
        this.treeView = tv;

        setRenderer(new DefaultTreeCellRenderer());
    }

    public void setRenderer(TreeCellRenderer tcr) {
        this.treeCellRend = tcr;
    }

    public void setBorderType(int type) {
        this.flexBorder.setType(type);
    }

    public Icon getOpenIcon() {
        return getLeafIcon();
    }
    public Icon getClosedIcon() {
        return getLeafIcon();
    }

    public Icon getLeafIcon() {
        TreeViewNode node = this.treeView.getSelectedNode();

        Component c = this.treeCellRend.getTreeCellRendererComponent(
                node.getTree(), node, node.isSelected(), node.isExpanded(),
                node.isLeaf(), 0, false);

        return ((JLabel) c).getIcon();
    }

    public Component getTreeCellRendererComponent(JTree tree, Object value,
            boolean sel, boolean exp, boolean leaf, int row, boolean hasFocus) {
        Component c = this.treeCellRend.getTreeCellRendererComponent(tree,
                value, sel, exp, leaf, row, hasFocus);

        if ((c instanceof JComponent)) {
            JComponent jc = (JComponent) c;

            TreeViewNode node = (TreeViewNode) value;

            jc.setBorder(node.isDndSelected() ? this.flexBorder : null);

            if ((c instanceof DefaultTreeCellRenderer)) {
                DefaultTreeCellRenderer dtcr = (DefaultTreeCellRenderer) c;

                dtcr.setToolTipText(node.getToolTipText());
            }
        }

        return c;
    }
}