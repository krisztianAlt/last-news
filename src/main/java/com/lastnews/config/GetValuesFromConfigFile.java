package com.lastnews.config;

import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Service
public class GetValuesFromConfigFile {

    private String result = "";
    private InputStream inputStream;

    public String getPropValue(String key) {

        try {
            Properties prop = new Properties();
            String propFileName = "config.properties";

            inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }

            // get the property value
            result = prop.getProperty(key);
            inputStream.close();
        } catch (IOException e) {
            System.out.println("File reading failed: " + e);
        }

        return result;
    }
}