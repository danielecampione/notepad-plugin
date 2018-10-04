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
import java.util.List;
import java.util.Stack;

import open_teradata_viewer.plugin.notepad.xml.XmlAttribute;
import open_teradata_viewer.plugin.notepad.xml.XmlDocument;
import open_teradata_viewer.plugin.notepad.xml.XmlElement;
import open_teradata_viewer.plugin.notepad.xml.XmlException;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class XmlReader implements ContentHandler, ErrorHandler {

    private XmlSimpleReader oldReader;

    private XMLReader saxReader;

    private Stack<XmlElement> stack;

    private Locator locator;

    public XmlReader() {
        try {
            saxReader = XMLReaderFactory
                    .createXMLReader("org.apache.crimson.parser.XMLReaderImpl");

            saxReader.setContentHandler(this);
            saxReader.setErrorHandler(this);
        } catch (SAXException e) {
            oldReader = new XmlSimpleReader();
        }
    }

    public XmlDocument read(String file) throws FileNotFoundException,
            IOException, XmlException {
        if (oldReader != null) {
            return oldReader.read(file);
        }

        Reader r = new InputStreamReader(new FileInputStream(file), "UTF-8");
        try {
            XmlDocument localXmlDocument = read(r);
            return localXmlDocument;
        } finally {
            r.close();
        }
    }

    public XmlDocument read(Reader r) throws IOException, XmlException {
        if (oldReader != null) {
            return oldReader.read(r);
        }

        try {
            saxReader.parse(new InputSource(r));

            XmlElement elFake = (XmlElement) stack.pop();
            XmlElement elRoot = null;

            List<?> list = elFake.getChildren();

            if (list.size() != 0) {
                elRoot = (XmlElement) list.get(0);
            }
            return new XmlDocument(elRoot);
        } catch (SAXException e) {
            throw new XmlException(e.getMessage());
        } catch (IOException e) {
        }

        return null;
    }

    public void startDocument() {
        stack = new Stack<XmlElement>();
        stack.push(new XmlElement());
    }

    public void endDocument() {
    }

    public void processingInstruction(String target, String data) {
    }

    public void setDocumentLocator(Locator l) {
        setLocator(l);
    }

    public void startElement(String namespaceURI, String localName,
            String qName, Attributes atts) {
        XmlElement elParent = (XmlElement) stack.peek();
        XmlElement elChild = new XmlElement(localName);

        for (int i = 0; i < atts.getLength(); i++) {
            elChild.setAttribute(new XmlAttribute(atts.getLocalName(i), atts
                    .getValue(i)));
        }
        elParent.addChild(elChild);
        stack.push(elChild);
    }

    public void endElement(String namespaceURI, String localName, String qName) {
        stack.pop();
    }

    public void skippedEntity(String name) {
    }

    public void characters(char[] ch, int start, int length) {
        XmlElement el = (XmlElement) stack.peek();

        el.setValue(el.getValue() + new String(ch, start, length));
    }

    public void ignorableWhitespace(char[] ch, int start, int length) {
    }

    public void startPrefixMapping(String prefix, String uri) {
    }

    public void endPrefixMapping(String prefix) {
    }

    public void warning(SAXParseException e) {
    }

    public void error(SAXParseException e) {
    }

    public void fatalError(SAXParseException e) throws SAXException {
        throw new SAXException("Fatal error", e);
    }

    public Locator getLocator() {
        return locator;
    }

    public void setLocator(Locator locator) {
        this.locator = locator;
    }
}