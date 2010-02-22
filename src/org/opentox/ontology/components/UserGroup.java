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
import java.net.URI;
import java.net.URISyntaxException;
import org.opentox.core.exceptions.Cause;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.io.publishable.JSONObject;
import org.opentox.io.publishable.PDFObject;
import org.opentox.io.publishable.RDFObject;
import org.opentox.io.publishable.TurtleObject;
import org.opentox.io.publishable.UriListObject;
import org.opentox.util.logging.YaqpLogger;
import org.opentox.util.logging.levels.Warning;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class UserGroup extends YaqpComponent {

    private String name = null,
            userAuth = null,
            modelAuth = null,
            algorithmAuth = null,
            userGroupAuth = null;

    private int level, models;

    private int _minLevel = Integer.MIN_VALUE, _maxLevel = Integer.MAX_VALUE;
    private int _minModels = Integer.MIN_VALUE, _maxModels = Integer.MAX_VALUE;

    public UserGroup() {
    }

    public UserGroup(String name) {
        this.name = name;
    }

    public UserGroup(String name, int level,  String modelAuth, String userAuth,
            String algorithmAuth, String userGroupAuth,  int models) {
        this.name = name;
        this.userAuth = userAuth;
        this.modelAuth = modelAuth;
        this.algorithmAuth = algorithmAuth;
        this.userGroupAuth = userGroupAuth;
        this.level = level;
        this._minLevel = level;
        this._maxLevel = level;
        this.models = models;
        this._minModels = models;
        this._maxModels = models;
    }

    public int getMaxLevel() {
        return _maxLevel;
    }

    public void setMaxLevel(int _maxLevel) {
        this._maxLevel = _maxLevel;
    }

    public int getMaxModels() {
        return _maxModels;
    }

    public void setMaxModels(int _maxModels) {
        this._maxModels = _maxModels;
    }

    public int getMinLevel() {
        return _minLevel;
    }

    public void setMinLevel(int _minLevel) {
        this._minLevel = _minLevel;
    }

    public int getMinModels() {
        return _minModels;
    }

    public void setMinModels(int _minModels) {
        this._minModels = _minModels;
    }

    public String getAlgorithmAuth() {
        return algorithmAuth;
    }

    public void setAlgorithmAuth(String algorithmAuth) {
        this.algorithmAuth = algorithmAuth;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
        this._minLevel = level;
        this._maxLevel = level;
    }

    public String getModelAuth() {
        return modelAuth;
    }

    public void setModelAuth(String modelAuth) {
        this.modelAuth = modelAuth;
    }

    public int getModels() {
        return models;
    }

    public void setModels(int models) {
        this.models = models;
        this._minModels = models;
        this._maxModels = models;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserAuth() {
        return userAuth;
    }

    public void setUserAuth(String userAuth) {
        this.userAuth = userAuth;
    }

    public String getUserGroupAuth() {
        return userGroupAuth;
    }

    public void setUserGroupAuth(String userGroupAuth) {
        this.userGroupAuth = userGroupAuth;
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
    public URI uri() throws YaqpException {
        String superUri = super.uri().toString();
        try{
        return new URI(superUri+"/"+getName());
        } catch (URISyntaxException ex){
            throw new YaqpException(Cause.XTC743, "Improper URI", ex);
        }
    }

    @Override
    public UriListObject getUriList() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public UserGroup getSkroutz(){
        return new UserGroup(this.getName());
    }

    @Override
    public boolean equals(Object obj){
        if(obj.getClass() == this.getClass()){
            UserGroup group = (UserGroup) obj;
            boolean result = (getName()==null && group.getName() == null);
            return result || (this.getName().equals(group.getName()));
        }else{
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }

}
