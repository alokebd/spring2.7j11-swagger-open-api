package com.vision.api.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.ActiveProfiles;

import com.vision.api.model.Book;

@ActiveProfiles("test")
@Tag("UnitTest")
@DisplayName("BookResponse Mapper  Unit Tests")
public class BookResponseDtoTests {
    private ModelMapper modelMapper = new ModelMapper();
    @Test
    @DisplayName("Test 1: when convert Book entity to BookResponseDto, then correct")
    public void test_1_whenConvert_BookEntity_To_BookResponseDto_thenCorrect() {

        //given
        Book book = Book.builder().id(1L).title("Example").genre("Comedy").build();

        //when
        BookResponseDto bookDto = modelMapper.map(book, BookResponseDto.class);

        //then
        assertEquals(book.getId(), bookDto.getId());
        assertEquals(book.getTitle(), bookDto.getTitle());
        assertEquals(book.getGenre(), bookDto.getGenre());
    }

    @Test
    @DisplayName("Test 2: when convert BookResponseDto to Book entity, then correct")
    public void test_2_whenConvert_BookDto_To_BookEntity_thenCorrect() {

        //given

    	BookResponseDto bookDto = new BookResponseDto();
    	bookDto.setId(1L);
    	bookDto.setTitle("Example");
    	bookDto.setGenre("Comedy");
        //when
        Book book = modelMapper.map(bookDto, Book.class);

        //then
        assertEquals(bookDto.getId(), book.getId());
        assertEquals(bookDto.getTitle(), book.getTitle());
        assertEquals(bookDto.getGenre(), book.getGenre());
    }
}
