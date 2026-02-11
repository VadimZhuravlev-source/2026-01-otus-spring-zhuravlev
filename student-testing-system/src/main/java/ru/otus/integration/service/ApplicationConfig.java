package ru.otus.integration.service;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("ru.otus")
@PropertySource("classpath:application.properties")
public class ApplicationConfig {
}