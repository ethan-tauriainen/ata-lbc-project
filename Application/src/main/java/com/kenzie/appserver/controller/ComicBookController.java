package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.ComicBookCreateRequest;
import com.kenzie.appserver.controller.model.ComicBookResponse;
import com.kenzie.appserver.controller.model.ComicBookUpdateRequest;
import com.kenzie.appserver.service.ComicBookService;
import com.kenzie.appserver.service.model.ComicBook;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
                bookCreateRequest.getCreatedBy(),
                bookCreateRequest.getReleaseYear(),
                bookCreateRequest.getTitle(),
                bookCreateRequest.getWriter(),
                bookCreateRequest.getIllustrator(),
                bookCreateRequest.getDescription());
        comicBookService.addNewBook(book);

        ComicBookResponse bookResponse = comicBookToResponse(book);

        return ResponseEntity.created(URI.create("/books/" + bookResponse.getAsin())).body(bookResponse);
    }

    @DeleteMapping("/delete/{asin}/createdBy/{name}")
    public ResponseEntity<String> deleteComicBook(@PathVariable("asin") String asin, @PathVariable("name") String name) {
        try {
            comicBookService.deleteComicBook(asin, name);
            return ResponseEntity.noContent().build();
        } catch (ResponseStatusException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{asin}")
    public ResponseEntity<ComicBookResponse> findBookByAsin(@PathVariable("asin") String asin) {

        ComicBook book = comicBookService.findBookByAsin(asin);

        ComicBookResponse bookResponse = comicBookToResponse(book);
        return ResponseEntity.ok(bookResponse);
    }

    @PutMapping
    public ResponseEntity updateComicBook(@RequestBody ComicBookUpdateRequest comicBookUpdateRequest) {
        try {
            ComicBook comicBook = new ComicBook(
                    comicBookUpdateRequest.getAsin(),
                    comicBookUpdateRequest.getCreatedBy(),
                    comicBookUpdateRequest.getReleaseYear(),
                    comicBookUpdateRequest.getTitle(),
                    comicBookUpdateRequest.getWriter(),
                    comicBookUpdateRequest.getIllustrator(),
                    comicBookUpdateRequest.getDescription());
            comicBookService.updateComicBook(comicBook);
            ComicBookResponse concertResponse = comicBookToResponse(comicBook);
            return ResponseEntity.ok(concertResponse);
        } catch (ResponseStatusException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private ComicBookResponse comicBookToResponse(ComicBook comicBook) {
        ComicBookResponse comicBookResponse = new ComicBookResponse();
        comicBookResponse.setAsin(comicBook.getAsin());
        comicBookResponse.setCreatedBy(comicBook.getCreatedBy());
        comicBookResponse.setReleaseYear(comicBook.getReleaseYear());
        comicBookResponse.setTitle(comicBook.getTitle());
        comicBookResponse.setWriter(comicBook.getWriter());
        comicBookResponse.setDescription(comicBook.getDescription());
        comicBookResponse.setIllustrator(comicBook.getIllustrator());
        return comicBookResponse;
    }
}