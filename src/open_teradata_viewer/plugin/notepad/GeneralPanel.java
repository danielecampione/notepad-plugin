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

import java.io.File;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class GeneralPanel extends JPanel {

    private static final long serialVersionUID = -4046816139581633089L;

    private NotepadCheckBox chbReload = new NotepadCheckBox("Reload last project");
    private NotepadCheckBox chbTip = new NotepadCheckBox("Show 'Tip of the day' next time");
    private NotepadCheckBox chbBackup = new NotepadCheckBox(
            "Create backup files when saving");
    private NotepadCheckBox chbTextAA = new NotepadCheckBox("Enable text anti aliasing");
    private NotepadCheckBox chbGuiAA = new NotepadCheckBox("Enable GUI anti aliasing");
    private JTextField txtTermin = new NotepadTextField(); //WCC
    private NotepadComboBox sqlSyntax = new NotepadComboBox();

    public GeneralPanel() {
        FlexLayout flexL = new FlexLayout(2, 7);
        flexL.setColProp(1, FlexLayout.EXPAND);
        setLayout(flexL);

        add("0,0,x,c,2", chbReload);
        add("0,1,x,c,2", chbTip);
        add("0,2,x,c,2", chbBackup);
        add("0,3,x,c,2", chbTextAA);
        add("0,4,x,c,2", chbGuiAA);

        add("0,5", new NotepadLabel("SQL Syntax"));
        add("0,6", new NotepadLabel("SQL Terminator"));
        add("1,5,x", sqlSyntax);
        add("1,6,x", txtTermin);
    }

    public void refresh() {
        chbReload.setSelected(Config.general.reloadLastProj);
        chbTip.setSelected(Config.general.showTip);
        chbBackup.setSelected(Config.general.createBackup);
        chbTextAA.setSelected(Config.general.isTextAAliased());
        chbGuiAA.setSelected(Config.general.guiAAliasing);
        txtTermin.setText(Config.general.sqlTerminator);
        sqlSyntax.removeAllItems();

        sqlSyntax.addItem();

        for (String syn : getSyntaxList())
            sqlSyntax.addItem(syn, syn);

        sqlSyntax.setSelectedItem(Config.general.sqlSyntax);
    }

    public void store() {
        Config.general.reloadLastProj = chbReload.isSelected();
        Config.general.showTip = chbTip.isSelected();
        Config.general.createBackup = chbBackup.isSelected();
        Config.general.guiAAliasing = chbGuiAA.isSelected();

        Config.general.setTextAAliasing(chbTextAA.isSelected());
        Config.general.sqlTerminator = txtTermin.getText();
        Config.general.sqlSyntax = sqlSyntax.getSelectedItem().toString();
    }

    /**
     * Get a list of possible dialects.
     * This method reads data/syntax directory, and return a list of xml files
     * found.
     * 
     * @return a list of available dialects
     */

    private ArrayList<String> getSyntaxList() {
        File dir = new File(System.getProperty("dir.data", "/data")
                + File.separator + "syntax");

        ArrayList<String> retval = new ArrayList<String>();
        File[] files = dir.listFiles();

        for (int i = 0; i < files.length; i++) {
            String fname = files[i].getName();

            if (fname.substring(fname.length() - 3, fname.length()).compareTo(
                    "xml") == 0)
                retval.add(fname.substring(0, fname.length() - 4));
        }

        return retval;
    }
}