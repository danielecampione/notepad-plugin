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

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.Dialog;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class FileIO {

    private static JFileChooser fileChooser;

    /** Ctor. */
    private FileIO() {
    }

    public static File saveFile(String fileName, String text) throws Exception {
        File selectedFile = null;
        if (fileName == null) {
            JFileChooser fileChooser = getFileChooser();
            if (JFileChooser.APPROVE_OPTION == fileChooser
                    .showSaveDialog(ApplicationFrame.getInstance())) {
                selectedFile = fileChooser.getSelectedFile();
                if (!selectedFile.exists()
                        || Dialog.YES_OPTION == Dialog.show("File exists",
                                "Overwrite existing file?",
                                Dialog.QUESTION_MESSAGE, Dialog.YES_NO_OPTION)) {
                    fileName = selectedFile.getAbsolutePath();
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } else {
            selectedFile = new File(fileName);
        }
        Config.recentFiles.addFile(fileName);
        writeFile(selectedFile, text);
        return selectedFile;
    }

    public static void writeFile(File file, String text) throws Exception {
        String fileName = file.getAbsolutePath();
        String encoding = "UTF-8";
        // Requires two arguments - the file name and the encoding to use.
        FileManager fileManager = new FileManager(fileName, encoding);
        fileManager.write(text);
    }

    protected static JFileChooser getFileChooser() throws Exception {
        if (fileChooser == null) {
            fileChooser = new JFileChooser();
        }
        return fileChooser;
    }

    public static void openFile(File file) throws IOException {
        Desktop.getDesktop().open(file);
    }

    public static File openFile() throws Exception {
        JFileChooser fileChooser = getFileChooser();
        if (JFileChooser.APPROVE_OPTION == fileChooser
                .showOpenDialog(ApplicationFrame.getInstance())) {
            File selectedFile = fileChooser.getSelectedFile();
            Config.recentFiles.addFile(selectedFile.getAbsolutePath());
            return selectedFile;
        }
        return null;
    }
}