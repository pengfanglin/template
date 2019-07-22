package com.fanglin.core.others;

import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * 全局异常捕获
 *
 * @author 彭方林
 * @version 1.0
 * @date 2019/4/3 16:35
 **/
@Slf4j
@ControllerAdvice
public class MyExceptionHandler {
    /**
     * 必传参数未传异常
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public Ajax handlerMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        return Ajax.error("【" + e.getParameterName() + "】必传，参数类型:【" + e.getParameterType() + "】");
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public Ajax handleValidateException(BusinessException e) {
        return Ajax.error(e.getMessage());
    }

    /**
     * 数据库异常
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseBody
    public Ajax handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        MysqlDataTruncation mysqlDataTruncation = (MysqlDataTruncation) e.getCause();
        String error;
        //获取sql状态码
        String sqlState = mysqlDataTruncation.getSQLState();
        if ("22001".equals(sqlState)) {
            error = "长度超出";
        } else {
            error = "参数非法";
        }
        return Ajax.error(error);
    }

    /**
     * 请求方式不支持
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public Ajax httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return Ajax.error(e.getMethod() + "不支持");
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public Ajax handleBindException(BindException e) {
        //获取异常内容
        FieldError fieldError = e.getFieldError();
        Class clazz = e.getFieldType(fieldError.getField());
        String hopeType;
        switch (clazz.getSimpleName()) {
            case "Integer":
            case "int":
            case "Long":
            case "long":
                hopeType = "整数";
                break;
            case "Boolean":
            case "boolean":
                hopeType = "布尔类型";
                break;
            case "Double":
            case "double":
            case "Float":
            case "float":
                hopeType = "小数";
                break;
            case "Date":
                hopeType = "日期";
                break;
            default:
                hopeType = clazz.getSimpleName();
                break;
        }
        //用户输入的内容
        Object input = fieldError.getRejectedValue();
        return Ajax.error("【" + input.toString() + "】格式非法,请输入【" + hopeType + "】");
    }

    /**
     * 默认异常处理
     *
     * @return
     */
    @ExceptionHandler
    @ResponseBody
    public Ajax handleException(Exception e) {
        log.warn(e.getMessage());
        //提取错误信息
        String error;
        if (e.getCause() != null) {
            error = e.getCause().getMessage() == null ? "空指针异常" : e.getCause().getMessage();
        } else {
            error = e.getMessage() == null ? "空指针异常" : e.getMessage();
        }
        return Ajax.error(error);
    }

    @ExceptionHandler
    @ResponseBody
    public Ajax exception(MethodArgumentTypeMismatchException e) {
        return Ajax.error(String.format("方法参数类型不匹配,参数名[%s],类型[%s]", e.getName(), e.getParameter().getParameterType().getSimpleName()));
    }

    @ExceptionHandler
    @ResponseBody
    public Ajax exception(MissingRequestHeaderException e) {
        return Ajax.error(String.format("请求头缺少参数,参数名[%s],类型[%s]", e.getHeaderName(), e.getParameter().getParameterType().getSimpleName()));
    }
}
