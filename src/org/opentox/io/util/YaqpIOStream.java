/*
 *
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
package org.opentox.io.util;

import java.io.InputStream;
import java.io.OutputStream;


/**
 *
 * @author Pantelis Sopasakis
 * @author Charalampos Chomenides
 */
public class YaqpIOStream {

    private InputStream in;
    private OutputStream out;


    public YaqpIOStream(final Object ioStream){
        if (ioStream instanceof InputStream){
            in = (InputStream) ioStream;
        }else if (ioStream instanceof OutputStream){
            out = (OutputStream) ioStream;
        }else{
            // TODO: Exception
        }
    }

    public YaqpIOStream(final InputStream in){
        this.in = in;
    }

    public YaqpIOStream(final OutputStream out){
        this.out = out;
    }
    
    public Object getStream(){
        if (in != null){
            return in;
        }else if (out != null){
            return out;
        }else{
            return null;
        }
    }
//
//    public static void main(String[] args) throws Exception{
//        String text = "abc";
//        ByteArrayInputStream i = new ByteArrayInputStream(text.getBytes("UTF-8"));
//        YaqpIOStream ys = new YaqpIOStream(i);
//        System.out.println(ys.getStream() instanceof InputStream);
//    }





}
