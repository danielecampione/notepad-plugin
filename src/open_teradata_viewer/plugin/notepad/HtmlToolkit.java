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
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.Hashtable;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JEditorPane;
import javax.swing.KeyStroke;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.Keymap;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class HtmlToolkit {

    private Hashtable<Object, Action> htActions = new Hashtable<Object, Action>();
    private JEditorPane jep;
    private HTMLDocument htmlDoc;
    private HTMLEditorKit htmlKit;
    public static final int STYLE_BOLD = 0;
    public static final int STYLE_ITALIC = 1;
    public static final int STYLE_UNDERLINE = 2;
    public static final int ALIGN_LEFT = 0;
    public static final int ALIGN_CENTER = 1;
    public static final int ALIGN_RIGHT = 2;
    public static final String FONT_SANS_SERIF = "SansSerif";
    public static final String FONT_MONOSPACED = "Monospaced";

    HtmlToolkit(JEditorPane p) {
        this.jep = p;
        this.jep.setContentType("text/html");

        this.htmlDoc = ((HTMLDocument) this.jep.getDocument());
        this.htmlKit = ((HTMLEditorKit) this.jep.getEditorKit());

        StyleSheet ss = this.htmlDoc.getStyleSheet();

        ss.addRule("body { font-family: sans-serif, default; font-size: 12pt; white-space: pre }");

        initActionTable();
        initKeyBindings();
    }

    public void requestFocus() {
        this.jep.requestFocus();
    }

    public void setText(String text) {
        this.jep.setText(text);
    }

    public String getText() {
        return this.jep.getText();
    }

    public void replaceSelection(String text) {
        this.jep.replaceSelection(text);
    }

    public HTMLDocument getDocument() {
        return this.htmlDoc;
    }
    public JEditorPane getEditorPane() {
        return this.jep;
    }

    public Element getParagraph() {
        return this.htmlDoc.getParagraphElement(pos());
    }

    public Element getRootElement() {
        return this.htmlDoc.getDefaultRootElement();
    }

    public int getCaretPosition() {
        return pos();
    }

    public String getWordAt(int pos) {
        try {
            int start = getWordStart(pos);
            int end = getWordEnd(pos);

            if (start == end) {
                return null;
            }
            return this.htmlDoc.getText(start, end - start);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getWordStart(int pos) {
        while (pos > 0) {
            if (isWhiteSpaceAt(pos - 1)) {
                break;
            }
            pos--;
        }

        return pos;
    }

    public int getWordEnd(int pos) {
        while (pos < this.htmlDoc.getLength()) {
            if (isWhiteSpaceAt(pos)) {
                break;
            }
            pos++;
        }

        return pos;
    }

    public boolean isWordAt(int pos) {
        return !isWhiteSpaceAt(pos);
    }

    public boolean isSelectionSet() {
        return this.jep.getSelectionStart() != this.jep.getSelectionEnd();
    }

    public void selectWord() {
        int start = getWordStart(pos());
        int end = getWordEnd(pos());

        this.jep.setSelectionStart(start);
        this.jep.setSelectionEnd(end);
    }

    public void setFontStyle(int style) {
        if (style == 0)
            invoke("font-bold");
        if (style == 1)
            invoke("font-italic");
        if (style == 2)
            invoke("font-underline");
    }

    public void setFontColor(Color c) {
        invoke(new StyledEditorKit.ForegroundAction("", c), "");
    }

    public void setFontSize(int size) {
        invoke(new StyledEditorKit.FontSizeAction("", size));
    }

    public void setFontName(String name) {
        invoke(new StyledEditorKit.FontFamilyAction("", name));
    }

    public Font getFont() {
        return this.htmlDoc.getFont(this.htmlDoc.getCharacterElement(pos())
                .getAttributes());
    }

    public void setAlignment(int align) {
        invoke(new StyledEditorKit.AlignmentAction("", align));
    }

    public Element getElementAt(int pos) {
        return this.htmlDoc.getCharacterElement(pos);
    }

    public boolean isWhiteSpaceAt(int pos) {
        try {
            String c = this.htmlDoc.getText(pos, 1);

            return (c.equals(" ")) || (c.equals("\n")) || (c.equals("\t"));
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void insertOrderedItem() {
        insertListItem(HTML.Tag.OL);
    }

    public void insertUnorderedItem() {
        insertListItem(HTML.Tag.UL);
    }

    public void insertImage(String file) {
        int pos = pos();

        Element el = getElementAt(pos);

        if (el.getStartOffset() + 1 == el.getEndOffset()) {
            pos = el.getEndOffset();
        }
        try {
            String html = "<img align=bottom border=1 src=\"file:" + file
                    + "\"></img>";

            this.htmlKit.insertHTML(this.htmlDoc, pos(), html, 0, 0,
                    HTML.Tag.IMG);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertAnchor(final String href) {
        AbstractAction action = new AbstractAction(href) {

            private static final long serialVersionUID = 1271832669737379637L;

            public void actionPerformed(ActionEvent e) {
                try {
                    String text = HtmlToolkit.this.jep.getSelectedText();

                    HtmlToolkit.this.htmlDoc.remove(
                            HtmlToolkit.this.jep.getSelectionStart(),
                            text.length());
                    String html = "<" + HTML.Tag.A + " href=\"" + href + "\">"
                            + text + "</" + HTML.Tag.A + ">";

                    HtmlToolkit.this.htmlKit.insertHTML(
                            HtmlToolkit.this.htmlDoc, HtmlToolkit.this.pos(),
                            html, 0, 0, HTML.Tag.A);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        };
        invoke(action);
    }

    private void initActionTable() {
        Action[] aa = this.jep.getActions();

        for (int i = 0; i < aa.length; i++)
            this.htActions.put(aa[i].getValue("Name"), aa[i]);
    }

    private void initKeyBindings() {
        Keymap keymap = JEditorPane.addKeymap("HtmlToolkit",
                this.jep.getKeymap());

        keymap.addActionForKeyStroke(KeyStroke.getKeyStroke(66, 2),
                new AbstractAction() {

                    private static final long serialVersionUID = -2806308676501747255L;

                    public void actionPerformed(ActionEvent e) {
                        HtmlToolkit.this.setFontStyle(0);
                    }

                });
        keymap.addActionForKeyStroke(KeyStroke.getKeyStroke(73, 2),
                new AbstractAction() {

                    private static final long serialVersionUID = 571935613450754270L;

                    public void actionPerformed(ActionEvent e) {
                        HtmlToolkit.this.setFontStyle(1);
                    }

                });
        keymap.addActionForKeyStroke(KeyStroke.getKeyStroke(85, 2),
                new AbstractAction() {

                    private static final long serialVersionUID = 8210678916389197176L;

                    public void actionPerformed(ActionEvent e) {
                        HtmlToolkit.this.setFontStyle(2);
                    }

                });
        this.jep.setKeymap(keymap);
    }

    private int pos() {
        return this.jep.getCaretPosition();
    }

    private void invoke(String actionName) {
        invoke(getActionByName(actionName), null);
    }

    private void invoke(Action action) {
        invoke(action, null);
    }

    private void invoke(Action action, String param) {
        ActionEvent e = new ActionEvent(this.jep, 0, param);

        if (isSelectionSet()) {
            action.actionPerformed(e);
        } else {
            int pos = pos();
            selectWord();
            action.actionPerformed(e);
            this.jep.setCaretPosition(pos);
        }
    }

    private Action getActionByName(String name) {
        return (Action) (Action) this.htActions.get(name);
    }

    private void insertListItem(HTML.Tag parentTag) {
        String t = parentTag.toString();

        String html = "<" + t + "><li></li></" + t + ">";
        try {
            Element par = getParagraph();
            int pos = par.getEndOffset() - 1;

            Element e = getParagraph().getParentElement();

            if (e.getAttributes().getAttribute(StyleConstants.NameAttribute) != HTML.Tag.LI) {
                this.htmlKit.insertHTML(this.htmlDoc, pos, html, 1, 0,
                        parentTag);
                this.jep.setCaretPosition(pos() - 1);
            } else if (par.getStartOffset() == pos) {
                this.htmlKit.insertHTML(this.htmlDoc, pos, "<p></p>", 3, 0,
                        HTML.Tag.P);
                this.htmlDoc.remove(pos + 1, 1);
                this.jep.setCaretPosition(pos);
            } else {
                this.htmlKit.insertHTML(this.htmlDoc, pos, "<li></li>", 2, 0,
                        HTML.Tag.LI);
                this.htmlDoc.remove(pos + 2, 1);
                this.jep.setCaretPosition(pos + 1);
            }

        } catch (Exception e) {
        }
    }
}