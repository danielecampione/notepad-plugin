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

import open_teradata_viewer.plugin.notepad.xml.XmlElement;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class Window {

    public static final String TAGNAME = "window";

    public int width = 750;
    public int height = 550;

    private static final String WIDTH = "width";
    private static final String HEIGHT = "height";

    /** Ctor. */
    Window() {
    }

    void setupConfig(XmlElement el) {
        if (el == null)
            return;

        width = Util.getIntValue(el.getChildValue(WIDTH), 750);
        height = Util.getIntValue(el.getChildValue(HEIGHT), 550);
    }

    XmlElement getConfig() {
        XmlElement elRoot = new XmlElement(TAGNAME);

        elRoot.addChild(new XmlElement(WIDTH, width + "")).addChild(
                new XmlElement(HEIGHT, height + ""));

        return elRoot;
    }
}