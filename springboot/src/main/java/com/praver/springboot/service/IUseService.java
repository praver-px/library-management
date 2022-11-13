package com.praver.springboot.service;

import com.github.pagehelper.PageInfo;
import com.praver.springboot.controller.request.UserPageRequest;
import com.praver.springboot.entity.User;

import java.util.List;

public interface IUseService {

    List<User> list();

    PageInfo<User> page(UserPageRequest userPageRequest);

    int save(User user);

    User getById(Integer id);

    int update(User user);

    int deleteByID(Integer id);


    int deleteList(int[] idList);

    int handleAccount(User user);
}
