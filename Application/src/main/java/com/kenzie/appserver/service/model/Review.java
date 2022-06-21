package com.kenzie.appserver.service.model;

public class Review {
    private final String reviewer;
    private final Double score;
    private final String description;

    public Review(String reviewer, Double score, String description) {
        this.reviewer = reviewer;
        this.score = score;
        this.description = description;
    }

    public String getReviewer() {
        return this.reviewer;
    }

    public Double getScore() {
        return this.score;
    }

    public String getDescription() {
        return this.description;
    }
}
