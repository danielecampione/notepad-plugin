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
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.ToolTipManager;
import javax.swing.event.EventListenerList;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class TreeView extends JScrollPane
        implements
            MouseListener,
            MouseMotionListener,
            TreeSelectionListener,
            TreeModelListener,
            TreeWillExpandListener {

    private static final long serialVersionUID = -9092346362465998913L;

    private boolean bEditable = true;
    private PopupGenerator popupGen;
    private TreeViewDndHandler dndHandler;
    private boolean bDnDDragging;
    private TreeViewNode currDnDNode;
    private TreeViewNode selDnDNode;
    private int currResp;
    private static Cursor defCursor = Cursor.getDefaultCursor();

    private EventListenerList listList = new EventListenerList();
    private IntCellRenderer intCellRend = new IntCellRenderer(this);
    JTree jTree;
    DefaultTreeModel defTreeMod;

    public TreeView() {
        this(true);
    }

    public TreeView(boolean editable) {
        TreeViewNode rootNode = new TreeViewNode();
        this.defTreeMod = new DefaultTreeModel(rootNode);
        this.defTreeMod.addTreeModelListener(this);

        this.jTree = new JTree(rootNode) {

            private static final long serialVersionUID = -5692022978255464551L;

            public void paintComponent(Graphics g) {
                CustomLook.setup(g);
                super.paintComponent(g);
            }

        };
        this.jTree.getSelectionModel().setSelectionMode(1);
        this.jTree.addTreeSelectionListener(this);
        this.jTree.addMouseListener(this);
        this.jTree.addMouseMotionListener(this);
        this.jTree.addTreeWillExpandListener(this);
        this.jTree.setShowsRootHandles(true);
        this.jTree.setRootVisible(false);
        this.jTree.setEditable(false);
        this.jTree.setModel(this.defTreeMod);
        this.jTree.putClientProperty("JTree.lineStyle", "Angled");
        this.jTree.setCellRenderer(this.intCellRend);
        this.jTree.setCellEditor(new DefaultTreeCellEditor(this.jTree,
                this.intCellRend));
        this.jTree.setToggleClickCount(0);
        this.jTree.setInvokesStopCellEditing(true);

        setViewportView(this.jTree);

        ToolTipManager.sharedInstance().registerComponent(this.jTree);

        setEditable(editable);

        InputMap im = new InputMap();

        im.setParent(this.jTree.getInputMap());
        this.jTree.setInputMap(0, im);

        ActionMap am = new ActionMap();

        am.setParent(this.jTree.getActionMap());
        this.jTree.setActionMap(am);
    }

    public void addKeyBinding(KeyStroke ks, Action a) {
        this.jTree.getInputMap().put(ks, a);
        this.jTree.getActionMap().put(a, a);
    }

    public void setRootNode(TreeViewNode root) {
        TreeViewNode oldRoot = getRootNode();

        if (oldRoot != null) {
            oldRoot.setTree(null);
        }

        root.setTree(this.jTree);
        this.defTreeMod.setRoot(root);
    }

    public void setShowRootHandles(boolean yesno) {
        this.jTree.setShowsRootHandles(yesno);
    }

    public TreeViewNode getRootNode() {
        return (TreeViewNode) this.defTreeMod.getRoot();
    }

    public void setRootVisible(boolean yesno) {
        this.jTree.setRootVisible(yesno);
    }

    public TreeViewNode getSelectedNode() {
        return (TreeViewNode) this.jTree.getLastSelectedPathComponent();
    }

    public void clearSelection() {
        this.jTree.clearSelection();
    }

    public void addSelectionListener(TreeViewSelListener l) {
        this.listList.add(TreeViewSelListener.class, l);
    }

    public void removeSelectionListener(TreeViewSelListener l) {
        this.listList.remove(TreeViewSelListener.class, l);
    }

    public void fireNodeSelected(TreeViewNode node, boolean doubleClick) {
        Object[] listeners = this.listList.getListenerList();

        TreeViewSelEvent e = new TreeViewSelEvent(this, node, doubleClick);

        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TreeViewSelListener.class)
                ((TreeViewSelListener) listeners[(i + 1)]).nodeSelected(e);
        }
    }

    public void setPopupGen(PopupGenerator p) {
        this.popupGen = p;
    }

    public PopupGenerator getPopupGen() {
        return this.popupGen;
    }

    public void setCellRenderer(TreeCellRenderer tcr) {
        this.intCellRend.setRenderer(tcr);
    }

    public void updateTree() {
        this.defTreeMod.reload();
    }

    public void setEditable(boolean yesno) {
        this.bEditable = yesno;
    }

    public void setDndHandler(TreeViewDndHandler h) {
        this.dndHandler = h;
    }

    public void mousePressed(MouseEvent e) {
        if (e.isMetaDown()) {
            TreePath path = this.jTree.getPathForLocation(e.getX(), e.getY());

            TreeViewNode treeNode = null;

            if (path != null) {
                treeNode = (TreeViewNode) path.getLastPathComponent();

                this.jTree.setSelectionPath(path);
            } else {
                this.jTree.clearSelection();
            }

            if (this.popupGen == null)
                return;

            JPopupMenu popup = this.popupGen.generate(treeNode);

            if (popup == null)
                return;

            if (popup.getComponentCount() != 0)
                popup.show(this.jTree, e.getX(), e.getY());
        } else {
            this.jTree.setEditable(false);

            this.selDnDNode = getSelectedNode();

            if (this.selDnDNode == null)
                return;

            if ((e.getClickCount() == 2) && (this.bEditable)
                    && (this.selDnDNode.isEditable())) {
                this.jTree.setEditable(true);
                this.jTree.startEditingAtPath(new TreePath(this.selDnDNode
                        .getPath()));
            } else if ((e.getClickCount() == 2) && (!this.bEditable)) {
                fireNodeSelected(this.selDnDNode, true);
            } else {
                if (this.dndHandler == null)
                    return;

                this.bDnDDragging = true;
                this.currDnDNode = null;
                this.currResp = 0;
            }
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (!this.bDnDDragging)
            return;

        this.bDnDDragging = false;
        this.jTree.setCursor(Cursor.getDefaultCursor());

        if (this.currDnDNode != null) {
            this.currDnDNode.setDnDSelection(false);
            this.currDnDNode.refresh();

            if (this.currResp != 0)
                this.dndHandler.handleDrop(this.selDnDNode, this.currDnDNode);
        }
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
        if (!this.bDnDDragging)
            return;

        TreePath path = this.jTree.getPathForLocation(e.getX(), e.getY());

        if (path != null) {
            TreeViewNode node = (TreeViewNode) path.getLastPathComponent();

            if (node == this.selDnDNode) {
                deselectDrop();
            } else {
                if (this.currDnDNode != null) {
                    if (this.currDnDNode != node) {
                        this.currDnDNode.setDnDSelection(false);
                        this.currDnDNode.refresh();
                    }

                }

                this.currResp = this.dndHandler.acceptDrop(this.selDnDNode,
                        node);

                if (this.currResp == 0)
                    this.jTree.setCursor(this.dndHandler.getNoDropCursor());
                else {
                    this.jTree.setCursor(defCursor);
                }

                switch (this.currResp) {
                    case 0 :
                        this.intCellRend.setBorderType(0);
                        break;
                    case 2 :
                        this.intCellRend.setBorderType(1);
                        break;
                    case 3 :
                        this.intCellRend.setBorderType(2);
                        break;
                    case 1 :
                        this.intCellRend.setBorderType(3);
                }

                node.setDnDSelection(true);
                node.refresh();
                this.currDnDNode = node;
            }
        } else {
            deselectDrop();
        }
    }

    private void deselectDrop() {
        this.jTree.setCursor(this.dndHandler.getNoDropCursor());

        if (this.currDnDNode != null) {
            this.currDnDNode.setDnDSelection(false);
            this.currDnDNode.refresh();
            this.currDnDNode = null;
        }
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void valueChanged(TreeSelectionEvent e) {
        TreeViewNode node = (TreeViewNode) this.jTree
                .getLastSelectedPathComponent();

        fireNodeSelected(node, false);
    }

    public void treeNodesChanged(TreeModelEvent e) {
        TreeViewNode node = (TreeViewNode) e.getTreePath()
                .getLastPathComponent();

        int[] indexes = e.getChildIndices();

        if (indexes != null) {
            node = node.getChild(indexes[0]);
        }
        node.textChanged();
    }

    public void treeNodesInserted(TreeModelEvent e) {
    }

    public void treeNodesRemoved(TreeModelEvent e) {
    }

    public void treeStructureChanged(TreeModelEvent e) {
    }

    public void treeWillExpand(TreeExpansionEvent e) throws ExpandVetoException {
        TreeViewNode node = (TreeViewNode) e.getPath().getLastPathComponent();

        node.nodeWillExpand(e);
    }

    public void treeWillCollapse(TreeExpansionEvent e)
            throws ExpandVetoException {
        TreeViewNode node = (TreeViewNode) e.getPath().getLastPathComponent();

        node.nodeWillCollapse(e);
    }
}