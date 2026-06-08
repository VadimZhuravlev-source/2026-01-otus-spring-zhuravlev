package ru.otus.dao;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import ru.otus.domain.Answer;
import ru.otus.domain.Question;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
public class QuestionDaoImpl implements QuestionDao {
    private final ResourceLoader resourceLoader;
    private final String questionsBasePath;

    public QuestionDaoImpl(ResourceLoader resourceLoader,
                           @Value("${questions.csv.base-path}") String questionsBasePath) {
        this.resourceLoader = resourceLoader;
        this.questionsBasePath = questionsBasePath;
    }

    @Override
    public List<Question> findAll() {
        List<Question> questions = new ArrayList<>();
        var resource = getLocalizedQuestionsResource();

        try (CSVReader reader = new CSVReader(new InputStreamReader(resource.getInputStream()))) {
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

    private org.springframework.core.io.Resource getLocalizedQuestionsResource() {
        Locale locale = LocaleContextHolder.getLocale();
        String language = locale.getLanguage();
        String localizedPath = questionsBasePath + "_" + language + ".csv";
        
        var resource = resourceLoader.getResource(localizedPath);
        if (resource.exists()) {
            return resource;
        }
        
        // Fallback to English if localized file doesn't exist
        return resourceLoader.getResource(questionsBasePath + "_en.csv");
    }
}
