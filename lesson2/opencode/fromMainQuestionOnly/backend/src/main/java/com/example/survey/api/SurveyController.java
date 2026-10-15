package com.example.survey.api;

import com.example.survey.model.AnswerSubmission;
import com.example.survey.model.Question;
import com.example.survey.model.SubmissionResult;
import com.example.survey.service.AnswerService;
import com.example.survey.service.QuestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class SurveyController implements SurveyApi {

    private final QuestionService questionService;
    private final AnswerService answerService;

    public SurveyController(QuestionService questionService, AnswerService answerService) {
        this.questionService = questionService;
        this.answerService = answerService;
    }

    @Override
    public Mono<ResponseEntity<Flux<Question>>> getQuestions(final ServerWebExchange exchange) {
        return Mono.just(ResponseEntity.ok(Flux.fromIterable(questionService.getQuestions())));
    }

    @Override
    public Mono<ResponseEntity<SubmissionResult>> submitAnswers(final Mono<AnswerSubmission> answerSubmission,
                                                                final ServerWebExchange exchange) {
        return answerSubmission
                .map(answerService::save)
                .map(ResponseEntity::ok);
    }
}
