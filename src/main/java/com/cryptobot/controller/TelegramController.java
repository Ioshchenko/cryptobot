package com.cryptobot.controller;

import com.cryptobot.model.CommandParameters;
import com.cryptobot.model.telegram.RequestMessage;
import com.cryptobot.model.telegram.ResponseMessage;
import com.cryptobot.model.telegram.UpdateEntity;
import com.cryptobot.service.TelegramMessageService;
import com.cryptobot.service.TelegramService;
import com.cryptobot.service.UserService;
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
    @Autowired
    private UserService userService;

    @PostMapping("/telegram")
    public ResponseEntity update(@RequestBody UpdateEntity entity) {
        log.info(entity);
        ResponseMessage responseMessage = entity.getMessage();
        if (responseMessage != null) {
            RequestMessage message = getRequestMessage(responseMessage);
            telegramService.sendMessage(message);
        }

        return ResponseEntity.ok().build();
    }

    private RequestMessage getRequestMessage(ResponseMessage responseMessage) {
        RequestMessage message = new RequestMessage();
        message.setChatId(responseMessage.getChat().getId());
        message.setReplyToMessageId(responseMessage.getMessageId());
        CommandParameters parameters = CommandParameters.builder()
                .userInput(responseMessage.getText())
                .user(userService.getUserByTelegramId(responseMessage.getFrom().getId()))
                .build();

        message.setText(messageService.buildTextMessage(parameters));
        message.setParseMode("HTML");
        return message;
    }


}
