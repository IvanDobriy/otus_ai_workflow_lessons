package com.example.survey.service;

import com.example.survey.model.Question;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

@Service
public class QuestionService {

    private static final Logger log = LoggerFactory.getLogger(QuestionService.class);

    private final List<Question> questions;

    public QuestionService(ObjectMapper objectMapper,
                           @Value("classpath:questions.json") Resource questionsResource) throws IOException {
        try (InputStream in = questionsResource.getInputStream()) {
            this.questions = objectMapper.readValue(in, new TypeReference<List<Question>>() {
            });
        }
        log.info("Загружено вопросов анкеты: {}", questions.size());
    }

    public List<Question> getQuestions() {
        return List.copyOf(questions);
    }
}
