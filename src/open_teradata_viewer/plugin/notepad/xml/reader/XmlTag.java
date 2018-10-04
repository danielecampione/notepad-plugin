/*
 * Open Teradata Viewer ( notepad plugin xml reader )
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

package open_teradata_viewer.plugin.notepad.xml.reader;

import java.util.Vector;

import open_teradata_viewer.plugin.notepad.xml.XmlAttribute;
import open_teradata_viewer.plugin.notepad.xml.XmlCodec;
import open_teradata_viewer.plugin.notepad.xml.XmlElement;
import open_teradata_viewer.plugin.notepad.xml.XmlException;

/**
 * 
 * 
 * @author D. Campione
 *
 */
class XmlTag {

    private String sName;

    private String sValue;

    private int iTagType;

    private Vector<XmlAttribute> vAttribs = new Vector<XmlAttribute>();

    private Vector<XmlElement> vChildren = new Vector<XmlElement>();

    public XmlTag(String tag) throws XmlException {
        sValue = "";

        boolean bStartSlash = tag.startsWith("/");
        boolean bEndSlash = tag.endsWith("/");

        if ((bStartSlash) && (bEndSlash)) {
            throw new XmlException("Not a starting/ending tag --> " + tag);
        }
        if ((!bStartSlash) && (!bEndSlash)) {
            iTagType = 0;
        } else if ((bStartSlash) && (!bEndSlash)) {
            iTagType = 1;

            tag = tag.substring(1);
        } else {
            iTagType = 2;

            tag = tag.substring(0, tag.length() - 1);
        }

        tag = tag.trim();

        int iSpacePos = tag.indexOf(" ");

        if (iSpacePos != -1) {
            String sAttribs = tag.substring(iSpacePos + 1).trim();

            if (iTagType == 1) {
                throw new XmlException("Attributes go only in the starting tag");
            }
            handleAttribs(sAttribs);

            tag = tag.substring(0, iSpacePos);
        }

        if (!tag.matches("[a-zA-Z_\\-\\?][\\w\\-]*")) {
            throw new XmlException("Tag name is not valid --> " + tag);
        }
        sName = tag;
    }

    public String getName() {
        return sName;
    }
    public boolean isStartingTag() {
        return iTagType == 0;
    }
    public boolean isEndingTag() {
        return iTagType == 1;
    }
    public boolean isFullTag() {
        return iTagType == 2;
    }

    public void setValue(String value) {
        sValue = value;
    }

    public XmlElement getXmlElement() {
        XmlElement el = new XmlElement(sName);

        for (int i = 0; i < vAttribs.size(); i++) {
            XmlAttribute attr = (XmlAttribute) vAttribs.elementAt(i);

            el.setAttribute(attr);
        }

        for (int i = 0; i < vChildren.size(); i++) {
            XmlElement elChild = (XmlElement) vChildren.elementAt(i);

            el.addChild(elChild);
        }

        el.setValue(sValue);

        return el;
    }

    public void addChild(XmlElement el) {
        vChildren.addElement(el);
    }

    public String toString() {
        String s = "[Name:" + sName + ", Type:" + iTagType + ", Attribs:\n";

        for (int i = 0; i < vAttribs.size(); i++) {
            s = s + vAttribs.elementAt(i) + "\n";
        }
        return s + "]";
    }

    private void handleAttribs(String attribs) throws XmlException {
        while (true) {
            String sOldAttribs = attribs;

            int iEquPos = attribs.indexOf("=");

            if (iEquPos == -1) {
                throw new XmlException("Invalid attribute syntax --> "
                        + sOldAttribs);
            }
            String sAttrName = attribs.substring(0, iEquPos).trim();

            if (!sAttrName.matches("[a-zA-Z_\\-\\?][\\w\\-]*")) {
                throw new XmlException("Attribute name is not valid --> "
                        + sAttrName);
            }
            attribs = attribs.substring(iEquPos + 1).trim();

            if (!attribs.startsWith("\"")) {
                throw new XmlException(
                        "Attribute value must start with a '\"' --> "
                                + sOldAttribs);
            }
            attribs = attribs.substring(1);

            int iQuotPos = attribs.indexOf("\"");

            if (iQuotPos == -1) {
                throw new XmlException(
                        "Attribute value must end with a '\"' --> "
                                + sOldAttribs);
            }
            String sAttrValue = attribs.substring(0, iQuotPos);
            attribs = attribs.substring(iQuotPos + 1);

            vAttribs.addElement(new XmlAttribute(sAttrName, XmlCodec
                    .decode(sAttrValue)));

            if (attribs.equals("")) {
                return;
            }
            if (!attribs.substring(0, 1).matches("\\s+")) {
                throw new XmlException(
                        "Attributes must be separated by a white space --> "
                                + sOldAttribs);
            }
            attribs = attribs.trim();
        }
    }
}