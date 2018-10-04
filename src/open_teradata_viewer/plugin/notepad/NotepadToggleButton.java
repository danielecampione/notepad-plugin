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
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JToggleButton;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class NotepadToggleButton extends JToggleButton {

    private static final long serialVersionUID = 2322633705830204995L;

    private boolean bInside = false;

    public NotepadToggleButton(String label, String command, ActionListener al) {
        super(label);

        setActionCommand(command);
        addActionListener(al);
        setPreferredSize(CustomLook.buttonSize);
        setBackground(CustomLook.buttonColor);
        setFont(CustomLook.buttonFont);
    }

    public NotepadToggleButton(ImageIcon img, String command, ActionListener al,
            String toolTip) {
        super(img);

        setActionCommand(command);
        addActionListener(al);
        addMouseListener(new TListener());
        setBorderPainted(false);
        setFocusPainted(false);
        setToolTipText(toolTip);

        setPreferredSize(CustomLook.buttonSmallSize);
        setMinimumSize(CustomLook.buttonSmallSize);
        setMaximumSize(CustomLook.buttonSmallSize);
    }

    public void paintComponent(Graphics g) {
        CustomLook.setup(g);
        super.paintComponent(g);

        if ((this.bInside) && (isEnabled())) {
            Color c1 = Color.white;
            Color c2 = Color.gray;

            if (isSelected()) {
                Color c = c1;
                c1 = c2;
                c2 = c;
            }

            int w = getWidth() - 1;
            int h = getHeight() - 1;

            g.setColor(c1);
            g.drawLine(0, 0, w, 0);
            g.drawLine(0, 0, 0, h);

            g.setColor(c2);
            g.drawLine(0, h, w, h);
            g.drawLine(w, h, w, 0);
        }
    }

    private class TListener extends MouseAdapter {

        private TListener() {
        }

        public void mouseEntered(MouseEvent e) {
            NotepadToggleButton.this.repaint();
        }

        public void mouseExited(MouseEvent e) {
            NotepadToggleButton.this.repaint();
        }
    }
}