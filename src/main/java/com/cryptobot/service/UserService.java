package com.cryptobot.service;

import com.cryptobot.model.User;
import com.cryptobot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User getUserByTelegramId(int id) {
        return userRepository.findByTelegramId(id);

    }

    public void save(User user) {
        userRepository.save(user);
    }

    public User getDefaultUser() {
        return getUserByTelegramId(Integer.valueOf(System.getenv("DEFAULT_USER_ID")));
    }
}
