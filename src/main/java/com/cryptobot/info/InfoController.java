package com.cryptobot.info;


import com.cryptobot.kafka.ExmoConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InfoController {
   @Autowired
   private ExmoConsumer consumer;

    @RequestMapping("/")
    public String info() {
        return "Start";
    }

    @RequestMapping("/exmo")
    public String exmo(){
        consumer.poll();
        return "Start consumer";
    }

}
