package com.kenzie.appserver.service;

import com.kenzie.appserver.repositories.ComicBookRepository;
import com.kenzie.appserver.repositories.model.ComicBookRecord;
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

        ComicBook book1 = new ComicBook(
                asin,
                releaseYear,
                title,
                writer,
                illustrator,
                comicDescription,
                reviews
        );

        String asin2 = UUID.randomUUID().toString();
        String reviewer2 = "John";
        Double score2 = 4.3D;
        String reviewDescription2 = "Excellent comic from start to finish! Love it.";

        Review review2 = new Review(
                reviewer2,
                score2,
                reviewDescription2
        );

        List<Review> reviews2 = new ArrayList<>(
                Collections.singleton(review2)
        );

        String releaseYear2 = "2022";
        String title2 = "Gang of Three";
        String writer2 = "Behzod Mamadiev";
        String illustrator2 = "Angel Prado";
        String comicDescription2 = "Contains the first appearance of Kenzie Group Four. Enjoy it!";

        ComicBook book2 = new ComicBook(
                asin2,
                releaseYear2,
                title2,
                writer2,
                illustrator2,
                comicDescription2,
                reviews2
        );

        ComicBookRecord bookRecord1 = new ComicBookRecord();

        bookRecord1.setAsin(book1.getAsin());
        bookRecord1.setReleaseYear(book1.getReleaseYear());
        bookRecord1.setTitle(book1.getTitle());
        bookRecord1.setWriter(book1.getWriter());
        bookRecord1.setIllustrator(book1.getIllustrator());
        bookRecord1.setDescription(book1.getDescription());
        bookRecord1.setReviews(book1.getReviews());

        ComicBookRecord bookRecord2 = new ComicBookRecord();

        bookRecord2.setAsin(book2.getAsin());
        bookRecord2.setReleaseYear(book2.getReleaseYear());
        bookRecord2.setTitle(book2.getTitle());
        bookRecord2.setWriter(book2.getWriter());
        bookRecord2.setIllustrator(book2.getIllustrator());
        bookRecord2.setDescription(book2.getDescription());
        bookRecord2.setReviews(book2.getReviews());

        List<ComicBookRecord> comicBookList = new ArrayList<>();

        comicBookList.add(bookRecord1);
        comicBookList.add(bookRecord2);

        Mockito.when(comicBookRepository.findAll()).thenReturn(comicBookList);
        List<ComicBook> comicBooks = comicBookService.findAll();

        Assertions.assertNotNull(comicBooks, "The list is returned.");
        Assertions.assertEquals(book1.getAsin(), comicBooks.get(0).getAsin(),
                "The asin matches");
        Assertions.assertEquals(book2.getAsin(), comicBooks.get(1).getAsin(),
                "The asin matches");
        Assertions.assertEquals(book1.getTitle(), comicBooks.get(0).getTitle(),
                "The titles match.");

        for (ComicBook book : comicBooks) {
            if (book.getAsin() == bookRecord1.getAsin()) {
                Assertions.assertEquals(book.getTitle(), bookRecord1.getTitle(), "The book title matches");
                Assertions.assertEquals(book.getIllustrator(), bookRecord1.getIllustrator(), "The book illustrator matches");
                Assertions.assertEquals(book.getDescription(), bookRecord1.getDescription(), "The book description matches");
                Assertions.assertEquals(book.getWriter(), bookRecord1.getWriter(), "The book writer matches");
                Assertions.assertEquals(book.getReleaseYear(), bookRecord1.getReleaseYear(), "The book release year matches");
            } else if (book.getAsin() == bookRecord2.getAsin()) {
                Assertions.assertEquals(book.getTitle(), bookRecord2.getTitle(), "The book title matches");
                Assertions.assertEquals(book.getIllustrator(), bookRecord2.getIllustrator(), "The book illustrator matches");
                Assertions.assertEquals(book.getDescription(), bookRecord2.getDescription(), "The book description matches");
                Assertions.assertEquals(book.getWriter(), bookRecord2.getWriter(), "The book writer matches");
                Assertions.assertEquals(book.getReleaseYear(), bookRecord2.getReleaseYear(), "The book release year matches");
            } else {
                Assertions.assertTrue(false, "The returned book is not in the records!");
            }
        }
    }
}
