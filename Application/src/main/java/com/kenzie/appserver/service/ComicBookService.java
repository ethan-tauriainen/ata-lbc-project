package com.kenzie.appserver.service;

import com.kenzie.appserver.repositories.ComicBookRepository;

import com.kenzie.appserver.repositories.model.ComicBookRecord;
import com.kenzie.appserver.service.model.ComicBook;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ComicBookService {
    private final ComicBookRepository comicBookRepository;

    public ComicBookService(ComicBookRepository comicBookRepository) {
        this.comicBookRepository = comicBookRepository;
    }

    public List<ComicBook> findAll() {
        List<ComicBook> comicBooks = new ArrayList<>();

        Iterable<ComicBookRecord> bookRecords = comicBookRepository.findAll();

        for (ComicBookRecord record : bookRecords) {
            comicBooks.add(new ComicBook(record.getAsin(),
                    record.getCreatedBy(),
                    record.getReleaseYear(),
                    record.getTitle(),
                    record.getWriter(),
                    record.getIllustrator(),
                    record.getDescription())
            );
        }
        return comicBooks;
    }

    public ComicBook addNewBook(ComicBook book) {
        ComicBookRecord bookRecord = new ComicBookRecord();
        bookRecord.setAsin(book.getAsin());
        bookRecord.setCreatedBy(book.getCreatedBy());
        bookRecord.setCreatedAt(ZonedDateTime.now());
        bookRecord.setReleaseYear(book.getReleaseYear());
        bookRecord.setTitle(book.getTitle());
        bookRecord.setWriter(book.getWriter());
        bookRecord.setIllustrator(book.getIllustrator());
        bookRecord.setDescription(book.getDescription());
        comicBookRepository.save(bookRecord);

        return book;
    }

    public void deleteComicBook(String asin, String name) {
        Optional<ComicBookRecord> recordOptional = comicBookRepository.findByAsin(asin);

        if (!recordOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Comic does not exist.");
        }

        ComicBookRecord record = recordOptional.get();

        if (!record.getCreatedBy().equals(name)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only the person who created the book may delete it.");
        }

        comicBookRepository.deleteByAsin(asin);
    }

    public ComicBook findBookByAsin(String asin) {
        ComicBook comicBook = comicBookRepository
                .findById(asin)
                .map(book -> new ComicBook (book.getAsin(),
                        book.getCreatedBy(),
                        book.getReleaseYear(),
                        book.getTitle(),
                        book.getWriter(),
                        book.getIllustrator(),
                        book.getDescription()))
                .orElse(null);

        return comicBook;
    }

    public void updateComicBook(ComicBook comicBook) {
        Optional<ComicBookRecord> optionalComicBookRecord = comicBookRepository.findByAsin(comicBook.getAsin());

        if (!optionalComicBookRecord.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Comic Book does not exist.");
        }

        ComicBookRecord record = optionalComicBookRecord.get();

        if (!record.getCreatedBy().equals(comicBook.getCreatedBy())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only the person who created the book may update it.");
        }

        ComicBookRecord updatedRecord = new ComicBookRecord();
        updatedRecord.setAsin(comicBook.getAsin());
        updatedRecord.setCreatedBy(comicBook.getCreatedBy());
        updatedRecord.setDescription(comicBook.getDescription());
        updatedRecord.setIllustrator((comicBook.getIllustrator()));
        updatedRecord.setTitle(comicBook.getTitle());
        updatedRecord.setWriter(comicBook.getWriter());
        updatedRecord.setReleaseYear(comicBook.getReleaseYear());
        updatedRecord.setModifiedAt(ZonedDateTime.now());
        updatedRecord.setModifiedBy(comicBook.getCreatedBy());

        comicBookRepository.save(updatedRecord);
    }

}
