package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;

public class ComicBookCreateRequest {
    @NotEmpty
    @JsonProperty("releaseYear")
    private String releaseYear;

    @NotEmpty
    @JsonProperty("title")
    private String title;

    @NotEmpty
    @JsonProperty("writer")
    private String writer;

    @NotEmpty
    @JsonProperty("illustrator")
    private String illustrator;

    @NotEmpty
    @JsonProperty("description")
    private String description;

    public String getReleaseYear() {
        return releaseYear;
    }

    public String getTitle() {
        return title;
    }

    public String getWriter() {
        return writer;
    }

    public String getIllustrator() {
        return illustrator;
    }

    public String getDescription() {
        return description;
    }
}
