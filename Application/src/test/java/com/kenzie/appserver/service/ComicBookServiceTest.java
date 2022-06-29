package com.kenzie.appserver.service;

import com.kenzie.appserver.repositories.ComicBookRepository;
import com.kenzie.appserver.repositories.model.ComicBookRecord;
import com.kenzie.appserver.service.model.ComicBook;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

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
        String createdBy = "Bob";
        String releaseYear = "1972";
        String title = "Werewolf by Night";
        String writer = "Roy Thomas";
        String illustrator = "Gerry Conway";
        String comicDescription = "Contains the first appearance of Moon Knight.";

        ComicBook book1 = new ComicBook(
                asin,
                createdBy,
                releaseYear,
                title,
                writer,
                illustrator,
                comicDescription
        );

        String asin2 = UUID.randomUUID().toString();
        String createdBy2 = "Alice";
        String releaseYear2 = "2022";
        String title2 = "Gang of Three";
        String writer2 = "Behzod Mamadiev";
        String illustrator2 = "Angel Prado";
        String comicDescription2 = "Contains the first appearance of Kenzie Group Four. Enjoy it!";

        ComicBook book2 = new ComicBook(
                asin2,
                createdBy2,
                releaseYear2,
                title2,
                writer2,
                illustrator2,
                comicDescription2
        );

        ComicBookRecord bookRecord1 = new ComicBookRecord();

        bookRecord1.setAsin(book1.getAsin());
        bookRecord1.setReleaseYear(book1.getReleaseYear());
        bookRecord1.setTitle(book1.getTitle());
        bookRecord1.setWriter(book1.getWriter());
        bookRecord1.setIllustrator(book1.getIllustrator());
        bookRecord1.setDescription(book1.getDescription());

        ComicBookRecord bookRecord2 = new ComicBookRecord();

        bookRecord2.setAsin(book2.getAsin());
        bookRecord2.setReleaseYear(book2.getReleaseYear());
        bookRecord2.setTitle(book2.getTitle());
        bookRecord2.setWriter(book2.getWriter());
        bookRecord2.setIllustrator(book2.getIllustrator());
        bookRecord2.setDescription(book2.getDescription());

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
            if (book.getAsin().equals(bookRecord1.getAsin())) {
                Assertions.assertEquals(book.getTitle(), bookRecord1.getTitle(), "The book title matches");
                Assertions.assertEquals(book.getIllustrator(), bookRecord1.getIllustrator(), "The book illustrator matches");
                Assertions.assertEquals(book.getDescription(), bookRecord1.getDescription(), "The book description matches");
                Assertions.assertEquals(book.getWriter(), bookRecord1.getWriter(), "The book writer matches");
                Assertions.assertEquals(book.getReleaseYear(), bookRecord1.getReleaseYear(), "The book release year matches");
            } else if (book.getAsin().equals(bookRecord2.getAsin())) {
                Assertions.assertEquals(book.getTitle(), bookRecord2.getTitle(), "The book title matches");
                Assertions.assertEquals(book.getIllustrator(), bookRecord2.getIllustrator(), "The book illustrator matches");
                Assertions.assertEquals(book.getDescription(), bookRecord2.getDescription(), "The book description matches");
                Assertions.assertEquals(book.getWriter(), bookRecord2.getWriter(), "The book writer matches");
                Assertions.assertEquals(book.getReleaseYear(), bookRecord2.getReleaseYear(), "The book release year matches");
            } else {
                Assertions.fail("The returned book is not in the records!");
            }
        }
    }

    /** ------------------------------------------------------------------------
     *  comicBookService.addNewBook
     *  ------------------------------------------------------------------------ **/

    @Test
    void addNewBook() {
        String asin = UUID.randomUUID().toString();
        String createdBy = "Bob";
        String releaseYear = "1999";
        String title = "Owl by Night";
        String writer = "Jack Dorsey";
        String illustrator = "Unknown Man";
        String comicDescription = "It was about a man who turns owl at night.";

        ComicBook book = new ComicBook(
                asin,
                createdBy,
                releaseYear,
                title,
                writer,
                illustrator,
                comicDescription
        );

        ComicBookRecord bookRecord = new ComicBookRecord();

        bookRecord.setAsin(book.getAsin());
        bookRecord.setReleaseYear(book.getReleaseYear());
        bookRecord.setTitle(book.getTitle());
        bookRecord.setWriter(book.getWriter());
        bookRecord.setIllustrator(book.getIllustrator());
        bookRecord.setDescription(book.getDescription());

        Mockito.when(comicBookService.addNewBook(book)).thenReturn(book);
        ComicBook addedBook = comicBookService.addNewBook(book);

        verify(comicBookRepository).save(bookRecord);

        Assertions.assertNotNull(addedBook, "The book is added.");
        Assertions.assertEquals(addedBook.getAsin(), book.getAsin(),
                "The asin matches");
        Assertions.assertEquals(addedBook.getReleaseYear(), book.getReleaseYear(),
                "The release year matches");
        Assertions.assertEquals(addedBook.getWriter(), book.getWriter(),
                "The writer matches.");
    }

    @Test
    void findBookByAsin() {
        String asin = randomUUID().toString();

        ComicBookRecord record = new ComicBookRecord();
        record.setAsin(asin);
        record.setReleaseYear("2000");
        record.setTitle("Sample Book");
        record.setWriter("Sample author");
        record.setIllustrator("Sample illustrator");
        record.setDescription("Sample description");

        when(comicBookRepository.findById(asin)).thenReturn(Optional.of(record));

        // WHEN
        ComicBook comicBook = comicBookService.findBookByAsin(asin);

        // THEN
        Assertions.assertNotNull(comicBook, "The book is returned");
        Assertions.assertEquals(record.getAsin(), comicBook.getAsin(), "The asin matches");
        Assertions.assertEquals(record.getTitle(), comicBook.getTitle(), "The book title matches");
        Assertions.assertEquals(record.getWriter(), comicBook.getWriter(), "The writer data matches");
        Assertions.assertEquals(record.getIllustrator(), comicBook.getIllustrator(), "The illustrator data matches");
        Assertions.assertEquals(record.getDescription(), comicBook.getDescription(), "The book description data matches");
    }

    @Test
    void deleteComicBook_success() {
        String asin = UUID.randomUUID().toString();
        String createdBy = "Bob";

        ComicBookRecord record = new ComicBookRecord();
        record.setAsin(asin);
        record.setCreatedBy(createdBy);
        record.setCreatedAt(ZonedDateTime.now());
        record.setReleaseYear("2022");
        record.setTitle("Pizza Man");
        record.setWriter("Bob");
        record.setIllustrator("Alice");
        record.setDescription("The tastiest super hero comic ever!");

        ArgumentCaptor<String> asinCapture = ArgumentCaptor.forClass(String.class);

        Optional<ComicBookRecord> recordOptional = Optional.of(record);
        Mockito.when(comicBookRepository.findByAsin(asin)).thenReturn(recordOptional);
        Mockito.doNothing().when(comicBookRepository).deleteByAsin(asinCapture.capture());
        comicBookService.deleteComicBook(asin, createdBy);

        Assertions.assertEquals(asin, asinCapture.getValue());
    }

    @Test
    void deleteComicBook_comicDoesNotExist_throwsException() {
        String asin = UUID.randomUUID().toString();
        String createdBy = "Bob";
        Optional<ComicBookRecord> recordOptional = Optional.empty();

        Mockito.when(comicBookRepository.findByAsin(asin)).thenReturn(recordOptional);

        Exception exception = Assertions.assertThrows(ResponseStatusException.class, () -> {
            comicBookService.deleteComicBook(asin, createdBy);
        });

        String expectedMessage = "Comic does not exist.";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void deleteComicBook_wrongName_throwsException() {
        String asin = UUID.randomUUID().toString();
        String createdBy = "Alice";

        ComicBookRecord record = new ComicBookRecord();
        record.setAsin(asin);
        record.setCreatedBy(createdBy);
        record.setCreatedAt(ZonedDateTime.now());
        record.setReleaseYear("2022");
        record.setTitle("Pizza Man");
        record.setWriter("Bob");
        record.setIllustrator("Alice");
        record.setDescription("The tastiest super hero comic ever!");

        Optional<ComicBookRecord> recordOptional = Optional.of(record);
        Mockito.when(comicBookRepository.findByAsin(asin)).thenReturn(recordOptional);

        Exception exception = Assertions.assertThrows(ResponseStatusException.class, () -> {
            comicBookService.deleteComicBook(asin, "Bob");
        });

        String expectedMessage = "Only the person who created the book may delete it.";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }
//
//    /** ------------------------------------------------------------------------
//     *  comicBookService.updateComicBook
//     *  ------------------------------------------------------------------------ **/
//    @Test
//    void updateComicBook_success() {
//        String asin = UUID.randomUUID().toString();
//        String createdBy = "Megan";
//
//        ComicBookRecord record = new ComicBookRecord();
//        record.setAsin(asin);
//        record.setCreatedBy(createdBy);
//        record.setCreatedAt(ZonedDateTime.now());
//        record.setReleaseYear("1993");
//        record.setTitle("Invincible");
//        record.setWriter("Saylem");
//        record.setIllustrator("Augustus");
//        record.setDescription("The one and only, Invincible.");
//
//        Optional<ComicBookRecord> recordOptional = Optional.of(record);
//
//        Mockito.when(comicBookRepository.findByAsin(asin)).thenReturn(recordOptional);
//
//
//    }
//    @Test
//    void updateComicBook_comicDoesNotExist_throwsException() {}
//    @Test
//    void updateComicBook_wrongName_throwsException() {}
//
}
