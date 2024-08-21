package com.vision.api.service;

import org.junit.jupiter.api.*;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.ActiveProfiles;

import com.vision.api.dto.AuthorDto;
import com.vision.api.exception.EntityNotFoundException;
import com.vision.api.model.Author;
import com.vision.api.repository.AuthorRepository;
import com.vision.api.service.AuthorService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestInstance(PER_CLASS)
@ActiveProfiles("test")
@Tag("UnitTest")
@DisplayName("Author Service Unit Tests")
public class AuthorServiceTest {

    private AuthorRepository authorRepositoryMock;
    private AuthorService authorService;


    @BeforeAll
    public void init() {
        authorRepositoryMock = mock(AuthorRepository.class);
        authorService = new AuthorService(authorRepositoryMock, new ModelMapper());
    }

    @Test
    @DisplayName("Test 1: given Author data, when create new Author, then Author id is returned")
    void test_1_given_AuthorData_whenCreateAuthor_ThenAuthorIdReturned() {

        //given
        AuthorDto authorDto1 = AuthorDto.builder().email("test@com.vision.api.com").firstName("Test").lastName("Surname").build();
        Author author1 = Author.builder().id(0L).email("test@com.vision.api.com").firstName("Test").lastName("Surname").build();

        //when
        when(authorRepositoryMock.save(any(Author.class))).thenReturn(author1);
        Long authorId1 = authorService.create(authorDto1);

        //then
        assertNotNull(authorId1);
        assertEquals(author1.getId(), authorId1);
    }

    @Test
    @DisplayName("Test 2: given Ad incomplete data, when create new Ad, then exception is thrown")
    void test_2_givenAdIncompleteData_whenCreateAd_ThenExceptionIsThrown() {

        //given
        AuthorDto authorDto1 = AuthorDto.builder().email("test@com.vision.api.com").lastName("Surname").build();
        Author author1 = Author.builder().email("test@com.vision.api.com").lastName("Surname").build();
        String errorMsg = "Unable to save an incomplete entity : "+authorDto1;

        //when
        when(authorRepositoryMock.save(author1)).thenThrow(new RuntimeException(errorMsg));
        RuntimeException throwException = assertThrows(RuntimeException.class, () ->  authorService.create(authorDto1));

        // then
        assertEquals(errorMsg, throwException.getMessage());
    }

    @Test
    @DisplayName("Test 3: given Author id, when delete Author, then Author is retrieved")
    void test_3_givenAuthorId_whenDeleteAuthor_ThenAuthorRetrieved() {

        //given
        long existingAuthorId = 0L;
        Author author1 = Author.builder().id(existingAuthorId).email("test@com.vision.api.com").firstName("Test").lastName("Surname").build();
        when(authorRepositoryMock.findById(existingAuthorId)).thenReturn(Optional.of(author1));

        //when
        AuthorDto authorDto1 = authorService.delete(existingAuthorId);

        //then
        assertNotNull(authorDto1);
        assertNotNull(authorDto1.getId());
        assertEquals(author1.getId(), author1.getId());
    }

    @Test
    @DisplayName("Test4: given Author id, when delete non existing Author, then exception is thrown")
    void test_4_givenAuthorId_whenDeleteNonExistingAuthor_ThenExceptionThrown() {

        //given
        Long nonExistingAuthorId = 404L;
        String errorMsg = "Author Not Found : "+nonExistingAuthorId;
        when(authorRepositoryMock.findById(nonExistingAuthorId)).thenThrow(new EntityNotFoundException(errorMsg));

        //when
        EntityNotFoundException throwException = assertThrows(EntityNotFoundException.class, () ->  authorService.delete(nonExistingAuthorId));

        // then
        assertEquals(errorMsg, throwException.getMessage());
    }

}
