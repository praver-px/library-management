package com.praver.springboot.service;

import com.github.pagehelper.PageInfo;
import com.praver.springboot.controller.request.BaseRequest;
import com.praver.springboot.entity.Admin;
import com.praver.springboot.entity.Category;

import java.util.List;

public interface ICategoryService {
    List<Category> list();

    PageInfo<Category> page(BaseRequest baseRequest);

    int save(Category category);

    Category getById(Integer id);

    int update(Category category);

    int deleteByID(Integer id);

    int deleteList(int[] idList);
}
