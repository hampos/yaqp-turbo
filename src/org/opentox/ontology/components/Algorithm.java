/*
 *
 * YAQP - Yet Another QSAR Project:
 * Machine Learning algorithms designed for the prediction of toxicological
 * features of chemical compounds become available on the Web. Yaqp is developed
 * under OpenTox (http://opentox.org) which is an FP7-funded EU research project.
 * This project was developed at the Automatic Control Lab in the Chemical Engineering
 * School of National Technical University of Athens. Please read README for more
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
package org.opentox.ontology.components;

import com.hp.hpl.jena.rdf.model.Resource;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.opentox.io.publishable.JSONObject;
import org.opentox.io.publishable.PDFObject;
import org.opentox.io.publishable.RDFObject;
import org.opentox.io.publishable.TurtleObject;
import org.opentox.io.util.YaqpIOStream;
import org.opentox.ontology.namespaces.OTAlgorithmTypes;
import org.opentox.ontology.util.AlgorithmMeta;
import org.opentox.ontology.util.AlgorithmParameter;
import org.opentox.ontology.util.YaqpAlgorithms;
import org.opentox.ontology.util.vocabulary.Audience;
import org.opentox.util.logging.YaqpLogger;
import org.opentox.util.logging.levels.Warning;
import org.restlet.data.MediaType;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class Algorithm extends YaqpComponent {

    //private  static final long serialVersionUID = -18477218378326540L;
    public AlgorithmMeta metadata;

    public Algorithm() {
    }

    public Algorithm(AlgorithmMeta meta) {
        this.metadata = meta;
    }

    public AlgorithmMeta getMeta() {
        return metadata;
    }

    @Override
    public String toString() {
        String algorithm = "";
        algorithm += "\n--ALGORITHM--\n";
        algorithm += "NAME          : " + metadata.name + "\n";
        return algorithm;
    }

    @Override
    public PDFObject getPDF() {
        PDFObject pdf = new PDFObject();
        pdf.setPdfTitle(getMeta().identifier);
        pdf.setPdfKeywords(getMeta().subject);
        Paragraph p1 = new Paragraph(new Chunk(
                "OpenTox - Algorithm Report\n\n",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));
        pdf.addElement(p1);
        try {
            PdfPTable table = new PdfPTable(2);

            table.setWidths(new int[]{10, 50});

            PdfPCell cell = new PdfPCell(new Paragraph("Algorithm Presentation - General"));
            cell.setColspan(2);
            cell.setBackgroundColor(new BaseColor(0xC0, 0xC0, 0xC0));
            table.addCell(cell);

            table.addCell("Name");
            table.addCell(getMeta().name);

            table.addCell("Title");
            table.addCell(getMeta().title);

            table.addCell("Subject");
            table.addCell(getMeta().subject);

            table.addCell("Description");
            table.addCell(getMeta().description);

            table.addCell("Identifier");
            table.addCell(getMeta().identifier);

            pdf.addElement(table);
            pdf.addElement(new Paragraph("\n\n\n"));

            table = new PdfPTable(2);
            table.setWidths(new int[]{10, 50});
            cell = new PdfPCell(new Paragraph("General Meta Information"));
            cell.setColspan(2);
            cell.setBackgroundColor(new BaseColor(0xC0, 0xC0, 0xC0));
            table.addCell(cell);

            table.addCell("Type");
            table.addCell(getMeta().type);

            table.addCell("Creator");
            table.addCell(getMeta().creator);

            table.addCell("Publisher");
            table.addCell(getMeta().publisher);

            table.addCell("Relation");
            table.addCell(getMeta().relation);

            table.addCell("Rights");
            table.addCell(getMeta().rights);

            table.addCell("Source");
            table.addCell(getMeta().source);

            table.addCell("Provenance");
            table.addCell(getMeta().provenance);

            table.addCell("Contributor");
            table.addCell(getMeta().contributor);

            table.addCell("Language");
            table.addCell(getMeta().language.getDisplayLanguage());

            table.addCell("Created on");
            table.addCell(getMeta().date.toString());

            table.addCell("Formats");
            ArrayList<MediaType> listMedia = getMeta().format;
            String formatTableEntry = "";
            for (int i = 0; i < listMedia.size(); i++) {
                formatTableEntry += listMedia.get(i).toString();
                if (i < listMedia.size() - 1) {
                    formatTableEntry += "\n";
                }
            }
            table.addCell(formatTableEntry);

            table.addCell("Audience");
            ArrayList<Audience> audiences = getMeta().audience;
            String auds = "";
            for (int i = 0; i < audiences.size(); i++) {
                auds += audiences.get(i).getName();
                if (i < audiences.size() - 1) {
                    auds += ",";
                }
            }
            table.addCell(auds);
            pdf.addElement(table);
            pdf.addElement(new Paragraph("\n\n\n"));

            table = new PdfPTable(4);
            table.setWidths(new int[]{30, 30, 30, 30});
            cell = new PdfPCell(new Paragraph("Algorithm Parameters"));
            cell.setColspan(4);
            cell.setBackgroundColor(new BaseColor(0xC0, 0xC0, 0xC0));
            table.addCell(cell);

            table.addCell("Parameter Name");
            table.addCell("XSD DataType");
            table.addCell("Default Value");
            table.addCell("Scope");

            ArrayList<AlgorithmParameter> paramList = getMeta().Parameters;
            for (int i = 0; i < paramList.size(); i++) {
                table.addCell(paramList.get(i).paramName);
                table.addCell(paramList.get(i).dataType.getURI());
                table.addCell(paramList.get(i).paramValue.toString());
                table.addCell(paramList.get(i).paramScope);
            }

            pdf.addElement(table);

            pdf.addElement(new Paragraph("\n\n\n"));

            table = new PdfPTable(1);
            cell = new PdfPCell(new Paragraph("Ontologies"));
            cell.setBackgroundColor(new BaseColor(0xC0, 0xC0, 0xC0));
            table.addCell(cell);
            OTAlgorithmTypes type = getMeta().algorithmType;
            table.addCell(type.getURI());

            Set<Resource> superOntologies = type.getSuperEntities();
            Iterator<Resource> it = superOntologies.iterator();

            while (it.hasNext()) {
                table.addCell(it.next().getURI());
            }

            pdf.addElement(table);
        } catch (DocumentException ex) {
            YaqpLogger.LOG.log(new Warning(getClass(), "XCF316 - Pdf Exception :" + ex.toString()));
        }
        return pdf;
    }

    public static void main(String[] args) throws FileNotFoundException{
        Algorithm a = YaqpAlgorithms.SVM;
        a.getPDF().publish(new YaqpIOStream(new FileOutputStream("/home/chung/Desktop/a.pdf")));
    }

    @Override
    public RDFObject getRDF() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public TurtleObject getTurtle() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public JSONObject getJson() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
