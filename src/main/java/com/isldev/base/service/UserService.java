package com.isldev.base.service;

import com.isldev.base.entity.User;

/**
 * @author YISivlay
 */
public interface UserService {

    User saveUser(User user);

    User getUserByChatId(String chatId);

}
