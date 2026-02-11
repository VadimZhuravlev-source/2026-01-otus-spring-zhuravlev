package ru.otus.integration.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.otus.services.QuestionService;
import ru.otus.domain.Question;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfig.class)
@TestPropertySource("classpath:application.properties")
public class ApplicationConfigTest {

    @Autowired
    private QuestionService questionService;

    @Test
    public void testContextLoads() {
        assertNotNull("QuestionService should be autowired", questionService);
    }

    @Test
    public void testQuestionsLoadedFromCSV() {
        List<Question> questions = questionService.getAllQuestions();

        assertNotNull("Questions should not be null", questions);
        assertFalse("Questions should not be empty", questions.isEmpty());
        assertEquals("Should have 3 test questions", 3, questions.size());

        Question firstQuestion = questions.get(0);
        assertTrue("Question should have options", firstQuestion.hasOptions());
    }
}
