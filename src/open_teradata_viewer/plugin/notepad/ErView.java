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
public class ErView {

    public static final String TAGNAME = "erView";

    public int snapSize = 4;
    public int scrollSize = 32;

    public Print print = new Print();

    private static final String SNAPSIZE = "snapSize";
    private static final String SCROLLSIZE = "scrollSize";

    /** Ctor. */
    public ErView() {
    }

    public void setupConfig(XmlElement el) {
        if (el == null)
            return;

        snapSize = Util.getIntValue(el.getChildValue(SNAPSIZE), 4);
        scrollSize = Util.getIntValue(el.getChildValue(SCROLLSIZE), 32);

        print.setupConfig(el.getChild(Print.TAGNAME));
    }

    public XmlElement getConfig() {
        XmlElement elRoot = new XmlElement(TAGNAME);

        elRoot.addChild(new XmlElement(SNAPSIZE, snapSize + ""))
                .addChild(new XmlElement(SCROLLSIZE, scrollSize + ""))
                .addChild(print.getConfig());

        return elRoot;
    }
}