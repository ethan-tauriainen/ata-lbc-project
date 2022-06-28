package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.ComicBookCreateRequest;
import com.kenzie.appserver.controller.model.ComicBookResponse;
import com.kenzie.appserver.service.ComicBookService;
import com.kenzie.appserver.service.model.ComicBook;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.UUID.randomUUID;

@RestController
@RequestMapping("/books")
public class ComicBookController {

    private ComicBookService comicBookService;

    ComicBookController(ComicBookService comicBookService) { this.comicBookService = comicBookService; }

    @GetMapping("/all")
    public ResponseEntity<List<ComicBookResponse>> getAllComicBooks() {

        List<ComicBook> comicBookList = comicBookService.findAll();

        List<ComicBookResponse> responses = comicBookList.stream().map(comicBook -> comicBookToResponse(comicBook)).collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<ComicBookResponse> addNewBook(@RequestBody ComicBookCreateRequest bookCreateRequest) {
        ComicBook book = new ComicBook(randomUUID().toString(),
                bookCreateRequest.getReleaseYear(),
                bookCreateRequest.getTitle(),
                bookCreateRequest.getWriter(),
                bookCreateRequest.getIllustrator(),
                bookCreateRequest.getDescription());
        comicBookService.addNewBook(book);

        ComicBookResponse bookResponse = comicBookToResponse(book);

        return ResponseEntity.created(URI.create("/books/" + bookResponse.getAsin())).body(bookResponse);
    }

    @GetMapping("/{asin}")
    public ResponseEntity<ComicBookResponse> findBookByAsin(@PathVariable("asin") String asin) {
        ComicBook book = comicBookService.findBookByAsin(asin);
        if (book == null) {
            return ResponseEntity.notFound().build();
        }
        ComicBookResponse bookResponse = comicBookToResponse(book);
        return ResponseEntity.ok(bookResponse);
    }


    private ComicBookResponse comicBookToResponse(ComicBook comicBook) {
        ComicBookResponse comicBookResponse = new ComicBookResponse();
        comicBookResponse.setAsin(comicBook.getAsin());
        comicBookResponse.setReleaseYear(comicBook.getReleaseYear());
        comicBookResponse.setTitle(comicBook.getTitle());
        comicBookResponse.setWriter(comicBook.getWriter());
        comicBookResponse.setDescription(comicBook.getDescription());
        comicBookResponse.setIllustrator(comicBook.getIllustrator());
        return comicBookResponse;
    }
}
