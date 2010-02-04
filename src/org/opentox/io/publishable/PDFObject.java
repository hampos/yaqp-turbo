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
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import org.opentox.io.interfaces.JPublishable;
import org.opentox.io.util.YaqpIOStream;
import org.opentox.util.logging.YaqpLogger;
import org.opentox.util.logging.levels.Warning;

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



    public void publish(YaqpIOStream stream) {

        try {
            Document doc = new Document();
            PdfWriter.getInstance(doc, (OutputStream) stream.getStream());
            doc.open();
            doc.addAuthor(pdfAuthor);
            doc.addCreationDate();
            doc.addProducer();
            doc.addSubject(subject);
            doc.addCreator(pdfCreator);
            doc.addTitle(pdfTitle);
            doc.addKeywords(pdfKeywords);
            doc.addHeader("License", "GNU GPL v3");
            try {
                Image image = Image.getInstance(new URL(OpenToxLogoUrl));
                image.scalePercent(40);
                image.setAnnotation(new Annotation(0, 0, 0, 0, "http://opentox.org"));
                doc.add(image);
            } catch (Exception ex) {
                YaqpLogger.LOG.log(new Warning(getClass(), "WCT517 - OpenTox Logo not found at "+OpenToxLogoUrl));
            }
            for (int i = 0;i < elements.size(); i++){
                doc.add(elements.get(i));
            }
            doc.close();
        } catch (DocumentException ex) {
            YaqpLogger.LOG.log(new Warning(getClass(), "XPD819 - Error while generating PDF representation."));
        }

    }
}
