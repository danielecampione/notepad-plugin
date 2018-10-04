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
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.Document;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class NotepadTextArea extends JScrollPane {

    private static final long serialVersionUID = -5063457171136070783L;

    private JTextArea txtArea;

    public NotepadTextArea() {
        this(10, 10);
    }

    public NotepadTextArea(int rows, int cols) {
        this("", rows, cols);
    }

    public NotepadTextArea(String text, int rows, int cols) {
        this(text, rows, cols, false);
    }

    public NotepadTextArea(String text, int rows, int cols, boolean isLabel) {
        this.txtArea = new JTextArea("", rows, cols) {

            private static final long serialVersionUID = 6674630369850461183L;

            public void paintComponent(Graphics g) {
                CustomLook.setup(g);
                super.paintComponent(g);
            }
        };
        setViewportView(this.txtArea);

        this.txtArea.setTabSize(3);
        this.txtArea.setLineWrap(true);
        this.txtArea.setWrapStyleWord(true);
        this.txtArea.setFont(CustomLook.defaultFont);

        setBorder(BorderFactory.createEtchedBorder(Color.white, Color.gray));
        setText(text);

        if (isLabel) {
            setEditable(false);
            setTextBackground(new Color(204, 204, 204));
            setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        }
    }

    public void setToolTipText(String text) {
        this.txtArea.setToolTipText(text);
    }

    public void scrollToTop() {
        getViewport().scrollRectToVisible(new Rectangle(0, 0, 10, 10));
    }

    public void setText(String text) {
        this.txtArea.setText(text);
    }

    public String getText() {
        return this.txtArea.getText();
    }

    public void append(String text) {
        this.txtArea.append(text);
    }

    public void setEditable(boolean yesno) {
        this.txtArea.setEditable(yesno);
    }

    public void setLineWrap(boolean yesno) {
        this.txtArea.setLineWrap(yesno);
    }

    public void setTextBackground(Color c) {
        this.txtArea.setBackground(c);
    }

    public void setFont(Font f) {
        if (this.txtArea != null)
            this.txtArea.setFont(f);
    }

    public void addKeyListener(KeyListener kl) {
        this.txtArea.addKeyListener(kl);
    }

    public void removeKeyListener(KeyListener kl) {
        this.txtArea.removeKeyListener(kl);
    }

    public JTextArea getTextArea() {
        return this.txtArea;
    }

    public Document getDocument() {
        return this.txtArea.getDocument();
    }
}