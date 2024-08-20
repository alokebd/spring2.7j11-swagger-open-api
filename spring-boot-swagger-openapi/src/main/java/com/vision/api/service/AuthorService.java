package com.vision.api.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.vision.api.dto.AuthorDto;
import com.vision.api.dto.AuthorResponseDto;
import com.vision.api.dto.BookDto;
import com.vision.api.dto.BookResponseDto;
import com.vision.api.exception.DuplicatedEntityException;
import com.vision.api.exception.EntityNotFoundException;
import com.vision.api.model.Author;
import com.vision.api.repository.AuthorRepository;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public AuthorService(AuthorRepository authorRepository, ModelMapper modelMapper) {
        this.authorRepository = authorRepository;
        this.modelMapper = modelMapper;
    }
    
    @Autowired
    private BookService bookService;

    @Transactional
    public Long create(AuthorDto authorDto) {
        if (authorDto.getId()!=null) {
            authorRepository.findById(authorDto.getId()).ifPresent(author -> {
                throw new DuplicatedEntityException("Entity Author with id " + author.getId() + " already exists");
            });
        }

        Author author = modelMapper.map(authorDto, Author.class);
        return authorRepository.save(author).getId();
    }

    @Transactional
    public AuthorDto delete(Long authorId) {
       // Author author = authorRepository.findById(authorId).orElseThrow(EntityNotFoundException::new);
    	Author author = authorRepository.findById(authorId).orElseThrow(() -> new EntityNotFoundException("Author not found by author id: "+authorId));
        authorRepository.delete(author);
        return modelMapper.map(author, AuthorDto.class);
    }
    
    @Transactional
    public Long update(AuthorDto authorDto) {
    	authorRepository.findById(authorDto.getId()).orElseThrow(() -> new EntityNotFoundException("Author not found"));
    	Author author = modelMapper.map(authorDto, Author.class);
    	return authorRepository.save(author).getId();
    }
    
    public AuthorDto getAuthorById(long authorId) {
    	   Author author = authorRepository.findById(authorId).orElseThrow(() -> new EntityNotFoundException("Author not found by author id: "+authorId));
    	   return modelMapper.map(author, AuthorDto.class);
    }
    
    
    public AuthorResponseDto getBooksByAuthorId(long authorId) {
    	  Author author = authorRepository.findById(authorId).orElseThrow(() -> new EntityNotFoundException("Author not found by author id: "+authorId));
    	  AuthorDto authorDto = modelMapper.map(author, AuthorDto.class);
    	  
    		  
    	  List<BookDto> books =bookService.getAllBooksByAutherId(authorId);
    	  List<BookResponseDto> dtos = books
    			  .stream()
    			  .map(user -> modelMapper.map(user, BookResponseDto.class))
    			  .collect(Collectors.toList());

    	  
    	  AuthorResponseDto dto = new AuthorResponseDto();
    	  dto.setBooks(dtos);
    	  dto.setId(authorDto.getId());
    	  dto.setFirstName(authorDto.getFirstName());
    	  dto.setLastName(authorDto.getLastName());
    	  dto.setEmail(authorDto.getEmail());
    	  return dto;
    }

}
