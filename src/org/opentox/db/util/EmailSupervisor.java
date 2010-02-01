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
package org.opentox.db.util;


/**
 *
 * <p>Checks the integrity of a provided email address. Checks if the email address
 * is non-empty, contains special characters, complies with the general structure
 * <code>username@mailserver.ext</code> or <code>username@mailserver.subdomain.ext</code>,
 * and checks whether the given email ends with a dot. Other tests might be also added
 * in the future.</p>
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class EmailSupervisor {

    /**
     * <p>Static methods to check the integrity of a given email address.</p>
     * 
     * <p><b>Copied here from EmailSupervisor :</b>
     * Checks the integrity of a provided email address. Checks if the email address
     * is non-empty, contains special characters, complies with the general structure
     * <code>username@mailserver.ext</code> or <code>username@mailserver.subdomain.ext</code>,
     * and checks whether the given email ends with a dot. Other tests might be also added
     * in the future.</p>
     * @param email given email address
     * @return <code>true</code> if the email address is valid, <code>false</code>
     * otherwise.
     * @see EmailSupervisor
     */
    public static boolean checkMail(String email){

        if (
                   email.contains("[")
                || email.contains("]")
                || email.contains("#")
                || email.contains("!")
                || email.contains("$")
                ||  email.contains("%")
                ||  email.contains("&")
                ||  email.contains("*")
                ||  email.contains("(")
                ||  email.contains(")")
                ||  email.contains("+")
                ||  email.contains("=")
                ||  email.contains("{")
                ||  email.contains("}")
                ||  email.contains("'")
                ||  email.contains("\"")
                ||  email.contains("?")
                ||  email.contains("<")
                ||  email.contains(">")
                ||  email.contains(",")
                ||  email.contains("~")
                ||  email.contains("`")
                ||  email.contains("|")
                ||  email.contains("\\")
                ||  email.contains(":")
                ) return false;

        String[] parts = email.split("@");
        if (parts.length!=2) return false;
        else{
            if (!parts[1].contains(".")) return false;
            
            if (parts[1].endsWith(".")) return false;
            
            if (parts[0].equalsIgnoreCase("") || parts[0]==null)  return false;
            
        }       
        return true;
    }


}
