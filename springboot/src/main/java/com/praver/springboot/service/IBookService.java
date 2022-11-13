package com.praver.springboot.service;

import com.github.pagehelper.PageInfo;
import com.praver.springboot.controller.request.BaseRequest;
import com.praver.springboot.entity.Book;

import java.util.List;

public interface IBookService {
    List<Book> list();

    PageInfo<Book> page(BaseRequest baseRequest);

    int save(Book obj);

    Book getById(Integer id);

    int update(Book obj);

    int deleteById(Integer id);

    int deleteList(int[] idList);
}
