package com.zbkj.admin.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.util.ZipUtil;
import com.zbkj.admin.service.WeChatMiniCodeDownloadService;
import com.zbkj.common.config.CrmebConfig;
import com.zbkj.common.constants.Constants;
import com.zbkj.common.constants.WeChatConstants;
import com.zbkj.common.exception.CrmebException;
import com.zbkj.service.service.SystemConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 小程序源码下载 服务实现类
 */
@Service
public class WeChatMiniCodeDownloadServiceImpl implements WeChatMiniCodeDownloadService {

    @Autowired
    private CrmebConfig crmebConfig;

    @Autowired
    private SystemConfigService systemConfigService;
    /**
     * 小程序源码下载
     *
     * @return 源码压缩包路径
     */
    @Override
    public String WeChatMiniCodeDownload() {

        String apiPath, adminApiPath, appPath, crmebName,crmebWeixinAppid;
        // 压缩后给出下载地址
        String miniCodeName = "/mp-weixin-target.zip";
        // 获取 需要运行微信小程序的必备配置
        crmebWeixinAppid = systemConfigService.getValueByKey(WeChatConstants.WECHAT_MINI_APPID);
        crmebName = systemConfigService.getValueByKey(WeChatConstants.WECHAT_MINI_NAME);
        apiPath = systemConfigService.getValueByKey(Constants.CONFIG_KEY_FRONT_API_URL);
        adminApiPath = systemConfigService.getValueByKey(Constants.CONFIG_KEY_API_URL);
        appPath = systemConfigService.getValueByKeyException(Constants.CONFIG_KEY_SITE_URL);
        if (StringUtils.isBlank(crmebWeixinAppid) || StringUtils.isBlank(crmebName)
        || StringUtils.isBlank(apiPath) || StringUtils.isBlank(appPath) || StringUtils.isBlank(adminApiPath)) {
            throw new CrmebException("应用设置中 微信小程序数据配置 或者 支付回调地址以及网站地址 配置不全");
        }
        // 解压新的源码用来修改配置后下载
        String baseCodePath = crmebConfig.getImagePath() + Constants.UPLOAD_TYPE_IMAGE;
        String sourceCodePath = baseCodePath + "/mp-weixin.zip";
        String WxMpCodePath = baseCodePath + "/mp-weixin/";
        if(!FileUtil.exist(sourceCodePath)){
            throw new CrmebException("源码包不存在 联系开发人员");
        }
        // 删除原有文件重新解压原始小程序代码
        FileUtil.del(WxMpCodePath); // 删除上次解压的数据
        FileUtil.del(baseCodePath + miniCodeName); // 删除上次压缩的目录
        ZipUtil.unzip(sourceCodePath,baseCodePath);


        // 修改源码中的配置文件

        String vendorJs = WxMpCodePath + "common/vendor.js";
        String projectConfigJson = WxMpCodePath + "project.config.json";

        // 待替代的关键字
        String vendorJsReplaceApi = "https://api";
        String vendorJsReplaceAdminApi = "https://adminapi";
        String vendorJsReplaceApp = "https://app";
        String projectConfigJsonCrmebName = "crmebName";
        String projectConfigJsonAppId = "crmebWeixinAppid";
        // 读取文件并替换
        FileReader vendorJsR = new FileReader(vendorJs);
        FileReader projectConfigJsonR = new FileReader(projectConfigJson);
        String vendorResult = vendorJsR.readString().replace(vendorJsReplaceApi,apiPath)
                .replace(vendorJsReplaceApp,appPath)
                .replace(vendorJsReplaceAdminApi,adminApiPath);
        String projectConfigJsonResult = projectConfigJsonR.readString().replace(projectConfigJsonCrmebName,crmebName)
                .replace(projectConfigJsonAppId,crmebWeixinAppid);
        FileWriter vendorJsW = new FileWriter(vendorJs);
        FileWriter projectConfigJsonW = new FileWriter(projectConfigJson);
        vendorJsW.write(vendorResult);
        projectConfigJsonW.write(projectConfigJsonResult);

        // 压缩后给出下载地址
        ZipUtil.zip(WxMpCodePath,baseCodePath + miniCodeName);
        return Constants.UPLOAD_TYPE_IMAGE + miniCodeName;
    }
}
