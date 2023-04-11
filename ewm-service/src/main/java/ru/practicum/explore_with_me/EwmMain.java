package ru.practicum.explore_with_me;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication(scanBasePackages = {"ru.practicum.explore_with_me"})
@Import({WebClientService.class})
public class EwmMain {
    public static void main(String[] args) {
        SpringApplication.run(EwmMain.class, args);
        System.out.println("*".repeat(105) + "\n" + "*".repeat(20)
                + "       Диплом. Explore with me. Основной сервер запущен.       " + "*".repeat(20)
                + "\n" + "*".repeat(105));
    }
}