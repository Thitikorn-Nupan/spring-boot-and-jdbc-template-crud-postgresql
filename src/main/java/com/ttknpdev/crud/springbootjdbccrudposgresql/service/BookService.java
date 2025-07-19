package com.ttknpdev.crud.springbootjdbccrudposgresql.service;

import com.ttknpdev.crud.springbootjdbccrudposgresql.model.Book;
import com.ttknpdev.crud.springbootjdbccrudposgresql.repository.BookRepository;
import com.ttknpdev.crud.springbootjdbccrudposgresql.service.sql.Sql;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class BookService implements BookRepository<Book> {

    private final JdbcTemplate jdbcTemplate;
    private final Sql sql;

    @Autowired
    public BookService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.sql = new Sql();
    }


    @Override
    public Map<String, List<Book>> create(Book obj) {
        Map<String, List<Book>> response = new HashMap<>();
        List<Book> books = new ArrayList<>();
        int row = jdbcTemplate.update(sql.getCREATE()
                , obj.getBookName()
                , obj.getBookMFD()
                , obj.getBookPrice()
                , obj.getBookSale()
                , obj.getBookReviews());
        if (row > 0) {
            // lambda style one
            jdbcTemplate.query(sql.getREADS(), (rs, rowNum) -> {
                Book book = new Book();
                book.setBookId(rs.getLong("book_id"));
                book.setBookName(rs.getString("book_name"));
                book.setBookMFD(LocalDate.parse(rs.getString("book_mfd")));
                book.setBookPrice(rs.getFloat("book_price"));
                book.setBookSale(rs.getInt("book_sale"));
                book.setBookReviews(convertStringToArray(rs.getString("book_reviews")));
                // in this statement it'll loop following rows
                books.add(book);
                return book;
            });
            response.put("created", books);
        }
        return response;
    }

    @Override
    public List<Book> reads() {
        List<Book> books = new ArrayList<>();
        // lambda style
        books.addAll(jdbcTemplate.query(sql.getREADS(), (rs, rowNum) -> {
            Book book = new Book();
            book.setBookId(rs.getLong("book_id"));
            book.setBookName(rs.getString("book_name"));
            book.setBookMFD(LocalDate.parse(rs.getString("book_mfd")));
            book.setBookPrice(rs.getFloat("book_price"));
            book.setBookSale(rs.getInt("book_sale"));
            String[] reviews = convertStringToArray(rs.getString("book_reviews"));
            book.setBookReviews(reviews);
            /*
                The JDBC driver returns composite types as strings.
                There is no feature to register any conversion from composite types to Java beans
            */
            return book;

        }));
        // System.out.println(books);
        return books;
    }

    @Override
    public List<Book> readsPriceMoreThan(Float price) {
        // New knowledge
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.getREAD_PRICE_MORE_THAN(), price);
        // using queryForList() it will return Map <String (name field table) , Object(value)>
        List<Book> books = new ArrayList<>();
        list.forEach(m -> {
            Book book = new Book();
            book.setBookId(Long.valueOf(m.get("book_id").toString())); // take this
            book.setBookMFD(LocalDate.parse(m.get("book_mfd").toString())); // take this
            book.setBookName((String) m.get("book_name"));
            book.setBookSale((Integer) m.get("book_sale"));
            book.setBookPrice((Float) m.get("book_price"));
            book.setBookReviews(convertStringToArray((String) m.get("book_reviews")));
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
                                convertStringToArray(rs.getString("book_reviews"))
                        ));
    }

    @Override
    public Map<String, Integer> update(Book obj, Long id) {
        Map<String, Integer> response = new HashMap<>();
        Integer row = jdbcTemplate.update(sql.getUPDATE()
                , obj.getBookName()
                , obj.getBookMFD()
                , obj.getBookPrice()
                , obj.getBookSale()
                , obj.getBookReviews()
                , id);
        if (row > 0) {
            response.put("updated", row);
        } else {
            response.put("update failed", row);
        }
        return response;
    }

    @Override
    public Map<String, Integer> delete(Long id) {
        Map<String, Integer> response = new HashMap<>();
        Integer row = jdbcTemplate.update(sql.getDELETE(), id);
        if (row > 0) {
            response.put("deleted", row);
        } else {
            response.put("delete failed", row);
        }
        return response;
    }


    private String[] convertStringToArray(String inputString) {
        // Step 1: Remove the outer curly braces
        // String cleanedString = inputString.substring(1, inputString.length() - 1);

        // Step 2: Split by the comma and handle quotes
        // This regex looks for a comma followed by a double quote, then captures everything until the next double quote.
        // Or, more simply, we can just split by "," and then clean up each element.
        // Let's go with cleaning up each element for simplicity.

        // First, handle the escaped double quotes in the original string (if they are truly part of the string)
        // Your example has {"text"}, if it was like "{\"text\"}" then this step would be crucial.
        // Assuming your input is exactly `{"A...", "B...", "C..."}`
        // and not `"{\"A...\"}"`, which is a common confusion.
        // If your string literally looks like `{"A hilarious romp through space!","Douglas Adams at his best."}`
        // then the individual strings within are not actually quoted, but just contain quotes.
        // Let's assume the quotes around each phrase are part of the structure you want to remove.

        // Refined approach: split by `","` (comma followed by double quote)
        // and then remove leading/trailing quotes from each part.

        // Remove the outer curly braces
        // have to 2 because it's weird "
        String content = inputString.substring(2, inputString.length() - 2);

        // Split by the pattern ","
        // This regex splits on a comma that is followed by a double quote.
        // The `(?=")` is a positive lookahead, meaning it asserts that what follows is a double quote, but doesn't consume it.
        // This is important so the double quote stays with the next element for stripping.
        String[] tempArray = content.split("\",\""); // Split by ","

        // Process each element to remove leading/trailing quotes
        String[] resultArray = new String[tempArray.length];
        for (int i = 0; i < tempArray.length; i++) {
            String element = tempArray[i];
            // Remove the first and last double quote if they exist
            if (element.startsWith("\"") && element.endsWith("\"")) {
                resultArray[i] = element.substring(1, element.length() - 1);
            } else {
                resultArray[i] = element; // Should not happen with your input format
            }
        }
        return resultArray;

    }
}
