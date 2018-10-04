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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.html.CSS;
import javax.swing.text.html.HTML;

import open_teradata_viewer.plugin.notepad.xml.XmlAttribute;
import open_teradata_viewer.plugin.notepad.xml.XmlElement;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class DocEditor extends JPanel implements ActionListener, ItemListener {

    private static final long serialVersionUID = -1003037663846629822L;

    private static final Object arColors[] = {Color.BLACK, Color.RED,
            Color.GREEN, Color.BLUE, Color.CYAN, Color.MAGENTA, Color.YELLOW,
            Color.GRAY, Color.ORANGE, Color.PINK,};

    private HtmlEditorPanel panEditor = new HtmlEditorPanel();
    private HtmlToolkit toolKit;

    private AbstractButton tbParLeft;
    private AbstractButton tbParCenter;
    private AbstractButton tbParRight;
    private AbstractButton tbStyleBold;
    private AbstractButton tbStyleItal;
    private AbstractButton tbStyleUnder;
    private AbstractButton tbFontMonospc;
    private AbstractButton tbImage;
    private AbstractButton tbListOrder;
    private AbstractButton tbListUnorder;
    private AbstractButton tbStructure;

    private NotepadComboBox tcColor = new NotepadComboBox();
    private NotepadComboBox tcSize = new NotepadComboBox();

    private JFileChooser fcImage = new JFileChooser();

    private static final String PAR_LEFT = "par-left";
    private static final String PAR_CENTER = "par-center";
    private static final String PAR_RIGHT = "par-right";

    private static final String STYLE_BOLD = "style-bold";
    private static final String STYLE_ITALIC = "style-italic";
    private static final String STYLE_UNDERLINE = "style-underline";

    private static final String FONT_MONOSPACED = "font-monospaced";
    private static final String FONT_COLOR = "font-color";
    private static final String FONT_SIZE = "font-size";

    private static final String IMAGE = "image";
    private static final String LIST_ORDERED = "list-ordered";
    private static final String LIST_UNORDERED = "list-unordered";

    private static final String STRUCTURE = "structure";

    /** Ctor. */
    public DocEditor() {
        FlexLayout flex = new FlexLayout(1, 2);
        flex.setColProp(0, FlexLayout.EXPAND);
        flex.setRowProp(1, FlexLayout.EXPAND);
        setLayout(flex);

        // Setup toolbar
        NotepadToolBar tb = new NotepadToolBar();

        setTbParLeft(tb.add(ImageFactory.PAR_LEFT, this, PAR_LEFT,
                "Left alignment"));
        setTbParCenter(tb.add(ImageFactory.PAR_CENTER, this, PAR_CENTER,
                "Center"));
        setTbParRight(tb.add(ImageFactory.PAR_RIGHT, this, PAR_RIGHT,
                "Right alignment"));

        tb.addSeparator();

        setTbStyleBold(tb.add(ImageFactory.STYLE_BOLD, this, STYLE_BOLD,
                "Bold style"));
        setTbStyleItal(tb.add(ImageFactory.STYLE_ITALIC, this, STYLE_ITALIC,
                "Italic style"));
        setTbStyleUnder(tb.add(ImageFactory.STYLE_UNDERLINE, this,
                STYLE_UNDERLINE, "Underline"));
        setTbFontMonospc(tb.add(ImageFactory.FONT_MONOSPACED, this,
                FONT_MONOSPACED, "Monospaced font"));
        tb.add(tcColor);

        tb.addSeparator();

        tb.add("Size ");
        tb.add(tcSize);

        tb.addSeparator();

        setTbImage(tb.add(ImageFactory.IMAGE, this, IMAGE, "Insert an image"));
        setTbListOrder(tb.add(ImageFactory.LIST_ORDERED, this, LIST_ORDERED,
                "Insert an ordered list"));
        setTbListUnorder(tb.add(ImageFactory.LIST_UNORDERED, this,
                LIST_UNORDERED, "Insert an unordered list"));

        tb.addSeparator();

        setTbStructure(tb.add(ImageFactory.STRUCTURE, this, STRUCTURE,
                "See structure / edit properties"));

        // The setup panel
        add("0,0,x", tb);
        add("0,1,x,x", panEditor);

        toolKit = panEditor.getHtmlToolkit();

        // The setup combos
        for (int i = 0; i < arColors.length; i++) {
            Icon icon = new ColorIcon(24, 19, (Color) arColors[i]);
            tcColor.addItem(icon, i, null);
        }

        tcSize.addItem(8, " -2");
        tcSize.addItem(10, " -1");
        tcSize.addItem(12, "Normal");
        tcSize.addItem(14, " +1");
        tcSize.addItem(16, " +2");
        tcSize.addItem(20, " +3");
        tcSize.addItem(30, " +4");

        // Listener should be set after the previos 'addItem' because they
        // trigger the ItemStateChanged event
        tcColor.setPreferredSize(new Dimension(44, 26));
        tcColor.setActionCommand(FONT_COLOR);
        tcColor.setMaximumRowCount(arColors.length);
        tcColor.addItemListener(this);

        tcSize.setPreferredSize(new Dimension(80, 26));
        tcSize.setActionCommand(FONT_SIZE);
        tcSize.setMaximumRowCount(10);
        tcSize.setSelectedKey(12);
        tcSize.addItemListener(this);

        fcImage.setDialogTitle("Choose image to insert");
    }

    public void setDoc(XmlElement doc) {
        String html = toHtml(doc);

        if (html.equals(""))
            html = "<p></p>";

        html = "<html><head></head><body>" + html + "</body></html>";

        panEditor.setText(html);
        parseAttribs(retrieveBody(toolKit.getRootElement()));
    }

    public void getDoc(XmlElement doc) {
        Element body = retrieveBody(toolKit.getRootElement());

        doc.removeChildren();

        buildDoc(body, doc);
    }

    public Document getDocument() {
        return toolKit.getDocument();
    }

    /** The ItemListener. */
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.DESELECTED)
            return;

        String cmd = ((NotepadComboBox) e.getSource()).getActionCommand();

        if (cmd.equals(FONT_COLOR)) {
            Color c = (Color) arColors[tcColor.getSelectedIntKey()];

            toolKit.setFontColor(c);
        }

        else if (cmd.equals(FONT_SIZE))
            toolKit.setFontSize(tcSize.getSelectedIntKey());

        toolKit.requestFocus();
    }

    /** The ActionListener. */
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        if (cmd.equals(PAR_LEFT))
            handleParLeft();
        if (cmd.equals(PAR_CENTER))
            handleParCenter();
        if (cmd.equals(PAR_RIGHT))
            handleParRight();

        if (cmd.equals(STYLE_BOLD))
            handleStyleBold();
        if (cmd.equals(STYLE_ITALIC))
            handleStyleItalic();
        if (cmd.equals(STYLE_UNDERLINE))
            handleStyleUnderline();

        if (cmd.equals(FONT_MONOSPACED))
            handleFontMonospaced();

        if (cmd.equals(IMAGE))
            handleImage();
        if (cmd.equals(LIST_ORDERED))
            handleListOrdered();
        if (cmd.equals(LIST_UNORDERED))
            handleListUnordered();

        if (cmd.equals(STRUCTURE))
            handleStructure();

        toolKit.requestFocus();
    }

    private void handleParLeft() {
        toolKit.setAlignment(HtmlToolkit.ALIGN_LEFT);
    }

    private void handleParCenter() {
        toolKit.setAlignment(HtmlToolkit.ALIGN_CENTER);
    }

    private void handleParRight() {
        toolKit.setAlignment(HtmlToolkit.ALIGN_RIGHT);
    }

    private void handleStyleBold() {
        toolKit.setFontStyle(HtmlToolkit.STYLE_BOLD);
    }
    private void handleStyleItalic() {
        toolKit.setFontStyle(HtmlToolkit.STYLE_ITALIC);
    }
    private void handleStyleUnderline() {
        toolKit.setFontStyle(HtmlToolkit.STYLE_UNDERLINE);
    }

    private void handleFontMonospaced() {
        if (toolKit.getFont().getName().equals(HtmlToolkit.FONT_MONOSPACED))
            toolKit.setFontName(HtmlToolkit.FONT_SANS_SERIF);
        else
            toolKit.setFontName(HtmlToolkit.FONT_MONOSPACED);
    }

    private void handleImage() {
        int res = fcImage.showDialog(this, "Insert");

        if (res == JFileChooser.APPROVE_OPTION)
            toolKit.insertImage(fcImage.getSelectedFile().getPath());
    }

    private void handleListOrdered() {
        toolKit.insertOrderedItem();
    }
    private void handleListUnordered() {
        toolKit.insertUnorderedItem();
    }

    private void handleStructure() {
        ElementTreePanel elementTreeFrame = NotepadPlugin.getInstance()
                .getElementTreeFrame();
        if (elementTreeFrame == null) {
            elementTreeFrame = new ElementTreePanel(GuiUtil.getFrame(this));
            elementTreeFrame.setHtmlToolkit(toolKit);
            elementTreeFrame.showDialog();
        } else {
            elementTreeFrame.refresh();
            elementTreeFrame.setVisible(true);
        }
    }

    /** The method performs the conversion from DOC to HTML. */
    private String toHtml(XmlElement e) {
        StringBuffer sb = new StringBuffer();

        // Handle children
        List<?> list = e.getChildren();

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

    private String toHtmlList(String tag, XmlElement e) {
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

    private String toHtmlListItem(XmlElement e) {
        return "<li>" + toHtml(e) + "</li>";
    }

    private String toHtmlParagraph(XmlElement e) {
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
        List<XmlElement> list = e.getChildren();

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

    private String toHtmlText(XmlElement e) {
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

        // It seems that the editor cannot convert stuff like &apos; -> ' so we
        // cannot use the HtmlLib.cencode method
        String value;

        value = Util.replaceStr(e.getValue(), "<", "&lt;");
        value = Util.replaceStr(value, ">", "&gt;");

        return pre + value + post;
    }

    private String toHtmlImage(XmlElement e) {
        String src = e.getAttributeValue(DocManager.ATTR_IMAGE_SRC);

        return "<img align=\"bottom\" border=\"1\" src=\""
                + HtmlLib.encode(src) + "\"></img>";
    }

    private void parseAttribs(Element e) {
        SimpleAttributeSet sas = new SimpleAttributeSet(e.getAttributes());

        int start = e.getStartOffset();
        int end = e.getEndOffset();

        Object name = sas.getAttribute(StyleConstants.NameAttribute);

        if (name == HTML.Tag.P) {
            parseParag(sas);
            toolKit.getDocument().setParagraphAttributes(start, 0, sas, true);
        }

        else if (name == HTML.Tag.CONTENT) {
            parseContent(sas);
            toolKit.getDocument().setCharacterAttributes(start, end - start,
                    sas, true);
        }

        for (int i = 0; i < e.getElementCount(); i++)
            parseAttribs(e.getElement(i));
    }

    private void parseParag(SimpleAttributeSet sas) {
        Object align = sas.getAttribute(HTML.Attribute.ALIGN);

        if (align != null) {
            sas.removeAttribute(HTML.Attribute.ALIGN);

            String a = align.toString();

            if (a.equals("center"))
                sas.addAttribute(StyleConstants.ParagraphConstants.Alignment,
                        new Integer(
                                StyleConstants.ParagraphConstants.ALIGN_CENTER));

            else if (a.equals("right"))
                sas.addAttribute(StyleConstants.ParagraphConstants.Alignment,
                        new Integer(
                                StyleConstants.ParagraphConstants.ALIGN_RIGHT));
        }
    }

    private void parseContent(SimpleAttributeSet sas) {
        sas.removeAttribute(HTML.Tag.B);
        sas.removeAttribute(HTML.Tag.I);
        sas.removeAttribute(HTML.Tag.U);
        sas.removeAttribute(HTML.Tag.FONT);
    }

    /** The method performs the conversion from HTML to DOC. */
    private void buildDoc(Element elParent, XmlElement xmlParent) {
        for (int i = 0; i < elParent.getElementCount(); i++) {
            Element elChild = elParent.getElement(i);
            ElementInfo info = new ElementInfo(elChild);

            if ((info.tag == HTML.Tag.P || info.tag == HTML.Tag.IMPLIED)
                    && (info.start + 1 != info.end))
                xmlParent.addChild(buildParagraph(elChild, info));

            if (info.tag == HTML.Tag.OL)
                xmlParent.addChild(buildList(DocManager.OLIST, elChild));

            if (info.tag == HTML.Tag.UL)
                xmlParent.addChild(buildList(DocManager.ULIST, elChild));
        }
    }

    private XmlElement buildParagraph(Element elParag, ElementInfo info) {
        XmlElement xmlParag = new XmlElement(DocManager.PARAG);

        // Setup attributes
        String align = (String) info.attribs.get(CSS.Attribute.TEXT_ALIGN);

        if (align != null) {
            if (align.equals("center"))
                xmlParag.setAttribute(new XmlAttribute(DocManager.ATTR_ALIGN,
                        DocManager.ATTR_ALIGN_CENTER));

            if (align.equals("right"))
                xmlParag.setAttribute(new XmlAttribute(DocManager.ATTR_ALIGN,
                        DocManager.ATTR_ALIGN_RIGHT));
        }

        // Scan children
        for (int i = 0; i < elParag.getElementCount(); i++) {
            Element elChild = elParag.getElement(i);
            ElementInfo childInfo = new ElementInfo(elChild);

            if (childInfo.tag == HTML.Tag.CONTENT)
                if ((childInfo.start + 1 != childInfo.end)
                        || (i != elParag.getElementCount() - 1))
                    xmlParag.addChild(buildText(childInfo));

            if (childInfo.tag == HTML.Tag.IMG)
                xmlParag.addChild(buildImage(childInfo));
        }

        return xmlParag;
    }

    private XmlElement buildText(ElementInfo info) {
        XmlElement xmlText = new XmlElement(DocManager.TEXT);

        // Setup attributes
        String fontName = (String) info.attribs.get(CSS.Attribute.FONT_FAMILY);
        String fontSize = (String) info.attribs.get(CSS.Attribute.FONT_SIZE);
        String bold = (String) info.attribs.get(CSS.Attribute.FONT_WEIGHT);
        String italic = (String) info.attribs.get(CSS.Attribute.FONT_STYLE);
        String underline = (String) info.attribs
                .get(CSS.Attribute.TEXT_DECORATION);
        String color = (String) info.attribs.get(CSS.Attribute.COLOR);

        if (fontName != null && fontName.toLowerCase().equals("monospaced"))
            xmlText.setAttribute(new XmlAttribute(DocManager.ATTR_FONTNAME,
                    fontName));

        if (fontSize != null)
            xmlText.setAttribute(new XmlAttribute(DocManager.ATTR_FONTSIZE,
                    fontSize));

        if (bold != null && bold.equals("bold"))
            xmlText.setAttribute(new XmlAttribute(DocManager.ATTR_BOLD, "y"));

        if (italic != null && italic.equals("italic"))
            xmlText.setAttribute(new XmlAttribute(DocManager.ATTR_ITALIC, "y"));

        if (underline != null && underline.equals("underline"))
            xmlText.setAttribute(new XmlAttribute(DocManager.ATTR_UNDERLINE,
                    "y"));

        if (color != null)
            xmlText.setAttribute(new XmlAttribute(DocManager.ATTR_COLOR, color
                    .substring(1)));

        // Setup text
        try {
            xmlText.setValue(toolKit.getDocument().getText(info.start,
                    info.end - info.start));
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        return xmlText;
    }

    private XmlElement buildImage(ElementInfo info) {
        XmlElement xmlImage = new XmlElement(DocManager.IMAGE);

        String src = (String) info.attribs.get(HTML.Attribute.SRC);

        xmlImage.setAttribute(new XmlAttribute(DocManager.ATTR_IMAGE_SRC, src));

        return xmlImage;
    }

    private XmlElement buildList(String type, Element elList) {
        XmlElement xmlList = new XmlElement(type);

        for (int i = 0; i < elList.getElementCount(); i++)
            xmlList.addChild(buildListItem(elList.getElement(i)));

        return xmlList;
    }

    private XmlElement buildListItem(Element elItem) {
        XmlElement xmlItem = new XmlElement(DocManager.ITEM);

        buildDoc(elItem, xmlItem);

        return xmlItem;
    }

    private Element retrieveBody(Element e) {
        for (int i = 0; i < e.getElementCount(); i++) {
            Element child = e.getElement(i);

            if (child.getAttributes()
                    .getAttribute(StyleConstants.NameAttribute) == HTML.Tag.BODY)
                return child;
        }

        return null;
    }

    public HtmlEditorPanel getPaneEditor() {
        return panEditor;
    }

    public AbstractButton getTbParLeft() {
        return tbParLeft;
    }

    public void setTbParLeft(AbstractButton tbParLeft) {
        this.tbParLeft = tbParLeft;
    }

    public AbstractButton getTbParCenter() {
        return tbParCenter;
    }

    public void setTbParCenter(AbstractButton tbParCenter) {
        this.tbParCenter = tbParCenter;
    }

    public AbstractButton getTbParRight() {
        return tbParRight;
    }

    public void setTbParRight(AbstractButton tbParRight) {
        this.tbParRight = tbParRight;
    }

    public AbstractButton getTbStyleBold() {
        return tbStyleBold;
    }

    public void setTbStyleBold(AbstractButton tbStyleBold) {
        this.tbStyleBold = tbStyleBold;
    }

    public AbstractButton getTbStyleItal() {
        return tbStyleItal;
    }

    public void setTbStyleItal(AbstractButton tbStyleItal) {
        this.tbStyleItal = tbStyleItal;
    }

    public AbstractButton getTbStyleUnder() {
        return tbStyleUnder;
    }

    public void setTbStyleUnder(AbstractButton tbStyleUnder) {
        this.tbStyleUnder = tbStyleUnder;
    }

    public AbstractButton getTbFontMonospc() {
        return tbFontMonospc;
    }

    public void setTbFontMonospc(AbstractButton tbFontMonospc) {
        this.tbFontMonospc = tbFontMonospc;
    }

    public AbstractButton getTbImage() {
        return tbImage;
    }

    public void setTbImage(AbstractButton tbImage) {
        this.tbImage = tbImage;
    }

    public AbstractButton getTbListOrder() {
        return tbListOrder;
    }

    public void setTbListOrder(AbstractButton tbListOrder) {
        this.tbListOrder = tbListOrder;
    }

    public AbstractButton getTbListUnorder() {
        return tbListUnorder;
    }

    public void setTbListUnorder(AbstractButton tbListUnorder) {
        this.tbListUnorder = tbListUnorder;
    }

    public AbstractButton getTbStructure() {
        return tbStructure;
    }

    public void setTbStructure(AbstractButton tbStructure) {
        this.tbStructure = tbStructure;
    }
}