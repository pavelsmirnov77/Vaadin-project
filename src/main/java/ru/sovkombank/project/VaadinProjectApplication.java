package ru.sovkombank.project;

import com.vaadin.flow.spring.annotation.EnableVaadin;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableVaadin
public class VaadinProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(VaadinProjectApplication.class, args);
    }
}
