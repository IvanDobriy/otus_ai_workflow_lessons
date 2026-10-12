package ru.otus.surveys.service;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import ru.otus.surveys.model.Question;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class QuestionService {

    private final List<Question> questions;

    public QuestionService(ObjectMapper objectMapper) {
        this.questions = loadQuestions(objectMapper);
    }

    private List<Question> loadQuestions(ObjectMapper objectMapper) {
        try {
            ClassPathResource resource = new ClassPathResource("questions.json");
            try (InputStream is = resource.getInputStream()) {
                List<Question> loaded = objectMapper.readValue(
                        is,
                        new TypeReference<List<Question>>() {}
                );
                return new ArrayList<>(loaded);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load questions.json", e);
        }
    }

    public List<Question> getAllQuestions() {
        return Collections.unmodifiableList(questions);
    }
}
