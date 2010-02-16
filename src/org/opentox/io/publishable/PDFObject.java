/*
 *
 * YAQP - Yet Another QSAR Project:
 * Machine Learning algorithms designed for the prediction of toxicological
 * features of chemical compounds become available on the Web. Yaqp is developed
 * under OpenTox (http://opentox.org) which is an FP7-funded EU research project.
 * This project was developed at the Automatic Control Lab in the Chemical Engineering
 * School of the National Technical University of Athens. Please read README for more
 * information.
 *
 * Copyright (C) 2009-2010 Pantelis Sopasakis & Charalampos Chomenides
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
 *
 * Contact:
 * Pantelis Sopasakis
 * chvng@mail.ntua.gr
 * Address: Iroon Politechniou St. 9, Zografou, Athens Greece
 * tel. +30 210 7723236
 */
package org.opentox.io.publishable;

import com.itextpdf.text.Annotation;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import org.opentox.config.ServerFolders;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.io.interfaces.JPublishable;
import org.opentox.io.util.YaqpIOStream;
import org.opentox.util.logging.YaqpLogger;
import org.opentox.util.logging.levels.Debug;
import org.opentox.util.logging.levels.Trace;
import org.opentox.util.logging.levels.Warning;
import org.restlet.data.MediaType;
import static org.opentox.core.exceptions.Cause.*;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class PDFObject implements JPublishable {

    private ArrayList<Element> elements = new ArrayList<Element>();
    private String subject = "OpenTox Entity Representation";
    private String pdfAuthor = "OpenTox NTUA WebServices";
    private String pdfCreator = "OpenTox NTUA Serivces";
    private String pdfTitle = "OpenTox Entity Representation";
    private String pdfKeywords = "";
    private static final String OpenToxLogoUrl = "http://opentox.org/logo.png";
    private static final String alternativeLogoPath = ServerFolders.images + "/logo.png";
    private static final String kinkyDesignLogo = ServerFolders.images + "/kd_logo.png";
    private static final String yaqpLogo = ServerFolders.images + "/yaqp_logo.png";

    public PDFObject() {
    }

    public void addElement(Element element) {
        elements.add(element);
    }

    public void setPdfAuthor(final String pdfAuthor) {
        this.pdfAuthor = pdfAuthor;
    }

    public void setPdfCreator(final String pdfCreator) {
        this.pdfCreator = pdfCreator;
    }

    public void setSubject(final String subject) {
        this.subject = subject;
    }

    public void setPdfTitle(final String pdfTitle) {
        this.pdfTitle = pdfTitle;
    }

    public void setPdfKeywords(String pdfKeywords) {
        this.pdfKeywords = pdfKeywords;
    }

    public void publish(YaqpIOStream stream) throws YaqpException{
        if (stream == null){
            throw new NullPointerException("Cannot public pdf to a null output stream");
        }
        try {
            Document doc = new Document();
            try {
                PdfWriter.getInstance(doc, (OutputStream) stream.getStream());
            } catch (ClassCastException ex) {
                throw new ClassCastException("The stream you provided is not a valid output stream");
            }
            doc.open();
            doc.addAuthor(pdfAuthor);
            doc.addCreationDate();
            doc.addProducer();
            doc.addSubject(subject);
            doc.addCreator(pdfCreator);
            doc.addTitle(pdfTitle);
            doc.addKeywords(pdfKeywords);
            doc.addHeader("License", "GNU GPL v3");
            Image image = null;
            try {
                image = Image.getInstance(new URL(OpenToxLogoUrl));
            } catch (Exception ex) {// OpenTox Logo was not found on the web...
                try {// use the cached image instead
                    YaqpLogger.LOG.log(new Trace(getClass(), "OpenTox Logo not found at " + OpenToxLogoUrl));
                    image = Image.getInstance(alternativeLogoPath);
                } catch (Exception ex1) {// if no image at local folder
                    YaqpLogger.LOG.log(new Debug(getClass(), "OpenTox Logo not found at " + alternativeLogoPath + " :: " + ex1));
                }
            }
            if (image != null) {
                image.scalePercent(40);
                image.setAnnotation(new Annotation(0, 0, 0, 0, "http://opentox.org"));
                Chunk ck_ot = new Chunk(image, -5, -30);
                doc.add(ck_ot);
            }
            try {
                Image yaqp = Image.getInstance(yaqpLogo);
                yaqp.scalePercent(30);
                yaqp.setAnnotation(new Annotation(0, 0, 0, 0, "https://opentox.ntua.gr"));
                yaqp.setAlt("YAQP(R), yet another QSAR Project");
                Chunk ck_yaqp = new Chunk(yaqp, 15, -30);
                doc.add(ck_yaqp);
            } catch (Exception ex) {
                YaqpLogger.LOG.log(new Warning(getClass(), "YAQP Logo not found at " + kinkyDesignLogo + " :: " + ex));
            }
            doc.add(new Paragraph("\n\n\n"));
            for (Element e : elements) {
                doc.add(e);
            }
            doc.close();
        } catch (DocumentException ex) {
            String message = "Error while generating PDF representation.";
            YaqpLogger.LOG.log(new Warning(getClass(), message));
            throw new YaqpException(XPDF18, message, ex);
        }

    }

    public MediaType getMediaType() {
        return MediaType.APPLICATION_PDF;
    }
}
