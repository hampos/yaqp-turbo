/*
 * YAQP - Yet Another QSAR Project: Machine Learning algorithms designed for
 * the prediction of toxicological features of chemical compounds become
 * available on the Web. Yaqp is developed under OpenTox (http://opentox.org)
 * which is an FP7-funded EU research project.
 *
 * Copyright (C) 2009-2010 Pantelis Sopasakis & Charalampos Chomenides
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
 */
package org.opentox.db.entities;

import java.io.Serializable;

/**
 *
 * A User accessing YAQP from the web.
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class User implements Serializable {

    private static final long serialVersionUID = 1726532809162869471L;

    private String
            _userName,
            _userPass,
            _firstName,
            _lastName,
            _email,
            _organization,
            _country,
            _city,
            _address,
            _webpage,
            _timeStamp,
            _userGroup;
                    /*
                     *  Note: _userGroup is the *NAME* of the user group
                     *  not the UID in the db.
                     */

    public User(){

    }

    /**
     *
     * @param userName username
     * @param userPass user password
     * @param firstName first name of the user
     * @param lastName last name of the user
     * @param email email
     * @param userGroup user group
     */
    public User(String userName, 
            String userPass,
            String firstName,
            String lastName,
            String email,
            String userGroup){
        setUserName(userName);
        setUserPass(userPass);
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
        setUserGroup(userGroup);
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
            String timestamp,
            String userGroup){
        this(userName, userPass, firstName, lastName, email, userGroup);
        setOrganization(organization);
        setCountry(country);
        setCity(city);
        setAddress(address);
        setWebpage(webpage);
        setTimeStamp(timestamp);
    }


    public String getAddress() {
        return _address;
    }

    public String getCity() {
        return _city;
    }

    public String getCountry() {
        return _country;
    }

    public String getEmail() {
        return _email;
    }

    public String getFirstName() {
        return _firstName;
    }

    public String getLastName() {
        return _lastName;
    }

    public String getOrganization() {
        return _organization;
    }

    public String getUserGroup() {
        return _userGroup;
    }


    public String getUserName() {
        return _userName;
    }

    public String getUserPass() {
        return _userPass;
    }

    public String getWebpage() {
        return _webpage;
    }

    public String getTimeStamp(){
        return _timeStamp;
    }



    public void setAddress(String address) {
        this._address = address;
    }

    public void setCity(String city) {
        this._city = city;
    }

    public void setCountry(String country) {
        this._country = country;
    }

    public void setEmail(String email) {
        this._email = email;
    }

    public void setFirstName(String firstName) {
        this._firstName = firstName;
    }

    public void setLastName(String lastName) {
        this._lastName = lastName;
    }

    public void setOrganization(String organization) {
        this._organization = organization;
    }

    public void setUserGroup(String _userGroup) {
        this._userGroup = _userGroup;
    }
    

    public void setUserName(String userName) {
        this._userName = userName;
    }

    public void setUserPass(String userPass) {
        this._userPass = userPass;
    }

    public void setWebpage(String webpage) {
        this._webpage = webpage;
    }

    public void setTimeStamp(String timestamp){
        this._timeStamp = timestamp;
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

}
