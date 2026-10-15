package com.example.survey.service;

import com.example.survey.model.AnswerSubmission;
import com.example.survey.model.SubmissionResult;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AnswerService {

    private static final Logger log = LoggerFactory.getLogger(AnswerService.class);

    private final Map<Long, AnswerSubmission> storage = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong();

    public SubmissionResult save(AnswerSubmission submission) {
        long id = sequence.incrementAndGet();
        storage.put(id, submission);
        int received = submission.getAnswers() == null ? 0 : submission.getAnswers().size();
        log.info("Принята анкета #{} от респондента '{}'. Ответов: {}. Всего анкет в памяти: {}",
                id, submission.getRespondent(), received, storage.size());
        log.debug("Содержимое анкеты #{}: {}", id, submission.getAnswers());
        return new SubmissionResult(true, received, "Спасибо! Ваши ответы приняты.");
    }

    public int storedCount() {
        return storage.size();
    }

    public Map<Long, AnswerSubmission> findAll() {
        return Map.copyOf(storage);
    }
}
