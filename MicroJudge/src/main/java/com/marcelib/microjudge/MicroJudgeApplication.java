package com.marcelib.microjudge;

import com.marcelib.microjudge.game.GameHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.Timer;
import java.util.TimerTask;

@SpringBootApplication
public class MicroJudgeApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(MicroJudgeApplication.class, args);

        Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                ((GameHandler) context.getBean("gameHandler")).initiateGame();
            }
        }, 0, 2000);
    }
}