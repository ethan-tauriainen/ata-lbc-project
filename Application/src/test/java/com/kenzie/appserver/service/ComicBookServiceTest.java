package com.kenzie.appserver.service;

import com.kenzie.appserver.service.model.ComicBook;
import com.kenzie.appserver.service.model.Review;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.mockito.Mockito.mock;

public class ComicBookServiceTest {
    private ComicBookRepository comicBookRepository;
    private ComicBookService comicBookService;

    @BeforeEach
    void setup() {
        comicBookRepository = mock(ComicBookRepository.class);
        comicBookService = new ComicBookService(comicBookRepository);
    }

    /** ------------------------------------------------------------------------
     *  comicBookService.findAll
     *  ------------------------------------------------------------------------ **/

    @Test
    void findAll() {
        String asin = UUID.randomUUID().toString();
        String reviewer = "Bob";
        Double score = 4.5D;
        String reviewDescription = "Excellent comic from start to finish!";

        Review review = new Review(
                reviewer,
                score,
                reviewDescription
        );

        List<Review> reviews = new ArrayList<>(
                Collections.singleton(review)
        );

        String releaseYear = "1972";
        String title = "Werewolf by Night";
        String writer = "Roy Thomas";
        String illustrator = "Gerry Conway";
        String comicDescription = "Contains the first appearance of Moon Knight.";

        ComicBook book = new ComicBook(
                asin,
                releaseYear,
                title,
                writer,
                illustrator,
                comicDescription,
                reviews
        );

        List<ComicBook> comicBookIterable = new ArrayList<>(
                Collections.singleton(book)
        );

        Mockito.when(comicBookRepository.findAll()).thenReturn(comicBookIterable);
        List<ComicBook> comicBooks = comicBookService.findAll();

        Assertions.assertNotNull(comicBooks, "The list is returned.");
        Assertions.assertEquals(book.getAsin(), comicBooks.get(0).getAsin(),
                "The asin matches");
        Assertions.assertEquals(book.getTitle(), comicBooks.get(0).getTitle(),
                "The titles match.");
    }
}
