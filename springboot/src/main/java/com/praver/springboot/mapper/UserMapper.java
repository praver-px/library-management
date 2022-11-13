package com.praver.springboot.mapper;

import com.praver.springboot.controller.request.BaseRequest;
import com.praver.springboot.controller.request.UserPageRequest;
import com.praver.springboot.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    List<User> list();

    List<User> listByCondition(BaseRequest baseRequest);

    int save(User user);

    User getById(Integer id);

    int updateById(User user);

    int deleteById(Integer id);

    int deleteList(int[] idList);

    User getByUsername(String username);

}
