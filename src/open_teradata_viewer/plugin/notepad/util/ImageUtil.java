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

package open_teradata_viewer.plugin.notepad.util;

import java.net.URL;

import javax.swing.ImageIcon;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class ImageUtil {

    public static synchronized ImageIcon getImageIcon(String iconName) {
        URL url;

        iconName = "icons/" + iconName + ".gif";

        url = ResourceLoader.getResource(iconName);
        if (url == null) {
            return null;
        }

        return new ImageIcon(url);
    }
}
