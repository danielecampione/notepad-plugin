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

import java.util.Vector;

import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreePath;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class TreeViewNode extends DefaultMutableTreeNode {

    private static final long serialVersionUID = -5786563643791685292L;

    private Object oUserData;
    private String toolTip;
    private Vector<Object> vColumns = new Vector<Object>();
    private boolean bDnDSelected = false;
    private boolean bEditable = true;
    private JTree jTree;
    private DefaultTreeModel defTreeMod;

    public TreeViewNode() {
        this("");
    }

    public TreeViewNode(String text) {
        this(text, null);
    }

    public TreeViewNode(String text, Object data) {
        setText(text);
        setUserData(data);
    }

    public void setEditable(boolean yesno) {
        this.bEditable = yesno;
    }

    public boolean isEditable() {
        return this.bEditable;
    }

    public TreeViewNode duplicate() {
        TreeViewNode node = getNewInstance();

        copyTo(node);

        for (int i = 0; i < getChildCount(); i++) {
            node.add(getChild(i).duplicate());
        }
        return node;
    }

    public void copyTo(TreeViewNode node) {
        node.toolTip = this.toolTip;

        node.setText(getText());
    }

    public void setText(String text) {
        setUserObject(text);
    }

    public String getText() {
        return (String) getUserObject();
    }

    public void setUserData(Object data) {
        this.oUserData = data;
    }

    public Object getUserData() {
        return this.oUserData;
    }

    public TreeViewNode addChild(TreeViewNode node) {
        return addChild(node, true);
    }

    public TreeViewNode addChild(TreeViewNode node, boolean select) {
        if (tree() != null) {
            model().insertNodeInto(node, this, getChildCount());
            if (select)
                node.select();
        } else {
            add(node);
        }
        return this;
    }

    public TreeViewNode removeChild(TreeViewNode node) {
        if (model() != null)
            model().removeNodeFromParent(node);
        else {
            remove(node);
        }
        return this;
    }

    public void insertChild(TreeViewNode node, int pos) {
        insertChild(node, pos, true);
    }

    public void insertChild(TreeViewNode node, int pos, boolean select) {
        if (tree() != null) {
            model().insertNodeInto(node, this, pos);
            if (select)
                node.select();
        } else {
            insert(node, pos);
        }
    }

    public void refresh() {
        model().nodeChanged(this);
    }

    public void refreshIcon() {
        model().nodeStructureChanged(getParent());
        select();
    }

    public void recalcChildren() {
        if (model() != null)
            model().nodeStructureChanged(this);
    }

    public void select() {
        JTree tree = tree();

        if (tree == null)
            return;

        TreePath p = new TreePath(getPath());

        tree.setSelectionPath(p);
        tree.scrollPathToVisible(p);
    }

    public void makeVisible() {
        TreePath p = new TreePath(getPath());

        tree().scrollPathToVisible(p);
    }

    public boolean isSelected() {
        JTree tree = tree();

        if (tree == null)
            return false;

        return this == (TreeViewNode) tree.getLastSelectedPathComponent();
    }

    public void expand(boolean yesno) {
        if (yesno)
            tree().expandPath(new TreePath(getPath()));
        else
            tree().collapsePath(new TreePath(getPath()));
    }

    public void expand(boolean yesno, int levels) {
        expand(yesno, levels, this);
    }

    private void expand(boolean yesno, int levels, TreeViewNode node) {
        node.expand(yesno);

        if (levels > 0) {
            for (int i = 0; i < node.getChildCount(); i++)
                expand(yesno, levels - 1, node.getChild(i));
        }
    }

    public boolean isExpanded() {
        JTree tree = tree();

        if (tree == null)
            return false;

        return tree.isExpanded(new TreePath(getPath()));
    }

    public TreeViewNode getChild(int index) {
        return (TreeViewNode) getChildAt(index);
    }

    @SuppressWarnings("unchecked")
    public void swapNodes(int i, int j) {
        if (i == j)
            return;

        if ((i < 0) || (i >= getChildCount()))
            return;
        if ((j < 0) || (j >= getChildCount()))
            return;

        TreeViewNode node1 = (TreeViewNode) getChildAt(i);
        TreeViewNode node2 = (TreeViewNode) getChildAt(j);

        this.children.setElementAt(node2, i);
        this.children.setElementAt(node1, j);

        int[] a = new int[2];

        a[0] = i;
        a[1] = j;

        if (model() != null) {
            model().nodesChanged(this, a);
        }
        node1.recalcChildren();
        node2.recalcChildren();
    }

    public void swapNodes(TreeViewNode iNode, TreeViewNode jNode) {
        int i = getIndex(iNode);
        int j = getIndex(jNode);

        swapNodes(i, j);
    }

    @SuppressWarnings("unchecked")
    public void sortChildren() {
        boolean bChanged = true;

        while (bChanged) {
            bChanged = false;

            for (int i = 0; i < getChildCount() - 1; i++) {
                TreeViewNode node1 = getChild(i);
                TreeViewNode node2 = getChild(i + 1);

                if (node1.getText().compareToIgnoreCase(node2.getText()) <= 0)
                    continue;
                bChanged = true;

                this.children.setElementAt(node2, i);
                this.children.setElementAt(node1, i + 1);
            }

        }

        recalcChildren();
    }

    public void textChanged() {
    }

    public void nodeWillExpand(TreeExpansionEvent e) throws ExpandVetoException {
    }

    public void nodeWillCollapse(TreeExpansionEvent e)
            throws ExpandVetoException {
    }

    public void setToolTipText(String tip) {
        this.toolTip = tip;
    }

    public String getToolTipText() {
        return this.toolTip;
    }

    public void setTree(JTree tree) {
        this.jTree = tree;

        if (tree == null)
            this.defTreeMod = null;
        else
            this.defTreeMod = ((DefaultTreeModel) tree.getModel());
    }

    public JTree getTree() {
        return tree();
    }

    public void addColumn(Object o) {
        this.vColumns.addElement(o);
    }

    public void removeColumn(int index) {
        this.vColumns.removeElementAt(index);
    }

    public Object getColumnAt(int index) {
        return this.vColumns.elementAt(index);
    }

    public void setColumnAt(Object obj, int index) {
        this.vColumns.setElementAt(obj, index);
    }

    protected TreeViewNode getNewInstance() {
        return new TreeViewNode();
    }

    private JTree tree() {
        return ((TreeViewNode) getRoot()).jTree;
    }

    private DefaultTreeModel model() {
        return ((TreeViewNode) getRoot()).defTreeMod;
    }

    void setDnDSelection(boolean yesno) {
        this.bDnDSelected = yesno;
    }

    boolean isDndSelected() {
        return this.bDnDSelected;
    }
}