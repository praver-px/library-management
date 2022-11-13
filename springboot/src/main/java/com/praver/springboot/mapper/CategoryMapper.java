package com.praver.springboot.mapper;

import com.praver.springboot.controller.request.BaseRequest;
import com.praver.springboot.entity.Category;
import com.praver.springboot.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {

    List<Category> list();

    List<Category> listByCondition(BaseRequest baseRequest);

    int save(Category category);

    Category getById(Integer id);

    int updateById(Category category);

    int deleteById(Integer id);


    int deleteList(int[] idList);
}
