package ru.otus.services;

import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

@Service
public class IOServiceImpl implements IOService {
    private final PrintStream output;
    private final Scanner input;

    public IOServiceImpl() {
        this(System.in, System.out);
    }

    public IOServiceImpl(InputStream inputStream, PrintStream outputStream) {
        this.input = new Scanner(inputStream);
        this.output = outputStream;
    }

    @Override
    public void outputLine(String message) {
        output.println(message);
    }

    @Override
    public String readLineWithPrompt(String prompt) {
        output.print(prompt);
        return input.nextLine();
    }
}