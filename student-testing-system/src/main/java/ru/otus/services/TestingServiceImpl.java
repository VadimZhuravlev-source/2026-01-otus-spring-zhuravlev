package ru.otus.services;

import ru.otus.domain.Answer;
import ru.otus.domain.Question;
import ru.otus.domain.Student;
import ru.otus.dao.QuestionDao;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestingServiceImpl implements TestingService {
    private final QuestionDao questionDao;
    private final StudentService studentService;
    private final IOService ioService;
    private final int passingScore;
    private final int totalQuestions;

    public TestingServiceImpl(QuestionDao questionDao,
                              StudentService studentService,
                              IOService ioService,
                              @Value("${testing.passing.score}") int passingScore,
                              @Value("${testing.total.questions}") int totalQuestions) {
        this.questionDao = questionDao;
        this.studentService = studentService;
        this.ioService = ioService;
        this.passingScore = passingScore;
        this.totalQuestions = totalQuestions;
    }

    @Override
    public void conductTest() {
        displayWelcome();
        Student student = studentService.getStudentInfo();
        greetStudent(student);

        List<Question> questions = selectQuestions();
        displayTestInstructions(questions.size());

        int correctAnswers = askQuestions(questions);
        displayResults(student, correctAnswers, questions.size());
    }

    private void displayWelcome() {
        ioService.outputLine("=== Student Testing System ===");
        ioService.outputLine("");
    }

    private void greetStudent(Student student) {
        ioService.outputLine("Hello, " + student.getFirstName() + " " + student.getLastName() + "!");
        ioService.outputLine("");
    }

    private List<Question> selectQuestions() {
        List<Question> allQuestions = questionDao.findAll();
        return allQuestions.size() > totalQuestions
                ? allQuestions.subList(0, totalQuestions)
                : allQuestions;
    }

    private void displayTestInstructions(int questionCount) {
        ioService.outputLine("Please answer the following " + questionCount + " questions:");
        ioService.outputLine("");
    }

    private int askQuestions(List<Question> questions) {
        int correctAnswers = 0;
        int questionNumber = 1;

        for (Question question : questions) {
            if (askQuestion(question, questionNumber)) {
                correctAnswers++;
            }
            ioService.outputLine("");
            questionNumber++;
        }

        return correctAnswers;
    }

    private boolean askQuestion(Question question, int questionNumber) {
        ioService.outputLine("Question " + questionNumber + ": " + question.getText());

        if (!question.hasOptions()) {
            return false;
        }

        List<Answer> options = question.getAnswerOptions();
        displayOptions(options);

        String userAnswer = ioService.readLineWithPrompt("Your answer (1-" + options.size() + "): ");
        return validateAnswer(userAnswer, options);
    }

    private void displayOptions(List<Answer> options) {
        for (int i = 0; i < options.size(); i++) {
            ioService.outputLine("  " + (i + 1) + ") " + options.get(i).getText());
        }
    }

    private boolean validateAnswer(String userAnswer, List<Answer> options) {
        int userChoice;
        try {
            userChoice = Integer.parseInt(userAnswer.trim());
        } catch (NumberFormatException exception) {
            return false;
        }

        if (userChoice < 1 || userChoice > options.size()) {
            return false;
        }

        return options.get(userChoice - 1).isCorrectAnswer();
    }

    private void displayResults(Student student, int correctAnswers, int totalQuestions) {
        ioService.outputLine("=== Test Results ===");
        ioService.outputLine("Student: " + student.getFirstName() + " " + student.getLastName());
        ioService.outputLine("Correct answers: " + correctAnswers + " out of " + totalQuestions);

        if (correctAnswers >= passingScore) {
            ioService.outputLine("Result: PASSED");
        } else {
            ioService.outputLine("Result: FAILED");
            ioService.outputLine("Passing score required: " + passingScore);
        }
    }

}
