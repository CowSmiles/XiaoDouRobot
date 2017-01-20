package com.klniu.xiaoyi;

import java.io.*;
import java.util.Map;
import java.util.Properties;

/**
 * Created by klniu on 17-1-13.
 */
public class Configuration extends Properties {
    private String filename;

    public Configuration(String filename) throws IOException {
        this.filename = filename;
        InputStream input = null;
        try {
            input = getClass().getClassLoader().getResourceAsStream(filename);
            // load a properties file
            this.load(input);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("can not find config file " + this.filename);
        } catch (IOException e) {
            throw new IOException("can not load configuration for " + this.filename);
        } finally {
            if (input != null) {
                input.close();
            }
        }
    }

    public void setProperties(Map<String, String> props) {
        for (Map.Entry<String, String> m : props.entrySet()) {
            this.setProperty(m.getKey(), m.getValue());
        }
    }

    public void save() throws IOException {
        OutputStream output = null;
        try {
            output = new FileOutputStream(this.filename);
            this.store(output, null);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("can not find config file " + this.filename);
        } catch (IOException e) {
            throw new IOException("can not store configuration for " + this.filename);
        } finally {
            if (output != null) {
                output.close();
            }
        }
        // save properties to project root folder
        output.close();
    }
}
