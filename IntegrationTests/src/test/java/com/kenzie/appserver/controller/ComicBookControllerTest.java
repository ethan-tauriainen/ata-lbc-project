package com.kenzie.appserver.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.kenzie.appserver.IntegrationTest;
import com.kenzie.appserver.controller.model.ComicBookCreateRequest;
import com.kenzie.appserver.controller.model.ComicBookResponse;
import com.kenzie.appserver.controller.model.ComicBookUpdateRequest;
import com.kenzie.appserver.service.ComicBookService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.kenzie.appserver.service.model.ComicBook;
import net.andreinc.mockneat.MockNeat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.ZonedDateTime;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@IntegrationTest
class ComicBookControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    ComicBookService comicBookService;

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

        // WHEN
        String response = mvc.perform(post("/books")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bookCreateRequest)))
                //THEN
                        .andExpect(status().isCreated())
                        .andReturn().getResponse().getContentAsString();

        ComicBookResponse comicBookResponse = mapper.readValue(response, new TypeReference<ComicBookResponse>() {});

        mvc.perform(delete("/books/delete/{asin}/createdBy/{name}", comicBookResponse.getAsin(), comicBookResponse.getCreatedBy()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void createNewBook_empty_badRequest() throws Exception {
        // GIVEN

        // WHEN
        mvc.perform(post("/books")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString("")))
                //THEN
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllBooks_success() throws Exception {
        // GIVEN
        String createdBy = "Bob";
        String releaseYear = "2000";
        String title = "Magic City";
        String writer = "Behzod Mamadiev";
        String illustrator = "Ethan Tauriainen";
        String description = "An interesting book written and illustrated by a group of nerds.";

        ComicBook book1 = comicBookService.addNewBook(new ComicBook(UUID.randomUUID().toString(), createdBy, releaseYear, title, writer, illustrator, description));

        String createdBy2 = "Alice";
        String releaseYear2 = "2010";
        String title2 = "Ghost City";
        String writer2 = "Angel Prado";
        String illustrator2 = "Ethan Tauriainen";
        String description2 = "The best comic book of all times!";

        ComicBook book2 = comicBookService.addNewBook(new ComicBook(UUID.randomUUID().toString(), createdBy2, releaseYear2, title2, writer2, illustrator2, description2));

        mapper.registerModule(new JavaTimeModule());

        // WHEN
        mvc.perform(get("/books/all")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON))
                // THEN
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].createdBy").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].releaseYear").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].writer").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].illustrator").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].createdBy").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].releaseYear").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].title").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].writer").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].illustrator").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].description").isString());

        mvc.perform(delete("/books/delete/{asin}/createdBy/{name}", book1.getAsin(), book1.getCreatedBy()))
                .andExpect(status().isNoContent());
        mvc.perform(delete("/books/delete/{asin}/createdBy/{name}", book2.getAsin(), book2.getCreatedBy()))
                .andExpect(status().isNoContent());
    }

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

    @Test
    public void findBookByAsin_success() throws Exception {
        ComicBookCreateRequest createRequest = new ComicBookCreateRequest();
        createRequest.setCreatedBy("Behzod");
        createRequest.setReleaseYear("1886");
        createRequest.setTitle("Das Capital");
        createRequest.setWriter("Karl Marx");
        createRequest.setIllustrator("Lenin");
        createRequest.setDescription("What a job! Serious book turned into a comic???");

        mapper.registerModule(new JavaTimeModule());

        String createResponse = mvc.perform(post("/books")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        ComicBookResponse createBookResponse = mapper.readValue(createResponse, new TypeReference<ComicBookResponse>() {} );
        String asin = createBookResponse.getAsin();

        mvc.perform(get("/books/{asin}", asin)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk());
        //clean up
        mvc.perform(delete("/books/delete/{asin}/createdBy/{name}", asin, createBookResponse.getCreatedBy()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void updateComicBook_success() throws Exception {
        ComicBookCreateRequest createRequest = new ComicBookCreateRequest();
        createRequest.setCreatedBy("Megan");
        createRequest.setTitle("Invincible");
        createRequest.setWriter("Augustus");
        createRequest.setIllustrator("Saylem");
        createRequest.setReleaseYear("1993");
        createRequest.setDescription("The one and only, Invincible! The greatest superhero on Earth.");

        mapper.registerModule(new JavaTimeModule());

        String createResponse = mvc.perform(post("/books")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        ComicBookResponse createBookResponse = mapper.readValue(createResponse, new TypeReference<ComicBookResponse>() {} );
        String asin = createBookResponse.getAsin();

        ComicBookUpdateRequest updateRequest = new ComicBookUpdateRequest();
        updateRequest.setAsin(asin);
        updateRequest.setCreatedBy("Megan");
        updateRequest.setTitle("Invincible");
        updateRequest.setIllustrator("Ethan");
        updateRequest.setWriter("Behzod");
        updateRequest.setReleaseYear("2021");
        updateRequest.setDescription("The one and only!");
        updateRequest.setModifiedAt(ZonedDateTime.now());

        mvc.perform(put("/books")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk());
        //clean up
        mvc.perform(delete("/books/delete/{asin}/createdBy/{name}", asin, updateRequest.getCreatedBy()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void updateComicBook_wrongName_badRequest() throws Exception {
        ComicBookCreateRequest createRequest = new ComicBookCreateRequest();
        createRequest.setCreatedBy("Megan");
        createRequest.setTitle("Invincible");
        createRequest.setWriter("Augustus");
        createRequest.setIllustrator("Saylem");
        createRequest.setReleaseYear("1993");
        createRequest.setDescription("The one and only, Invincible! The greatest superhero on Earth.");

        mapper.registerModule(new JavaTimeModule());

        String createResponse = mvc.perform(post("/books")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        ComicBookResponse createBookResponse = mapper.readValue(createResponse, new TypeReference<ComicBookResponse>() {} );
        String asin = createBookResponse.getAsin();

        ComicBookUpdateRequest updateRequest = new ComicBookUpdateRequest();
        updateRequest.setAsin(asin);
        updateRequest.setCreatedBy("WRONG-NAME");
        updateRequest.setTitle("Invincible");
        updateRequest.setIllustrator("Ethan");
        updateRequest.setWriter("Behzod");
        updateRequest.setReleaseYear("2021");
        updateRequest.setDescription("The one and only!");
        updateRequest.setModifiedAt(ZonedDateTime.now());

        mvc.perform(put("/books")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updateRequest)))
                .andExpect(status().isBadRequest());
        // clean up
        mvc.perform(delete("/books/delete/{asin}/createdBy/{name}", asin, createRequest.getCreatedBy()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void updateComicBook_nonExistentAsin_badRequest() throws Exception {
        ComicBookCreateRequest createRequest = new ComicBookCreateRequest();
        createRequest.setCreatedBy("Megan");
        createRequest.setTitle("Invincible");
        createRequest.setWriter("Augustus");
        createRequest.setIllustrator("Saylem");
        createRequest.setReleaseYear("1993");
        createRequest.setDescription("The one and only, Invincible! The greatest superhero on Earth.");

        mapper.registerModule(new JavaTimeModule());

        String response = mvc.perform(post("/books")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();

        ComicBookResponse createBookResponse = mapper.readValue(response, new TypeReference<ComicBookResponse>() {} );
        String asin = createBookResponse.getAsin();

        String nonExistentAsin = UUID.randomUUID().toString();

        ComicBookUpdateRequest updateRequest = new ComicBookUpdateRequest();
        updateRequest.setAsin(nonExistentAsin);
        updateRequest.setCreatedBy("Megan");
        updateRequest.setTitle("Invincible");
        updateRequest.setIllustrator("Ethan");
        updateRequest.setWriter("Behzod");
        updateRequest.setReleaseYear("2021");
        updateRequest.setDescription("The one and only!");
        updateRequest.setModifiedAt(ZonedDateTime.now());

        mvc.perform(put("/books")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updateRequest)))
                .andExpect(status().isBadRequest());
        //clean up
        mvc.perform(delete("/books/delete/{asin}/createdBy/{name}", asin, createRequest.getCreatedBy()))
                .andExpect(status().isNoContent());
    }
}