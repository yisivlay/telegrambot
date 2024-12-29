package com.isldev.base.service;

import com.isldev.base.entity.User;
import com.isldev.base.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author YISivlay
 */
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserByChatId(String chatId) {
        return userRepository.findByChatId(chatId).orElse(null);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }
}
