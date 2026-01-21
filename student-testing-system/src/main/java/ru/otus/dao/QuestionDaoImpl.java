package ru.otus.dao;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import ru.otus.domain.Question;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class QuestionDaoImpl implements QuestionDao {
    private final Resource questionsResource;

    public QuestionDaoImpl(Resource questionsResource) {
        this.questionsResource = questionsResource;
    }

    @Override
    public List<Question> findAll() {
        List<Question> questions = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new InputStreamReader(questionsResource.getInputStream()))) {
            List<String[]> records = reader.readAll();

            for (String[] record : records) {
                if (record.length >= 2) {
                    String questionText = record[0];
                    List<String> answers = new ArrayList<>();

                    for (int i = 1; i < record.length; i++) {
                        if (record[i] != null && !record[i].trim().isEmpty()) {
                            answers.add(record[i].trim());
                        }
                    }

                    questions.add(new Question(questionText, answers));
                }
            }
        } catch (IOException | CsvException e) {
            throw new RuntimeException("Failed to read questions from CSV", e);
        }

        return questions;
    }
}
