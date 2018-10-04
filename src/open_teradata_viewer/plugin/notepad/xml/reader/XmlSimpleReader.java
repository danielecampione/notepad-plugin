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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Vector;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import open_teradata_viewer.plugin.notepad.xml.XmlCodec;
import open_teradata_viewer.plugin.notepad.xml.XmlDocument;
import open_teradata_viewer.plugin.notepad.xml.XmlElement;
import open_teradata_viewer.plugin.notepad.xml.XmlException;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class XmlSimpleReader {

    public XmlDocument read(String file) throws FileNotFoundException,
            IOException, XmlException {
        Reader r = new InputStreamReader(new FileInputStream(file), "UTF-8");
        try {
            XmlDocument localXmlDocument = read(r);
            return localXmlDocument;
        } finally {
            r.close();
        }
    }

    public XmlDocument read(Reader r) throws IOException, XmlException {
        StringBuffer sb = new StringBuffer(131072);

        char[] caData = new char[16384];
        int bytes;
        while ((bytes = r.read(caData)) != -1) {
            sb.append(caData, 0, bytes);
        }
        return parse(sb.toString());
    }

    private XmlDocument parse(String data) throws XmlException {
        Vector<Object> vTokens = tokenize(data);
        try {
            XmlElement elRoot = buildTree(vTokens);

            return new XmlDocument(elRoot);
        } catch (XmlException e) {
            printStringOfTokens(vTokens);
        }
        return null;

    }

    private Vector<Object> tokenize(String data) throws XmlException {
        Vector<Object> vTokens = new Vector<Object>(131072);
        while (true) {
            int iPosLT = data.indexOf("<");
            int iPosGT = data.indexOf(">");

            if ((iPosLT == -1) && (iPosGT == -1)) {
                vTokens.addElement(data);
                return vTokens;
            }

            if (iPosLT == -1) {
                throw new XmlException(
                        "Found '>' simbol without preceding '<' simbol");
            }

            if (iPosGT == -1) {
                throw new XmlException("Closing '>' simbol not found");
            }

            if (iPosLT > iPosGT) {
                throw new XmlException(
                        "Found '>' simbol before relative '<' simbol");
            }
            String sTag = data.substring(iPosLT + 1, iPosGT).trim();

            if (iPosLT > 0) {
                String prefix = data.substring(0, iPosLT);

                if (!prefix.trim().equals("")) {
                    vTokens.addElement(prefix);
                }
            }
            data = data.substring(iPosGT + 1);

            if ((!sTag.startsWith("?xml ")) && (!sTag.startsWith("!-- ")))
                vTokens.addElement(new XmlTag(sTag));
        }
    }

    private XmlElement buildTree(Vector<Object> vTokens) throws XmlException {
        if (vTokens.size() != 0) {
            applyRule5(vTokens, 0);
        }
        if (vTokens.size() > 0) {
            applyRule5(vTokens, vTokens.size() - 1);
        }
        if (vTokens.size() == 0) {
            return new XmlElement();
        }

        while (true) {
            boolean bMatched = false;

            for (int i = vTokens.size() - 2; i >= 0; i--) {
                while (applyRule4(vTokens, i)) {
                    bMatched = true;
                }
                if (applyRule1(vTokens, i))
                    bMatched = true;
                if (applyRule2(vTokens, i))
                    bMatched = true;
                if (!applyRule3(vTokens, i))
                    continue;
                bMatched = true;
            }

            if (!bMatched) {
                if ((vTokens.size() == 1)
                        && ((vTokens.elementAt(0) instanceof XmlElement))) {
                    return (XmlElement) vTokens.elementAt(0);
                }
                throw new XmlException("Unable to build document");
            }
        }
    }

    private boolean applyRule1(Vector<Object> v, int i) {
        if (i + 2 > v.size())
            return false;

        if (!(v.elementAt(i) instanceof XmlTag))
            return false;
        if (!(v.elementAt(i + 1) instanceof String))
            return false;

        XmlTag xmlTag = (XmlTag) v.elementAt(i);
        String sValue = (String) v.elementAt(i + 1);

        if (!xmlTag.isStartingTag())
            return false;

        xmlTag.setValue(XmlCodec.decode(sValue));
        v.removeElementAt(i + 1);

        return true;
    }

    private boolean applyRule2(Vector<Object> v, int i) {
        if (i + 2 > v.size())
            return false;

        if (!(v.elementAt(i) instanceof XmlTag))
            return false;
        if (!(v.elementAt(i + 1) instanceof XmlTag))
            return false;

        XmlTag startingTag = (XmlTag) v.elementAt(i);
        XmlTag endingTag = (XmlTag) v.elementAt(i + 1);

        if (!startingTag.isStartingTag())
            return false;
        if (!endingTag.isEndingTag())
            return false;
        if (!startingTag.getName().equals(endingTag.getName()))
            return false;

        XmlElement el = startingTag.getXmlElement();

        v.setElementAt(el, i);
        v.removeElementAt(i + 1);

        return true;
    }

    private boolean applyRule3(Vector<Object> v, int i) {
        if (!(v.elementAt(i) instanceof XmlTag))
            return false;

        XmlTag xmlTag = (XmlTag) v.elementAt(i);

        if (!xmlTag.isFullTag())
            return false;

        v.setElementAt(xmlTag.getXmlElement(), i);

        return true;
    }

    private boolean applyRule4(Vector<Object> v, int i) {
        if (i + 2 > v.size())
            return false;

        if (!(v.elementAt(i) instanceof XmlTag))
            return false;
        if (!(v.elementAt(i + 1) instanceof XmlElement))
            return false;

        XmlTag xmlTag = (XmlTag) v.elementAt(i);
        XmlElement elChild = (XmlElement) v.elementAt(i + 1);

        if (!xmlTag.isStartingTag())
            return false;

        xmlTag.addChild(elChild);
        v.removeElementAt(i + 1);

        return true;
    }

    private boolean applyRule5(Vector<Object> v, int i) throws XmlException {
        if (!(v.elementAt(i) instanceof String)) {
            return false;
        }
        String s = (String) v.elementAt(i);

        if (!s.trim().equals("")) {
            throw new XmlException(
                    "Bad text before starting (or after ending) tag");
        }
        v.removeElementAt(i);

        return true;
    }

    private void printStringOfTokens(Vector<Object> tokens) {
        ApplicationFrame.getInstance().getConsole().println("Tokens");

        for (int i = 0; i < tokens.size(); i++) {
            ApplicationFrame.getInstance().getConsole()
                    .print("[ " + i + " ] : ");

            Object obj = tokens.elementAt(i);

            if ((obj instanceof String))
                ApplicationFrame.getInstance().getConsole()
                        .println("[String]<" + obj + ">");
            else
                ApplicationFrame.getInstance().getConsole()
                        .println("[" + obj.getClass().getName() + "]" + obj);
        }
    }
}