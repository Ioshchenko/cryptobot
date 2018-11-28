package com.cryptobot.repository;

import com.cryptobot.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findByTelegramId(long telegramId);
}
