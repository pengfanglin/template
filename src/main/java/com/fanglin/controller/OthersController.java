package com.fanglin.controller;

import com.fanglin.core.others.Ajax;
import com.fanglin.service.OthersService;
import com.fanglin.service.SystemService;
import com.fanglin.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 其他接口控制器
 *
 * @author 彭方林
 * @version 1.0
 * @date 2019/4/3 16:36
 **/
@RestController
@RequestMapping("/others/")
@Api(value = "/common/", tags = {"app", "基本服务"})
public class OthersController {

    @Autowired
    OthersService othersService;
    @Autowired
    SystemService systemService;

    @ApiOperation("上传多个文件")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "files", value = "图片文件", required = true),
        @ApiImplicitParam(name = "small", value = "是否生成缩略图", defaultValue = "false"),
        @ApiImplicitParam(name = "path", value = "保存路径", defaultValue = "/files/others")
    })
    @PostMapping("uploadFiles")
    public Ajax uploadFiles(@RequestParam("file") MultipartFile[] files, Boolean small, String path) {
        return Ajax.ok(OthersUtils.uploadFiles(files, small, path));
    }

    @ApiOperation("发送验证码")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "mobile", value = "手机号", required = true),
    })
    @PostMapping("sendCode")
    public Ajax sendCode(@RequestParam String mobile) {
        return othersService.sendCode(mobile);
    }
}
