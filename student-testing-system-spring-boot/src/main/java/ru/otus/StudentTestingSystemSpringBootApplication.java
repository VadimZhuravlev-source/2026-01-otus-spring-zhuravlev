package ru.otus;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.otus.services.TestingService;

@SpringBootApplication
public class StudentTestingSystemSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudentTestingSystemSpringBootApplication.class, args);
	}

	@Bean
	public CommandLineRunner run(TestingService testingService) {
		return args -> {
			testingService.conductTest();
		};
	}

}
