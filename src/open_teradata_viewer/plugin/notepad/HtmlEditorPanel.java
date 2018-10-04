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
import java.awt.Graphics;

import javax.swing.JEditorPane;
import javax.swing.JScrollPane;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class HtmlEditorPanel extends JScrollPane {

    private static final long serialVersionUID = 4306347925797275160L;

    private JEditorPane jep = new JEditorPane() {

        private static final long serialVersionUID = 7228916553457261442L;

        public void paintComponent(Graphics g) {
            CustomLook.setup(g);
            super.paintComponent(g);
        }

    };
    private HtmlToolkit kit;

    public HtmlEditorPanel() {
        this.kit = new HtmlToolkit(this.jep);

        setPreferredSize(new Dimension(700, 500));
        setViewportView(this.jep);
    }

    public void setText(String text) {
        this.jep.setText(text);
    }

    public String getText() {
        return this.jep.getText();
    }

    public HtmlToolkit getHtmlToolkit() {
        return this.kit;
    }

    public JEditorPane getTextComponent() {
        return jep;
    }
}