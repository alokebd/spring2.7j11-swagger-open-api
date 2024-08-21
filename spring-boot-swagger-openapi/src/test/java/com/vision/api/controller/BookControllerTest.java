package com.vision.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vision.api.dto.AuthorDto;
import com.vision.api.dto.BookDto;
import com.vision.api.exception.DuplicatedEntityException;
import com.vision.api.exception.EntityNotFoundException;
import com.vision.api.service.BookService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;



import java.math.BigDecimal;
import java.util.ArrayList;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Tag("IntegrationTest")
@DisplayName("Book Resource Integration Tests")
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService bookService;

    @Test
    @DisplayName("Test 1: get Book, should return expected Book")
    public void test_1_get_Book_Should_Return_200() throws Exception {

        //given
        long bookId = 0L;
        given(this.bookService.get(bookId)).willReturn(new BookDto());

        //when-then
        this.mockMvc.perform(get("/api/v1/books/"+bookId)
                .accept(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test 2: get Book list, should return complete Book list")
    public void test_2_getBookList_ShouldReturn_200() throws Exception {

        //given
        given(this.bookService.list()).willReturn(new ArrayList<>());

        //when-then
        this.mockMvc.perform(get("/api/v1/books")
                .accept(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test 3: get non existing Book, should return 404")
    public void test_3_getNotExistingBook_ShouldReturn_404() throws Exception {

        //given
        long nonExistentBookId = 404L;
        given(this.bookService.get(nonExistentBookId))
                .willThrow(new EntityNotFoundException("Book not found for book id: "+nonExistentBookId));

        //when-then
        this.mockMvc.perform(get("/api/v1/books/"+nonExistentBookId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Test 4: create Book, should return 201")
    public void test_4_createBook_ShouldReturn_201() throws Exception {

        //given
    	long authorId = 1L;
    	long bookID = 0L;
    	AuthorDto defaultAuthor = AuthorDto.builder()
        		.id(authorId)
        		.email("test@gmail.com")
        		.firstName("Test")
        		.lastName("Surname").build();
    	
        BookDto book = BookDto.builder()
        		.id(bookID)
        		.description("com.vision.api")
        		.genre("Drama")
        		.price(new BigDecimal(2.0))
        		.title("test")
        		.author(defaultAuthor)
        		.build();
        String json = objectMapper.writeValueAsString(book);
        when(this.bookService.create(book)).thenReturn(bookID);

        //when-then
        this.mockMvc.perform(post("/api/v1/books/")
                .contentType(APPLICATION_JSON_UTF8)
                .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Test 5: create existing Book id, should return 409")
    public void test_5_createExistingBookId_ShouldReturn_409() throws Exception {

        //given
        long existingBookId = 1L;
        //AuthorDto defaultAuthor = AuthorDto.builder().id(existingBookId).build();
        long authorId = 1L;
        AuthorDto defaultAuthor = AuthorDto.builder()
        		.id(authorId)
        		.email("test@gmail.com")
        		.firstName("Test")
        		.lastName("Surname").build();
        
        BookDto book1 = BookDto.builder()
        		.description("com.vision.api")
        		.genre("Drama")
        		.title("test")
        		.price(new BigDecimal(2.0))
        		.author(defaultAuthor)
        		.build();
        String json = objectMapper.writeValueAsString(book1);
        when(this.bookService.create(book1)).thenThrow(new DuplicatedEntityException("Entity Book with id " + existingBookId + " alrebooky exists"));

        //when-then
        this.mockMvc.perform(post("/api/v1/books/")
                .contentType(APPLICATION_JSON_UTF8)
                .content(json))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Test 6: create incomplete Book, should return 400")
    public void test_6_createIncompleteBook_ShouldReturn_400() throws Exception {

        //given
        BookDto book1 = BookDto.builder().
        		description("com.vision.api").build();
        String json = objectMapper.writeValueAsString(book1);

        //when-then
        this.mockMvc.perform(post("/api/v1/books/")
                .contentType(APPLICATION_JSON_UTF8)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(notNullValue())))
                .andExpect(jsonPath("$.message", is(containsString("Title is mandatory"))))
                .andExpect(jsonPath("$.message", is(containsString("Genre is mandatory"))))
                .andExpect(jsonPath("$.message", is(containsString("Please provide a price"))));
    }

    @Test
    @DisplayName("Test 7: update Book, should return 201")
    public void test_7_createBook_ShouldReturn_201() throws Exception {
        //given
    	long authorId = 1L;
    	long bookID = 1L;
    	AuthorDto defaultAuthor = AuthorDto.builder()
        		.id(authorId)
        		.email("test@gmail.com")
        		.firstName("Test")
        		.lastName("Surname").build();
    	
        BookDto book = BookDto.builder()
        		.id(bookID)
        		.description("com.vision.api")
        		.genre("Drama")
        		.price(new BigDecimal(2.0))
        		.title("test")
        		.author(defaultAuthor)
        		.build();
        String json = objectMapper.writeValueAsString(book);
        when(this.bookService.create(book)).thenReturn(bookID);

        //when-then
        this.mockMvc.perform(put("/api/v1/books/")
                .contentType(APPLICATION_JSON_UTF8)
                .content(json))
                .andExpect(status().isCreated());
    }
}
