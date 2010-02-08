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

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import org.opentox.io.publishable.JSONObject;
import org.opentox.io.publishable.PDFObject;
import org.opentox.io.publishable.RDFObject;
import org.opentox.io.publishable.TurtleObject;
import org.opentox.io.util.YaqpIOStream;
import org.opentox.ontology.exceptions.ImproperEntityException;
import org.opentox.ontology.namespaces.OTClass;
import org.opentox.util.logging.YaqpLogger;
import org.opentox.util.logging.levels.Warning;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class UserGroup extends YaqpComponent {

    private String name;
    private int level;

    public UserGroup() {
    }

    public UserGroup(String name, int level) {
        this.name = name;
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    /**
     * The name of the user group as it is registered in the database.
     * @return
     */
    public String getName() {
        return name;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setName(String name) {
        this.name = name;
    }

    // TODO: This was just a test. Implement the method!
    @Override
    public String toString() {
        String userGroup = "\n-- USER GROUP --\n";
        userGroup += "GROUP NAME         : " + getName() + "\n";
        userGroup += "LEVEL              : " + getLevel() + "\n";
        return userGroup;
    }

    @Override
    public PDFObject getPDF() {
        PDFObject pdf = new PDFObject();
        Paragraph p1 = new Paragraph(new Chunk(
                "OpenTox - UserGroup Report\n\n",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));
        pdf.addElement(p1);


        try {
            PdfPTable table = new PdfPTable(2);
            table.setWidths(new int[]{30, 50});
            PdfPCell cell = new PdfPCell(new Paragraph("UserGroup General Information"));
            cell.setColspan(2);
            cell.setBackgroundColor(new BaseColor(0xC0, 0xC0, 0xC0));
            table.addCell(cell);

            table.addCell("Name");
            table.addCell(getName());

            table.addCell("Authorization Level");
            table.addCell(Integer.toString(getLevel()));

            pdf.addElement(table);

        } catch (DocumentException ex) {
            YaqpLogger.LOG.log(new Warning(getClass(), "XPI908 - Error while generating " +
                    "PDF representation for User Group "));
        }

        return pdf;
    }

    public static void main(String args[]) throws FileNotFoundException {
        UserGroup ug = new UserGroup("Administrator", 100);
        ug.getPDF().publish(new YaqpIOStream(new FileOutputStream("/home/chung/Desktop/a.pdf")));

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

    @Override
    protected String getTag() {
        return "usergroup";
    }

     @Override
    public Uri uri() throws ImproperEntityException {
        Uri u = super.uri();
        u.setUri(u.toString()+"/"+getName());
        u.setOntology(OTClass.UserGroup);
        return u;
    }
}
