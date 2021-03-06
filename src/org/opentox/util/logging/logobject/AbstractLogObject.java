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
package org.opentox.util.logging.logobject;

import org.apache.log4j.Level;

/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class AbstractLogObject implements LogObject{

    private String message = "";
    private Level level = Level.WARN;
    private Class source = null;

    public AbstractLogObject(Class clash) {
        this.source = clash;
    }

    public AbstractLogObject(Class clash, String message) {
        this.source = clash;
        this.message = message;
    }

    

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public void setLogType(Level level) {
        this.level = level;
    }

    public void setSource(Class clash) {
        this.source = clash;
    }

    public Class getSource() {
        return source;
    }

    public Level getLevel() {
        return level;
    }

}
