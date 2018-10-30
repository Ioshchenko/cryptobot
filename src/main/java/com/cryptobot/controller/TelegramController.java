package com.cryptobot.controller;

import com.cryptobot.model.telegram.UpdateEntity;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
public class TelegramController {

    @PostMapping("/telegram")
    public ResponseEntity update(@RequestBody UpdateEntity entity) {
        log.info(entity);
        return ResponseEntity.ok().build();
    }
}
