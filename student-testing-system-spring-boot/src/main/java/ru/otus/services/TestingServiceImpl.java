package ru.otus.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import ru.otus.dao.QuestionDao;
import ru.otus.domain.Answer;
import ru.otus.domain.Question;
import ru.otus.domain.Student;

import java.util.List;
import java.util.Locale;

@Service
public class TestingServiceImpl implements TestingService {
    private final QuestionDao questionDao;
    private final StudentService studentService;
    private final IOService ioService;
    private final LocaleService localeService;
    private final int passingScore;
    private final int totalQuestions;
    @Value("${locale.default}")
    private String locale;

    public TestingServiceImpl(QuestionDao questionDao,
                              StudentService studentService,
                              IOService ioService,
                              LocaleService localeService,
                              @Value("${testing.passing.score}") int passingScore,
                              @Value("${testing.total.questions}") int totalQuestions) {
        this.questionDao = questionDao;
        this.studentService = studentService;
        this.ioService = ioService;
        this.localeService = localeService;
        this.passingScore = passingScore;
        this.totalQuestions = totalQuestions;
    }

    @Override
    public void conductTest() {
        changeLocale();
        displayWelcome();
        Student student = studentService.getStudentInfo();
        greetStudent(student);

        List<Question> questions = selectQuestions();
        displayTestInstructions(questions.size());

        int correctAnswers = askQuestions(questions);
        displayResults(student, correctAnswers, questions.size());
    }

    private void changeLocale() {
        var locale = this.locale;
        if (locale == null || locale.isEmpty()) {
            locale = "en_US";
        }

        LocaleContextHolder.setDefaultLocale(Locale.forLanguageTag(locale));
    }

    private void displayWelcome() {
        ioService.outputLine(localeService.getMessage("welcome.header"));
        ioService.outputLine("");
    }

    private void greetStudent(Student student) {
        ioService.outputLine(localeService.getMessage("greeting.hello", student.getFirstName(), student.getLastName()));
        ioService.outputLine("");
    }

    private List<Question> selectQuestions() {
        List<Question> allQuestions = questionDao.findAll();
        return allQuestions.size() > totalQuestions
                ? allQuestions.subList(0, totalQuestions)
                : allQuestions;
    }

    private void displayTestInstructions(int questionCount) {
        ioService.outputLine(localeService.getMessage("test.instructions", questionCount));
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
        ioService.outputLine(localeService.getMessage("test.question.number", questionNumber, question.getText()));

        if (!question.hasOptions()) {
            return false;
        }

        List<Answer> options = question.getAnswerOptions();
        displayOptions(options);

        String userAnswer = ioService.readLineWithPrompt(localeService.getMessage("test.answer.prompt", options.size()));
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
        ioService.outputLine(localeService.getMessage("results.header"));
        ioService.outputLine(localeService.getMessage("results.student", student.getFirstName(), student.getLastName()));
        ioService.outputLine(localeService.getMessage("results.score", correctAnswers, totalQuestions));

        if (correctAnswers >= passingScore) {
            ioService.outputLine(localeService.getMessage("results.passed"));
        } else {
            ioService.outputLine(localeService.getMessage("results.failed"));
            ioService.outputLine(localeService.getMessage("results.passing.score", passingScore));
        }
    }

}
