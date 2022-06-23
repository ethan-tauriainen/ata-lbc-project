//package com.kenzie.appserver.service;
//
//import com.kenzie.appserver.repositories.ComicBookRepository;
//import com.kenzie.appserver.repositories.model.ExampleRecord;
//import com.kenzie.appserver.service.model.Example;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.Optional;
//
//import static java.util.UUID.randomUUID;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//public class ExampleServiceTest {
//    private ComicBookRepository comicBookRepository;
//    private ComicBookService comicBookService;
//
//    @BeforeEach
//    void setup() {
//        comicBookRepository = mock(ComicBookRepository.class);
//        comicBookService = new ComicBookService(comicBookRepository);
//    }
//    /** ------------------------------------------------------------------------
//     *  exampleService.findById
//     *  ------------------------------------------------------------------------ **/
//
//    @Test
//    void findById() {
//        // GIVEN
//        String id = randomUUID().toString();
//
//        ExampleRecord record = new ExampleRecord();
//        record.setId(id);
//        record.setName("concertname");
//
//        // WHEN
//        when(comicBookRepository.findById(id)).thenReturn(Optional.of(record));
//        Example example = comicBookService.findById(id);
//
//        // THEN
//        Assertions.assertNotNull(example, "The object is returned");
//        Assertions.assertEquals(record.getId(), example.getId(), "The id matches");
//        Assertions.assertEquals(record.getName(), example.getName(), "The name matches");
//    }
//
//    @Test
//    void findByConcertId_invalid() {
//        // GIVEN
//        String id = randomUUID().toString();
//
//        when(comicBookRepository.findById(id)).thenReturn(Optional.empty());
//
//        // WHEN
//        Example example = comicBookService.findById(id);
//
//        // THEN
//        Assertions.assertNull(example, "The example is null when not found");
//    }
//
//}
