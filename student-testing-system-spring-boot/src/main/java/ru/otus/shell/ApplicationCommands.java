package ru.otus.shell;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.services.TestingService;

@ShellComponent
public class ApplicationCommands {

    private final TestingService testingService;

    public ApplicationCommands(TestingService testingService) {
        this.testingService = testingService;
    }

    @ShellMethod(value = "Hello", key = {"start", "st"})
    public void startTest() {
        testingService.conductTest();
    }
}