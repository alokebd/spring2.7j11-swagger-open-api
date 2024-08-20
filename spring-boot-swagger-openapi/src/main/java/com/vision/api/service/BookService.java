package com.vision.api.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vision.api.dto.BookDto;
import com.vision.api.exception.DuplicatedEntityException;
import com.vision.api.exception.EntityNotFoundException;
import com.vision.api.model.Book;
import com.vision.api.repository.BookRepository;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public BookService(BookRepository bookRepository, ModelMapper modelMapper) {
        this.bookRepository = bookRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public Long create(BookDto bookDto) {

        if (bookDto.getId()!=null) {
            bookRepository.findById(bookDto.getId()).ifPresent(book -> {
                throw new DuplicatedEntityException("Entity Book with id " + book.getId() + " alrebooky exists");
            });
        }

        Book book = modelMapper.map(bookDto, Book.class);
        return bookRepository.save(book).getId();
    }
    
    @Transactional
    public Long update(BookDto bookDto) {
    	   bookRepository.findById(bookDto.getId())
                   .orElseThrow(() -> new EntityNotFoundException("Book not found"));
    	   
    	   Book book = modelMapper.map(bookDto, Book.class);
           return bookRepository.save(book).getId();
    }

    @Transactional(readOnly=true)
    public BookDto get(Long bookId) {
        Book book = bookRepository.findById(bookId)
                                        .orElseThrow(() -> new EntityNotFoundException("Book not found for book id: "+bookId));
        return toDto(book);
    }

    @Transactional(readOnly=true)
    public List<BookDto> list() {
        return bookRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly=true)
    public List<BookDto> getAllBooksByAutherId(Long authorId){
    	//List<Book> books = bookRepository.getBooksByAuthorId(authorId);
    	return bookRepository.findAll()
                .stream()
                .filter(book -> authorId.equals(book.getAuthor().getId()))
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private BookDto toDto(Book book) {

        return modelMapper.map(book, BookDto.class);
    }
    
}
