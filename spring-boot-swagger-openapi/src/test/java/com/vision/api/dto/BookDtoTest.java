package com.vision.api.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.ActiveProfiles;

import com.vision.api.dto.BookDto;
import com.vision.api.model.Book;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@Tag("UnitTest")
@DisplayName("Book Mapper Unit Tests")
public class BookDtoTest {

    private ModelMapper modelMapper = new ModelMapper();

    @Test
    @DisplayName("Test 1: when convert Book entity to Book dto, then correct")
    public void test_1_whenConvert_BookEntity_To_BookDto_thenCorrect() {

        //given
        Book book = Book.builder().id(1L).title("Example").genre("Comedy").build();

        //when
        BookDto bookDto = modelMapper.map(book, BookDto.class);

        //then
        assertEquals(book.getId(), bookDto.getId());
        assertEquals(book.getTitle(), bookDto.getTitle());
        assertEquals(book.getGenre(), bookDto.getGenre());
    }

    @Test
    @DisplayName("Test 2: when convert Book dto to Book entity, then correct")
    public void test_2_whenConvert_BookDto_To_BookEntity_thenCorrect() {

        //given
        BookDto bookDto = BookDto.builder().id(1L).title("Example").genre("Comedy").build();

        //when
        Book book = modelMapper.map(bookDto, Book.class);

        //then
        assertEquals(bookDto.getId(), book.getId());
        assertEquals(bookDto.getTitle(), book.getTitle());
        assertEquals(bookDto.getGenre(), book.getGenre());
    }
}
