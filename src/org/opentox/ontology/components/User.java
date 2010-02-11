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
import java.net.URI;
import java.net.URISyntaxException;
import org.opentox.core.exceptions.YaqpException;
import org.opentox.db.exceptions.DbException;
import org.opentox.db.handlers.ReaderHandler;
import org.opentox.io.publishable.JSONObject;
import org.opentox.io.publishable.PDFObject;
import org.opentox.io.publishable.RDFObject;
import org.opentox.io.publishable.TurtleObject;
import org.opentox.io.util.YaqpIOStream;
import org.opentox.util.logging.YaqpLogger;
import org.opentox.util.logging.levels.Warning;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class User extends YaqpComponent {
     private static final long serialVersionUID = 1726532809162869471L;

    private String
            userName = null,
            userPass = null,
            firstName = null,
            lastName = null,
            email = null,
            organization = null,
            country = null,
            city = null,
            address = null,
            webpage = null,
            timeStamp = null;
    private UserGroup userGroup = null;    


    public User(){

    }

 
    public User(
            String userName,
            String userPass,
            String firstName,
            String lastName,
            String email,
            String organization,
            String country,
            String city,
            String address,
            String webpage,
            String timeStamp,
            UserGroup userGroup) {
        this.userName = userName;
        this.userPass = userPass;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.organization = organization;
        this.country = country;
        this.city = city;
        this.address = address;
        this.webpage = webpage;
        this.timeStamp = timeStamp;
        this.userGroup = userGroup;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getOrganization() {
        return organization;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public UserGroup getUserGroup() {
        return userGroup;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPass() {
        return userPass;
    }

    public String getWebpage() {
        return webpage;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setUserGroup(UserGroup userGroup) {
        this.userGroup = userGroup;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public void setWebpage(String webpage) {
        this.webpage = webpage;
    }




    
    @Override
    public String toString() {
        String groupName = null;
        int level = -1;
        if (getUserGroup()!=null){
            groupName = getUserGroup().getName();
            level = getUserGroup().getLevel();
        }
        String user = "-- USER --\n";
        user += "USERNAME          : "+getUserName()+"\n";
        user += "PASSWORD DIGEST   : "+getUserPass()+"\n";
        user += "USER GROUP NAME   : "+groupName+"\n";
        user += "LEVEL             : "+level+"\n";
        user += "FIRST NAME        : "+getFirstName()+"\n";
        user += "LAST NAME         : "+getLastName()+"\n";
        user += "e-MAIL            : "+getEmail()+"\n";
        user += "ORGANIZATION      : "+getOrganization()+"\n";
        user += "COUNTRY           : "+getCountry()+"\n";
        user += "CITY              : "+getCity()+"\n";
        user += "ADDRESS           : "+getAddress()+"\n";
        user += "WEBPAGE URL       : "+getWebpage()+"\n";
        user += "TIMESTAMP         : "+getTimeStamp();
        return user;
    }

    @Override
    public PDFObject getPDF() {
        PDFObject pdf = new PDFObject();
        pdf.setPdfTitle(getUserName());
        pdf.setPdfKeywords("User, "+getUserName()+", Account");
        pdf.setSubject("User Account Information for user "+getUserName());
        Paragraph p1 = new Paragraph(new Chunk(
                "OpenTox - User Report\n\n",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));
        pdf.addElement(p1);


        try {
            PdfPTable table = new PdfPTable(2);
            table.setWidths(new int[]{30, 50});
            PdfPCell cell = new PdfPCell(new Paragraph("User Account Information"));
            cell.setColspan(2);
            cell.setBackgroundColor(new BaseColor(0xC0, 0xC0, 0xC0));
            table.addCell(cell);

            table.addCell("UserName");
            table.addCell(getUserName());

            table.addCell("First Name");
            table.addCell(getFirstName());

            table.addCell("Last Name");
            table.addCell(getLastName());

            table.addCell("e-mail");
            table.addCell(getEmail());

            table.addCell("UserGroup");
            table.addCell(getUserGroup().getName());

            table.addCell("Authorization Level");
            table.addCell(Integer.toString(getUserGroup().getLevel()));
            

            table.addCell("Organization");
            table.addCell(getOrganization());


            table.addCell("Country");
            table.addCell(getCountry());


            table.addCell("City");
            table.addCell(getCity());


            table.addCell("Address");
            table.addCell(getAddress());

            table.addCell("Web Page");
            table.addCell(getWebpage());

            table.addCell("Created on");
            table.addCell(getTimeStamp());


            pdf.addElement(table);

        } catch (DocumentException ex) {
            YaqpLogger.LOG.log(new Warning(getClass(), "XEI909 - Error while generating " +
                    "PDF representation for User"));
        }

        return pdf;
    }


     public static void main(String args[]) throws FileNotFoundException, DbException {
        User u = ReaderHandler.searchUsers(new User()).get(1);
        u.getPDF().publish(new YaqpIOStream(new FileOutputStream("/home/chung/Desktop/user.pdf")));

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
        return "user";
    }

    @Override
    public URI uri() throws YaqpException {
        String superUri = super.uri().toString();
        try{
        return new URI(superUri+"/"+getUserName());
        } catch (URISyntaxException ex){
            throw new YaqpException("XGL79", "Improper URI", ex);
        }
    }




}