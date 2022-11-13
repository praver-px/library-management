package com.praver.springboot.controller;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.praver.springboot.common.Result;
import com.praver.springboot.controller.request.UserPageRequest;
import com.praver.springboot.entity.Admin;
import com.praver.springboot.entity.BaseEntity;
import com.praver.springboot.entity.User;
import com.praver.springboot.exception.ServiceException;
import com.praver.springboot.service.IUseService;
import com.praver.springboot.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;


@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    IUseService useService;


    @GetMapping("/list")
    public Result list() {
        return Result.success(useService.list());
    }

    @GetMapping("/page")
    public Result page(UserPageRequest userPageRequest) {
        return Result.success(useService.page(userPageRequest));
    }

    @PostMapping("/save")
    public Result save(@RequestBody User user) {
        return Result.success(useService.save(user));
    }

    @PostMapping("/account")
    public Result account(@RequestBody User user) {
        return Result.success(useService.handleAccount(user));
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        return Result.success(useService.getById(id));
    }

    @PutMapping("/update")
    public Result update(@RequestBody User user) {
        return Result.success(useService.update(user));
    }

    @DeleteMapping("/delete/{id}")
    public Result deleteByID(@PathVariable Integer id) {
        return Result.success(useService.deleteByID(id));
    }

    @DeleteMapping("/deleteList/{ids}")
    public Result deleteList(@PathVariable String ids) {
        String[] split = ids.split(",");
        int[] idList = Arrays.stream(split).mapToInt(Integer::parseInt).toArray();
        return Result.success(useService.deleteList(idList));
    }
}
