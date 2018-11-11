package com.cryptobot.controller;

import com.cryptobot.model.telegram.RequestMessage;
import com.cryptobot.model.telegram.UpdateEntity;
import com.cryptobot.service.TelegramMessageService;
import com.cryptobot.service.TelegramService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
public class TelegramController {
    @Autowired
    private TelegramService telegramService;

    @Autowired
    private TelegramMessageService messageService;

    @PostMapping("/telegram")
    public ResponseEntity update(@RequestBody UpdateEntity entity) {
        log.info(entity);

        RequestMessage message = new RequestMessage();
        message.setChatId(entity.getResponseMessage().getChat().getId());
        message.setReplyToMessageId(entity.getResponseMessage().getMessageId());
        message.setText(messageService.buildTextMessage(entity.getResponseMessage().getText()));
        telegramService.sendMessage(message);

        return ResponseEntity.ok().build();
    }


}
