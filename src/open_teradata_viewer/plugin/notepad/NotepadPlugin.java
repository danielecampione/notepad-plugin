/*
 * Open Teradata Viewer ( notepad plugin )
 * Copyright (C) 2011, D. Campione
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
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;
import javax.swing.text.Segment;
import javax.swing.text.TextAction;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.Main;
import net.sourceforge.open_teradata_viewer.plugin.PluginEntry;
import open_teradata_viewer.plugin.notepad.actions.Actions;
import open_teradata_viewer.plugin.notepad.util.ImageUtil;

/**
 * An embedded notepad that uses the simple text editor component that supports
 * only one font.
 *
 * @author  D. Campione
 * 
 */
public class NotepadPlugin extends JPanel implements PluginEntry {

    private static final long serialVersionUID = -2901116171964303834L;

    private static NotepadPlugin PLUGIN;

    private static boolean exitAfterFirstPaint;

    private UndoAction undoAction = new UndoAction();

    private RedoAction redoAction = new RedoAction();

    private JTextComponent editor;

    @SuppressWarnings("rawtypes")
    private Hashtable commands;

    @SuppressWarnings("rawtypes")
    private Hashtable menuItems;

    private JMenuBar menubar;

    private JToolBar toolbar;

    private JFrame elementTreeFrame;

    protected ElementTreePanel elementTreePanel;

    protected FileDialog fileDialog;

    /** Listener for the edits on the current document. */
    protected UndoableEditListener undoHandler = new UndoHandler();

    /** UndoManager that we add edits to. */
    protected UndoManager undo = new UndoManager();

    public static final String openAction = "open";

    public static final String newAction = "new";

    public static final String saveAction = "save";

    public static final String showElementTreeAction = "showElementTree";

    @SuppressWarnings({"rawtypes", "unchecked"})
    public NotepadPlugin() {
        super(true);

        PLUGIN = this;

        setBorder(BorderFactory.createEtchedBorder());
        setLayout(new BorderLayout());

        // Create the embedded JTextComponent
        editor = createEditor();
        // The listener for undoable edits
        editor.getDocument().addUndoableEditListener(undoHandler);

        // Install the command table
        commands = new Hashtable();
        Action[] actions = getActions();
        for (int i = 0; i < actions.length; i++) {
            Action a = actions[i];
            commands.put(a.getValue(Action.NAME), a);
        }

        JScrollPane scroller = new JScrollPane();
        JViewport port = scroller.getViewport();
        port.add(editor);
        port.setScrollMode(JViewport.SIMPLE_SCROLL_MODE);

        menuItems = new Hashtable();
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add("North", createToolbar());
        panel.add("Center", scroller);
        add("Center", panel);

        JMenuBar menuBar = ApplicationFrame.getInstance().getJMenuBar();
        int menuPos = getMenuPositionByActionCommand(menuBar, "file");
        if (menuPos != -1) {
            JMenu fileMenu = menuBar.getMenu(menuPos);
            fileMenu.insertSeparator(fileMenu.getMenuComponentCount());
            fileMenu.add(Actions.NOTEPAD);
        }
    }

