package com.lastnews.dataFromApi;

import com.lastnews.config.GetValuesFromConfigFile;
import com.lastnews.service.EmailService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class NewsApi {

    private final Logger logger = org.slf4j.LoggerFactory.getLogger(NewsApi.class);

    @Autowired
    URLReader urlReader;

    @Autowired
    GetValuesFromConfigFile getValuesFromConfigFile;

    public String getDataByCountryName(String countryName){
        String API_KEY = getValuesFromConfigFile.getPropValue("newsapi-key");
        String endpoint = "https://newsapi.org/v2/everything?q=" +
                countryName +
                "&sources=reuters&sortBy=publishedAt&apiKey=" +
                API_KEY;

        String answer = "";

        try {
            answer = urlReader.readFromUrl(endpoint);
        } catch (IOException e){
            logger.error("API calling failed: " + e.getMessage());
        }

        return answer;
    }

}
