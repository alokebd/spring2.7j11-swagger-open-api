package com.vision.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vision.api.dto.AuthorDto;
import com.vision.api.dto.AuthorResponseDto;
import com.vision.api.dto.BookDto;
import com.vision.api.dto.BookResponseDto;
import com.vision.api.exception.DuplicatedEntityException;
import com.vision.api.exception.EntityNotFoundException;
import com.vision.api.model.Author;
import com.vision.api.model.Book;
import com.vision.api.service.AuthorService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Tag("IntegrationTest")
@DisplayName("Author Resource Integration Tests")
public class AuthorControllerTest{

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthorService authorService;

    @Test
    @DisplayName("Test 1: create Author, should return expected 201")
    public void test_1_create_Author_Should_Return_201() throws Exception {

        //given
        AuthorDto authorDto = AuthorDto.builder()
        		.email("test@gmail.com")
        		.firstName("Test")
        		.lastName("Surname")
        		.build();
        String json = objectMapper.writeValueAsString(authorDto);
        when(this.authorService.create(authorDto)).thenReturn(0L);

        //when-then
        this.mockMvc.perform(post("/api/v1/authors/")
                .contentType(APPLICATION_JSON_UTF8)
                .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Test 2: create incomplete Book, should return 400")
    public void test_2_create_Incomplete_Book_Should_Return_400() throws Exception {

        //given
        AuthorDto authorDto = AuthorDto
        		.builder()
        		.email("test_examplecom").lastName("S@#¢∞name").build();
        String json = objectMapper.writeValueAsString(authorDto);

        //when-then
        this.mockMvc.perform(post("/api/v1/authors/")
                .contentType(APPLICATION_JSON_UTF8)
                .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is(notNullValue())))
                .andExpect(jsonPath("$.message", is(containsString("First name is mandatory"))))
                .andExpect(jsonPath("$.message", is(containsString("A valid mail address is mandatory"))))
                .andExpect(jsonPath("$.message", is(containsString("An alphanumeric last name is mandatory"))));
    }

    @Test
    @DisplayName("Test 3: create existing Author id, should return 409")
    public void test_3_create_Existing_AuthorId_Should_Return_409() throws Exception {

        //given
        long existingAuthorId = 0L;
        AuthorDto author1 = AuthorDto.builder().id(existingAuthorId).email("test@gmail.com").firstName("Test").lastName("Surname").build();
        String json = objectMapper.writeValueAsString(author1);
        when(this.authorService.create(author1)).thenThrow(new DuplicatedEntityException("Entity Author with id " + existingAuthorId + " already exists"));

        //when-then
        this.mockMvc.perform(post("/api/v1/authors/")
                .contentType(APPLICATION_JSON_UTF8)
                .content(json))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Test 4: delete Author, should return expected Author Id")
    public void test_4_delete_Author_Should_Return_204() throws Exception {

        //given
        long existingAuthorId = 0L;
        when(this.authorService.delete(existingAuthorId)).thenReturn(null);

        //when-then
        this.mockMvc.perform(delete("/api/v1/authors/"+existingAuthorId))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Test 5: delete non existing Author id, should return 404")
    public void test_5_delete_Existing_AuthorId_Should_Return_404() throws Exception {

        //given
        long nonExistingAuthorId = 404L;
        when(this.authorService.delete(nonExistingAuthorId)).thenThrow(new EntityNotFoundException("Author not found by author id: "+nonExistingAuthorId));

        //when-then
        this.mockMvc.perform(delete("/api/v1/authors/"+nonExistingAuthorId))
                .andExpect(status().isNotFound());
    }
    
    
    @Test
    @DisplayName("Test 6: updated Author, should return expected 201")
    public void test_6_update_Author_Should_Return_200() throws Exception {

        //given
        AuthorDto authorDto = AuthorDto.builder()
        		.id(1L)
        		.email("test@gmail.com")
        		.firstName("Test")
        		.lastName("Surname").build();
        String json = objectMapper.writeValueAsString(authorDto);
        when(this.authorService.update(authorDto)).thenReturn(1L);

        //when-then
        this.mockMvc.perform(put("/api/v1/authors/")
                .contentType(APPLICATION_JSON_UTF8)
                .content(json))
                .andExpect(status().isCreated());
    }
    
    
    @Test
    @DisplayName("Test 7: Get Author, should return expected Author")
    public void test_7_get_Author_Should_Return_200() throws Exception {
        //given
        long authorId = 1L;
        AuthorDto authorDto = AuthorDto.builder()
        		.id(1L)
        		.email("test@gmail.com")
        		.firstName("Test")
        		.lastName("Surname").build();
        given(this.authorService.getAuthorById(authorId)).willReturn(authorDto);

        //when-then
        this.mockMvc.perform(get("/api/v1/authors/"+authorId)
                .accept(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }
    
    @Test
    @DisplayName("Test 8: get Book list, should return complete Book list")
    public void test_8_getBookListByAuthorID_ShouldReturn_200() throws Exception {
    	
        //given
    	long existingId =1L;
    	 //when
        AuthorResponseDto authorDto = new AuthorResponseDto();
        authorDto.setId(existingId);
        authorDto.setFirstName("Test");
        authorDto.setLastName("Surname");
        authorDto.setEmail("test@gmail.com");
        BookResponseDto bookReponseDto = new BookResponseDto();
        bookReponseDto.setDescription("example");
        bookReponseDto.setGenre("Comedy");
        bookReponseDto.setPrice(BigDecimal.TEN);
        bookReponseDto.setTitle("Example");
        bookReponseDto.setId(existingId);
        List<BookResponseDto>list = new ArrayList();
        list.add(bookReponseDto);
        authorDto.setBooks(list);
        
        given(this.authorService.getBooksByAuthorId(existingId)).willReturn(authorDto);

        //when-then
        this.mockMvc.perform(get("/api/v1/books/authors/"+existingId)
                .accept(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }


}
