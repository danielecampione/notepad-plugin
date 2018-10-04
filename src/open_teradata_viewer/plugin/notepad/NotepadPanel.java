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

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class NotepadPanel extends JPanel {

    private static final long serialVersionUID = 7165590965279916098L;

    private TitledBorder t;

    public NotepadPanel(String title) {
        this(title, null);
    }

    public NotepadPanel(String title, Component c) {
        Border b = BorderFactory.createEtchedBorder();

        this.t = BorderFactory.createTitledBorder(b, title);
        this.t.setTitleJustification(1);
        this.t.setTitlePosition(2);
        this.t.setTitleColor(Color.black);

        setBorder(this.t);
        setFont(CustomLook.titleFont);

        if (c != null) {
            FlexLayout fl = new FlexLayout(1, 1);
            fl.setColProp(0, 1);
            fl.setRowProp(0, 1);
            setLayout(fl);

            add("0,0,x,x", c);
        }
    }

    public void setTitle(String title) {
        this.t.setTitle(title);
        repaint();
    }

    public void paintComponent(Graphics g) {
        CustomLook.setup(g);
        super.paintComponent(g);
    }
}