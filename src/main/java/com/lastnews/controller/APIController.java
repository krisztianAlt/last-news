package com.lastnews.controller;

import com.lastnews.dataFromApi.NewsApi;
import com.lastnews.model.User;
import com.lastnews.repository.UserRepository;
import com.lastnews.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;
import javax.json.*;

@RestController
public class APIController {

    // JsonBuilder from javax.json:
    private JsonObjectBuilder objectBuilder = Json.createBuilderFactory(null).createObjectBuilder();

    // JsonParser from org.springframework.boot.json:
    private JsonParser jsonParser = JsonParserFactory.getJsonParser();

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    NewsApi newsApi;

    // if JavaScript code sends jsonified map object:
    @RequestMapping(value = "/api/get-news", method = RequestMethod.POST)
    public String sendNews(@RequestBody String dataPackage){
        Map<String, Object> result = jsonParser.parseMap(dataPackage);
        String countryName = (String) result.get("countryName");
        String countryCode = (String) result.get("countryCode");
        String news = newsApi.getDataByCountryName(countryName);
        JsonObject answer = objectBuilder.add("answer", news).build();
        return answer.toString();
    }

    /*// if JavaScript code sends map:
    @RequestMapping(value = "/api/get-news", method = RequestMethod.POST)
    public String sendNews(@RequestParam Map<String,String> allRequestParams){
        String countryName = allRequestParams.get("countryName");
        String countryCode = allRequestParams.get("countryCode");
        String news = newsApi.getDataByCountryName(countryName);
        JsonObject answer = objectBuilder.add("answer", news).build();
        return answer.toString();
    }*/

    // FUNCTIONS FOR TESTING.
    // IN ORDER TO USAGE, PUT PERMISSIONS INTO SecurityConfig:
    // .antMatchers("/session-check").permitAll()
    // .antMatchers("/database-check").permitAll()
    // .antMatchers("/email-check").permitAll()

    @RequestMapping(value = "/session-check", method={RequestMethod.GET, RequestMethod.POST})
    public String sessionData(HttpSession session){
        if (session.getAttribute("user_id") != null){
            return session.getAttribute("user_id").toString();
        }
        return "No user id in session.";
    }

    @RequestMapping(value = "/database-check", method={RequestMethod.GET, RequestMethod.POST})
    public String checkDatabase(){
        try{
            User user = userRepository.findByEmail("teszt@freemail.hu");
            return "OKAY: " + user.toString();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        return "No user in database yet.";
    }

    @RequestMapping(value = "/email-check", method={RequestMethod.GET, RequestMethod.POST})
    public String checkEmailSending(){
        try{
            User user = userRepository.findByEmail("meropa@freemail.hu");
            emailService.sendMessage(user, "here_comes_the_key");
            return "Email sent successfully.";
        } catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
        return "Error during email sending process.";
    }

}