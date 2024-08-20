package com.vision.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vision.api.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    long deleteByTitle(String title);
    
    @Query(value = "select * from book b " +
            "where  b.author_id = :id", nativeQuery = true)
    List<Book> getBooksByAuthorId(@Param("id") long id);
}

