package ru.otus.dao;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.springframework.stereotype.Component;
import ru.otus.domain.Answer;
import ru.otus.domain.Question;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Component
public class QuestionDaoImpl implements QuestionDao {
    private final Resource questionsResource;

    public QuestionDaoImpl(@Value("${questions.csv.path}") Resource questionsResource) {
        this.questionsResource = questionsResource;
    }

    @Override
    public List<Question> findAll() {
        List<Question> questions = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new InputStreamReader(questionsResource.getInputStream()))) {
            List<String[]> records = reader.readAll();

            for (String[] record : records) {
                if (record.length >= 3) {
                    String questionText = record[0];
                    List<Answer> answers = new ArrayList<>();
                    int correctAnswerIndex = 0;

                    // Parse correct answer index (last column)
                    try {
                        correctAnswerIndex = Integer.parseInt(record[record.length - 1].trim());
                    } catch (NumberFormatException e) {
                        throw new RuntimeException("Invalid correct answer index in CSV", e);
                    }

                    // Parse answer options (all columns except first and last)
                    for (int i = 1; i < record.length - 1; i++) {
                        if (record[i] != null && !record[i].trim().isEmpty()) {
                            var isCorrectAnswer = i == correctAnswerIndex;
                            var answer = new Answer(record[i].trim(), isCorrectAnswer);
                            answers.add(answer);
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
