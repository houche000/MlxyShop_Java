package com.zbkj.admin.controller;

import com.zbkj.admin.service.WeChatMiniCodeDownloadService;
import com.zbkj.common.response.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 微信小程序源码下载 前端控制器
 */
@Slf4j
@RestController
@RequestMapping("api/admin/wechat/code")
@Api(tags = "微信开放平台 -- 微信小程序源码下载")
public class WeChatMiniCodeDownloadController {


    @Autowired
    WeChatMiniCodeDownloadService weChatMiniCodeDownloadService;

    /**
     * 分页显示微信关键字回复表
     */
    @PreAuthorize("hasAuthority('admin:wechat:code:download')")
    @ApiOperation(value = "小程序源码下载")
    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public CommonResult<String>  getList() {
        String miniCodeSourcePath = weChatMiniCodeDownloadService.WeChatMiniCodeDownload();
        return CommonResult.success(miniCodeSourcePath,"下载微信小程序代码成功");
    }
}
