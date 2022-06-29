package com.kenzie.appserver.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.kenzie.appserver.IntegrationTest;
import com.kenzie.appserver.controller.model.ComicBookCreateRequest;
import com.kenzie.appserver.controller.model.ComicBookResponse;
import com.kenzie.appserver.repositories.ComicBookRepository;
import com.kenzie.appserver.repositories.model.ComicBookRecord;
import com.kenzie.appserver.service.ComicBookService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kenzie.appserver.service.model.ComicBook;
import net.andreinc.mockneat.MockNeat;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@IntegrationTest
class ComicBookControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    ComicBookService comicBookService;
    ComicBookRepository comicBookRepository;

    private final MockNeat mockNeat = MockNeat.threadLocal();

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void createNewBook_CreateSuccessful() throws Exception {
        // GIVEN
        String createdBy = "Bob";
        String releaseYear = "2021";
        String title = "Not for Every One";
        String writer = "Anonymous";
        String illustrator = "Jack Ma";
        String description = "A treasure that you rarely find.";


        ComicBookCreateRequest bookCreateRequest = new ComicBookCreateRequest();
        bookCreateRequest.setCreatedBy(createdBy);
        bookCreateRequest.setReleaseYear(releaseYear);
        bookCreateRequest.setTitle(title);
        bookCreateRequest.setWriter(writer);
        bookCreateRequest.setIllustrator(illustrator);
        bookCreateRequest.setDescription(description);

        mapper.registerModule(new JavaTimeModule());

        ComicBook book = comicBookService.addNewBook(new ComicBook(UUID.randomUUID().toString(), createdBy, releaseYear, title, writer, illustrator, description));

        // WHEN
        mvc.perform(post("/books")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bookCreateRequest)))
                // THEN
                .andExpect(jsonPath("releaseYear")
                        .value(is(releaseYear)))
                .andExpect(jsonPath("title")
                        .value(is(title)))
                .andExpect(jsonPath("writer")
                        .value(is(writer)))
                .andExpect(jsonPath("illustrator")
                        .value(is(illustrator)))
                .andExpect(jsonPath("description")
                        .value(is(description)));

        comicBookService.deleteComicBook(book.getAsin(), book.getCreatedBy());

    }

    @Test
    void getAllBooks_success() throws Exception {
        // GIVEN
        String createdBy = "Bob";
        String releaseYear = "2000";
        String title = "Magic City";
        String writer = "Behzod Mamadiev";
        String illustrator = "Ethan Tauriainen";
        String description = "An interesting book written and illustrated by a group of nerds";

        ComicBook book1 = comicBookService.addNewBook(new ComicBook(UUID.randomUUID().toString(), createdBy, releaseYear, title, writer, illustrator, description));

        String createdBy2 = "Alice";
        String releaseYear2 = "2010";
        String title2 = "Ghost City";
        String writer2 = "Angel Prado";
        String illustrator2 = "Ethan Tauriainen";
        String description2 = "The best comic book of all times!";

        ComicBook book2 = comicBookService.addNewBook(new ComicBook(UUID.randomUUID().toString(), createdBy2, releaseYear2, title2, writer2, illustrator2, description2));

        mapper.registerModule(new JavaTimeModule());
        comicBookService.findAll();

        mapper.registerModule(new JavaTimeModule());

        // WHEN
        mvc.perform(get("/books/all")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON))
                // THEN
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].releaseYear").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].writer").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].illustrator").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].releaseYear").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].title").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].writer").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].illustrator").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].description").isString());

        comicBookService.deleteComicBook(book1.getAsin(), book1.getCreatedBy());
        comicBookService.deleteComicBook(book2.getAsin(), book2.getCreatedBy());
    }


//    public void getById_Exists() throws Exception {
//        String id = UUID.randomUUID().toString();
//        String name = mockNeat.strings().valStr();
//
//        Example example = new Example(id, name);
//        Example persistedExample = exampleService.addNewExample(example);
//        mvc.perform(get("/example/{id}", persistedExample.getId())
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("id")
//                        .value(is(id)))
//                .andExpect(jsonPath("name")
//                        .value(is(name)))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    public void createExample_CreateSuccessful() throws Exception {
//        String name = mockNeat.strings().valStr();
//
//        ExampleCreateRequest exampleCreateRequest = new ExampleCreateRequest();
//        exampleCreateRequest.setName(name);
//
//        mapper.registerModule(new JavaTimeModule());
//
//        mvc.perform(post("/example")
//                        .accept(MediaType.APPLICATION_JSON)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(mapper.writeValueAsString(exampleCreateRequest)))
//                .andExpect(jsonPath("id")
//                        .exists())
//                .andExpect(jsonPath("name")
//                        .value(is(name)))
//                .andExpect(status().isCreated());
//    }

    @Test
    public void deleteComicBook_success() throws Exception {
        String createdBy = "Alice";

        ComicBookCreateRequest request = new ComicBookCreateRequest();
        request.setCreatedBy(createdBy);
        request.setReleaseYear("2022");
        request.setTitle("Awesome Sauce");
        request.setWriter("Bob");
        request.setIllustrator("Nancy");
        request.setDescription("Splendid sauce. Awesome story.");

        String response = mvc.perform(post("/books")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                        .andExpect(status().isCreated())
                        .andReturn().getResponse().getContentAsString();

        ComicBookResponse comicBookResponse = mapper.readValue(response, new TypeReference<ComicBookResponse>() {});
        String asin = comicBookResponse.getAsin();

        mvc.perform(delete("/books/delete/{asin}/createdBy/{name}", asin, createdBy))
                        .andExpect(status().isNoContent());
    }

    @Test
    public void deleteComicBook_wrongName_badRequest() throws Exception {
        String createdBy = "Alice";

        ComicBookCreateRequest request = new ComicBookCreateRequest();
        request.setCreatedBy(createdBy);
        request.setReleaseYear("2022");
        request.setTitle("Awesome Sauce");
        request.setWriter("Bob");
        request.setIllustrator("Nancy");
        request.setDescription("Splendid sauce. Awesome story.");

        String response = mvc.perform(post("/books")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                        .andExpect(status().isCreated())
                        .andReturn().getResponse().getContentAsString();

        ComicBookResponse comicBookResponse = mapper.readValue(response, new TypeReference<ComicBookResponse>() {});
        String asin = comicBookResponse.getAsin();

        mvc.perform(delete("/books/delete/{asin}/createdBy/{name}", asin, "Bob"))
                .andExpect(status().isBadRequest());

        // cleanup
        mvc.perform(delete("/books/delete/{asin}/createdBy/{name}", asin, createdBy))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteComicBook_nonExistentAsin_badRequest() throws Exception {
        mvc.perform(delete("/books/delete/{asin}/createdBy/{name}", UUID.randomUUID().toString(), "Bob"))
                .andExpect(status().isBadRequest());
    }
}