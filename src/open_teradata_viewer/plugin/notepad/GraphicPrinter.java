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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.List;
import java.util.Vector;

import javax.print.PrintService;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Media;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.MediaSizeName;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class GraphicPrinter implements Printable {

    private int lastPage;
    private double scale = 1.0D;
    private PrinterJob job;
    private PrintService service;
    private Vector<Double> vPages;
    private GraphicPrinterSource source;
    private GraphicPrinterListener listener;
    private PrintRequestAttributeSet attr = new HashPrintRequestAttributeSet();
    public static final String PAGE_A4 = "A4";
    public static final String PAGE_LETTER = "letter";
    public static final String PAGE_LEGAL = "legal";

    public GraphicPrinter() {
        this.job = PrinterJob.getPrinterJob();
        this.job.setPrintable(this);

        PrintService[] services = PrinterJob.lookupPrintServices();

        this.service = (services.length > 0 ? services[0] : null);

        this.attr.add(MediaSizeName.ISO_A4);
        setMargins(new PageMargins(10.0F, 10.0F, 10.0F, 10.0F));
    }

    public boolean printerExists() {
        return this.service != null;
    }

    public void setPrinterSource(GraphicPrinterSource s) {
        this.source = s;
    }

    public void setPrinterListener(GraphicPrinterListener l) {
        this.listener = l;
    }

    public void setScaleFactor(double scale) {
        this.scale = scale;
    }

    public double getScaleFactor() {
        return this.scale;
    }

    public void setMargins(PageMargins m) {
        MediaSizeName media = (MediaSizeName) this.attr.get(Media.class);
        MediaSize mediaSize = MediaSize.getMediaSizeForName(media);

        float width = mediaSize.getX(1000);
        float height = mediaSize.getY(1000);

        MediaPrintableArea area = new MediaPrintableArea(m.left, m.top, width
                - m.left - m.right, height - m.top - m.bottom, 1000);

        this.attr.add(area);
    }

    public PageMargins getMargins() {
        MediaSizeName media = (MediaSizeName) this.attr.get(Media.class);
        MediaSize mediaSize = MediaSize.getMediaSizeForName(media);

        MediaPrintableArea area = (MediaPrintableArea) this.attr
                .get(MediaPrintableArea.class);

        float width = mediaSize.getX(1000);
        float height = mediaSize.getY(1000);

        float left = area.getX(1000);
        float top = area.getY(1000);
        float right = width - area.getWidth(1000) - left;
        float bottom = height - area.getHeight(1000) - top;

        return new PageMargins(left, top, right, bottom);
    }

    public void setPageSize(String name) {
        MediaSizeName media;
        if (name.equals("letter")) {
            media = MediaSizeName.NA_LETTER;
        } else {
            if (name.equals("legal")) {
                media = MediaSizeName.NA_LEGAL;
            } else
                media = MediaSizeName.ISO_A4;
        }
        this.attr.add(media);
    }

    public String getPageSize() {
        MediaSizeName media = (MediaSizeName) this.attr.get(Media.class);

        if (media == MediaSizeName.NA_LETTER)
            return "letter";
        if (media == MediaSizeName.NA_LEGAL)
            return "legal";

        return "A4";
    }

    public boolean showDialog() {
        return this.job.printDialog(this.attr);
    }

    public void print() throws PrinterException {
        this.vPages = null;

        this.job.print(this.attr);
    }

    public List<BufferedImage> preview(int width) {
        PageFormat pf = getPageFormat();

        double pageWidth = pf.getWidth();
        double pageHeight = pf.getHeight();

        double imgX = pf.getImageableX();
        double imgY = pf.getImageableY();
        double imgW = pf.getImageableWidth();
        double imgH = pf.getImageableHeight();

        prepare(pf);

        int height = (int) (width * pageHeight / pageWidth);

        double imgRatio = width / pageWidth;

        int left = (int) (imgX * imgRatio);
        int top = (int) (imgY * imgRatio);
        int w = (int) (imgW * imgRatio);
        int h = (int) (imgH * imgRatio);

        int right = width - w - left;
        int bottom = height - h - top;

        Vector<BufferedImage> result = new Vector<BufferedImage>();

        for (int i = 0; i < this.vPages.size(); i++) {
            BufferedImage bimg = new BufferedImage(width, height, 5);

            Graphics2D g = bimg.createGraphics();

            g.setColor(Color.WHITE);
            g.fillRect(0, 0, width, height);

            Point2D.Double delta = (Point2D.Double) this.vPages.elementAt(i);

            AffineTransform save = g.getTransform();

            g.scale(imgRatio, imgRatio);
            g.translate(imgX, imgY);
            g.translate(-delta.x, -delta.y);
            g.scale(this.scale, this.scale);

            this.source.print(g);

            g.setTransform(save);
            g.setColor(Color.WHITE);

            g.fillRect(0, 0, left - 1, height);
            g.fillRect(0, 0, width, top - 1);
            g.fillRect(width - right + 1, 0, right - 1, height);
            g.fillRect(0, height - bottom + 1, width, bottom - 1);

            g.dispose();
            result.add(bimg);
        }

        return result;
    }

    public int print(Graphics g, PageFormat pf, int pageIndex) {
        Graphics2D g2d = (Graphics2D) g;

        if (this.vPages == null) {
            this.lastPage = -1;
            prepare(pf);

            if (this.listener != null) {
                this.listener.begin(this.vPages.size());
            }

        }

        if (pageIndex < this.vPages.size()) {
            if ((this.lastPage != pageIndex) && (this.listener != null)) {
                this.listener.printing(pageIndex + 1, this.vPages.size());
            }
            Point2D.Double delta = (Point2D.Double) this.vPages
                    .elementAt(pageIndex);

            g2d.translate(pf.getImageableX(), pf.getImageableY());
            g2d.translate(-delta.x, -delta.y);
            g2d.scale(this.scale, this.scale);

            this.source.print(g2d);

            if ((this.lastPage != pageIndex) && (this.listener != null)) {
                this.listener.printed(pageIndex + 1, this.vPages.size());
            }
            this.lastPage = pageIndex;

            return 0;
        }

        if (this.listener != null) {
            this.listener.end();
        }
        return 1;
    }

    private void prepare(PageFormat pf) {
        this.vPages = calcNumPages(this.source.getWidth() * this.scale,
                this.source.getHeight() * this.scale, pf.getImageableWidth(),
                pf.getImageableHeight());
    }

    private Vector<Double> calcNumPages(double srcWidth, double srcHeight,
            double prtWidth, double prtHeight) {
        Vector<Double> pages = new Vector<Double>();

        for (double y = 0.0D; y < srcHeight; y += prtHeight) {
            for (double x = 0.0D; x < srcWidth; x += prtWidth)
                pages.addElement(new Point2D.Double(x, y));
        }
        return pages;
    }

    private PageFormat getPageFormat() {
        MediaSizeName media = (MediaSizeName) this.attr.get(Media.class);
        MediaSize mediaSize = MediaSize.getMediaSizeForName(media);

        MediaPrintableArea area = (MediaPrintableArea) this.attr
                .get(MediaPrintableArea.class);

        double width = 2.834645669291339D * mediaSize.getX(1000);
        double height = 2.834645669291339D * mediaSize.getY(1000);

        double imgX = 2.834645669291339D * area.getX(1000);
        double imgY = 2.834645669291339D * area.getY(1000);
        double imgW = 2.834645669291339D * area.getWidth(1000);
        double imgH = 2.834645669291339D * area.getHeight(1000);

        Paper paper = new Paper();
        paper.setSize(width, height);
        paper.setImageableArea(imgX, imgY, imgW, imgH);

        PageFormat pf = new PageFormat();
        pf.setPaper(paper);

        return pf;
    }
}