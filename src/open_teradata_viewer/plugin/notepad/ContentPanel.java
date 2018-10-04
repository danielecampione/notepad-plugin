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
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.text.BadLocationException;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class ContentPanel extends JPanel {

    private static final long serialVersionUID = 6492917251094168196L;

    private NotepadTextArea taContent = new NotepadTextArea(4, 20);
    private FlexTable ftAttribs = new FlexTable(false);

    private DefaultFlexTableModel model = new DefaultFlexTableModel();

    /** Ctor. */
    public ContentPanel() {
        FlexLayout fl = new FlexLayout(1, 2);
        fl.setColProp(0, FlexLayout.EXPAND);
        fl.setRowProp(1, FlexLayout.EXPAND);
        setLayout(fl);

        add("0,0,x", new NotepadPanel("Attribs", ftAttribs));
        add("0,1,x,x", new NotepadPanel("Content", taContent));

        model.addColumn("Name", 100);
        model.addColumn("Value", 100);
        ftAttribs.setFlexModel(model);
        ftAttribs.setPreferredSize(new Dimension(250, 130));

        taContent.setEditable(false);
    }

    public void refresh(ElementInfo info, HtmlToolkit kit) {
        fillTable(info);

        try {
            String text = kit.getDocument().getText(info.start,
                    info.end - info.start);

            taContent.setText(text);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private void fillTable(ElementInfo info) {
        model.clearData();

        for (Enumeration<?> e = info.attribs.keys(); e.hasMoreElements();) {
            Object name = e.nextElement();
            Object value = info.attribs.get(name);

            Vector<Object> row = new Vector<Object>();
            row.add(name);
            row.add(value);

            model.addRow(row);
        }

        ftAttribs.updateTable();
    }
}