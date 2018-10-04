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

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.util.StringTokenizer;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class FlexLayout implements LayoutManager {

    private int xgap;
    private int ygap;
    private int width;
    private int height;
    private int xnullgap = 24;
    private int ynullgap = 24;
    private FlexCell[][] cells;
    public static final int USEPREFERRED = 0;
    public static final int EXPAND = 1;
    private int[] xflags;
    private int[] yflags;
    private int[] xpref;
    private int[] ypref;

    public FlexLayout(int width, int height) {
        this(width, height, 4, 4);
    }

    public FlexLayout(int width, int height, int xgap, int ygap) {
        if ((width < 1) || (height < 1)) {
            throw new IllegalArgumentException("width & height must be >0");
        }
        this.width = width;
        this.height = height;
        this.xgap = xgap;
        this.ygap = ygap;

        cells = new FlexCell[width][height];

        xflags = new int[width];
        yflags = new int[height];

        xpref = new int[width];
        ypref = new int[height];
    }

    public void setColProp(int index, int flag) {
        xflags[index] = flag;
    }

    public void setRowProp(int index, int flag) {
        yflags[index] = flag;
    }

    public void setNullGaps(int xgap, int ygap) {
        xnullgap = xgap;
        ynullgap = ygap;
    }

    public void setXgap(int xgap) {
        this.xgap = xgap;
    }
    public void setYgap(int ygap) {
        this.ygap = ygap;
    }
    public int getXgap() {
        return xgap;
    }
    public int getYgap() {
        return ygap;
    }
    public int getXNullgap() {
        return xnullgap;
    }
    public int getYNullgap() {
        return ynullgap;
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }

    public void addLayoutComponent(String name, Component comp) {
        StringTokenizer strTk = new StringTokenizer(name, ",");

        int x = 1;
        int y = 1;
        int xalign = 0;
        int yalign = 1;
        int xext = 1;
        int yext = 1;

        if (strTk.hasMoreTokens())
            x = Integer.parseInt(strTk.nextToken());
        if (strTk.hasMoreTokens())
            y = Integer.parseInt(strTk.nextToken());
        if (strTk.hasMoreTokens()) {
            String align = strTk.nextToken().toLowerCase();

            if (align.equals("l"))
                xalign = 0;
            if (align.equals("c"))
                xalign = 1;
            if (align.equals("r"))
                xalign = 2;
            if (align.equals("x"))
                xalign = 3;
        }
        if (strTk.hasMoreTokens()) {
            String align = strTk.nextToken().toLowerCase();

            if (align.equals("t"))
                yalign = 0;
            if (align.equals("c"))
                yalign = 1;
            if (align.equals("b"))
                yalign = 2;
            if (align.equals("x"))
                yalign = 3;
        }
        if (strTk.hasMoreTokens())
            xext = Integer.parseInt(strTk.nextToken());
        if (strTk.hasMoreTokens())
            yext = Integer.parseInt(strTk.nextToken());

        cells[x][y] = new FlexCell(xalign, yalign, xext, yext, comp);
    }

    public void removeLayoutComponent(Component comp) {
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++) {
                if (cells[x][y].component != comp)
                    continue;
                cells[x][y] = null;
                return;
            }
    }

    public Dimension preferredLayoutSize(Container parent) {
        synchronized (parent.getTreeLock()) {
            calcMaxWidthArray();
            calcMaxHeightArray();

            int maxW = 0;
            int maxH = 0;

            for (int x = 0; x < width; x++)
                maxW += xpref[x];
            for (int y = 0; y < height; y++)
                maxH += ypref[y];

            Insets insets = parent.getInsets();

            return new Dimension(insets.left + insets.right + maxW
                    + (width - 1) * xgap, insets.top + insets.bottom + maxH
                    + (height - 1) * ygap);
        }
    }

    public Dimension minimumLayoutSize(Container parent) {
        return preferredLayoutSize(parent);
    }

    public void layoutContainer(Container parent) {
        synchronized (parent.getTreeLock()) {
            calcMaxWidthArray();
            calcMaxHeightArray();

            Insets i = parent.getInsets();

            int maxWidth = parent.getSize().width - i.left - i.right
                    - (width - 1) * xgap;
            int maxHeight = parent.getSize().height - i.top - i.bottom
                    - (height - 1) * ygap;

            int fixedWidth = 0;
            int fixedHeight = 0;

            int varXCount = 0;
            int varYCount = 0;

            int varWidth = 0;
            int varHeight = 0;

            for (int x = 0; x < width; x++) {
                if (xflags[x] == 0)
                    fixedWidth += xpref[x];
                else {
                    varXCount++;
                }

            }

            if (varXCount != 0) {
                varWidth = (maxWidth - fixedWidth) / varXCount;
                if (varWidth < 0)
                    varWidth = 0;

            }

            for (int x = 0; x < width; x++) {
                if (xflags[x] != 1)
                    continue;
                xpref[x] = varWidth;
            }

            for (int y = 0; y < height; y++) {
                if (yflags[y] == 0)
                    fixedHeight += ypref[y];
                else {
                    varYCount++;
                }
            }

            if (varYCount != 0) {
                varHeight = (maxHeight - fixedHeight) / varYCount;
                if (varHeight < 0)
                    varHeight = 0;

            }

            for (int y = 0; y < height; y++) {
                if (yflags[y] != 1)
                    continue;
                ypref[y] = varHeight;
            }

            int currentX = i.left;
            int currentY = i.top;

            for (int y = 0; y < height; y++) {
                currentX = i.left;

                for (int x = 0; x < width; x++) {
                    int cellW = 0;
                    int cellH = 0;

                    FlexCell cell = cells[x][y];

                    if (cell != null) {
                        for (int xc = x; xc < x + cell.xext; xc++)
                            cellW += xpref[xc];
                        for (int yc = y; yc < y + cell.yext; yc++)
                            cellH += ypref[yc];

                        Dimension compSize = cell.component.getPreferredSize();
                        int compX;
                        int compW;
                        switch (cell.xalign) {
                            case 0 :
                                compX = currentX;
                                compW = compSize.width;
                                break;
                            case 1 :
                                compX = currentX + (cellW - compSize.width) / 2;
                                compW = compSize.width;
                                break;
                            case 2 :
                                compX = currentX + cellW - compSize.width;
                                compW = compSize.width;
                                break;
                            case 3 :
                                compX = currentX;
                                compW = cellW;
                                break;
                            default :
                                ApplicationFrame.getInstance().changeLog
                                        .append("FlexLayout: invalid X align type.\n",
                                                ApplicationFrame.WARNING_FOREGROUND_COLOR_LOG);
                                compX = currentX;
                                compW = cellW;
                        }
                        int compY;
                        int compH;
                        switch (cell.yalign) {
                            case 0 :
                                compY = currentY;
                                compH = compSize.height;
                                break;
                            case 1 :
                                compY = currentY + (cellH - compSize.height)
                                        / 2;
                                compH = compSize.height;
                                break;
                            case 2 :
                                compY = currentY + cellH - compSize.height;
                                compH = compSize.height;
                                break;
                            case 3 :
                                compY = currentY;
                                compH = cellH;
                                break;
                            default :
                                ApplicationFrame.getInstance().changeLog
                                        .append("FlexLayout: invalid Y align type.\n",
                                                ApplicationFrame.WARNING_FOREGROUND_COLOR_LOG);
                                compY = currentY;
                                compH = cellH;
                        }

                        cell.component.setBounds(compX, compY, compW
                                + (cell.xext - 1) * xgap, compH
                                + (cell.yext - 1) * ygap);
                    }

                    currentX += xpref[x] + xgap;
                }

                currentY += ypref[y] + ygap;
            }
        }
    }

    private void calcMaxWidthArray() {
        for (int x = 0; x < width; x++) {
            int maxPrefW = 0;

            for (int y = 0; y < height; y++) {
                FlexCell cell = cells[x][y];
                if ((cell == null) || (cell.xext != 1))
                    continue;
                int curPrefW = cell.component.getPreferredSize().width;
                if (curPrefW <= maxPrefW)
                    continue;
                maxPrefW = curPrefW;
            }

            if (maxPrefW == 0) {
                maxPrefW = xnullgap;
            }

            xpref[x] = maxPrefW;
        }
    }

    private void calcMaxHeightArray() {
        for (int y = 0; y < height; y++) {
            int maxPrefH = 0;

            for (int x = 0; x < width; x++) {
                FlexCell cell = cells[x][y];
                if ((cell == null) || (cell.yext != 1))
                    continue;
                int curPrefH = cell.component.getPreferredSize().height;
                if (curPrefH <= maxPrefH)
                    continue;
                maxPrefH = curPrefH;
            }

            if (maxPrefH == 0)
                maxPrefH = ynullgap;

            ypref[y] = maxPrefH;
        }
    }
}