package com.cryptobot.repository;

import com.cryptobot.model.ExmoIndex;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExmoRepository extends CrudRepository<ExmoIndex, Long> {
}
