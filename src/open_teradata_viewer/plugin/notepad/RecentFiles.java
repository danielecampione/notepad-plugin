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

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class RecentFiles {

    private static final int MAX_FILES = 10;
    private Vector<String> vFiles = new Vector<String>();

    public void clear() {
        this.vFiles.removeAllElements();
    }

    public void addFile(String file) {
        this.vFiles.remove(file);
        this.vFiles.insertElementAt(file, 0);

        if (this.vFiles.size() > MAX_FILES)
            this.vFiles.remove(MAX_FILES);
    }

    public String getFileAt(int index) {
        return (String) this.vFiles.elementAt(index);
    }

    public int getFileCount() {
        return this.vFiles.size();
    }
}