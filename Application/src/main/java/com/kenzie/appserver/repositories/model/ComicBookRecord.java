package com.kenzie.appserver.repositories.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;

import java.time.Instant;
import java.util.Objects;

@DynamoDBTable(tableName = "TB_COMIC_BOOKS")
public class ComicBookRecord {
    private Instant createdAt;
    private Instant modifiedAt;
    private String createdBy;
    private String modifiedBy;
    private String asin;
    private String releaseYear;
    private String title;
    private String writer;
    private String illustrator;
    private String description;

    public ComicBookRecord() {
        this.createdAt = Instant.now();
    }

    @DynamoDBAttribute(attributeName = "MODIFIED")
    @DynamoDBTypeConverted( converter = InstantConverter.class )
    public Instant getModifiedAt() {
        return modifiedAt;
    }

    @DynamoDBAttribute(attributeName = "CREATED")
    @DynamoDBTypeConverted( converter = InstantConverter.class )
    public Instant getCreatedAt() {
        return createdAt;
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

    @DynamoDBAttribute(attributeName = "RELEASE_YEAR")
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

    static public class InstantConverter implements DynamoDBTypeConverter<String, Instant> {

        @Override
        public String convert( final Instant time ) {

            return time.toString();
        }

        @Override
        public Instant unconvert( final String stringValue ) {

            return Instant.parse(stringValue);
        }
    }
}
