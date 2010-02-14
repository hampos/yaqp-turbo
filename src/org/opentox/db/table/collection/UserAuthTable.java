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
 * Authorization is specified by a four-character array. The first
 * character corresponds to the ability of a user to perform GET (<code>A</code>
 * stands for no, <code>B</code> stands for yes but limited to resoruces belonging
 * to the user, <code>C</code> is yes but limited to users of the same group of of
 * a group with smaller level and <code>D</code> is always yes. <code>E</code>,
 * <code>F</code> and <code>G</code> are reserved for special cases). The second, third
 * and fourth character are related to POST, PUT and DELETE METHODS.
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public final class UserAuthTable {

    private static final String
            _TABLE = "USER_AUTH",
            _NAME = "NAME",
            _USERLEVEL = "USER_LEVEL",
            _MODELAUTH = "MODEL_AUTH", DEFAULT_MODELAUTH = "'CCAB'",
            _USERAUTH = "USER_AUTH", DEFAULT_USERAUTH = "'BCAA'",
            _ALGORITHM_AUTH = "ALGORITHM_AUTH", DEFAULT_ALGORITHM_AUTH = "'CCAA'",
            _USER_GROUP_AUTH = "USER_GROUP_AUTH", DEFAULT_USERGROUP_AUTH = "'AAAA'",
            _MAX_MODELS = "MAX_MODELS";

    private static final int NAME_SIZE = 40,
            DEFAULT_USERLEVEL = 1,
            DEFAULT_MAX_MODELS = 2000;

    
    public static final TableColumn NAME = name();
    public static final TableColumn USER_LEVEL = userLevel();
    public static final TableColumn MODEL_AUTH = modelAuth();
    public static final TableColumn ALGORITHM_AUTH = algorithmAuth();
    public static final TableColumn USER_AUTH = userAuth();
    public static final TableColumn USER_GROUP_AUTH = userGroupAuth();
    public static final TableColumn MAX_MODELS = maxModels();
    public static final Table TABLE = table();

    
    private static final Table table() {
        Table table = new Table(_TABLE);
        table.addColumn(NAME);
        table.addColumn(USER_LEVEL);
        table.addColumn(MODEL_AUTH);
        table.addColumn(USER_AUTH);
        table.addColumn(ALGORITHM_AUTH);
        table.addColumn(USER_GROUP_AUTH);
        table.addColumn(MAX_MODELS);
        return table;
    }

    private static TableColumn name() {
        TableColumn name = new TableColumn(_NAME);
        name.setPrimaryKey(true, false);
        name.setColumnType(SQLDataTypes.VarChar(NAME_SIZE));
        name.setNotNull(true);
        return name;
    }

    private static TableColumn userLevel() {
        TableColumn user_level = new TableColumn(_USERLEVEL);
        user_level.setColumnType(SQLDataTypes.Int());
        user_level.setDefaultValue(Integer.toString(DEFAULT_USERLEVEL));
        return user_level;
    }

    private static TableColumn modelAuth() {
        TableColumn mod_auth = new TableColumn(_MODELAUTH);
        mod_auth.setColumnType(SQLDataTypes.VarChar(5));
        mod_auth.setNotNull(true);
        mod_auth.setDefaultValue(DEFAULT_MODELAUTH);
        return mod_auth;
    }

    private static TableColumn userAuth() {
        TableColumn user_auth = new TableColumn(_USERAUTH);
        user_auth.setColumnType(SQLDataTypes.VarChar(5));
        user_auth.setNotNull(true);
        user_auth.setDefaultValue(DEFAULT_USERAUTH);
        return user_auth;
    }

    private static TableColumn algorithmAuth() {
        TableColumn alg = new TableColumn(_ALGORITHM_AUTH);
        alg.setColumnType(SQLDataTypes.VarChar(5));
        alg.setNotNull(true);
        alg.setDefaultValue(DEFAULT_ALGORITHM_AUTH);
        return alg;
    }

    private static TableColumn userGroupAuth() {
        TableColumn user_group_auth = new TableColumn(_USER_GROUP_AUTH);
        user_group_auth.setColumnType(SQLDataTypes.VarChar(5));
        user_group_auth.setNotNull(true);
        user_group_auth.setDefaultValue(DEFAULT_USERGROUP_AUTH);
        return user_group_auth;
    }

    private static TableColumn maxModels() {
        TableColumn max_models = new TableColumn(_MAX_MODELS);
        max_models.setColumnType(SQLDataTypes.Int());
        max_models.setNotNull(true);
        max_models.setDefaultValue(Integer.toString(DEFAULT_MAX_MODELS));
        return max_models;
    }

   
}
