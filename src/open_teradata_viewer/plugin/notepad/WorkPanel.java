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

import javax.swing.JPanel;
import javax.swing.text.html.HTML;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class WorkPanel extends MultiPanel {

    private static final long serialVersionUID = -4862828612113065744L;

    private ContentPanel panContent = new ContentPanel();

    /** Ctor. */
    public WorkPanel() {
        add("blank", new JPanel());
        add("content", panContent);
    }

    public void refresh(ElementInfo info, HtmlToolkit kit) {
        if (info == null) {
            show("blank");
            return;
        }

        if (info.tag == HTML.Tag.CONTENT || info.tag == HTML.Tag.P) {
            panContent.refresh(info, kit);
            show("content");
        }

        else {
            show("blank");
        }
    }
}