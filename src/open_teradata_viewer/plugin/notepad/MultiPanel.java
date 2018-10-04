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

import java.awt.CardLayout;

import javax.swing.JPanel;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class MultiPanel extends JPanel {

    private static final long serialVersionUID = 5870622682542482779L;
    CardLayout cardLayout = new CardLayout();
    String stack1;
    String stack2;
    String stack3;

    public MultiPanel() {
        setLayout(this.cardLayout);
    }

    public void show(String strPanel) {
        this.cardLayout.show(this, strPanel);
    }

    public void push() {
        this.stack3 = this.stack2;
        this.stack2 = this.stack1;
        this.stack1 = this.cardLayout.toString();
    }

    public void pop() {
        this.cardLayout.show(this, this.stack1);

        this.stack1 = this.stack2;
        this.stack2 = this.stack3;
    }
}