package ru.otus.domain;

import java.util.List;

public class Question {
    private final String text;
    private final List<Answer> answerOptions;

    public Question(String text, List<Answer> answerOptions) {
        this.text = text;
        this.answerOptions = answerOptions;
    }

    public String getText() {
        return text;
    }

    public List<Answer> getAnswerOptions() {
        return answerOptions;
    }

    public boolean hasOptions() {
        return answerOptions != null && !answerOptions.isEmpty();
    }

}
