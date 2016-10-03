package com.marcelib.microplayer;

import com.marcelib.microplayer.beans.ConnectionBean;
import com.marcelib.microplayer.web.rest.PollController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class MicroPlayerApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(MicroPlayerApplication .class, args);
        PollController pollController = new PollController((ConnectionBean)context.getBean("connectionBean"));
        pollController.sendJudgeRecognize();
    }
}
