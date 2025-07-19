package com.ttknpdev.crud.springbootjdbccrudposgresql.business;

import com.ttknpdev.crud.springbootjdbccrudposgresql.model.Book;
import com.ttknpdev.crud.springbootjdbccrudposgresql.repository.BookRepository;
import com.ttknpdev.crud.springbootjdbccrudposgresql.service.BookService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.*;

import java.time.LocalDate;
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@JdbcTest
@Rollback(value = false)
public class MyBusiness {

    private BookRepository bookService;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    public void setBookService() {
        bookService = new BookService(jdbcTemplate);
    }
    @Test
    public void  create() {
        Book bookA = new Book();
        bookA.setBookName("Java + Spring framework 2023");
        bookA.setBookSale(1600);
        bookA.setBookMFD(LocalDate.now());
        bookA.setBookPrice(699.99F);
        bookA.setBookReviews(new String[]{"Very good book!","Great! , I'll buy more","This book's so amazing"});
        assertNotNull(bookService.create(bookA));
    }
    @Test
    public void update() {
        /*
        {
            "bookId": 4,
            "bookName": "Java Core 2020",
            "bookMFD": "2023-06-04",
            "bookPrice": 500.5,
            "bookSale": 1100,
            "bookReviews": [
                "{\"I really love it.\",\"So do I!\",\"Help me so much.\"}"
            ]
        }
        */
        Book bookA = new Book();
        bookA.setBookName("Java Core 2020");
        bookA.setBookSale(1100);
        bookA.setBookMFD(LocalDate.now());
        bookA.setBookPrice(500.5F);
        bookA.setBookReviews(new String[]{"I really love it.","So do I. and take less time to learn","Help me so much."});
        assertNotNull(bookService.update(bookA,4L).get("updated"));

    }
    @Test
    public void read() {
        assertNotNull(bookService.read(4L));
    }
}
