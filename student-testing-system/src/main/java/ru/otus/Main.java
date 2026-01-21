package ru.otus;

import ru.otus.services.TestingService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
        TestingService testingService = context.getBean(TestingService.class);
        testingService.conductTest();
    }
}