    public static NotepadPlugin getInstance() {
        return PLUGIN;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void initPluginEntry(Object param) {
        HashMap ht = (HashMap) param;
        exitAfterFirstPaint = ((String) ht.get("exit-after-paint"))
                .equalsIgnoreCase("true");
    }

    @Override
    public void startPluginEntry() {
        try {
            if (getFrame() != null && getFrame().isVisible()) {
                return;
            }

            JFrame frame = new JFrame(Main.APPLICATION_NAME + " ( " + this
                    + " ) ");
            frame.setBackground(Color.lightGray);
            frame.getContentPane().setLayout(new BorderLayout());
            frame.getContentPane().add("Center", this);
            frame.setJMenuBar(createMenubar());
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.pack();
            frame.setSize(500, 600);
            frame.setVisible(true);
        } catch (Throwable t) {
            ApplicationFrame.getInstance().printStackTraceOnGUI(t);
        }
    }
    @Override
    public void stopPluginEntry() {
    }

    @Override
    public void pausePluginEntry() {
        getFrame().setVisible(false);
    }

    /**
     * Fetch the list of actions supported by this editor. It is implemented to
     * return the list of actions supported by the embedded JTextComponent
     * augmented with the actions defined locally.
     */
    public Action[] getActions() {
        return TextAction.augmentList(editor.getActions(), defaultActions);
    }

    /** Create an editor to represent the given document. */
    protected JTextComponent createEditor() {
        JTextComponent c = new JTextArea();
        c.setDragEnabled(true);
        c.setFont(new Font("monospaced", Font.PLAIN, 12));
        return c;
    }

    /** Fetch the editor contained in this panel */
    protected JTextComponent getEditor() {
        return editor;
    }

    /** Find the hosting frame, for the file-chooser dialog. */
    protected Frame getFrame() {
        for (Container p = getParent(); p != null; p = p.getParent()) {
            if (p instanceof Frame) {
                return (Frame) p;
            }
        }
        return null;
    }

    /** This is the hook through which all menu items are created. */
    @SuppressWarnings("unchecked")
    protected JMenuItem createMenuItem(String cmd) {
        String menuItemLabel = cmd;
        if (cmd.equalsIgnoreCase("new")) {
            menuItemLabel = "New";
        } else if (cmd.equalsIgnoreCase("save")) {
            menuItemLabel = "Save As..";
        } else if (cmd.equalsIgnoreCase("open")) {
            menuItemLabel = "Open..";
        } else if (cmd.equalsIgnoreCase("copy-to-clipboard")) {
            menuItemLabel = "Copy";
        } else if (cmd.equalsIgnoreCase("cut-to-clipboard")) {
            menuItemLabel = "Cut";
        } else if (cmd.equalsIgnoreCase("paste-from-clipboard")) {
            menuItemLabel = "Paste";
        } else if (cmd.equalsIgnoreCase("dump-model")) {
            menuItemLabel = "Dump model to System.err";
        } else if (cmd.equalsIgnoreCase("showElementTree")) {
            menuItemLabel = "Show Elements";
        }
        JMenuItem mi = new JMenuItem(menuItemLabel);
        mi.setActionCommand(cmd);
        Action a = getAction(cmd);
        if (a != null) {
            mi.addActionListener(a);
            a.addPropertyChangeListener(createActionChangeListener(mi));
            mi.setEnabled(a.isEnabled());
        } else {
            mi.setEnabled(false);
        }
        menuItems.put(cmd, mi);
        return mi;
    }

    /**
     * Fetch the menu item that was created for the given command.
     * 
     * @param cmd  Name of the action.
     * @returns item created for the given command or null if one wasn't
     *   created.
     *  
     */
    protected JMenuItem getMenuItem(String cmd) {
        return (JMenuItem) menuItems.get(cmd);
    }

    protected Action getAction(String cmd) {
        return (Action) commands.get(cmd);
    }

    protected Container getToolbar() {
        return toolbar;
    }

    protected JMenuBar getMenubar() {
        return menubar;
    }

    /** Resets the undo manager. */
    protected void resetUndoManager() {
        undo.discardAllEdits();
        undoAction.update();
        redoAction.update();
    }

    /** Create the toolbar. */
    private Component createToolbar() {
        toolbar = new JToolBar();
        String[] toolKeys = tokenize("new open save - cut-to-clipboard"
                + " copy-to-clipboard paste-from-clipboard");
        for (int i = 0; i < toolKeys.length; i++) {
            if (toolKeys[i].equals("-")) {
                toolbar.add(Box.createHorizontalStrut(5));
            } else {
                toolbar.add(createTool(toolKeys[i]));
            }
        }
        toolbar.add(Box.createHorizontalGlue());
        return toolbar;
    }
    /** Hook through which every toolbar item is created. */
    protected Component createTool(String key) {
        return createToolbarButton(key);
    }

    /**
     * Create a button to go inside of the toolbar. By default this will load an
     * image resource. The image filename is relative to the classpath
     * (including the '.' directory if its a part of the classpath), and may
     * either be in a JAR file or a separate file.
     */
    protected JButton createToolbarButton(String key) {
        JButton b = new JButton(ImageUtil.getImageIcon(key)) {

            private static final long serialVersionUID = -7031792730313426304L;

            public float getAlignmentY() {
                return 0.5f;
            }
        };
        b.setRequestFocusEnabled(false);
        b.setMargin(new Insets(1, 1, 1, 1));

        Action a = getAction(key);
        if (a != null) {
            b.setActionCommand(key);
            b.addActionListener(a);
        } else {
            b.setEnabled(false);
        }

        return b;
    }

    /**
     * Take the given string and chop it up into a series of strings on
     * whitespace boundaries.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    protected String[] tokenize(String input) {
        Vector v = new Vector();
        StringTokenizer t = new StringTokenizer(input);
        String cmd[];

        while (t.hasMoreTokens())
            v.addElement(t.nextToken());
        cmd = new String[v.size()];
        for (int i = 0; i < cmd.length; i++)
            cmd[i] = (String) v.elementAt(i);

        return cmd;
    }

    /** Create the menubar for the plugin. */
    protected JMenuBar createMenubar() {
        JMenuBar mb = new JMenuBar();

        String[] menuKeys = tokenize("file edit debug");
        for (int i = 0; i < menuKeys.length; i++) {
            JMenu m = createMenu(menuKeys[i]);
            if (m != null) {
                mb.add(m);
            }
        }
        this.menubar = mb;
        return mb;
    }

    /** Create a menu for the plugin. */
    protected JMenu createMenu(String key) {
        String menuLabel = key;
        String actions = null;
        if (key.equalsIgnoreCase("file")) {
            menuLabel = "File";
            actions = "new open save";
        } else if (key.equalsIgnoreCase("edit")) {
            menuLabel = "Edit";
            actions = "cut-to-clipboard copy-to-clipboard paste-from-clipboard"
                    + " - Undo Redo";
        } else if (key.equalsIgnoreCase("debug")) {
            menuLabel = "Debug";
            actions = "dump-model showElementTree";
        }
        String[] itemKeys = tokenize(actions);
        JMenu menu = new JMenu(menuLabel);
        for (int i = 0; i < itemKeys.length; i++) {
            if (itemKeys[i].equals("-")) {
                menu.addSeparator();
            } else {
                JMenuItem mi = createMenuItem(itemKeys[i]);
                menu.add(mi);
            }
        }
        return menu;
    }

    /** Yarked from JMenu, ideally this would be public. */
    protected PropertyChangeListener createActionChangeListener(JMenuItem b) {
        return new ActionChangedListener(b);
    }

    /** Actions defined by the NotepadPlugin class. */
    private Action[] defaultActions = {new NewAction(), new OpenAction(),
            new SaveAction(), new ShowElementTreeAction(), undoAction,
            redoAction};

    public int getMenuPositionByActionCommand(JMenuBar menuBar,
            String actionCommand) {
        if (menuBar != null) {
            for (int i = 0; i < menuBar.getMenuCount(); i++) {
                JMenu menu = menuBar.getMenu(i);
                if (menu.getActionCommand().equalsIgnoreCase(actionCommand)) {
                    return i;
                }
            }
            return -1;
        }
        return -1;
    }

    @Override
    public String toString() {
        return "Notepad plug-in";
    }

    public void paintChildren(Graphics g) {
        super.paintChildren(g);
        if (exitAfterFirstPaint) {
            pausePluginEntry();
        }
    }

    /**
     * 
     * 
     * @author D. Campione
     * 
     */
    private class ActionChangedListener implements PropertyChangeListener {
        JMenuItem menuItem;

        ActionChangedListener(JMenuItem mi) {
            super();
            this.menuItem = mi;
        }
        public void propertyChange(PropertyChangeEvent e) {
            String propertyName = e.getPropertyName();
            if (e.getPropertyName().equals(Action.NAME)) {
                String text = (String) e.getNewValue();
                menuItem.setText(text);
            } else if (propertyName.equals("enabled")) {
                Boolean enabledState = (Boolean) e.getNewValue();
                menuItem.setEnabled(enabledState.booleanValue());
            }
        }
    }

    /**
     * 
     * 
     * @author D. Campione
     * 
     */
    class UndoHandler implements UndoableEditListener {

        /**
         * Messaged when the Document has created an edit, the edit is added to
         * <code>undo</code>, an instance of UndoManager.
         */
        public void undoableEditHappened(UndoableEditEvent e) {
            undo.addEdit(e.getEdit());
            undoAction.update();
            redoAction.update();
        }
    }

    /**
     * 
     * 
     * @author D. Campione
     * 
     */
    class UndoAction extends AbstractAction {

        private static final long serialVersionUID = 656811250789128493L;

        public UndoAction() {
            super("Undo");
            setEnabled(false);
        }

        public void actionPerformed(ActionEvent e) {
            try {
                undo.undo();
            } catch (CannotUndoException ex) {
                ApplicationFrame.getInstance().changeLog.append(
                        "Unable to undo: " + ex + "\n",
                        ApplicationFrame.WARNING_FOREGROUND_COLOR_LOG);
            }
            update();
            redoAction.update();
        }

        protected void update() {
            if (undo.canUndo()) {
                setEnabled(true);
                putValue(Action.NAME, undo.getUndoPresentationName());
            } else {
                setEnabled(false);
                putValue(Action.NAME, "Undo");
            }
        }
    }

    /**
     * 
     * 
     * @author D. Campione
     * 
     */
    class RedoAction extends AbstractAction {

        private static final long serialVersionUID = -6842851564928870215L;

        public RedoAction() {
            super("Redo");
            setEnabled(false);
        }

        public void actionPerformed(ActionEvent e) {
            try {
                undo.redo();
            } catch (CannotRedoException ex) {
                ApplicationFrame.getInstance().changeLog.append(
                        "Unable to redo: " + ex + "\n",
                        ApplicationFrame.WARNING_FOREGROUND_COLOR_LOG);
            }
            update();
            undoAction.update();
        }

        protected void update() {
            if (undo.canRedo()) {
                setEnabled(true);
                putValue(Action.NAME, undo.getRedoPresentationName());
            } else {
                setEnabled(false);
                putValue(Action.NAME, "Redo");
            }
        }
    }

    /**
     * 
     * 
     * @author D. Campione
     * 
     */
    class OpenAction extends NewAction {

        private static final long serialVersionUID = -1372701266564520823L;

        OpenAction() {
            super(openAction);
        }

        public void actionPerformed(ActionEvent e) {
            Frame frame = getFrame();
            JFileChooser chooser = new JFileChooser();
            int ret = chooser.showOpenDialog(frame);

            if (ret != JFileChooser.APPROVE_OPTION) {
                return;
            }

            File f = chooser.getSelectedFile();
            if (f.isFile() && f.canRead()) {
                Document oldDoc = getEditor().getDocument();
                if (oldDoc != null)
                    oldDoc.removeUndoableEditListener(undoHandler);
                if (elementTreePanel != null) {
                    elementTreePanel.setEditor(null);
                }
                getEditor().setDocument(new PlainDocument());
                getFrame().setTitle(
                        Main.APPLICATION_NAME + " ( " + PLUGIN + " ) "
                                + f.getName());
                Thread loader = new FileLoader(f, editor.getDocument());
                loader.start();
            } else {
                ApplicationFrame.getInstance().changeLog.append(
                        "Could not open file: " + f + "\n",
                        ApplicationFrame.WARNING_FOREGROUND_COLOR_LOG);
                JOptionPane.showMessageDialog(getFrame(),
                        "Could not open file: " + f, "Error opening file",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * 
     * 
     * @author D. Campione
     * 
     */
    class SaveAction extends AbstractAction {

        private static final long serialVersionUID = 924808061379488049L;

        SaveAction() {
            super(saveAction);
        }

        public void actionPerformed(ActionEvent e) {
            Frame frame = getFrame();
            JFileChooser chooser = new JFileChooser();
            int ret = chooser.showSaveDialog(frame);

            if (ret != JFileChooser.APPROVE_OPTION) {
                return;
            }

            File f = chooser.getSelectedFile();
            getFrame().setTitle(
                    Main.APPLICATION_NAME + " ( " + PLUGIN + " ) "
                            + f.getName());
            Thread saver = new FileSaver(f, editor.getDocument());
            saver.start();
        }
    }

    /**
     * 
     * 
     * @author D. Campione
     * 
     */
    class NewAction extends AbstractAction {

        private static final long serialVersionUID = 7324396695898344758L;

        NewAction() {
            super(newAction);
        }

        NewAction(String nm) {
            super(nm);
        }

        public void actionPerformed(ActionEvent e) {
            Document oldDoc = getEditor().getDocument();
            if (oldDoc != null)
                oldDoc.removeUndoableEditListener(undoHandler);
            getEditor().setDocument(new PlainDocument());
            getEditor().getDocument().addUndoableEditListener(undoHandler);
            resetUndoManager();
            getFrame().setTitle(Main.APPLICATION_NAME + " ( " + PLUGIN + " )");
            revalidate();
        }
    }

    /**
     * Action that brings up a JFrame with a JTree showing the structure of the
     * document.
     * 
     * @author D. Campione
     * 
     */
    class ShowElementTreeAction extends AbstractAction {

        private static final long serialVersionUID = -8134968458136835588L;

        ShowElementTreeAction() {
            super(showElementTreeAction);
        }

        ShowElementTreeAction(String nm) {
            super(nm);
        }

        public void actionPerformed(ActionEvent e) {
            if (elementTreeFrame == null) {
                // Create a frame containing an instance of ElementTreePanel
                String title = "Elements";
                elementTreeFrame = new JFrame(Main.APPLICATION_NAME + " ( "
                        + PLUGIN + " ) " + title);

                elementTreeFrame.addWindowListener(new WindowAdapter() {
                    public void windowClosing(WindowEvent weeee) {
                        elementTreeFrame.setVisible(false);
                    }
                });
                Container fContentPane = elementTreeFrame.getContentPane();

                fContentPane.setLayout(new BorderLayout());
                elementTreePanel = new ElementTreePanel(getEditor());
                fContentPane.add(elementTreePanel);
                elementTreeFrame.pack();
            }
            elementTreeFrame.setVisible(true);
        }
    }

    /**
     * Thread to load a file into the text storage model.
     * 
     * @author D. Campione
     * 
     */
    class FileLoader extends Thread {

        FileLoader(File f, Document doc) {
            setPriority(4);
            this.f = f;
            this.doc = doc;
        }

        public void run() {
            try {
                // Try to start reading
                Reader in = new FileReader(f);
                char[] buff = new char[4096];
                int nch;
                while ((nch = in.read(buff, 0, buff.length)) != -1) {
                    doc.insertString(doc.getLength(), new String(buff, 0, nch),
                            null);
                }
            } catch (IOException e) {
                final String msg = e.getMessage();
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        ApplicationFrame.getInstance().changeLog.append(
                                "Could not open file: " + msg + "\n",
                                ApplicationFrame.WARNING_FOREGROUND_COLOR_LOG);
                        JOptionPane
                                .showMessageDialog(getFrame(),
                                        "Could not open file: " + msg,
                                        "Error opening file",
                                        JOptionPane.ERROR_MESSAGE);
                    }
                });
            } catch (BadLocationException e) {
                ApplicationFrame.getInstance().changeLog.append(e.getMessage()
                        + "\n", ApplicationFrame.WARNING_FOREGROUND_COLOR_LOG);
            }

            if (elementTreePanel != null) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        elementTreePanel.setEditor(getEditor());
                    }
                });
            }
        }

        Document doc;
        File f;
    }

    /**
     * Thread to save a document to file.
     * 
     * @author D. Campione
     * 
     */
    class FileSaver extends Thread {
        Document doc;
        File f;

        FileSaver(File f, Document doc) {
            setPriority(4);
            this.f = f;
            this.doc = doc;
        }

        public void run() {
            try {
                // Start writing
                Writer out = new FileWriter(f);
                Segment text = new Segment();
                text.setPartialReturn(true);
                int charsLeft = doc.getLength();
                int offset = 0;
                while (charsLeft > 0) {
                    doc.getText(offset, Math.min(4096, charsLeft), text);
                    out.write(text.array, text.offset, text.count);
                    charsLeft -= text.count;
                    offset += text.count;
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                out.flush();
                out.close();
            } catch (IOException e) {
                final String msg = e.getMessage();
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        ApplicationFrame.getInstance().changeLog.append(
                                "Could not save file: " + msg + "\n",
                                ApplicationFrame.WARNING_FOREGROUND_COLOR_LOG);
                        JOptionPane.showMessageDialog(getFrame(),
                                "Could not save file: " + msg,
                                "Error saving file", JOptionPane.ERROR_MESSAGE);
                    }
                });
            } catch (BadLocationException e) {
                ApplicationFrame.getInstance().changeLog.append(e.getMessage()
                        + "\n", ApplicationFrame.WARNING_FOREGROUND_COLOR_LOG);
            }
        }
    }
}
