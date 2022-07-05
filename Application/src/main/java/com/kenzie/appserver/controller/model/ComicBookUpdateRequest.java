package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;
import java.time.ZonedDateTime;

public class ComicBookUpdateRequest {

    @NotEmpty
    @JsonProperty("asin")
    private String asin;

    @JsonProperty("createdBy")
    private String createdBy;

    @NotEmpty
    @JsonProperty("modifiedAt")
    private ZonedDateTime modifiedAt;

    @JsonProperty("releaseYear")
    private String releaseYear;

    @NotEmpty
    @JsonProperty("title")
    private String title;

    @JsonProperty("writer")
    private String writer;

    @JsonProperty("illustrator")
    private String illustrator;

    @JsonProperty("description")
    private String description;

    public String getCreatedBy() { return this.createdBy; }

    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public String getAsin() { return this.asin; }

    public void setAsin(String asin) { this.asin = asin; }

    public ZonedDateTime getModifiedAt() { return this.modifiedAt; }

    public void setModifiedAt(ZonedDateTime modifiedAt) { this.modifiedAt = modifiedAt; }

    public String getReleaseYear() { return this.releaseYear; }

    public void setReleaseYear(String releaseYear) { this.releaseYear = releaseYear; }

    public String getTitle() { return this.title; }

    public void setTitle(String title) { this.title = title; }

    public String getWriter() { return this.writer; }

    public void setWriter(String writer) { this.writer = writer; }

    public String getIllustrator() { return this.illustrator; }

    public void setIllustrator(String illustrator) { this.illustrator = illustrator; }

    public String getDescription() { return this.description; }

    public void setDescription(String description) { this.description = description; }
}
