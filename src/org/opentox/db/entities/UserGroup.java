package org.opentox.db.entities;

import java.io.Serializable;

/**
 *
 * A Group of users all of which have the same authorization level.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class UserGroup implements Serializable{

    private static final long serialVersionUID = 8259426573061974195L;

    private String _name;
    private int _level;

    public UserGroup(){

    }

    public UserGroup(String name, int level){
        setName(name);
        setLevel(level);
    }

    public String getName() {
        return _name;
    }

    public int getLevel() {
        return _level;
    }

    public void setName(String name) {
        this._name = name;
    }

    public void setLevel(int level) {
        this._level = level;
    }

    @Override
    public String toString(){
        String userGroup = "";
        userGroup += "GROUP NAME          : "+getName()+"\n";
        userGroup += "AUTHORIZATION LEVEL : "+getLevel();
        return userGroup;
    }




}
