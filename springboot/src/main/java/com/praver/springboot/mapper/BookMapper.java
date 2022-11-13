package com.praver.springboot.mapper;

import com.praver.springboot.controller.request.BaseRequest;
import com.praver.springboot.entity.Book;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BookMapper {

    List<Book> list();

    List<Book> listByCondition(BaseRequest baseRequest);

    int save(Book obj);

    Book getById(Integer id);

    int updateById(Book user);

    int deleteById(Integer id);

    Book getByNo(String bookNo);

    int updateNumByNo(String bookNo);

    int deleteList(int[] idList);
}
