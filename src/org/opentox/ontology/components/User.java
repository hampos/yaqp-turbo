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

import org.opentox.io.publishable.JSONObject;
import org.opentox.io.publishable.OntObject;
import org.opentox.io.publishable.PDFObject;
import org.opentox.io.publishable.RDFObject;
import org.opentox.io.publishable.TurtleObject;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class User extends YaqpComponent {
     private static final long serialVersionUID = 1726532809162869471L;

    private String
            userName,
            userPass,
            firstName,
            lastName,
            email,
            organization,
            country,
            city,
            address,
            webpage,
            timeStamp;
    private UserGroup userGroup;


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
        String user = "-- USER --\n";
        user += "USERNAME          : "+getUserName()+"\n";
        user += "PASSWORD DIGEST   : "+getUserPass()+"\n";
        user += "USER GROUP        : "+getUserGroup()+"\n";
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
        throw new UnsupportedOperationException("Not supported yet.");
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