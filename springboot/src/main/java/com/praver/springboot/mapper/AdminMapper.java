package com.praver.springboot.mapper;

import com.praver.springboot.controller.dto.LoginDTO;
import com.praver.springboot.controller.request.AdminPageRequest;
import com.praver.springboot.controller.request.BaseRequest;
import com.praver.springboot.controller.request.LoginRequest;
import com.praver.springboot.entity.Admin;
import com.praver.springboot.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminMapper {
    List<Admin> list();

    List<Admin> listByCondition(BaseRequest baseRequest);

    int save(Admin admin);

    Admin getById(Integer id);

    int updateById(Admin admin);

    int deleteById(Integer id);

    int deleteList(int[] idList);

    Admin getForLogin(LoginRequest request);

    Admin getByUsername(String username);
}
