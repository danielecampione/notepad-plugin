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

import javax.swing.table.TableColumn;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class FlexTableColumn extends TableColumn {
    private static final long serialVersionUID = 2175411943200605080L;

    private boolean bEditable;
    private boolean bVisible;
    private Object objUser;

    public FlexTableColumn(String label, int width) {
        setHeaderValue(label);
        setPreferredWidth(width);

        this.bVisible = true;
        this.bEditable = true;
    }

    public boolean isEditable() {
        return this.bEditable;
    }
    public boolean isVisible() {
        return this.bVisible;
    }

    public void setEditable(boolean yesno) {
        this.bEditable = yesno;
    }

    public void setVisible(boolean yesno) {
        this.bVisible = yesno;
    }

    public void setUserObject(Object o) {
        this.objUser = o;
    }

    public Object getUserObject() {
        return this.objUser;
    }
}