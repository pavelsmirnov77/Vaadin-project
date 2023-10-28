package ru.sovkombank.project;

import com.vaadin.flow.spring.annotation.EnableVaadin;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableVaadin
@ComponentScan(basePackages = {
        "ru.sovkombank.project.entities",
        "ru.sovkombank.project.config",
        "ru.sovkombank.project.repositories",
        "ru.sovkombank.project.services"
})
public class VaadinProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(VaadinProjectApplication.class, args);
    }
}
