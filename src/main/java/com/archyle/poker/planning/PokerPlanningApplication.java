package com.archyle.poker.planning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableWebSecurity
@SpringBootApplication
public class PokerPlanningApplication {
    public static void main(String[] args) {
        SpringApplication.run(PokerPlanningApplication.class, args);
    }
}
