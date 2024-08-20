package com.vision.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.vision.api.dto.AuthorDto;
import com.vision.api.dto.AuthorResponseDto;
import com.vision.api.service.AuthorService;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api")
@Slf4j
public class AuthorController {

    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @Operation(summary = "Creates a new author")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created the author"),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content) })
    @PostMapping(path = "/v1/authors", consumes = {"application/json"})
    public ResponseEntity<Void> create(@Valid @RequestBody AuthorDto authorData) {

        log.info("POST /api/v1/authors : "+authorData);
        Long adId = authorService.create(authorData);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(adId).toUri();

        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "Removes a requested author")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content) })
    @DeleteMapping(path = "/v1/authors/{id}", produces = {"application/json"})
    public ResponseEntity<Void> delete(@PathVariable("id") Long authorId) {

        log.info("DELETE /api/v1/authors/"+authorId);
        authorService.delete(authorId);
        return ResponseEntity.noContent().build();
    }
    
    @Operation(summary = "Update an existing author")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated the author"),
            @ApiResponse(responseCode = "400", description = "Bad request",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content) })
    @PutMapping(path = "/v1/authors", consumes = {"application/json"})
    public ResponseEntity<Void> update(@Valid @RequestBody AuthorDto authorData){
    	 log.info("PUT /api/v1/authors : "+authorData);
    	 
    	 Long adId = authorService.update(authorData);
    	 URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                 .buildAndExpand(adId).toUri();

         return ResponseEntity.created(location).build();
    }
    
    @Operation(summary = "Get a requested author")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content) })
    @GetMapping(path = "/v1/authors/{id}", produces = {"application/json"})
    public ResponseEntity<AuthorDto> getAuthorById(@PathVariable("id") Long authorId){
    	
    	 log.info("GET /api/v1/authors/"+authorId);
    	 AuthorDto dto = authorService.getAuthorById(authorId);
    	 return new ResponseEntity<>(dto, HttpStatus.OK);
    }
    
    @Operation(summary = "Get books for requested author")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content) })
    @GetMapping(path = "/v1/books/authors/{id}", produces = {"application/json"})
    public ResponseEntity<AuthorResponseDto> getAllBooksByAuthorId(@PathVariable("id") Long authorId){
    	
    	 log.info("GET /api/v1/books/authors/"+authorId);
    	 AuthorResponseDto dto = this.authorService.getBooksByAuthorId(authorId);
    	 return new ResponseEntity<>(dto, HttpStatus.OK);
    }
    
    
}
