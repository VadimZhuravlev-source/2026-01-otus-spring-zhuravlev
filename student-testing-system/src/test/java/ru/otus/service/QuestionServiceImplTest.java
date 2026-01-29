package ru.otus.service;

import ru.otus.dao.QuestionDao;
import ru.otus.domain.Question;
import ru.otus.services.QuestionService;
import ru.otus.services.QuestionServiceImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class QuestionServiceImplTest {

    private QuestionService questionService;
    private QuestionDao mockQuestionDao;

    @Before
    public void setUp() {
        mockQuestionDao = new QuestionDao() {
            @Override
            public List<Question> findAll() {
                return Arrays.asList(
                        new Question("Test question 1?", Arrays.asList("Answer 1", "Answer 2")),
                        new Question("Test question 2?", Arrays.asList("Answer A", "Answer B", "Answer C"))
                );
            }
        };

        questionService = new QuestionServiceImpl(mockQuestionDao);
    }

    @Test
    public void testGetAllQuestions() {
        List<Question> questions = questionService.getAllQuestions();

        assertNotNull("Questions list should not be null", questions);
        assertEquals("Should return 2 questions", 2, questions.size());
        assertEquals("First question text should match", "Test question 1?", questions.get(0).getText());
        assertEquals("First question should have 2 options", 2, questions.get(0).getAnswerOptions().size());
    }

    @Test
    public void testQuestionHasOptions() {
        List<Question> questions = questionService.getAllQuestions();

        assertTrue("Question should have options", questions.get(0).hasOptions());
    }
}
