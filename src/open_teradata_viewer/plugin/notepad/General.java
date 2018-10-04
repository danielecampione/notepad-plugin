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

import open_teradata_viewer.plugin.notepad.xml.XmlElement;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class General {

    public static final String TAGNAME = "general";

    public boolean showTip = true;
    public boolean reloadLastProj = true;
    public boolean createBackup = false;
    public boolean guiAAliasing = true;
    public String sqlTerminator = ";";
    public String sqlSyntax = "postgres";

    public Window window = new Window();

    private boolean textAAliasing = true;

    private static final String SHOW_TIP = "showTip";
    private static final String RELOAD_PROJ = "reloadLastProj";
    private static final String CREATE_BACKUP = "createBackup";
    private static final String TEXT_AALIASING = "textAAliasing";
    private static final String GUI_AALIASING = "guiAAliasing";
    private static final String SQL_TERMINATOR = "sqlTerminator";
    private static final String SQL_SYNTAX = "sqlSyntax";

    /** Ctor. */
    public General() {
    }

    public void setupConfig(XmlElement el) {
        if (el == null)
            return;

        showTip = Util.getBooleanValue(el.getChildValue(SHOW_TIP), true);
        reloadLastProj = Util.getBooleanValue(el.getChildValue(RELOAD_PROJ),
                true);
        createBackup = Util.getBooleanValue(el.getChildValue(CREATE_BACKUP),
                false);
        textAAliasing = Util.getBooleanValue(el.getChildValue(TEXT_AALIASING),
                true);
        guiAAliasing = Util.getBooleanValue(el.getChildValue(GUI_AALIASING),
                true);
        sqlTerminator = Util.getStringValue(el.getChildValue(SQL_TERMINATOR),
                ";");
        sqlSyntax = Util.getStringValue(el.getChildValue(SQL_SYNTAX),
                "postgres");

        window.setupConfig(el.getChild(Window.TAGNAME));

        CustomLook.setTextAntiAliasing(textAAliasing);
    }

    public XmlElement getConfig() {
        XmlElement elRoot = new XmlElement(TAGNAME);

        elRoot.addChild(new XmlElement(SHOW_TIP, showTip + ""))
                .addChild(new XmlElement(RELOAD_PROJ, reloadLastProj + ""))
                .addChild(new XmlElement(CREATE_BACKUP, createBackup + ""))
                .addChild(new XmlElement(TEXT_AALIASING, textAAliasing + ""))
                .addChild(new XmlElement(GUI_AALIASING, guiAAliasing + ""))
                .addChild(new XmlElement(SQL_TERMINATOR, sqlTerminator + ""))
                .addChild(new XmlElement(SQL_SYNTAX, sqlSyntax + ""))
                .addChild(window.getConfig());

        return elRoot;
    }

    public void setTextAAliasing(boolean yesno) {
        textAAliasing = yesno;
        CustomLook.setTextAntiAliasing(yesno);
    }

    public boolean isTextAAliased() {
        return textAAliasing;
    }
}