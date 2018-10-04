/*
 * Open Teradata Viewer ( notepad plugin xml )
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

package open_teradata_viewer.plugin.notepad.xml;

import open_teradata_viewer.plugin.notepad.Util;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class XmlAttribute {

    private String sName;

    private String sValue;

    public XmlAttribute(String name, int value) {
        this(name, value + "");
    }

    public XmlAttribute(String name, float value) {
        this(name, value + "");
    }

    public XmlAttribute(String name, boolean value) {
        this(name, value + "");
    }

    public XmlAttribute(String name, String value) {
        setName(name);
        setValue(value);
    }

    public String getName() {
        return sName;
    }

    public void setName(String name) {
        sName = name;
    }

    public XmlAttribute duplicate() {
        return new XmlAttribute(sName, sValue);
    }

    public String toString() {
        return "[" + sName + "='" + sValue + "']";
    }

    public String getValue() {
        return sValue;
    }

    public int getIntValue(int defValue) {
        return Util.getIntValue(sValue, defValue);
    }
    public float getFloatValue(float defValue) {
        return Util.getFloatValue(sValue, defValue);
    }
    public boolean getBooleanValue(boolean defValue) {
        return Util.getBooleanValue(sValue, defValue);
    }

    public void setValue(String value) {
        sValue = value;
    }

    public void setValue(int value) {
        setValue(value + "");
    }
    public void setValue(float value) {
        setValue(value + "");
    }
    public void setValue(boolean value) {
        setValue(value + "");
    }
}