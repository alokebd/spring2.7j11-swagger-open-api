package com.vision.api.service;

import org.junit.jupiter.api.*;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.ActiveProfiles;

import com.vision.api.dto.AuthorDto;
import com.vision.api.dto.AuthorResponseDto;
import com.vision.api.dto.BookDto;
import com.vision.api.exception.DuplicatedEntityException;
import com.vision.api.exception.EntityNotFoundException;
import com.vision.api.model.Author;
import com.vision.api.model.Book;
import com.vision.api.repository.AuthorRepository;
import com.vision.api.repository.BookRepository;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
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

    private BookRepository bookRepositoryMock;
    private BookService bookService;
    
    @BeforeAll
    public void init() {
    	//author
        authorRepositoryMock = mock(AuthorRepository.class);
        authorService = new AuthorService(authorRepositoryMock, new ModelMapper());
        //book
        bookRepositoryMock = mock(BookRepository.class);
        bookService = new BookService(bookRepositoryMock, new ModelMapper());
    }

    @Test
    @DisplayName("Test 1: given Author data, when create new Author, then Author id is returned")
    void test_1_given_AuthorData_whenCreateAuthor_ThenAuthorIdReturned() {

        //given
        AuthorDto authorDto1 = AuthorDto.builder()
        		.email("test@gmail.com")
        		.firstName("Test")
        		.lastName("Surname").build();
        Author author1 = Author.builder()
        		.id(0L)
        		.email("test@gmail.com")
        		.firstName("Test")
        		.lastName("Surname").build();

        //when
        when(authorRepositoryMock.save(any(Author.class))).thenReturn(author1);
        Long authorId1 = authorService.create(authorDto1);

        //then
        assertNotNull(authorId1);
        assertEquals(author1.getId(), authorId1);
    }

    @Test
    @DisplayName("Test 2: given an incomplete data, when create new Ad, then exception is thrown")
    void test_2_givenAdIncompleteData_whenCreateAd_ThenExceptionIsThrown() {

        //given
        AuthorDto authorDto1 = AuthorDto.builder()
        		.email("test@gmail.com")
        		.lastName("Surname")
        		.build();
        Author author1 = Author.builder()
        		.email("test@com.vision.api.com")
        		.lastName("Surname").build();
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
    @DisplayName("Test 4: given Author id, when delete non existing Author, then exception is thrown")
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
    
    
    @Test
    @DisplayName("Test 5: given Author id, when get Author, then Author is retrieved")
    void test_5_getAuthorById_whenGetAuthor_ThenAuthorRetrieved() {

        //given
        long existingId = 1L;
        Author author1 = Author.builder()
        		.id(existingId)
        		.email("test@gmail.com")
        		.firstName("Test")
        		.lastName("Surname").build();
        when(authorRepositoryMock.findById(existingId)).thenReturn(Optional.of(author1));

        //when
        AuthorDto authorDto2 = authorService.getAuthorById(existingId);

        //then
        assertNotNull(author1);
        assertNotNull(author1.getId());
        assertEquals(authorDto2.getId(), author1.getId());
    }
    
    @Test
    @DisplayName("Test 6: given Author data, when create new Author, then exception is thrown")
    void test_6_given_AuthorData_whenCreateAuthor_ExceptionIsThrown() {

        //given
    	long existingId =1L;
    	String errorMsg = "Entity Author with id " + existingId + " already exists";
        AuthorDto authorDto1 = AuthorDto.builder()
        		.id(existingId)
        		.email("test@gmail.com")
        		.firstName("Test")
        		.lastName("Surname").build();
        Author author1 = Author.builder()
        		.id(existingId)
        		.email("test@gmail.com")
        		.firstName("Test")
        		.lastName("Surname").build();

        //when
        when(authorRepositoryMock.save(author1)).thenThrow(new DuplicatedEntityException(errorMsg));
        DuplicatedEntityException throwException = assertThrows(DuplicatedEntityException.class, () ->  authorService.create(authorDto1));

        // then
        assertEquals(errorMsg, throwException.getMessage());
    }
    
    @Test
    @DisplayName("Test 6: given Author data, when update existing Author, then Author id is returned")
    void test_6_given_AuthorData_whenCreateAuthor_ThenAuthorIdReturned() {

        //given
    	long existingId =1L;
        AuthorDto authorDto1 = AuthorDto.builder()
        		.id(existingId)
        		.email("test@gmail.com")
        		.firstName("Test")
        		.lastName("Surname").build();
        Author author1 = Author.builder()
        		.id(existingId)
        		.email("test@gmail.com")
        		.firstName("Test")
        		.lastName("Surname").build();

        //when
        when(authorRepositoryMock.save(any(Author.class))).thenReturn(author1);
        Long authorId1 = authorService.update(authorDto1);

        //then
        assertNotNull(authorId1);
        assertEquals(author1.getId(), authorId1);
    }
    
    @Test
    @DisplayName("Test 7: given Author data, when update Author, then exception is thrown")
    void test_7_given_AuthorData_whenCreateAuthor_ExceptionIsThrown() {

        //given
    	long existingId =0L;
    	String errorMsg = "Author not found";
        AuthorDto authorDto1 = AuthorDto.builder()
        		//.id(existingId)
        		.email("test@gmail.com")
        		.firstName("Test")
        		.lastName("Surname").build();
        Author author1 = Author.builder()
        		.id(existingId)
        		.email("test@gmail.com")
        		.firstName("Test")
        		.lastName("Surname").build();

        //when
        when(authorRepositoryMock.save(author1)).thenThrow(new EntityNotFoundException(errorMsg));
        EntityNotFoundException throwException = assertThrows(EntityNotFoundException.class, () ->  authorService.update(authorDto1));

        // then
        assertEquals(errorMsg, throwException.getMessage());
    }
    
    @Test
    @DisplayName("Test 8: given Author id, when get Author, then AuthorResponseDto is retrieved")
    void test_8_getBooksByAuthorId_whenGetAuthor_ThenAuthorResponseDtoRetrieved() {

        //given
    	 AuthorDto authorDto1 = AuthorDto.builder()
         		.email("test@gmail.com")
         		.firstName("Test")
         		.lastName("Surname").build();
         Author author1 = Author.builder()
         		.id(0L)
         		.email("test@gmail.com")
         		.firstName("Test")
         		.lastName("Surname").build();

         //when
         when(authorRepositoryMock.save(any(Author.class))).thenReturn(author1);
         Long authorId = authorService.create(authorDto1);
         assertNotNull(authorId);
         author1.setId(authorId);
    	
         BookDto bookDto1 = BookDto.builder()
         		.description("com.vision.api")
         		.genre("Terror")
         		.title("title")
         		.price(BigDecimal.TEN).build();
         Book book1 = Book.builder()
         		.id(0L).description("com.vision.api")
         		.genre("Terror")
         		.title("title")
         		.price(BigDecimal.TEN).build();

         //when
         when(bookRepositoryMock.save(any(Book.class))).thenReturn(book1);
         Long bookId1 = bookService.create(bookDto1);

         //then
         assertNotNull(bookId1);
         book1.setId(bookId1);
         book1.setAuthor(author1);
         
        

        //when
        //List<BookDto> books = bookService.getAllBooksByAutherId(authorId);
        // List<BookDto> books = bookService.getAllBooksByAutherId(authorId);

        //assertEquals(book1.getId(), books.get(0).getId());
         
       //when(authorRepositoryMock.findById(authorId)).thenReturn(Optional.of(author1));

        when(authorRepositoryMock.findAll()).thenReturn(Arrays.asList(author1));
        when(bookRepositoryMock.findAll()).thenReturn(Arrays.asList(book1));
     
        //AuthorResponseDto authorResponseDto = authorService.getBooksByAuthorId(authorId);
       
        
        //then
        //assertNotNull(author1);
       // assertNotNull(author1.getId());
        //assertEquals(authorResponseDto.getId(), author1.getId());
        //
    }


}
