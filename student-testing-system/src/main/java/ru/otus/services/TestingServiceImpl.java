package ru.otus.services;

import ru.otus.domain.Question;

import java.util.List;

public class TestingServiceImpl implements TestingService {
    private final QuestionService questionService;

    public TestingServiceImpl(QuestionService questionService) {
        this.questionService = questionService;
    }

    @Override
    public void conductTest() {
        List<Question> questions = questionService.getAllQuestions();

        System.out.println("=== Student Testing System ===");
        System.out.println("");

        int questionNumber = 1;
        for (Question question : questions) {
            System.out.printf("Question %d: %s" + "%n", questionNumber, question.getText());

            if (question.hasOptions()) {
                List<String> options = question.getAnswerOptions();
                for (int i = 0; i < options.size(); i++) {
                    System.out.printf("  %d) %s" + "%n", i + 1, options.get(i));
                }
            } else {
                System.out.println("  (Free answer)");
            }

            System.out.println("");
            questionNumber++;
        }
    }
}
