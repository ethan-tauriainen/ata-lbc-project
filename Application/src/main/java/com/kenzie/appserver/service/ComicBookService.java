package com.kenzie.appserver.service;

import com.kenzie.appserver.repositories.ComicBookRepository;

import com.kenzie.appserver.repositories.model.ComicBookRecord;
import com.kenzie.appserver.service.model.ComicBook;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        bookRecord.setReleaseYear(book.getReleaseYear());
        bookRecord.setTitle(book.getTitle());
        bookRecord.setWriter(book.getWriter());
        bookRecord.setIllustrator(book.getIllustrator());
        bookRecord.setDescription(book.getDescription());
        comicBookRepository.save(bookRecord);

        return book;
    }

    public void deleteComicBook(String asin) {
        comicBookRepository.deleteByAsin(asin);
    }

}
