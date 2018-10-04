/*
 * Open Teradata Viewer ( notepad plugin xml )
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

package open_teradata_viewer.plugin.notepad.xml;

import open_teradata_viewer.plugin.notepad.Util;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class XmlCodec {

    public static String encode(String s) {
        s = Util.replaceStr(s, "&", "&amp;");
        s = Util.replaceStr(s, "<", "&lt;");
        s = Util.replaceStr(s, ">", "&gt;");

        s = Util.replaceStr(s, "\"", "&quot;");

        return s;
    }

    public static String decode(String s) {
        s = Util.replaceStr(s, "&quot;", "\"");
        s = Util.replaceStr(s, "&apos;", "'");
        s = Util.replaceStr(s, "&gt;", ">");
        s = Util.replaceStr(s, "&lt;", "<");
        s = Util.replaceStr(s, "&amp;", "&");

        return s;
    }
}