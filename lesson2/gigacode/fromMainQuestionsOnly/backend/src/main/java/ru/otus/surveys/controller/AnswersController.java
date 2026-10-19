package ru.otus.surveys.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.otus.surveys.model.Answer;
import ru.otus.surveys.model.Question;
import ru.otus.surveys.service.QuestionService;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = "*")
public class AnswersController {

    private static final Logger log = LoggerFactory.getLogger(AnswersController.class);
    private final QuestionService questionService;
    private final Map<String, Answer> answersStore = new ConcurrentHashMap<>();

    public AnswersController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("/questions")
    public ResponseEntity<?> getQuestions() {
        try {
            return ResponseEntity.ok(questionService.getAllQuestions());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "error", "Failed to load questions: " + e.getMessage()
            ));
        }
    }

    @PostMapping("/answers")
    public ResponseEntity<?> postAnswers(@RequestBody Answer answer) {
        try {
            if (answer.getAnswers() == null || answer.getAnswers().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                        "error", "Answers cannot be empty"
                ));
            }

            String id = UUID.randomUUID().toString();
            answer.setId(id);
            answersStore.put(id, answer);

            log.info("Survey answer received: id={}, timestamp={}, answers={}",
                    id, LocalDateTime.now(), answer.getAnswers());

            return ResponseEntity.ok(Map.of(
                    "message", "Thank you!",
                    "id", id,
                    "timestamp", LocalDateTime.now().toString()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                    "error", "Failed to save answers: " + e.getMessage()
            ));
        }
    }
}
