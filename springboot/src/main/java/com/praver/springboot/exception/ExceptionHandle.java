package com.praver.springboot.exception;


import cn.hutool.core.util.StrUtil;
import com.praver.springboot.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice
public class ExceptionHandle {

    @ExceptionHandler(value = ServiceException.class)
    public Result serviceExceptionError(ServiceException e){
        log.error("业务错误",e);
        String code = e.getCode();
        if (StrUtil.isNotBlank(code)){
            return Result.error(code,e.getMessage());
        }
        return Result.error(e.getMessage() );
    }

    @ExceptionHandler(value = Exception.class)
    public Result exceptionError(Exception e){
        log.error("系统错误",e);
       return Result.error("系统错误");
    }
}
