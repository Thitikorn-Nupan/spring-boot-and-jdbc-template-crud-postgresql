package com.ttknpdev.crud.springbootjdbccrudposgresql.service.sql;

import lombok.Getter;

@Getter
public class Sql {

    private final String CREATE = "insert into public.books(book_name,book_mfd,book_price,book_sale,book_reviews) values(?,?,?,?,?); ";
    private final String UPDATE = "update public.books set book_name = ? , book_mfd = ? , book_price = ? , book_sale = ? , book_reviews =?  where book_id = ? ;";
    private final String DELETE = "delete from public.books where book_id = ?;";
    private final String READS = "select * from public.books ; ";
    private final String READ = "select * from public.books where book_id = ? ;";
    private final String READ_PRICE_MORE_THAN = "select * from public.books where book_price > ?;";

}
