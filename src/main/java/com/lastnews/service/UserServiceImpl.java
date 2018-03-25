package com.lastnews.service;

import com.lastnews.model.Role;
import com.lastnews.model.User;
import com.lastnews.repository.RoleRepository;
import com.lastnews.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {


    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private UserRepository userRepository;

    private RoleRepository roleRepository;

    private final String USER_ROLE = "USER";

    @Autowired
    EmailService emailService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        return new UserDetailsImpl(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public String registerUser(User userToRegister) {
        User userCheck = userRepository.findByEmail(userToRegister.getEmail());

        if (userCheck != null)
            return "alreadyExists";

        Role userRole = roleRepository.findByRole(USER_ROLE);
        if (userRole != null) {
            userToRegister.getRoles().add(userRole);
        } else {
            userToRegister.addRoles(USER_ROLE);
        }

        userToRegister.setEnabled(false);
        String key = generateKey();
        userToRegister.setActivation(key);
        userRepository.save(userToRegister);
        try {
            emailService.sendMessage(userToRegister, key);
        } catch (MessagingException e){
            return "email sending error";
        }

        return "ok";
    }

    public String generateKey()
    {
        Random random = new Random();
        char[] word = new char[16];
        for (int j = 0; j < word.length; j++) {
            word[j] = (char) ('a' + random.nextInt(26));
        }
        String toReturn = new String(word);
        log.debug("random code: " + toReturn);
        return new String(word);
    }

    @Override
    public String userActivation(String code) {
        User user = userRepository.findByActivation(code);
        if (user == null)
            return "noresult";

        user.setEnabled(true);
        user.setActivation("");
        userRepository.save(user);
        return "ok";
    }
}
