package com.cryptobot.info;


import com.cryptobot.kafka.ExmoConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class InfoController {

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    @RequestMapping("/")
    public String info() {
        return "Start";
    }

    @RequestMapping("/exmo")
    public String exmo() {
        //
        return "Start consumer";
    }

}
