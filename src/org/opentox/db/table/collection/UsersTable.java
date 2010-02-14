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
package org.opentox.db.table.collection;

import org.opentox.db.table.Table;
import org.opentox.db.table.TableColumn;
import org.opentox.db.util.SQLDataTypes;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public final class UsersTable {

    private static final String 
            _TABLE = "USERS",
            _USERNAME = "USERNAME",
            _USERPASS = "PASS",
            _SALT = "SALT",
            _FIRSTNAME = "FIRSTNAME",
            _LASTNAME = "LASTNAME",
            _EMAIL = "EMAIL",
            _COUNTRY = "COUNTRY",
            _CITY = "CITY",
            _ADDRESS = "ADDRESS",
            _ORGANIZATION = "ORGANIZATION",
            _WEBPAGE = "WEBPAGE",
            _TIMESTAMP = "TMSTMP",
            _ROLE = "ROLE";
    private static final int USERNAME_SIZE = 20,
            PASS_SIZE = 255,
            SALT_SIZE = 40,
            FIRSTNAME_SIZE = 20,
            LASTNAME_SIZE = 20,
            EMAIL_SIZE = 50,
            COUNTRY_SIZE = 20,
            CITY_SIZE = 20,
            ADDRESS_SIZE = 50,
            ORGANIZATION_SIZE = 20,
            WEBPAGE_SIZE = 150;

    public static final TableColumn USERNAME = userName();
    public static final TableColumn PASSWORD = password();
    public static final TableColumn SALT = salt();
    public static final TableColumn FIRSTNAME = firstName();
    public static final TableColumn LASTNAME = lastName();
    public static final TableColumn EMAIL = eMail();
    public static final TableColumn COUNTRY = country();
    public static final TableColumn CITY = city();
    public static final TableColumn ADDRESS = address();
    public static final TableColumn ORGANIZATION = organization();
    public static final TableColumn ROLE = role();
    public static final TableColumn TIMESTAMP = timeStamp();
    public static final TableColumn WEBPAGE = webPage();

    public static final Table TABLE = table();

    private static Table table(){
        Table table = new Table(_TABLE);
        table.addColumn(USERNAME);
        table.addColumn(PASSWORD);
        table.addColumn(FIRSTNAME);
        table.addColumn(LASTNAME);
        table.addColumn(EMAIL);
        table.addColumn(ORGANIZATION);
        table.addColumn(COUNTRY);
        table.addColumn(CITY);        
        table.addColumn(ADDRESS);
        table.addColumn(WEBPAGE);
        table.addColumn(TIMESTAMP);
        table.addColumn(ROLE);                
        return table;
    }

    private static TableColumn userName() {
        TableColumn username = new TableColumn(_USERNAME);
        username.setColumnType(SQLDataTypes.VarChar(USERNAME_SIZE));
        username.setUnique(true);
        username.setNotNull(true);
        return username;
    }

    private static TableColumn password() {
        TableColumn password = new TableColumn(_USERPASS);
        password.setColumnType(SQLDataTypes.VarChar(PASS_SIZE));
        password.setNotNull(true);
        return password;
    }

    private static TableColumn salt() {
        TableColumn salt = new TableColumn(_SALT);
        salt.setColumnType(SQLDataTypes.VarChar(SALT_SIZE));
        return salt;
    }

    private static TableColumn firstName() {
        TableColumn firstname = new TableColumn(_FIRSTNAME);
        firstname.setColumnType(SQLDataTypes.VarChar(FIRSTNAME_SIZE));
        firstname.setNotNull(true);
        return firstname;
    }

    private static TableColumn lastName() {
        TableColumn lastname = new TableColumn(_LASTNAME);
        lastname.setColumnType(SQLDataTypes.VarChar(LASTNAME_SIZE));
        lastname.setNotNull(true);
        return lastname;
    }

    private static TableColumn eMail() {
        TableColumn email = new TableColumn(_EMAIL);
        email.setColumnType(SQLDataTypes.VarChar(EMAIL_SIZE));
        email.setPrimaryKey(true, false);
        return email;
    }

    private static TableColumn organization() {
        TableColumn organization = new TableColumn(_ORGANIZATION);
        organization.setColumnType(SQLDataTypes.VarChar(ORGANIZATION_SIZE));
        return organization;
    }

    private static TableColumn country() {
        TableColumn country = new TableColumn(_COUNTRY);
        country.setColumnType(SQLDataTypes.VarChar(COUNTRY_SIZE));
        return country;
    }

    private static TableColumn city() {
        TableColumn city = new TableColumn(_CITY);
        city.setColumnType(SQLDataTypes.VarChar(CITY_SIZE));
        return city;
    }

    private static TableColumn address() {
        TableColumn address = new TableColumn(_ADDRESS);
        address.setColumnType(SQLDataTypes.VarChar(ADDRESS_SIZE));
        return address;
    }

    private static TableColumn webPage() {
        TableColumn webPage = new TableColumn(_WEBPAGE);
        webPage.setColumnType(SQLDataTypes.VarChar(WEBPAGE_SIZE));
        return webPage;
    }

    private static TableColumn timeStamp() {
        TableColumn timestamp = new TableColumn(_TIMESTAMP);
        timestamp.setColumnType(SQLDataTypes.Timestamp());
        timestamp.setDefaultValue("CURRENT TIMESTAMP");
        return timestamp;
    }

    private static TableColumn role() {
        TableColumn role = new TableColumn(_ROLE);
        role.setColumnType(UserAuthTable.NAME.getColumnType());
        role.setForeignKey(UserAuthTable.TABLE.getTableName(), UserAuthTable.NAME.getColumnName(), true);
        return role;
    }
}
