package com.kenzie.appserver.repositories.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.kenzie.appserver.controller.model.ReviewRecord;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@DynamoDBTable(tableName = "TB_COMIC_BOOKS")
public class ComicBookRecord {
    protected Instant createdAt;
    protected Instant modifiedAt;
    protected String createdBy;
    protected String modifiedBy;
    private String asin;
    private String releaseYear;
    private String title;
    private String writer;
    private String illustrator;
    private String description;
    private List<ReviewRecord> reviews;

    public ComicBookRecord() {
        this.createdAt = Instant.now();
    }

    @DynamoDBAttribute(attributeName = "MODIFIED")
    public Instant getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Instant modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    @DynamoDBAttribute(attributeName = "CREATED_BY")
    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @DynamoDBAttribute(attributeName = "MODIFIED_BY")
    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    @DynamoDBHashKey(attributeName = "ASIN")
    public String getAsin() {
        return asin;
    }

    public void setAsin(String asin) {
        this.asin = asin;
    }

    @DynamoDBRangeKey(attributeName = "RELEASE_YEAR")
    public String getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
    }

    @DynamoDBAttribute(attributeName = "TITLE")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @DynamoDBAttribute(attributeName = "WRITER")
    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    @DynamoDBAttribute(attributeName = "ILLUSTRATOR")
    public String getIllustrator() {
        return illustrator;
    }

    public void setIllustrator(String illustrator) {
        this.illustrator = illustrator;
    }

    @DynamoDBAttribute(attributeName = "DESCRIPTION")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @DynamoDBAttribute(attributeName = "REVIEWS")
    public List<ReviewRecord> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewRecord> reviews) {
        this.reviews = reviews;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComicBookRecord that = (ComicBookRecord) o;
        return Objects.equals(asin, that.asin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(asin);
    }
}
