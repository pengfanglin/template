package com.fanglin.controller;

import com.fanglin.utils.TimeUtils;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.util.Date;

/**
 * 基本Controller
 * @author 彭方林
 * @date 2019/4/3 16:30
 * @version 1.0
 **/
public class BaseController {
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(TimeUtils.getSimpleDateFormat(), true));
    }
}