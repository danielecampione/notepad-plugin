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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class XmlWriter {

    private String sSpace;

    private String sNewLine;

    public XmlWriter() {
        this("\t", true);
    }

    public XmlWriter(String space, boolean putLF) {
        sSpace = space;
        sNewLine = (putLF ? System.getProperty("line.separator", "\n") : "");
    }

    public void write(String file, XmlDocument doc) throws IOException {
        Writer w = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
        try {
            write(w, doc);
        } finally {
            w.close();
        }
    }

    public void write(Writer w, XmlDocument doc) throws IOException {
        w.write(write(doc));
    }

    public String write(XmlDocument doc) {
        StringBuffer sb = new StringBuffer(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

        sb.append(sNewLine);
        sb.append(sNewLine);

        String docType = doc.getDocType();

        if (docType != null) {
            sb.append("<!DOCTYPE ");
            sb.append(docType);
            sb.append(">");
            sb.append(sNewLine);
            sb.append(sNewLine);
        }

        sb.append(write(doc.getRootElement()));

        return sb.toString();
    }

    public String write(XmlElement el) {
        return buildXml(el, "");
    }

    private String buildXml(XmlElement el, String prefix) {
        StringBuffer sb = new StringBuffer();

        sb.append(prefix);
        sb.append("<");
        sb.append(el.getName());

        List<?> listAttr = el.getAttributes();

        for (int i = 0; i < listAttr.size(); i++) {
            XmlAttribute attr = (XmlAttribute) listAttr.get(i);

            sb.append(" ");
            sb.append(attr.getName());
            sb.append("=");
            sb.append("\"" + XmlCodec.encode(attr.getValue()) + "\"");
        }

        List<?> listCh = el.getChildren();

        if (listCh.size() == 0) {
            if (el.getValue().equals("")) {
                sb.append("/");
            } else {
                sb.append(">");
                sb.append(XmlCodec.encode(el.getValue()));
                sb.append("</");
                sb.append(el.getName());
            }
            sb.append(">" + sNewLine);
        } else {
            sb.append(">" + sNewLine);

            for (int i = 0; i < listCh.size(); i++) {
                XmlElement elChild = (XmlElement) listCh.get(i);

                sb.append(buildXml(elChild, prefix + sSpace));
            }

            sb.append(prefix);
            sb.append("</");
            sb.append(el.getName());
            sb.append(">" + sNewLine);
        }

        return sb.toString();
    }
}