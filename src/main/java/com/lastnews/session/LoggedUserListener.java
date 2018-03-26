package com.lastnews.session;

import com.lastnews.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import javax.servlet.http.HttpSession;

@Configuration
public class LoggedUserListener implements
        ApplicationListener<AuthenticationSuccessEvent> {

    @Autowired
    private HttpSession session;

    // SESSION MANAGEMENT: PUT USER ID INTO SESSION
    // Hint: https://stackoverflow.com/questions/24882007/populate-user-session-after-login
    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        UserDetailsImpl userDetails = (UserDetailsImpl) event.getAuthentication().getPrincipal();
        session.setAttribute("user_id", userDetails.getId());
    }

}