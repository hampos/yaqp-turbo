package org.opentox.db.util;

import org.opentox.db.exceptions.BadEmailException;

/**
 *
 * Checks the integrity of a provided email address
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class EmailSupervisor {

    public static boolean checkMail(String email) throws BadEmailException{
        String[] parts = email.split("@");
        if (parts.length!=2){
            return false;
        }else{
            if (!parts[1].contains(".")){
                return false;
            }
        }       
        return true;
    }


}
