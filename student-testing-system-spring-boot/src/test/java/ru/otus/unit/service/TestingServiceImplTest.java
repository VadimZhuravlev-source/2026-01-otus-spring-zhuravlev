package ru.otus.unit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.dao.QuestionDao;
import ru.otus.domain.Answer;
import ru.otus.domain.Question;
import ru.otus.domain.Student;
import ru.otus.services.IOService;
import ru.otus.services.LocaleService;
import ru.otus.services.StudentService;
import ru.otus.services.TestingServiceImpl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TestingServiceImplTest {

    @Mock
    private QuestionDao questionDao;

    @Mock
    private StudentService studentService;

    @Mock
    private IOService ioService;

    @Mock
    private LocaleService localeService;

    private TestingServiceImpl testingService;

    private List<Question> testQuestions;
    private Student testStudent;

    @BeforeEach
    void setUp() {
        testStudent = new Student("John", "Doe");

        testQuestions = Arrays.asList(
                new Question("What is 2+2?", Arrays.asList(
                        new Answer("3", false),
                        new Answer("4", true),
                        new Answer("5", false)
                )),
                new Question("What is the capital of France?", Arrays.asList(
                        new Answer("London", false),
                        new Answer("Paris", true),
                        new Answer("Berlin", false)
                ))
        );

        // Mock LocaleService to return English messages (using lenient to avoid UnnecessaryStubbingException)
        lenient().when(localeService.getMessage(eq("welcome.header"))).thenReturn("=== Student Testing System ===");
        lenient().when(localeService.getMessage(eq("greeting.hello"), anyString(), anyString())).thenAnswer(invocation ->
                "Hello, " + invocation.getArgument(1) + " " + invocation.getArgument(2) + "!");
        lenient().when(localeService.getMessage(eq("test.instructions"), anyInt())).thenAnswer(invocation ->
                "Please answer the following " + invocation.getArgument(1) + " questions:");
        lenient().when(localeService.getMessage(eq("test.question.number"), anyInt(), anyString())).thenAnswer(invocation ->
                "Question " + invocation.getArgument(1) + ": " + invocation.getArgument(2));
        lenient().when(localeService.getMessage(eq("test.answer.prompt"), anyInt())).thenAnswer(invocation ->
                "Your answer (1-" + invocation.getArgument(1) + "): ");
        lenient().when(localeService.getMessage(eq("results.header"))).thenReturn("=== Test Results ===");
        lenient().when(localeService.getMessage(eq("results.student"), anyString(), anyString())).thenAnswer(invocation ->
                "Student: " + invocation.getArgument(1) + " " + invocation.getArgument(2));
        lenient().when(localeService.getMessage(eq("results.score"), anyInt(), anyInt())).thenAnswer(invocation ->
                "Correct answers: " + invocation.getArgument(1) + " out of " + invocation.getArgument(2));
        lenient().when(localeService.getMessage(eq("results.passed"))).thenReturn("Result: PASSED");
        lenient().when(localeService.getMessage(eq("results.failed"))).thenReturn("Result: FAILED");
        lenient().when(localeService.getMessage(eq("results.passing.score"), anyInt())).thenAnswer(invocation ->
                "Passing score required: " + invocation.getArgument(1));

        // passingScore = 1, totalQuestions = 5
        testingService = new TestingServiceImpl(questionDao, studentService, ioService, localeService, 1, 5);
    }

    @Test
    void conductTest_shouldDisplayWelcomeMessage() {
        when(studentService.getStudentInfo()).thenReturn(testStudent);
        when(questionDao.findAll()).thenReturn(testQuestions);
        when(ioService.readLineWithPrompt(anyString())).thenReturn("1");

        testingService.conductTest();

        verify(ioService).outputLine("=== Student Testing System ===");
    }

    @Test
    void conductTest_shouldGreetStudent() {
        when(studentService.getStudentInfo()).thenReturn(testStudent);
        when(questionDao.findAll()).thenReturn(testQuestions);
        when(ioService.readLineWithPrompt(anyString())).thenReturn("1");

        testingService.conductTest();

        verify(ioService).outputLine("Hello, John Doe!");
    }

    @Test
    void conductTest_shouldAskAllQuestions() {
        when(studentService.getStudentInfo()).thenReturn(testStudent);
        when(questionDao.findAll()).thenReturn(testQuestions);
        when(ioService.readLineWithPrompt(anyString())).thenReturn("1");

        testingService.conductTest();

        verify(ioService).outputLine("Question 1: What is 2+2?");
        verify(ioService).outputLine("Question 2: What is the capital of France?");
    }

    @Test
    void conductTest_shouldDisplayPassedWhenScoreIsSufficient() {
        when(studentService.getStudentInfo()).thenReturn(testStudent);
        when(questionDao.findAll()).thenReturn(testQuestions);
        when(ioService.readLineWithPrompt(anyString())).thenReturn("2"); // correct answer

        testingService.conductTest();

        verify(ioService).outputLine("Result: PASSED");
        verify(ioService, never()).outputLine(contains("FAILED"));
    }

    @Test
    void conductTest_shouldDisplayFailedWhenScoreIsInsufficient() {
        when(studentService.getStudentInfo()).thenReturn(testStudent);
        when(questionDao.findAll()).thenReturn(testQuestions);
        when(ioService.readLineWithPrompt(anyString())).thenReturn("1"); // wrong answer

        testingService.conductTest();

        verify(ioService).outputLine("Result: FAILED");
        verify(ioService).outputLine("Passing score required: 1");
    }

    @Test
    void conductTest_shouldLimitQuestionsToConfiguredTotal() {
        List<Question> manyQuestions = Arrays.asList(
                new Question("Q1", Arrays.asList(new Answer("A", true))),
                new Question("Q2", Arrays.asList(new Answer("A", true))),
                new Question("Q3", Arrays.asList(new Answer("A", true))),
                new Question("Q4", Arrays.asList(new Answer("A", true))),
                new Question("Q5", Arrays.asList(new Answer("A", true))),
                new Question("Q6", Arrays.asList(new Answer("A", true))),
                new Question("Q7", Arrays.asList(new Answer("A", true)))
        );

        when(studentService.getStudentInfo()).thenReturn(testStudent);
        when(questionDao.findAll()).thenReturn(manyQuestions);
        when(ioService.readLineWithPrompt(anyString())).thenReturn("1");

        testingService.conductTest();

        verify(ioService).outputLine("Please answer the following 5 questions:");
        verify(ioService, never()).outputLine("Question 6: Q6");
    }

    @Test
    void conductTest_shouldHandleInvalidNumericInput() {
        when(studentService.getStudentInfo()).thenReturn(testStudent);
        when(questionDao.findAll()).thenReturn(testQuestions);
        when(ioService.readLineWithPrompt(anyString())).thenReturn("invalid");

        testingService.conductTest();

        verify(ioService).outputLine("Correct answers: 0 out of 2");
    }

    @Test
    void conductTest_shouldHandleOutOfBoundsAnswer() {
        when(studentService.getStudentInfo()).thenReturn(testStudent);
        when(questionDao.findAll()).thenReturn(testQuestions);
        when(ioService.readLineWithPrompt(anyString())).thenReturn("99");

        testingService.conductTest();

        verify(ioService).outputLine("Correct answers: 0 out of 2");
    }

    @Test
    void conductTest_shouldHandleQuestionsWithoutOptions() {
        List<Question> questionsWithoutOptions = Collections.singletonList(
                new Question("Question without options", Collections.emptyList())
        );

        when(studentService.getStudentInfo()).thenReturn(testStudent);
        when(questionDao.findAll()).thenReturn(questionsWithoutOptions);

        testingService.conductTest();

        verify(ioService).outputLine("Correct answers: 0 out of 1");
    }

    @Test
    void conductTest_shouldCountCorrectAnswers() {
        when(studentService.getStudentInfo()).thenReturn(testStudent);
        when(questionDao.findAll()).thenReturn(testQuestions);
        when(ioService.readLineWithPrompt(anyString()))
                .thenReturn("2")  // correct for first question
                .thenReturn("2"); // correct for second question

        testingService.conductTest();

        verify(ioService).outputLine("Correct answers: 2 out of 2");
        verify(ioService).outputLine("Result: PASSED");
    }

    @Test
    void conductTest_shouldDisplayAllAnswerOptions() {
        when(studentService.getStudentInfo()).thenReturn(testStudent);
        when(questionDao.findAll()).thenReturn(Collections.singletonList(testQuestions.get(0)));
        when(ioService.readLineWithPrompt(anyString())).thenReturn("1");

        testingService.conductTest();

        verify(ioService).outputLine("  1) 3");
        verify(ioService).outputLine("  2) 4");
        verify(ioService).outputLine("  3) 5");
    }
}