package com.praver.springboot.controller;

import cn.hutool.core.collection.CollUtil;
import com.praver.springboot.common.Result;
import com.praver.springboot.controller.request.CategoryPageRequest;
import com.praver.springboot.entity.Book;
import com.praver.springboot.entity.Category;
import com.praver.springboot.service.ICategoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Resource
    ICategoryService categoryService;


    @GetMapping("/list")
    public Result list() {
        return Result.success(categoryService.list());
    }

    @GetMapping("/tree")
    public Result tree() {
        List<Category> list = categoryService.list();

//        List<Category> treeList = list.stream().filter(v -> v.getPid() == null).collect(Collectors.toList());
        return Result.success(createTree(null,list));
    }

    //    完全递归的方法来实现递归树
    private List<Category> createTree(Integer pid, List<Category> categories) {
        List<Category> treeList = new ArrayList<>();
        for (Category category : categories) {
            if (pid == null) {
                if (category.getPid() == null) {  // 那这就是第一级节点
                    treeList.add(category);
                    category.setChildren(createTree(category.getId(), categories));
                }
            } else {
                if (pid.equals(category.getPid())) {
                    treeList.add(category);
                    category.setChildren(createTree(category.getId(), categories));
                }
            }
            if (CollUtil.isEmpty(category.getChildren())) {
                category.setChildren(null);
            }
        }
        return treeList;
    }

    @GetMapping("/page")
    public Result page(CategoryPageRequest CategoryPageRequest) {
        return Result.success(categoryService.page(CategoryPageRequest));
    }

    @PostMapping("/save")
    public Result save(@RequestBody Category Category) {
        return Result.success(categoryService.save(Category));
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        return Result.success(categoryService.getById(id));
    }

    @PutMapping("/update")
    public Result update(@RequestBody Category Category) {
        return Result.success(categoryService.update(Category));
    }

    @DeleteMapping("/delete/{id}")
    public Result deleteByID(@PathVariable Integer id) {
        return Result.success(categoryService.deleteByID(id));
    }

    @DeleteMapping("/deleteList/{ids}")
    public Result deleteList(@PathVariable String ids) {
        String[] split = ids.split(",");
        int[] idList = Arrays.stream(split).mapToInt(Integer::parseInt).toArray();
        return Result.success(categoryService.deleteList(idList));
    }
}
