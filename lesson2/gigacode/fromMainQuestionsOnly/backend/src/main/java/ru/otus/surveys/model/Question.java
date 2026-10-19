package ru.otus.surveys.model;

import java.util.List;

public class Question {

    private String id;
    private String type;
    private String label;
    private List<String> options;
    private boolean required;

    public Question() {
    }

    public Question(String id, String type, String label, List<String> options, boolean required) {
        this.id = id;
        this.type = type;
        this.label = label;
        this.options = options;
        this.required = required;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }
}
