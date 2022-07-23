package com.zbkj.admin.filter;

import com.zbkj.common.constants.Constants;
import com.zbkj.common.utils.SpringUtil;
import com.zbkj.service.service.SystemAttachmentService;

/**
 * response路径处理
 */
public class ResponseRouter {

    public String filter(String data, String path) {
        boolean result = un().contains(path);
        if (result) {
            return data;
        }

        if (!path.contains("api/admin/") && !path.contains("api/front/")) {
            return data;
        }

        //根据需要处理返回值
        if (data.contains(Constants.UPLOAD_TYPE_IMAGE+"/") && !data.contains("data:image/png;base64")) {
            if (data.contains(Constants.UPLOAD_TYPE_FILE+"/"+Constants.UPLOAD_MODEL_PATH_EXCEL)) {
                data = SpringUtil.getBean(SystemAttachmentService.class).prefixFile(data);
            } else  {
                data = SpringUtil.getBean(SystemAttachmentService.class).prefixImage(data);
            }
        }

//        if (data.contains("file/")) { 附件也走crmebimage 目录下
//            data = SpringUtil.getBean(SystemAttachmentService.class).prefixFile(data);
//        }

        return data;
    }

    public static String un() {
        return "";
    }
}
