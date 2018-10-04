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

package open_teradata_viewer.plugin.notepad.actions;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.Main;
import open_teradata_viewer.plugin.notepad.Config;
import open_teradata_viewer.plugin.notepad.FileIO;
import open_teradata_viewer.plugin.notepad.FileManager;
import open_teradata_viewer.plugin.notepad.HtmlEditorPanel;
import open_teradata_viewer.plugin.notepad.NotepadPlugin;
import open_teradata_viewer.plugin.notepad.Util;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class FileOpenAction extends AbstractAction {

    private static final long serialVersionUID = -4963752523739647195L;

    /** Ctor. */
    protected FileOpenAction() {
        super(NotepadPlugin.openAction);
        setEnabled(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            File file = FileIO.openFile();
            if (file != null) {
                String path = file.getAbsolutePath();
                Config.recentFiles.addFile(path);
                String encoding = "UTF-8";
                // Requires two arguments - the file name and the encoding to
                // use.
                FileManager fileManager = new FileManager(path, encoding);
                String text = fileManager.read();
                String NL = "\r\n";

                text = Util.replaceAll(text, NL + NL, "<p>");
                text = Util.replaceAll(text, NL, "<br>");

                HtmlEditorPanel htmlEditorPanel = NotepadPlugin.getInstance()
                        .getEditor().getPaneEditor();
                htmlEditorPanel.setText(text);
                NotepadPlugin
                        .getInstance()
                        .getFrame()
                        .setTitle(
                                Util.extractFileName(path) + " - "
                                        + Main.APPLICATION_NAME + " ( "
                                        + NotepadPlugin.getInstance() + " )");
                ApplicationFrame.getInstance().getConsole()
                        .println(path + " has been successfully read.");
            }
        } catch (Throwable t) {
            ExceptionDialog.notifyException(t);
        }
    }
}