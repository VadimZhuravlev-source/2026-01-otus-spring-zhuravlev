package ru.otus.services;

import ru.otus.domain.Question;
import ru.otus.dao.QuestionDao;

import java.util.List;

public class QuestionServiceImpl implements QuestionService {
    private final QuestionDao questionDao;

    public QuestionServiceImpl(QuestionDao questionDao) {
        this.questionDao = questionDao;
    }

    @Override
    public List<Question> getAllQuestions() {
        return questionDao.findAll();
    }
}
