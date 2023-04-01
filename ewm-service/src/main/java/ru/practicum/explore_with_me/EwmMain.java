package ru.practicum.explore_with_me;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication(scanBasePackages = {"ru.practicum.explore_with_me"})
@Import({StatsClient.class})
public class EwmMain {
    public static void main(String[] args) {
        SpringApplication.run(EwmMain.class, args);
    }
}
