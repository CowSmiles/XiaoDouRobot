package com.klniu.xiaoyi;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Created by klniu on 17-1-12.
 */ /* Used to trick minim constructor (needs an object with those methods) */
public class MinimInput {
    public String sketchPath( String fileName ) {
        return fileName;
    }
    public InputStream createInput(String fileName) {
        InputStream is = null;
        try{
            is = new FileInputStream(sketchPath(fileName));
        }
        catch(Exception e){
            System.out.println(e.toString());
        }
        return is;
    }
}
