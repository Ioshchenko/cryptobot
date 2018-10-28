package com.cryptobot.controler;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InfoController {

    @GetMapping("/")
    public String info() {
        return "index";
    }

}
