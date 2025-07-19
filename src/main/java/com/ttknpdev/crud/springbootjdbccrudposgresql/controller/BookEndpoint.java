package com.ttknpdev.crud.springbootjdbccrudposgresql.controller;

import com.ttknpdev.crud.springbootjdbccrudposgresql.model.Book;
import com.ttknpdev.crud.springbootjdbccrudposgresql.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;



@RestController
@RequestMapping(value = "/api/book")
public class BookEndpoint {

    private static final Logger log = LoggerFactory.getLogger(BookEndpoint.class);
    private final BookRepository<Book> bookRepository;

    @Autowired
    public BookEndpoint(BookRepository<Book> bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping(value = "/reads")
    private ResponseEntity<List<Book>> reads() {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(bookRepository.reads());
    }
    @GetMapping(value = "/read/{id}")
    private ResponseEntity<Book> read(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(bookRepository.read(id));
    }
    @GetMapping(value = "/reads-price-more-than/{price}")
    private ResponseEntity<List<Book>> readsPrice(@PathVariable Float price) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(bookRepository.readsPriceMoreThan(price));
    }
    @PostMapping(value = "/create")
    private ResponseEntity<Map<String,List<Book>>> create(@RequestBody Book book) {
        log.info("Creating new book: {}", book);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(bookRepository.create(book));
    }
    @PutMapping(value = "/update/{id}")
    private ResponseEntity<Map<String,Integer>> update(@PathVariable Long id , @RequestBody Book book) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(bookRepository.update(book,id));
    }

    @DeleteMapping(value = "/delete/{id}")
    private ResponseEntity<Map<String,Integer>> delete(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(bookRepository.delete(id));
    }
}
