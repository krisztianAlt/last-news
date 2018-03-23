package com.lastnews.controller;

import com.lastnews.model.User;
import com.lastnews.repository.UserRepository;
import com.lastnews.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class APIController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailService emailService;

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
