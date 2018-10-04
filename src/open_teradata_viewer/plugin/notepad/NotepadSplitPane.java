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

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JSplitPane;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class NotepadSplitPane extends JSplitPane {

    private static final long serialVersionUID = -7700936646620267637L;

    public NotepadSplitPane(JComponent c1, JComponent c2) {
        this(1, c1, c2);
    }

    public NotepadSplitPane(int orientation, JComponent c1, JComponent c2) {
        super(orientation, c1, c2);

        setOneTouchExpandable(true);
        setDividerLocation(200);
        setContinuousLayout(true);

        Dimension d = new Dimension(100, 50);

        c1.setMinimumSize(d);
        c2.setMinimumSize(d);
    }
}