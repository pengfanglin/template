package com.fanglin.controller;

import com.fanglin.core.others.Ajax;
import com.fanglin.entity.others.CodeEntity;
import com.fanglin.enums.pay.TestEnum;
import com.fanglin.service.OthersService;
import com.fanglin.service.SystemService;
import com.fanglin.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

/**
 * 其他接口控制器
 *
 * @author 彭方林
 * @version 1.0
 * @date 2019/4/3 16:36
 **/
@RestController
@RequestMapping("/others/")
public class OthersController {

    @Autowired
    OthersService othersService;
    @Autowired
    SystemService systemService;

    /**
     * 获取微信授权
     *
     * @param url
     * @return
     */
    @PostMapping("getWxAuthorization")
    public Ajax getWXAuthorization(String url) {
        return Ajax.ok(WxUtils.getWXAuthorization(url));
    }

    /**
     * 获取html页面内容
     *
     * @param url
     * @return
     */
    @PostMapping("getHtmlContent")
    public Ajax getHtmlContent(String url) {
        String desc = OthersUtils.readHtml(url);
        int start = desc.indexOf("<content>");
        int end = desc.indexOf("</content>");
        if (start > 0 && end > 0) {
            desc = desc.substring(start + 9, end);
        }
        return Ajax.ok(desc);
    }

    /**
     * 上传多个文件
     *
     * @return
     */
    @PostMapping("uploadFiles")
    public Ajax uploadFiles(@RequestParam("file") MultipartFile[] files, Boolean small, String path) {
        return Ajax.ok(OthersUtils.uploadFiles(files, small, path));
    }

    /**
     * 发送验证码
     *
     * @param codeBean
     * @return
     */
    @PostMapping("sendCode")
    public Ajax sendCode(CodeEntity codeBean) {
        String code = OthersUtils.createRandom(4);
        if (SmsUtils.zhuTong(codeBean.getMobile(), code)) {
            Date create = new Date();
            codeBean.setCode(code)
                .setEffectiveTime(TimeUtils.getTimeMinuteAfter(create, 10))
                .setCreateTime(create);
            othersService.insertCode(codeBean);
            return Ajax.ok(code);
        } else {
            return Ajax.error("发送失败");
        }
    }
}
