package com.praver.springboot.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.praver.springboot.controller.dto.LoginDTO;
import com.praver.springboot.controller.request.BaseRequest;
import com.praver.springboot.controller.request.LoginRequest;
import com.praver.springboot.entity.Admin;
import com.praver.springboot.entity.User;
import com.praver.springboot.exception.ExceptionHandle;
import com.praver.springboot.exception.ServiceException;
import com.praver.springboot.mapper.AdminMapper;
import com.praver.springboot.service.IAdminService;
import com.praver.springboot.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class AdminServiceImpl implements IAdminService {

    private static final String DEFAULT_PASS = "123456";
    private static final String PASS_SALT = "Praver";

    @Resource
    AdminMapper adminMapper;

    @Override
    public List<Admin> list() {
        return adminMapper.list();
    }

    @Override
    public PageInfo<Admin> page(BaseRequest baseRequest) {
        PageHelper.startPage(baseRequest.getPageNum(), baseRequest.getPageSize());
        List<Admin> admins = adminMapper.listByCondition(baseRequest);
        return new PageInfo<>(admins);

    }

    @Override
    public int save(Admin admin) {
        if (StrUtil.isBlank(admin.getPassword())){
            admin.setPassword(DEFAULT_PASS);
        }
        admin.setPassword(securePass(admin.getPassword()));
        try {
            return adminMapper.save(admin);
        }catch (DuplicateKeyException e){
            log.error("?????????????????????username{}",admin.getUsername(),e);
            throw new ServiceException("?????????????????? ");
        }
    }

    @Override
    public Admin getById(Integer id) {
        return adminMapper.getById(id);
    }

    @Transactional
    public int update(Admin admin) {
        if (admin == null){
            throw  new ServiceException("????????????????????????!");
        }
        if (StrUtil.isBlank(admin.getPassword())){
            admin.setPassword(DEFAULT_PASS);
        }
        admin.setPassword(securePass(admin.getPassword()));
        admin.setUpdatetime(new Date());
        return adminMapper.updateById(admin);
    }

    @Override
    public int deleteByID(Integer id) {
        return adminMapper.deleteById(id);
    }

    @Override
    public int deleteList(int[] idList) {
        return adminMapper.deleteList(idList);
    }

    @Override
    public LoginDTO login(LoginRequest request) {
//        Admin admin = adminMapper.getForLogin(request);
        Admin admin = null;
        try{
             admin = adminMapper.getByUsername(request.getUsername());
        }catch (Exception e){
            log.error("???????????????{}????????????",request.getUsername());
            throw new ServiceException("??????????????????");
        }
        if (admin == null){
            throw  new ServiceException("????????????????????????!");
        }
        String securePass = securePass(request.getPassword());
        if (! securePass.equals(admin.getPassword())){
            throw  new ServiceException("????????????????????????!");
        }
        if(!admin.isStatus()){
            throw  new ServiceException("?????????????????????!");
        }
        LoginDTO loginDTO= new LoginDTO();
        BeanUtils.copyProperties(admin,loginDTO);

        String token = TokenUtils.genToken(String.valueOf(admin.getId()), admin.getPassword());
        loginDTO.setToken(token);
        return loginDTO;

    }

//    ????????????
    private String securePass(String Password){
        return SecureUtil.md5(Password + PASS_SALT);
    }

}
