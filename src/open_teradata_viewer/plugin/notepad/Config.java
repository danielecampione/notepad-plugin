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

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import open_teradata_viewer.plugin.notepad.xml.XmlDocument;
import open_teradata_viewer.plugin.notepad.xml.XmlElement;
import open_teradata_viewer.plugin.notepad.xml.XmlWriter;
import open_teradata_viewer.plugin.notepad.xml.reader.XmlReader;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class Config {

    /** Configuration constants. */
    private static final String CONFIG_FILE = "config.xml";

    /** Tags. */
    private static final String CONFIG = "config";

    private static final String RECENT_FILES = "recentFiles";

    private static final String FILE = "file";

    public static Os os;

    public static String dir;

    /** Vars from configuration. */
    public static final ErView erView = new ErView();

    public static final General general = new General();

    public static final RecentFiles recentFiles = new RecentFiles();

    /** Init method. */
    public static void init() {
        os = new Os();
        dir = System.getProperty("user.home") + os.fileSep;

        // Load the configuration file
        try {
            XmlDocument xmlDoc = new XmlReader().read(dir + CONFIG_FILE);

            setup(xmlDoc.getRootElement());
        } catch (FileNotFoundException e) {
            // The configuration file doesn't exist
        } catch (Exception e) {
            ApplicationFrame
                    .getInstance()
                    .getConsole()
                    .println("Error loading config file --> " + e,
                            ApplicationFrame.WARNING_FOREGROUND_COLOR_LOG);
        }
    }

    /** Setup. */
    private static void setup(XmlElement elConfig) {
        erView.setupConfig(elConfig.getChild(ErView.TAGNAME));
        general.setupConfig(elConfig.getChild(General.TAGNAME));

        // The recent files
        XmlElement el = elConfig.getChild(RECENT_FILES);

        if (el != null) {
            List<XmlElement> list = el.getChildren(FILE);

            for (int i = list.size() - 1; i >= 0; i--) {
                XmlElement elChild = (XmlElement) list.get(i);

                recentFiles.addFile(elChild.getValue());
            }
        }
    }

    /** Save. */
    public static boolean save() {
        // Build recent files element
        XmlElement elRecFiles = new XmlElement(RECENT_FILES);

        for (int i = 0; i < recentFiles.getFileCount(); i++) {
            elRecFiles.addChild(new XmlElement(FILE, recentFiles.getFileAt(i)));
        }

        // Put all together
        XmlElement elRoot = new XmlElement(CONFIG);

        new File(dir).mkdirs();

        try {
            new XmlWriter().write(dir + CONFIG_FILE, new XmlDocument(elRoot));
            return true;
        } catch (Exception e) {
            ApplicationFrame
                    .getInstance()
                    .getConsole()
                    .println(
                            "Error saving config file --> " + dir + CONFIG_FILE,
                            ApplicationFrame.WARNING_FOREGROUND_COLOR_LOG);
            return false;
        }
    }
}