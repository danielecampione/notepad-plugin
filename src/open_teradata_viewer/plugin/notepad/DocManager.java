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

import java.util.List;

import open_teradata_viewer.plugin.notepad.xml.XmlElement;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class DocManager {

    public static final String PARAG = "p";
    public static final String TEXT = "t";
    public static final String IMAGE = "i";
    public static final String OLIST = "ol";
    public static final String ULIST = "ul";
    public static final String ITEM = "li";

    /** ATTR_ALIGN is a paragraph attribute. */
    public static final String ATTR_ALIGN = "a";

    /** ATTR_ALIGN_LEFT is a paragraph attribute. */
    public static final String ATTR_ALIGN_LEFT = "l";

    /** ATTR_ALIGN_CENTER is a paragraph attribute. */
    public static final String ATTR_ALIGN_CENTER = "c";

    /** ATTR_ALIGN_RIGHT is a paragraph attribute. */
    public static final String ATTR_ALIGN_RIGHT = "r";

    /** ATTR_BOLD is a text attribute. */
    public static final String ATTR_BOLD = "b";

    /** ATTR_ITALIC is a text attribute. */
    public static final String ATTR_ITALIC = "i";

    /** ATTR_UNDERLINE is a text attribute. */
    public static final String ATTR_UNDERLINE = "u";

    /** ATTR_COLOR is a text attribute. */
    public static final String ATTR_COLOR = "c";

    /** ATTR_FONTNAME is a text attribute. */
    public static final String ATTR_FONTNAME = "fn";

    /** ATTR_FONTSIZE is a text attribute. */
    public static final String ATTR_FONTSIZE = "fs";

    /** ATTR_IMAGE_SRC is an image attribute. */
    public static final String ATTR_IMAGE_SRC = "s";

    public static void convert(XmlElement xmlDoc, String text) {
        XmlElement elPar = new XmlElement(PARAG);
        XmlElement elText = new XmlElement(TEXT, text);

        elPar.addChild(elText);
        xmlDoc.addChild(elPar);
    }

    /** The method performs the conversion from DOC to ASCII. */
    public static String toText(XmlElement docs) {
        StringBuffer sb = new StringBuffer();

        // Handle children
        List<?> list = docs.getChildren();

        for (int i = 0; i < list.size(); i++) {
            XmlElement child = (XmlElement) list.get(i);

            if (child.getName().equals(DocManager.PARAG))
                sb.append(toTextParagraph(child));

            if (child.getName().equals(DocManager.OLIST))
                sb.append(toTextList(true, child));

            if (child.getName().equals(DocManager.ULIST))
                sb.append(toTextList(false, child));
        }

        return sb.toString().trim();
    }

    private static String toTextList(boolean enumerate, XmlElement e) {
        StringBuffer sb = new StringBuffer("\n");

        // Handle children
        List<?> list = e.getChildren();

        for (int i = 0; i < list.size(); i++) {
            XmlElement child = (XmlElement) list.get(i);

            String prefix = (enumerate) ? Integer.toString(i + 1) : "-";

            if (child.getName().equals(DocManager.ITEM))
                sb.append(prefix + " " + toText(child));
        }

        return sb.toString();
    }

    private static String toTextParagraph(XmlElement e) {
        StringBuffer sb = new StringBuffer();

        // Handle children (if any)
        List<?> list = e.getChildren();

        for (int i = 0; i < list.size(); i++) {
            XmlElement child = (XmlElement) list.get(i);

            if (child.getName().equals(DocManager.TEXT))
                sb.append(child.getValue());

            if (child.getName().equals(DocManager.IMAGE))
                sb.append("<image>");
        }

        // Append closing tag
        sb.append(Config.os.lineSep);

        return sb.toString();
    }

    /** The method performs the conversion from DOC to HTML. */
    public static String toHtml(XmlElement docs) {
        StringBuffer sb = new StringBuffer();

        // Handle children
        List<?> list = docs.getChildren();

        for (int i = 0; i < list.size(); i++) {
            XmlElement child = (XmlElement) list.get(i);

            if (child.getName().equals(DocManager.PARAG))
                sb.append(toHtmlParagraph(child));

            if (child.getName().equals(DocManager.OLIST))
                sb.append(toHtmlList("ol", child));

            if (child.getName().equals(DocManager.ULIST))
                sb.append(toHtmlList("ul", child));
        }

        return sb.toString();
    }

    private static String toHtmlList(String tag, XmlElement e) {
        StringBuffer sb = new StringBuffer();

        sb.append("<" + tag + ">");

        // Handle children
        List<?> list = e.getChildren();

        for (int i = 0; i < list.size(); i++) {
            XmlElement child = (XmlElement) list.get(i);

            if (child.getName().equals(DocManager.ITEM))
                sb.append(toHtmlListItem(child));
        }

        sb.append("</" + tag + ">");

        return sb.toString();
    }

    private static String toHtmlListItem(XmlElement e) {
        return "<li>" + toHtml(e) + "</li>";
    }

    private static String toHtmlParagraph(XmlElement e) {
        StringBuffer sb = new StringBuffer();

        // Handle align attrib
        String align = e.getAttributeValue(DocManager.ATTR_ALIGN);

        if (align == null)
            sb.append("<p>");
        else {
            sb.append("<p align=\"");

            if (align.equals(DocManager.ATTR_ALIGN_RIGHT))
                sb.append("right");

            else if (align.equals(DocManager.ATTR_ALIGN_CENTER))
                sb.append("center");

            else
                sb.append("left");

            sb.append("\">");
        }

        // Handle children (if any)
        List<?> list = e.getChildren();

        for (int i = 0; i < list.size(); i++) {
            XmlElement child = (XmlElement) list.get(i);

            if (child.getName().equals(DocManager.TEXT))
                sb.append(toHtmlText(child));

            if (child.getName().equals(DocManager.IMAGE))
                sb.append(toHtmlImage(child));
        }

        // Append closing tag
        sb.append("</p>");

        return sb.toString();
    }

    private static String toHtmlText(XmlElement e) {
        String pre = "";
        String post = "";

        String bold = e.getAttributeValue(DocManager.ATTR_BOLD);
        String ital = e.getAttributeValue(DocManager.ATTR_ITALIC);
        String under = e.getAttributeValue(DocManager.ATTR_UNDERLINE);
        String col = e.getAttributeValue(DocManager.ATTR_COLOR);
        String font = e.getAttributeValue(DocManager.ATTR_FONTNAME);
        String size = e.getAttributeValue(DocManager.ATTR_FONTSIZE);

        if (font != null || size != null || col != null) {
            pre = pre + "<font";

            if (font != null)
                pre = pre + " face=\"" + font + "\"";

            if (size != null)
                pre = pre + " size=\"" + size + "\"";

            if (col != null)
                pre = pre + " color=\"#" + col + "\"";

            pre = pre + ">";
            post = "</font>" + post;
        }

        if (bold != null) {
            pre = pre + "<b>";
            post = "</b>" + post;
        }

        if (ital != null) {
            pre = pre + "<i>";
            post = "</i>" + post;
        }

        if (under != null) {
            pre = pre + "<u>";
            post = "</u>" + post;
        }

        return pre + HtmlLib.encode(e.getValue()) + post;
    }

    private static String toHtmlImage(XmlElement e) {
        String src = e.getAttributeValue(DocManager.ATTR_IMAGE_SRC);

        return "<img align=\"bottom\" border=\"1\" src=\"" + src + "\"></img>";
    }
}