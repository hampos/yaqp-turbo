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

    /**
     *
     * @param level
     * @throws NumberFormatException in case the provided level in not an integer
     */
    public void setLevel(int level) throws NumberFormatException{
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
