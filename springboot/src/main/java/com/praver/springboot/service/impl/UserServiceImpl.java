package com.praver.springboot.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.praver.springboot.controller.request.UserPageRequest;
import com.praver.springboot.entity.User;
import com.praver.springboot.mapper.UserMapper;
import com.praver.springboot.service.IUseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements IUseService {

    @Resource
    UserMapper userMapper;

    @Override
    public int handleAccount(User user) {
        Integer score = user.getScore();
        if (score == null){
            return 0;
        }
        User dbUser = userMapper.getById(user.getId());
        dbUser.setAccount(dbUser.getAccount() + score);
        return userMapper.updateById(dbUser);
    }

    @Override
    public List<User> list() {
        return userMapper.list();
    }

    @Override
    public PageInfo<User> page(UserPageRequest userPageRequest) {
        PageHelper.startPage(userPageRequest.getPageNum(), userPageRequest.getPageSize());
        List<User> users = userMapper.listByCondition(userPageRequest);
        return new PageInfo<>(users);

    }

    @Override
    public int save(User user) {
        Date date = new Date();
        user.setUsername(DateUtil.format(date, "yyMMdd") + Math.abs(IdUtil.fastSimpleUUID().hashCode()));
        return userMapper.save(user);
    }

    @Override
    public User getById(Integer id) {
        return userMapper.getById(id);
    }

    @Override
    public int update(User user) {
        user.setUpdatetime(new Date());
        return userMapper.updateById(user);
    }

    @Override
    public int deleteByID(Integer id) {
        return userMapper.deleteById(id);
    }

    @Override
    public int deleteList(int[] idList) {
        return userMapper.deleteList(idList);
    }


}
