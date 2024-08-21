package com.vision.api.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.*;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.ActiveProfiles;

import com.vision.api.dto.BookDto;
import com.vision.api.exception.DuplicatedEntityException;
import com.vision.api.exception.EntityNotFoundException;
import com.vision.api.model.Author;
import com.vision.api.model.Book;
import com.vision.api.repository.BookRepository;
import com.vision.api.service.BookService;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestInstance(PER_CLASS)
@ActiveProfiles("test")
@Tag("UnitTest")
@DisplayName("Book Service Unit Tests")
public class BookServiceTest {

    private BookRepository bookRepositoryMock;
    private BookService bookService;


    @BeforeAll
    public void init() {
        bookRepositoryMock = mock(BookRepository.class);
        bookService = new BookService(bookRepositoryMock, new ModelMapper());
    }

    @Test
    @DisplayName("Test 1: given Book id, when get Book, then Book is retrieved")
    void test_1_givenBookId_whenGetBook_ThenBookRetrieved() {

        //given
        long existingBookId = 0L;
        Book book1 = Book.builder()
        		.id(0L)
        		.description("com.vision.api")
        		.genre("Terror")
        		.title("title")
        		.price(BigDecimal.TEN).build();
        when(bookRepositoryMock.findById(existingBookId)).thenReturn(Optional.of(book1));

        //when
        BookDto bookDto1 = bookService.get(existingBookId);

        //then
        assertNotNull(book1);
        assertNotNull(book1.getId());
        assertEquals(bookDto1.getId(), book1.getId());
    }

    @Test
    @DisplayName("Test 2: given Book id, when get non existing Book, then exception is thrown")
    void test_2_givenBookId_whenGetNonExistingBook_ThenExceptionThrown() {

        //given
        Long nonExistingBookId = 404L;
        String errorMsg = "Book Not Found : "+nonExistingBookId;
        when(bookRepositoryMock.findById(nonExistingBookId)).thenThrow(new EntityNotFoundException(errorMsg));

        //when
        EntityNotFoundException throwException = assertThrows(EntityNotFoundException.class, () ->  bookService.get(nonExistingBookId));

        // then
        assertEquals(errorMsg, throwException.getMessage());
    }

    @Test
    @DisplayName("Test 3: when list Book, then Books are retrieved")
    void test_3_whenListBooks_ThenBooksRetrieved() {

        //given
        long existingBookId = 0L;
        Book book1 = Book.builder()
        		.id(existingBookId)
        		.description("com.vision.api")
        		.genre("Terror")
        		.title("title")
        		.price(BigDecimal.TEN).build();
        when(bookRepositoryMock.findAll()).thenReturn(Arrays.asList(book1));

        //when
        List<BookDto> books = bookService.list();

        //then
        assertNotNull(books);
        assertFalse(books.isEmpty());
        assertEquals(book1.getId(), books.get(0).getId());
    }

    @Test
    @DisplayName("Test 4: given Book data, when create new Book, then Book id is returned")
    void test_4_givenBookData_whenCreateBook_ThenBookIdReturned() {

        //given
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
        assertEquals(book1.getId(), bookId1);
    }

    @Test
    @DisplayName("Test 5: given Book incomplete data, when create new Book, then exception is thrown")
    void test_5_givenBookIncompleteData_whenCreateBook_ThenExceptionIsThrown() {

        //given
        BookDto bookDto1 = BookDto.builder()
        		.description("com.vision.api")
        		.genre("Terror")
        		.title("title")
        		.price(BigDecimal.TEN).build();
        Book Book1 = Book.builder()
        		.description("com.vision.api")
        		.genre("Terror")
        		.title("title")
        		.price(BigDecimal.TEN).build();
        String errorMsg = "Unable to save an incomplete entity : "+bookDto1;

        //when
        when(bookRepositoryMock.save(Book1)).thenThrow(new RuntimeException(errorMsg));
        RuntimeException throwException = assertThrows(RuntimeException.class, () ->  bookService.create(bookDto1));

        // then
        assertEquals(errorMsg, throwException.getMessage());
    }
    
    @Test
    @DisplayName("Test 6: given existing Book to update, then Book is retrieved")
    void test_6_givenBook_when_update_ThenRetrieved() {

        //given
        long existingBookId = 0L;
        Book book1 = Book.builder()
        		.id(existingBookId)
        		.description("com.vision.api")
        		.genre("Terror")
        		.title("title")
        		.price(BigDecimal.TEN).build();
        //when
        when(bookRepositoryMock.save(any(Book.class))).thenReturn(book1);
        BookDto bookDto1 = BookDto.builder()
        		.id(existingBookId)
        		.description("com.vision.api")
        		.genre("Terror")
        		.title("title")
        		.price(BigDecimal.TEN).build();
        Long bookId = bookService.update(bookDto1);

        //then
        assertNotNull(bookId);
        assertEquals(book1.getId(), bookId);
    }
    

    @Test
    @DisplayName("Test 7: create Book existing, when get existing Book, then exception is thrown")
    void test_7_givenBook_whenGeExistingBook_ThenExceptionThrown() {

        //given
        Long existingBookId = 1L;
        BookDto bookDto1 = BookDto.builder()
        		.id(existingBookId)
        		.description("com.vision.api")
        		.genre("Terror")
        		.title("title")
        		.price(BigDecimal.TEN).build();
        String errorMsg = "Entity Book with id " + existingBookId + " alrebooky exists";
        when(bookRepositoryMock.findById(existingBookId)).thenThrow(new DuplicatedEntityException(errorMsg));

        //when
        DuplicatedEntityException throwException = assertThrows(DuplicatedEntityException.class, () ->  bookService.create(bookDto1));

        // then
        assertEquals(errorMsg, throwException.getMessage());
    }
    
    @Test
    @DisplayName("Test 8: given Book and Author data, a list of book by author id is returned")
    void test_8_givenBookData_whenCreateBook_Then_Book_list_Returned() {
    	//given
        long existingBookId = 0L;
        long authorId =1L;
        Author author1 = Author.builder()
         		.id(authorId)
         		.email("test@gmail.com")
         		.firstName("Test")
         		.lastName("Surname").build();
        Book book1 = Book.builder()
        		.author(author1)
        		.id(existingBookId)
        		.description("com.vision.api")
        		.genre("Terror")
        		.title("title")
        		.price(BigDecimal.TEN).build();
        when(bookRepositoryMock.findAll()).thenReturn(Arrays.asList(book1));

        //when
        List<BookDto> books = bookService.getAllBooksByAutherId(authorId);

        //then
        assertNotNull(books);
        assertFalse(books.isEmpty());
        assertEquals(book1.getId(), books.get(0).getId());
    }

}
