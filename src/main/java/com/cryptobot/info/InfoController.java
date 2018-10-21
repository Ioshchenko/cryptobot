package com.cryptobot.info;

import com.cryptobot.kafka.KafkaConector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InfoController {
    @Autowired
    private KafkaConector conector;

    @RequestMapping("/")
    public String info() {
        conector.produce();
        conector.consume();
        return "Start";
    }

}
