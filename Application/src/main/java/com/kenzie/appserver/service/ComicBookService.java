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

}
