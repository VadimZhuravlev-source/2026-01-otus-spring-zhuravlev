package ru.otus.domain;

public class Answer {
    private final String text;
    private final boolean isCorrectAnswer;

    public Answer(String text, boolean isCorrectAnswer) {
        this.isCorrectAnswer = isCorrectAnswer;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public boolean isCorrectAnswer() {
        return isCorrectAnswer;
    }
}
