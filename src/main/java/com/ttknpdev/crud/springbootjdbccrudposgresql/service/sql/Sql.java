package com.ttknpdev.crud.springbootjdbccrudposgresql.service.sql;

import lombok.Getter;
@Getter
public class Sql {

    private final String CREATE = "insert into schema_demo.books(book_name,book_mfd,book_price,book_sale,book_reviews) " +
            "values(?,?,?,?,?);";
    private final String READS = "select * from schema_demo.books ; ";
    private final String UPDATE = "update schema_demo.books set book_name = ? , book_mfd = ? , book_price = ? ," +
            " book_sale = ? , book_reviews =?  where book_id = ? ;";
    private final String DELETE = "delete from schema_dem.books where book_id = ?;";
    private final String READ_PRICE_MORE_THAN = "select * from schema_demo.books where book_price > ?;";
    private final String READ = "select * from schema_demo.books where book_id = ? ;";
}
