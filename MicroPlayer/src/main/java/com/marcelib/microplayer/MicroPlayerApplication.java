package com.marcelib.microplayer;

import com.marcelib.microplayer.beans.ConnectionBean;
import com.marcelib.microplayer.web.rest.PollController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

@SpringBootApplication
public class MicroPlayerApplication {

    private final static Logger LOGGER = Logger.getLogger(MicroPlayerApplication.class.getName());

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(MicroPlayerApplication.class, args);
        PollController pollController = new PollController((ConnectionBean) context.getBean("connectionBean"));
        pollController.sendJudgeRecognize();
        LOGGER.info(""+((ConnectionBean) context.getBean("connectionBean")).getConnectedServer().getId());

        Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                pollController.sendJudgePoll();
            }
        }, 0, 2000);
    }
}
