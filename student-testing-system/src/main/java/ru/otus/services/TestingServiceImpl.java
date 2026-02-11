package ru.otus.services;

import ru.otus.domain.Answer;
import ru.otus.domain.Question;
import ru.otus.domain.Student;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

@Service
public class TestingServiceImpl implements TestingService {
    private final QuestionService questionService;
    private final int passingScore;
    private final int totalQuestions;
    private final PrintStream output = System.out;
    private final Scanner input = new Scanner(System.in);

    public TestingServiceImpl(QuestionService questionService,
                              @Value("${testing.passing.score}") int passingScore,
                              @Value("${testing.total.questions}") int totalQuestions) {
        this.questionService = questionService;
        this.passingScore = passingScore;
        this.totalQuestions = totalQuestions;
    }

    @Override
    public void conductTest() {
        outputLine("=== Student Testing System ===");
        outputLine("");

        Student student = getStudentInfo();
        outputLine("Hello, " + student.getFirstName() + " " + student.getLastName() + "!");
        outputLine("");

        List<Question> allQuestions = questionService.getAllQuestions();
        List<Question> questions = allQuestions.size() > totalQuestions 
            ? allQuestions.subList(0, totalQuestions) 
            : allQuestions;

        outputLine("Please answer the following " + questions.size() + " questions:");
        outputLine("");

        int correctAnswers = 0;
        int questionNumber = 1;

        for (Question question : questions) {
            outputLine("Question " + questionNumber + ": " + question.getText());

            if (question.hasOptions()) {
                List<Answer> options = question.getAnswerOptions();
                for (int i = 0; i < options.size(); i++) {
                    outputLine("  " + (i + 1) + ") " + options.get(i).getText());
                }

                String userAnswer = readLineWithPrompt("Your answer (1-" + options.size() + "): ");

                int userChoice;
                try {
                    userChoice = Integer.parseInt(userAnswer.trim());
                } catch (NumberFormatException exception) {
                    continue;
                }

                for (int i = 0; i < options.size(); i++) {
                    var answer = options.get(i);
                    if (answer.isCorrectAnswer() && userChoice - 1 == i) {
                        correctAnswers++;
                    }
                }

            }

            outputLine("");
            questionNumber++;
        }

        outputLine("=== Test Results ===");
        outputLine("Student: " + student.getFirstName() + " " + student.getLastName());
        outputLine("Correct answers: " + correctAnswers + " out of " + questions.size());

        if (correctAnswers >= passingScore) {
            outputLine("Result: PASSED");
        } else {
            outputLine("Result: FAILED");
            outputLine("Passing score required: " + passingScore);
        }
    }

    private void outputLine(String message) {
        output.println(message);
    }

    private String readLineWithPrompt(String prompt) {
        output.print(prompt);
        return input.nextLine();
    }

    private Student getStudentInfo() {
        String firstName = readLineWithPrompt("Please enter your first name: ");
        String lastName = readLineWithPrompt("Please enter your last name: ");
        return new Student(firstName, lastName);
    }

}
