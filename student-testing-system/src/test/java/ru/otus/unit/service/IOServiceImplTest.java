package ru.otus.unit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.services.IOServiceImpl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class IOServiceImplTest {

    private ByteArrayOutputStream outputStream;
    private IOServiceImpl ioService;

    @BeforeEach
    void setUp() {
        outputStream = new ByteArrayOutputStream();
    }

    @Test
    void outputLine_shouldPrintMessageWithNewline() {
        ioService = new IOServiceImpl(System.in, new PrintStream(outputStream));
        String message = "Test message";

        ioService.outputLine(message);

        assertEquals(message + System.lineSeparator(), outputStream.toString());
    }

    @Test
    void outputLine_shouldPrintEmptyLine() {
        ioService = new IOServiceImpl(System.in, new PrintStream(outputStream));

        ioService.outputLine("");

        assertEquals(System.lineSeparator(), outputStream.toString());
    }

    @Test
    void readLineWithPrompt_shouldDisplayPromptAndReadInput() {
        String input = "User input";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        ioService = new IOServiceImpl(inputStream, new PrintStream(outputStream));

        String result = ioService.readLineWithPrompt("Enter name: ");

        assertEquals(input, result);
        assertEquals("Enter name: ", outputStream.toString());
    }

    @Test
    void readLineWithPrompt_shouldHandleEmptyInput() {
        String input = "";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        ioService = new IOServiceImpl(inputStream, new PrintStream(outputStream));

        String result = ioService.readLineWithPrompt("Prompt: ");

        assertEquals(input, result);
    }

    @Test
    void readLineWithPrompt_shouldHandleMultipleLines() {
        String input = "First line\nSecond line";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        ioService = new IOServiceImpl(inputStream, new PrintStream(outputStream));

        String firstResult = ioService.readLineWithPrompt("First: ");
        String secondResult = ioService.readLineWithPrompt("Second: ");

        assertEquals("First line", firstResult);
        assertEquals("Second line", secondResult);
    }
}