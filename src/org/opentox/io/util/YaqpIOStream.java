package org.opentox.io.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

/**
 *
 * @author chung
 */
public class YaqpIOStream {

    private InputStream in = null;
    private OutputStream out = null;

    public YaqpIOStream(Object ioStream){
        if (ioStream instanceof InputStream){
            System.out.println("I-Stream");
            in = (InputStream) ioStream;
        }else if (ioStream instanceof OutputStream){
            System.out.println("O-Stream");
            out = (OutputStream) ioStream;
        }else{
            System.out.println("NO-Stream");
            // TODO: Exception
        }
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