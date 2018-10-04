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

import java.awt.Graphics;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.JComboBox;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class NotepadComboBox extends JComboBox<Object> {

    private static final long serialVersionUID = 348899201365379841L;

    private Vector<Icon> vImages = new Vector<Icon>();
    private Vector<String> vKeys = new Vector<String>();

    public NotepadComboBox() {
        setRenderer(new NotepadComboRenderer(this));
        setPreferredSize(CustomLook.comboSize);
        setBackground(CustomLook.comboColor);
        setFont(CustomLook.comboFont);
    }

    public void addItem() {
    }

    public void addItem(String key, Object data) {
        addItem(null, key, data);
    }

    public void addItem(int key, Object data) {
        addItem(null, Integer.toString(key), data);
    }

    public void addItem(Icon img, String key, Object data) {
        this.vImages.addElement(img);
        this.vKeys.addElement(key);

        if (data == null) {
            data = "       " + key;
        }
        addItem(data);
    }

    public void addItem(Icon img, int key, Object data) {
        addItem(img, Integer.toString(key), data);
    }

    public String getKeyAt(int index) {
        return (String) this.vKeys.elementAt(index);
    }

    public int getIntKeyAt(int index) {
        return Integer.parseInt(getKeyAt(index));
    }

    public String getSelectedKey() {
        int index = getSelectedIndex();

        if (index == -1)
            return null;

        return (String) this.vKeys.elementAt(index);
    }

    public int getSelectedIntKey() {
        return Integer.parseInt(getSelectedKey());
    }

    public Icon getItemIcon(int index) {
        if (index == -1)
            return null;

        return (Icon) this.vImages.elementAt(index);
    }

    public void insertItemAt(String key, Object data, int index) {
        insertItemAt(null, key, data, index);
    }

    public void insertItemAt(Icon img, String key, Object data, int index) {
        this.vImages.insertElementAt(img, index);
        this.vKeys.insertElementAt(key, index);
        insertItemAt(data, index);
    }

    public void insertItemAt(Icon img, int key, Object data, int index) {
        insertItemAt(img, "" + key, data, index);
    }

    public void removeAllItems() {
        this.vImages.removeAllElements();
        this.vKeys.removeAllElements();
        super.removeAllItems();
    }

    public void removeItemAt(int index) {
        this.vImages.removeElementAt(index);
        this.vKeys.removeElementAt(index);
        super.removeItemAt(index);
    }

    public void setSelectedKey(String key) {
        int index = this.vKeys.indexOf(key);

        if (index == -1)
            return;

        setSelectedIndex(index);
    }

    public void setSelectedKey(int key) {
        setSelectedKey("" + key);
    }

    public Object getItemFromKey(String key) {
        int index = this.vKeys.indexOf(key);

        if (index == -1) {
            throw new IllegalArgumentException("Key not found:" + key);
        }
        return getItemAt(index);
    }

    public Object getItemFromKey(int key) {
        return getItemFromKey("" + key);
    }

    public void paintComponent(Graphics g) {
        CustomLook.setup(g);
        super.paintComponent(g);
    }
}