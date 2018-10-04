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

import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.text.AttributeSet;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;
import javax.swing.text.html.HTML;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class ElementInfo {

    public HTML.Tag tag;
    public int start;
    public int end;

    public Hashtable<Object, String> attribs = new Hashtable<Object, String>();

    /** Ctor. */
    public ElementInfo(Element e) {
        tag = (HTML.Tag) e.getAttributes().getAttribute(
                StyleConstants.NameAttribute);
        start = e.getStartOffset();
        end = e.getEndOffset();

        AttributeSet as = e.getAttributes();

        for (Enumeration<?> en = as.getAttributeNames(); en.hasMoreElements();) {
            Object name = en.nextElement();
            Object obj = as.getAttribute(name);

            attribs.put(name, obj.toString());
        }
    }

    public String toString() {
        return "[" + start + " " + end + "]";
    }
}