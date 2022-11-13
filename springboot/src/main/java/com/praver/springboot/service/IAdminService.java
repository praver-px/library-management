package com.praver.springboot.service;

import com.github.pagehelper.PageInfo;
import com.praver.springboot.controller.dto.LoginDTO;
import com.praver.springboot.controller.request.BaseRequest;
import com.praver.springboot.controller.request.LoginRequest;
import com.praver.springboot.entity.Admin;
import com.praver.springboot.entity.User;

import java.util.List;

public interface IAdminService {

    List<Admin> list();

    PageInfo<Admin> page(BaseRequest baseRequest);

    int save(Admin admin);

    Admin getById(Integer id);

    int update(Admin admin);

    int deleteByID(Integer id);


    int deleteList(int[] idList);

    LoginDTO login(LoginRequest request);
}
