package ru.practicum.explore_with_me;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MainStatsService {
    public static void main(String[] args) {
        SpringApplication.run(MainStatsService.class, args);
        System.out.println("*".repeat(105) + "\n" + "*".repeat(20)
                + "       Диплом. Explore with me. Сервер статистики запущен.       " + "*".repeat(20)
                + "\n" + "*".repeat(105));
    }
}