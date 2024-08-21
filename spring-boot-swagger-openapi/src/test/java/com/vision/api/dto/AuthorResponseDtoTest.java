package com.vision.api.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.ActiveProfiles;

import com.vision.api.model.Author;

@ActiveProfiles("test")
@Tag("UnitTest")
@DisplayName("AuthorResponseDto Mapper Unit Tests")
public class AuthorResponseDtoTest {
	private ModelMapper modelMapper = new ModelMapper();

    @Test
    @DisplayName("Test 1: when convert Author entity to AuthorResponseDto, then correct")
    public void test_1_whenConvert_AuthorEntity_To_AuthorResponseDto_thenCorrect() {

        //given
        Author author = Author.builder().id(1L).email("test@com.vision.api.com").firstName("Test").lastName("Surname").build();

        //when
        AuthorResponseDto authorDto = modelMapper.map(author, AuthorResponseDto.class);

        //then
        assertEquals(authorDto.getId(), author.getId());
        assertEquals(authorDto.getFirstName(), author.getFirstName());
        assertEquals(authorDto.getLastName(), author.getLastName());
        assertEquals(authorDto.getEmail(), author.getEmail());
    }

    @Test
    @DisplayName("Test 2: when convert AuthorResponseDto  to Author entity, then correct")
    public void test_2_whenConvert_AuthorDto_ToAuthorEntity_thenCorrect() {

        //given
        //AuthorDto authorDto = AuthorDto.builder().id(1L).email("test@com.vision.api.com").firstName("Test").lastName("Surname").build();
    	AuthorResponseDto authorDto = new AuthorResponseDto();
    	authorDto.setId(1L);
    	authorDto.setEmail("test@gmail.com");
    	authorDto.setFirstName("Test");
    	authorDto.setLastName("Surname");
        //when
        Author author = modelMapper.map(authorDto, Author.class);

        //then
        assertEquals(author.getId(), authorDto.getId());
        assertEquals(author.getFirstName(), authorDto.getFirstName());
        assertEquals(author.getLastName(), authorDto.getLastName());
        assertEquals(author.getEmail(), authorDto.getEmail());

    }

}
