package com.ttknpdev.crud.springbootjdbccrudposgresql.service;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.ttknpdev.crud.springbootjdbccrudposgresql.model.Book;
import com.ttknpdev.crud.springbootjdbccrudposgresql.repository.BookRepositories;
import com.ttknpdev.crud.springbootjdbccrudposgresql.service.sql.Sql;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class BookService implements BookRepositories<Book> {

    private JdbcTemplate jdbcTemplate;
    private Sql sql = new Sql();
    @Autowired
    public BookService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Map<String, List<Book>> create(Book obj) {
        Map<String,List<Book>> response = new HashMap<>();
        List<Book> books = new ArrayList<>();
        int row = jdbcTemplate.update(sql.getCREATE()
                ,obj.getBookName()
                ,obj.getBookMFD()
                ,obj.getBookPrice()
                ,obj.getBookSale()
                ,obj.getBookReviews());
        if (row > 0) {
            /* lambda style one */
             jdbcTemplate.query(sql.getREADS(),(rs,rowNum) -> {
                 Book book = new Book();
                 book.setBookId(rs.getLong("book_id"));
                 book.setBookName(rs.getString("book_name"));
                 book.setBookMFD(LocalDate.parse(rs.getString("book_mfd")));
                 book.setBookPrice(rs.getFloat("book_price"));
                 book.setBookSale(rs.getInt("book_sale"));
                 book.setBookReviews(new String[]{rs.getString("book_reviews")});
                 /* in this statement it'll loop following rows */
                 books.add(book);
                 return book;
             });
            response.put("created",books);
        }
        return response;
    }

    @Override
    public List<Book> reads() {
        List<Book> books = new ArrayList<>();
        /* lambda style second */
       books.addAll(jdbcTemplate.query(sql.getREADS(), (rs, rowNum) -> {
            Book book = new Book();
            book.setBookId(rs.getLong("book_id"));
            book.setBookName(rs.getString("book_name"));
            book.setBookMFD(LocalDate.parse(rs.getString("book_mfd")));
            book.setBookPrice(rs.getFloat("book_price"));
            book.setBookSale(rs.getInt("book_sale"));
            book.setBookReviews(new String[]{rs.getString("book_reviews")});
            /*
                The JDBC driver returns composite types as strings.
                There is no feature to register any conversion from composite types to Java beans
                Clear !
            */
            return book;

        }));
        // System.out.println(books);
        return books;
    }

    @Override
    public List<Book> readsPriceMoreThan(Float price) {
        /** New knowledge */
        List< Map<String, Object> > list = jdbcTemplate.queryForList(sql.getREAD_PRICE_MORE_THAN(),price);
        /** using queryForList() it will return Map <String (name field table) , Object(value)>  */
        /*       Type String store real name field table
        *        Type Object store the value of field
        *        Like this when log
        *        return field of table (real name table)
        *        [{ book_id=1, book_name=Java + Spring framework 2023,...   */
        List<Book> books = new ArrayList<>();
        list.forEach(m -> {

            Book book = new Book();
            // book.setBookId((Long) m.get("book_id")); // can't covert by (Long)
            book.setBookId( Long.valueOf (m.get("book_id").toString()) ); // take this

            // book.setBookReviews((String []) m.get("book_reviews")); // can't covert by (String [])
            book.setBookReviews( new String[]{m.get("book_reviews").toString()} ); // take this

            // book.setBookMFD((LocalDate) m.get("book_mfd")); // can't covert by (LocalDate)
            book.setBookMFD( LocalDate.parse(m.get("book_mfd").toString()) ); // take this

            book.setBookName((String) m.get("book_name"));
            book.setBookSale((Integer) m.get("book_sale"));
            book.setBookPrice((Float) m.get("book_price"));
            books.add(book);

        });
        return books;
    }

    @Override
    public Book read(Long id) {
        return jdbcTemplate.queryForObject(
                sql.getREAD() //direct sql
                , new Object[]{id} // parameter
                , (rs, rowNum) -> //
                        new Book(
                                rs.getLong("book_id"),
                                rs.getString("book_name"),
                                (LocalDate.parse(rs.getString("book_mfd"))),
                                rs.getFloat("book_price"),
                                rs.getInt("book_sale"),
                                (new String[]{rs.getString("book_reviews")})
                        ));
    }

    @Override
    public Map<String, Integer> update(Book obj, Long id) {
        Map<String,Integer> response = new HashMap<>();
        Integer row = jdbcTemplate.update(sql.getUPDATE()
                ,obj.getBookName()
                ,obj.getBookMFD()
                ,obj.getBookPrice()
                ,obj.getBookSale()
                ,obj.getBookReviews()
                ,id);
        if (row > 0) {
            response.put("updated",row);
        }
        else {
            response.put("update failed",row);
        }
        return response;
    }

    @Override
    public Map<String, Integer> delete(Long id) {
        Map<String,Integer> response = new HashMap<>();
        Integer row = jdbcTemplate.update(sql.getDELETE(),id);
        if (row > 0) {
            response.put("deleted",row);
        }
        else {
            response.put("delete failed",row);
        }
        return response;
    }
}
