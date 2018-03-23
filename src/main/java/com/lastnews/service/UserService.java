package com.lastnews.service;

import com.lastnews.model.User;

public interface UserService {

    String registerUser(User user);

    User findByEmail(String email);

    String userActivation(String activationCode);

}
