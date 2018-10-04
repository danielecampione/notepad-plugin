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

/**
 *
 *
 * @author  D. Campione
 * 
 */
public class Os {

    /** The file separator. Can be "/" (for Linux) or "\" (for Windows) */
    public String fileSep;

    /** The line separator. Can be "/n" (for Linux) or "/r/n" (for Windows) */
    public String lineSep;

    /** Operating system Can be "linux" etc... */
    public String name;

    /** Ctor. */
    public Os() {
        fileSep = System.getProperty("file.separator", "/");
        lineSep = System.getProperty("line.separator", "/n");
        name = System.getProperty("os.name", "unknown").toLowerCase();
    }
}