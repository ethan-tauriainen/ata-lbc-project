package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.ComicBookResponse;
import com.kenzie.appserver.service.ComicBookService;
import com.kenzie.appserver.service.model.ComicBook;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

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
