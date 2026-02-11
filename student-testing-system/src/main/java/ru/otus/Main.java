package ru.otus;

import ru.otus.integration.service.ApplicationConfig;
import ru.otus.services.TestingService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        TestingService testingService = context.getBean(TestingService.class);
        testingService.conductTest();
    }
}