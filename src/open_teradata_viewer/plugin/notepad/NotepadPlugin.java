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
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Hashtable;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.ImageManager;
import net.sourceforge.open_teradata_viewer.Main;
import net.sourceforge.open_teradata_viewer.plugin.PluginEntry;
import open_teradata_viewer.plugin.notepad.actions.Actions;

/**
 * An embedded notepad that uses a text editor component that supports
 * multiple fonts.
 *
 * @author  D. Campione
 * 
 */
public class NotepadPlugin extends JPanel implements PluginEntry {

    private static final long serialVersionUID = -2901116171964303834L;

    private static NotepadPlugin PLUGIN;

    private static boolean exitAfterFirstPaint;

    private DocEditor editor;

    private Hashtable<Object, Action> commands;

    private Hashtable<String, JMenuItem> menuItems;

    private JMenuBar menubar;

    private JToolBar toolbar;

    private ElementTreePanel elementTreeFrame;

    public static final String openAction = "open";

    public static final String newAction = "new";

    public NotepadPlugin() {
        super(true);

        PLUGIN = this;

        setBorder(BorderFactory.createEtchedBorder());
        setLayout(new BorderLayout());

        ImageFactory.init();
        // Create the embedded document editor
        editor = createEditor();

        // Install the command table
        commands = new Hashtable<Object, Action>();
        Action[] actions = getActions();
        for (int i = 0; i < actions.length; i++) {
            Action a = actions[i];
            commands.put(a.getValue(Action.NAME), a);
        }

        menuItems = new Hashtable<String, JMenuItem>();
        add("North", createToolbar());
        add("Center", editor);

        JMenuBar menubar = ApplicationFrame.getInstance().getJMenuBar();
        int menuPos = getMenuPositionByActionCommand(menubar, "file");
        if (menuPos != -1) {
            JMenu fileMenu = menubar.getMenu(menuPos);
            fileMenu.insertSeparator(fileMenu.getMenuComponentCount());
            fileMenu.add(Actions.NOTEPAD);
        }
    }

    public static NotepadPlugin getInstance() {
        return PLUGIN;
    }

    @Override
    public void initPluginEntry(Object param) {
        HashMap<?, ?> ht = (HashMap<?, ?>) param;
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
                    + " )");
            frame.setIconImage(ImageManager.getImage("/icons/notepad.png")
                    .getImage());
            frame.setBackground(Color.lightGray);
            frame.getContentPane().setLayout(new BorderLayout());
            frame.getContentPane().add("Center", this);
            frame.setJMenuBar(menubar = createMenubar());
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.pack();
            frame.setSize(600, 600);
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

    /** Fetch the list of actions supported by this editor. */
    public Action[] getActions() {
        return defaultActions;
    }

    /** Create an editor to represent the given document. */
    protected DocEditor createEditor() {
        return new DocEditor();
    }

    /** Fetch the editor contained in this panel */
    public DocEditor getEditor() {
        return editor;
    }

    /** Find the hosting frame, for the file-chooser dialog. */
    public Frame getFrame() {
        for (Container p = getParent(); p != null; p = p.getParent()) {
            if (p instanceof Frame) {
                return (Frame) p;
            }
        }
        return null;
    }

    /** This is the hook through which all menu items are created. */
    protected JMenuItem createMenuItem(String cmd) {
        String menuItemLabel = cmd;
        if (cmd.equalsIgnoreCase("new")) {
            menuItemLabel = "New";
        } else if (cmd.equalsIgnoreCase("open")) {
            menuItemLabel = "Open..";
        } else if (cmd.equalsIgnoreCase("copy")) {
            menuItemLabel = "Copy";
        } else if (cmd.equalsIgnoreCase("cut")) {
            menuItemLabel = "Cut";
        } else if (cmd.equalsIgnoreCase("paste")) {
            menuItemLabel = "Paste";
        }
        JMenuItem mi = new JMenuItem(menuItemLabel);
        mi.setActionCommand(cmd);
        Action a = getAction(cmd);
        if (a != null) {
            mi.addActionListener(a);
            mi.setText(menuItemLabel);
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
     * @param cmd Name of the action.
     * @returns item created for the given command or null if one wasn't
     *          created.
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

    /** Create the toolbar. */
    private Component createToolbar() {
        toolbar = new JToolBar();
        String[] toolKeys = Util.tokenize("new open - cut copy paste");
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
        String imageIcon = key + ".gif";
        JButton b = new JButton(ImageManager.getImage("/icons/" + imageIcon)) {

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

    /** Create the menubar for the plugin. */
    protected JMenuBar createMenubar() {
        JMenuBar mb = new JMenuBar();

        String[] menuKeys = Util.tokenize("file edit");
        for (int i = 0; i < menuKeys.length; i++) {
            JMenu m = createMenu(menuKeys[i]);
            if (m != null) {
                mb.add(m);
            }
        }
        return mb;
    }

    /** Create a menu for the plugin. */
    protected JMenu createMenu(String key) {
        String menuLabel = key;
        String actions = null;
        if (key.equalsIgnoreCase("file")) {
            menuLabel = "File";
            actions = "new open";
        } else if (key.equalsIgnoreCase("edit")) {
            menuLabel = "Edit";
            actions = "cut copy paste";
        }
        String[] itemKeys = Util.tokenize(actions);
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

    /** Actions defined by the NotepadPlugin class. */
    private Action[] defaultActions = {Actions.FILE_NEW, Actions.FILE_OPEN,
            Actions.CUT, Actions.COPY, Actions.PASTE};

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

    public ElementTreePanel getElementTreeFrame() {
        return elementTreeFrame;
    }
}
