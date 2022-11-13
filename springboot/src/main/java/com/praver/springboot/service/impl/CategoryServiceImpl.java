package com.praver.springboot.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.praver.springboot.controller.request.BaseRequest;
import com.praver.springboot.entity.Category;
import com.praver.springboot.mapper.CategoryMapper;
import com.praver.springboot.service.ICategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;


@Service
@Slf4j
public class CategoryServiceImpl implements ICategoryService {

    @Resource
    CategoryMapper categoryMapper;


    @Override
    public List<Category> list() {
        return categoryMapper.list();
    }

    @Override
    public PageInfo<Category> page(BaseRequest baseRequest) {
        PageHelper.startPage(baseRequest.getPageNum(),baseRequest.getPageSize());
        return new PageInfo<>(categoryMapper.listByCondition(baseRequest));
    }

    @Override
    public int save(Category category) {
        return categoryMapper.save(category);
    }

    @Override
    public Category getById(Integer id) {
        return categoryMapper.getById(id);
    }

    @Override
    public int update(Category category) {
        category.setUpdatetime(LocalDate.now());
        return categoryMapper.updateById(category);
    }

    @Override
    public int deleteByID(Integer id) {
        return categoryMapper.deleteById(id);
    }

    @Override
    public int deleteList(int[] idList) {
        return categoryMapper.deleteList(idList);
    }
}
