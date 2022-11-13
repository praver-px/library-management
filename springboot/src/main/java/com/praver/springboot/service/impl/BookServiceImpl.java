package com.praver.springboot.service.impl;


import cn.hutool.core.collection.CollUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.praver.springboot.controller.request.BaseRequest;
import com.praver.springboot.entity.Book;
import com.praver.springboot.entity.Borrow;
import com.praver.springboot.exception.ServiceException;
import com.praver.springboot.mapper.BookMapper;
import com.praver.springboot.mapper.BorrowMapper;
import com.praver.springboot.service.IBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class BookServiceImpl implements IBookService {

    @Resource
    BookMapper bookMapper;

    @Resource
    BorrowMapper borrowMapper;

    @Override
    public List<Book> list() {
        return bookMapper.list();
    }

    @Override
    public PageInfo<Book> page(BaseRequest baseRequest) {
        PageHelper.startPage(baseRequest.getPageNum(), baseRequest.getPageSize());
        return new PageInfo<>(bookMapper.listByCondition(baseRequest));
    }

    @Override
    public int save(Book book) {
        try {
            book.setCategory(category(book.getCategories()));
            return bookMapper.save(book);
        }catch (Exception e){
            throw new ServiceException("数据插入错误",e);
        }
    }

    @Override
    public Book getById(Integer id) {
        return bookMapper.getById(id);
    }

    @Override
    public int update(Book book) {
        return bookMapper.updateById(book);
    }

    @Override
    public int deleteById(Integer id) {
        Book book = bookMapper.getById(id);
        List<Borrow> borrowList = borrowMapper.getByBookNo(book.getBookNo());
        for (Borrow borrow : borrowList) {
            if (!(borrow.getStatus()).equals("已归还")){
                throw new  ServiceException("该图书还未归还");
            }
        }
        return bookMapper.deleteById(id);
    }

    @Override
    public int deleteList(int[] idList) {
        for (int id : idList) {
            Book book = bookMapper.getById(id);
            List<Borrow> borrowList = borrowMapper.getByBookNo(book.getBookNo());
            for (Borrow borrow : borrowList) {
                if (!(borrow.getStatus()).equals("已归还")){
                    throw new  ServiceException("存在图书还未归还");
                }
            }
        }
        return bookMapper.deleteList(idList);
    }

    private String category(List<String> categories) {
        StringBuilder sb = new StringBuilder();
        if (CollUtil.isNotEmpty(categories)) {
            categories.forEach(v -> sb.append(v).append(" > "));
            return sb.substring(0, sb.lastIndexOf(" > "));
        }
        return sb.toString();
    }
}
