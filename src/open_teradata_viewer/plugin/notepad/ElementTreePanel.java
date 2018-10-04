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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.util.Enumeration;

import javax.swing.JSplitPane;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;
import javax.swing.text.html.HTML;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class ElementTreePanel extends NotepadDialog
        implements
            DocumentListener,
            TreeViewSelListener,
            CaretListener {

    private static final long serialVersionUID = 8764756585900756774L;

    private TreeView docView = new TreeView(false);
    private WorkPanel workPanel = new WorkPanel();

    private HtmlToolkit toolKit;

    /** Ctor. */
    public ElementTreePanel(Frame f) {
        super(f, "Document structure", false);

        JSplitPane p = new NotepadSplitPane(docView, workPanel);
        getContentPane().add(p, BorderLayout.CENTER);

        docView.setRootVisible(false);
        docView.setCellRenderer(new TreeViewRenderer());
        docView.addSelectionListener(this);

        p.setPreferredSize(new Dimension(550, 300));
    }

    public void setHtmlToolkit(HtmlToolkit kit) {
        toolKit = kit;

        kit.getDocument().addDocumentListener(this);
        kit.getEditorPane().addCaretListener(this);

        refresh();
    }

    public void refresh() {
        TreeViewNode root = buildTree(retrieveBody(toolKit.getRootElement()));

        docView.setRootNode(root);
        root.expand(true, 10);

        selectCurrentNode(toolKit.getCaretPosition());
    }

    /** The DocumentListener */
    public void changedUpdate(DocumentEvent e) {
        if (!isVisible())
            return;

        refresh();
    }

    public void insertUpdate(DocumentEvent e) {
        if (!isVisible())
            return;

        refresh();
    }

    public void removeUpdate(DocumentEvent e) {
        if (!isVisible())
            return;

        refresh();
    }

    /** The TreeViewSelListener */
    public void nodeSelected(TreeViewSelEvent e) {
        TreeViewNode node = e.getSelectedNode();
        ElementInfo info = null;

        if (node != null) {
            info = (ElementInfo) node.getUserData();
        }

        workPanel.refresh(info, toolKit);
    }

    /** The CaretListener */
    public void caretUpdate(CaretEvent e) {
        if (!isVisible())
            return;

        selectCurrentNode(e.getDot());
    }

    public Element retrieveBody(Element e) {
        for (int i = 0; i < e.getElementCount(); i++) {
            Element child = e.getElement(i);

            if (child.getAttributes()
                    .getAttribute(StyleConstants.NameAttribute) == HTML.Tag.BODY)
                return child;
        }

        return null;
    }

    private TreeViewNode buildTree(Element e) {
        ElementInfo info = new ElementInfo(e);
        TreeViewNode node = new TreeViewNode(info.toString(), info);

        for (int i = 0; i < e.getElementCount(); i++)
            node.addChild(buildTree(e.getElement(i)));

        return node;
    }

    private void selectCurrentNode(int pos) {
        TreeViewNode root = docView.getRootNode();

        for (Enumeration<?> e = root.depthFirstEnumeration(); e
                .hasMoreElements();) {
            TreeViewNode node = (TreeViewNode) e.nextElement();
            ElementInfo info = (ElementInfo) node.getUserData();

            boolean inside = (info.start <= pos) && (pos <= info.end);

            if (node.isLeaf() && inside) {
                node.select();
                return;
            }
        }
    }
}