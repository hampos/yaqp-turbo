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
