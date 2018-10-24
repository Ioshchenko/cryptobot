package com.cryptobot.info;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller
public class InfoController {

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    @GetMapping("/")
    public String info() {
        return "index";
    }


}
