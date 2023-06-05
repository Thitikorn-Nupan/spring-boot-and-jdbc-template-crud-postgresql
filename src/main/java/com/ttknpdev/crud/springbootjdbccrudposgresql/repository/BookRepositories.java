package com.ttknpdev.crud.springbootjdbccrudposgresql.repository;

import java.util.List;
import java.util.Map;

public interface BookRepositories<T> {
    Map<String,List<T>> create(T obj);
    List<T> reads();
    List<T> readsPriceMoreThan(Float income);
    T read(Long id);
    Map<String,Integer> update(T obj, Long id);
    Map<String,Integer> delete(Long id);

}
