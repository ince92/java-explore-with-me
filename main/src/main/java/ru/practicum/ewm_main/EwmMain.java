package ru.practicum.ewm_main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"ru.practicum.ewm_main", "ru.practicum.ewm_client"})
public class EwmMain {
    public static void main(String[] args) {
        SpringApplication.run(EwmMain.class, args);
    }
}
