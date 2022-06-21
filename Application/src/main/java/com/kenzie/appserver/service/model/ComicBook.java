package com.kenzie.appserver.service.model;

import java.util.List;

public class ComicBook {

    private final String asin;
    private final String releaseYear;
    private final String title;
    private final String writer;
    private final String illustrator;
    private final String description;
    private final List<Review> reviews;

    public ComicBook(String asin, String releaseYear, String title, String writer, String illustrator, String description, List<Review> reviews) {
        this.asin = asin;
        this.releaseYear = releaseYear;
        this.title = title;
        this.writer = writer;
        this.illustrator = illustrator;
        this.description = description;
        this.reviews = reviews;
    }

    public String getAsin() {
        return this.asin;
    }

    public String getReleaseYear() {
        return this.releaseYear;
    }

    public String getTitle() {
        return this.title;
    }

    public String getWriter() {
        return this.writer;
    }

    public String getIllustrator() {
        return this.illustrator;
    }

    public String getDescription() {
        return this.description;
    }

    public List<Review> getReviews() {
        return this.reviews;
    }
}
