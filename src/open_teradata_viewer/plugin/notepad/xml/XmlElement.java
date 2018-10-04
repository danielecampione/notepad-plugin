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

import java.util.List;
import java.util.Vector;

import open_teradata_viewer.plugin.notepad.Util;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class XmlElement {

    private String sName;

    private String sValue;

    private Vector<XmlAttribute> vAttributes = new Vector<XmlAttribute>();

    private Vector<XmlElement> vChildren = new Vector<XmlElement>();

    public XmlElement() {
        this("?", "");
    }

    public XmlElement(String name) {
        this(name, "");
    }

    public XmlElement(String name, String value) {
        setName(name);
        setValue(value);
    }

    public String getName() {
        return sName;
    }
    public String getValue() {
        return sValue;
    }

    public XmlElement setName(String name) {
        sName = name;

        return this;
    }

    public XmlElement setValue(String value) {
        sValue = value;

        return this;
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

    public boolean isLeaf() {
        return vChildren.size() == 0;
    }

    public XmlElement duplicate() {
        XmlElement e = new XmlElement(sName, sValue);

        for (int i = 0; i < vAttributes.size(); i++) {
            e.setAttribute(((XmlAttribute) vAttributes.get(i)).duplicate());
        }
        for (int i = 0; i < vChildren.size(); i++) {
            XmlElement child = (XmlElement) vChildren.get(i);
            e.addChild(child.duplicate());
        }

        return e;
    }

    public XmlElement setAttribute(XmlAttribute a) {
        for (int i = 0; i < vAttributes.size(); i++) {
            XmlAttribute atCurr = (XmlAttribute) vAttributes.elementAt(i);

            if (!a.getName().equals(atCurr.getName()))
                continue;
            vAttributes.setElementAt(a, i);

            return this;
        }

        vAttributes.addElement(a);

        return this;
    }

    public boolean removeAttribute(String name) {
        for (int i = 0; i < vAttributes.size(); i++) {
            XmlAttribute a = (XmlAttribute) vAttributes.elementAt(i);

            if (!a.getName().equals(name))
                continue;
            vAttributes.removeElementAt(i);

            return true;
        }

        return false;
    }

    public void removeAttributes() {
        vAttributes.removeAllElements();
    }

    public XmlAttribute getAttribute(String name) {
        for (int i = 0; i < vAttributes.size(); i++) {
            XmlAttribute a = (XmlAttribute) vAttributes.elementAt(i);

            if (a.getName().equals(name))
                return a;
        }

        return null;
    }

    public String getAttributeValue(String name) {
        XmlAttribute a = getAttribute(name);

        if (a != null)
            return a.getValue();

        return null;
    }

    public List<XmlAttribute> getAttributes() {
        return vAttributes;
    }

    public XmlElement addChild(XmlElement el) {
        vChildren.addElement(el);

        return this;
    }

    public XmlElement insertChildAt(int index, XmlElement el) {
        vChildren.insertElementAt(el, index);

        return this;
    }

    public boolean removeChild(String name) {
        for (int i = 0; i < vChildren.size(); i++) {
            XmlElement el = (XmlElement) vChildren.elementAt(i);

            if (!el.getName().equals(name))
                continue;
            vChildren.removeElementAt(i);

            return true;
        }

        return false;
    }

    public boolean removeChildren(String name) {
        int oldSize = vChildren.size();

        for (int i = 0; i < vChildren.size(); i++) {
            XmlElement el = (XmlElement) vChildren.elementAt(i);

            if (el.getName().equals(name)) {
                vChildren.removeElementAt(i--);
            }
        }
        return oldSize != vChildren.size();
    }

    public void removeChildren() {
        vChildren.removeAllElements();
    }

    public XmlElement getChild(String name) {
        for (int i = 0; i < vChildren.size(); i++) {
            XmlElement el = (XmlElement) vChildren.elementAt(i);

            if (el.getName().equals(name))
                return el;
        }

        return null;
    }

    public String getChildValue(String name) {
        XmlElement el = getChild(name);

        if (el != null)
            return el.getValue();

        return null;
    }

    public List<XmlElement> getChildren(String name) {
        Vector<XmlElement> v = new Vector<XmlElement>();

        for (int i = 0; i < vChildren.size(); i++) {
            XmlElement el = (XmlElement) vChildren.elementAt(i);

            if (!el.getName().equals(name))
                continue;
            v.addElement(el);
        }

        return v;
    }

    public List<XmlElement> getChildren() {
        return vChildren;
    }

    public List<XmlElement> preorderEnum() {
        Vector<XmlElement> v = new Vector<XmlElement>();

        v.add(this);

        for (int i = 0; i < vChildren.size(); i++) {
            XmlElement child = (XmlElement) vChildren.get(i);

            v.addAll(child.preorderEnum());
        }

        return v;
    }

    public String toString() {
        String s = "[Name:" + sName + ", Value:" + sValue + "]";

        return s;
    }
}