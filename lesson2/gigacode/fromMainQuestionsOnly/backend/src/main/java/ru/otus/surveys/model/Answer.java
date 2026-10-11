package ru.otus.surveys.model;

import java.util.Map;

public class Answer {

    private String id;
    private Map<String, String> answers;

    public Answer() {
    }

    public Answer(String id, Map<String, String> answers) {
        this.id = id;
        this.answers = answers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, String> getAnswers() {
        return answers;
    }

    public void setAnswers(Map<String, String> answers) {
        this.answers = answers;
    }
}
