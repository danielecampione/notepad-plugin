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

import javax.swing.ImageIcon;

import net.sourceforge.open_teradata_viewer.ImageManager;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class ImageFactory {

    public static ImageIcon FONT_FAMIL;
    public static ImageIcon FONT_MONOSPACED;

    public static ImageIcon STYLE_PLAIN;
    public static ImageIcon STYLE_BOLD;
    public static ImageIcon STYLE_ITALIC;
    public static ImageIcon STYLE_BOLDITAL;
    public static ImageIcon STYLE_UNDERLINE;

    public static ImageIcon PARAGRAPH;
    public static ImageIcon PAR_LEFT;
    public static ImageIcon PAR_CENTER;
    public static ImageIcon PAR_RIGHT;

    public static ImageIcon IMAGE;
    public static ImageIcon STRUCTURE;

    public static ImageIcon LIST_ORDERED;
    public static ImageIcon LIST_UNORDERED;
    public static ImageIcon LIST_ITEM;

    /** The init method. */
    public static void init() {
        FONT_FAMIL = ImageManager.getImage("/icons/font-family.png");

        FONT_MONOSPACED = ImageManager.getImage("/icons/font-monospaced.png");

        STYLE_PLAIN = ImageManager.getImage("/icons/style-plain.gif");
        STYLE_BOLD = ImageManager.getImage("/icons/style-bold.gif");
        STYLE_ITALIC = ImageManager.getImage("/icons/style-italic.gif");
        STYLE_BOLDITAL = ImageManager.getImage("/icons/style-bolditalic.gif");
        STYLE_UNDERLINE = ImageManager.getImage("/icons/style-underline.png");

        PARAGRAPH = ImageManager.getImage("/icons/paragraph.png");
        PAR_LEFT = ImageManager.getImage("/icons/par-left.png");
        PAR_CENTER = ImageManager.getImage("/icons/par-center.png");
        PAR_RIGHT = ImageManager.getImage("/icons/par-right.png");

        IMAGE = ImageManager.getImage("/icons/image.png");
        STRUCTURE = ImageManager.getImage("/icons/structure.png");

        LIST_ORDERED = ImageManager.getImage("/icons/list-ordered.png");
        LIST_UNORDERED = ImageManager.getImage("/icons/list-unordered.png");
        LIST_ITEM = ImageManager.getImage("/icons/list-item.png");
    }
}