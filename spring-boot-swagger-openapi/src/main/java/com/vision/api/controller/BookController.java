package com.vision.api.controller;

import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.vision.api.dto.BookDto;
import com.vision.api.service.BookService;

import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequestMapping("/api")
@Slf4j
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @Operation(summary = "Gets the list of available books")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the books",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class)) }),
    })
    @GetMapping(path = "/v1/books", produces = {"application/json"})
    public ResponseEntity<List<BookDto>> list() {
    	
        log.info("GET /api/v1/books");
        return ResponseEntity.ok(bookService.list());
    }

    @Operation(summary = "Get a book by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the book",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BookDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = @Content) })
    @GetMapping(path = "/v1/books/{id}", produces = {"application/json"})
    public ResponseEntity<BookDto> get(@PathVariable("id") @Min(1) Long bookId) {

        log.info("GET /api/v1/books/"+bookId);
        return ResponseEntity.ok(bookService.get(bookId));
    }

    @Operation(summary = "Creates a new book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created the book"),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content) })
    @PostMapping(path = "/v1/books", consumes = {"application/json"})
    public ResponseEntity<Void> create(@Valid @RequestBody BookDto bookDto) {

        log.info("POST /api/v1/books : "+bookDto);
        Long bookId = bookService.create(bookDto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(bookId).toUri();

        return ResponseEntity.created(location).build();
    }
    
    
    @Operation(summary = "Update a new book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update the book"),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content) })
    @PutMapping(path = "/v1/books", consumes = {"application/json"})
    public ResponseEntity<Void> update(@Valid @RequestBody BookDto bookDto) {

        log.info("PUT /api/v1/books : "+bookDto);
        Long bookId = bookService.update(bookDto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(bookId).toUri();

        return ResponseEntity.created(location).build();
    }
}
