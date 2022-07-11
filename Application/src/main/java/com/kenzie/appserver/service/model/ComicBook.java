package com.kenzie.appserver.service.model;

public class ComicBook {

    private final String asin;
    private final String createdBy;
    private final String releaseYear;
    private final String title;
    private final String writer;
    private final String illustrator;
    private final String description;

    public ComicBook(String asin, String createdBy, String releaseYear, String title, String writer, String illustrator, String description) {
        this.asin = asin;
        this.createdBy = createdBy;
        this.releaseYear = releaseYear;
        this.title = title;
        this.writer = writer;
        this.illustrator = illustrator;
        this.description = description;
    }

    public String getAsin() {
        return this.asin;
    }

    public String getCreatedBy() {
        return this.createdBy;
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

}
