package ru.otus.services;

public interface IOService {
    void outputLine(String message);

    String readLineWithPrompt(String prompt);
}