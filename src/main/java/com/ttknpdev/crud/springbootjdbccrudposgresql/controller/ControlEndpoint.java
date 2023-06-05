package com.ttknpdev.crud.springbootjdbccrudposgresql.controller;

import com.ttknpdev.crud.springbootjdbccrudposgresql.model.Book;
import com.ttknpdev.crud.springbootjdbccrudposgresql.repository.BookRepositories;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/api/postgres")
public class ControlEndpoint {
    private BookRepositories bookRepositories;
    @Autowired
    public ControlEndpoint(BookRepositories bookRepositories) {
        this.bookRepositories = bookRepositories;
    }
    @GetMapping(value = "/reads")
    private ResponseEntity reads() {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(bookRepositories.reads());
    }
    @GetMapping(value = "/read/{id}")
    private ResponseEntity read(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(bookRepositories.read(id));
    }
    @GetMapping(value = "/reads-price/{price}")
    private ResponseEntity readsPrice(@PathVariable Float price) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(bookRepositories.readsPriceMoreThan(price));
    }
    @PostMapping(value = "/create")
    private ResponseEntity create(@RequestBody Book book) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(bookRepositories.create(book));
    }
    @PutMapping(value = "/update/{id}")
    private ResponseEntity update(@PathVariable Long id , @RequestBody Book book) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(bookRepositories.update(book,id));
    }
    @DeleteMapping(value = "/delete/{id}")
    private ResponseEntity delete(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(bookRepositories.delete(id));
    }
}
