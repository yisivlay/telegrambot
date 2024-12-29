package com.isldev.base.repository;

import com.isldev.base.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author YISivlay
 */
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByChatId(String chatId);

}
